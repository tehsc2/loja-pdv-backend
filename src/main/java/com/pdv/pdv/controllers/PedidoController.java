package com.pdv.pdv.controllers;

import com.pdv.pdv.entities.Pedido;
import com.pdv.pdv.entities.Produto;
import com.pdv.pdv.entities.Usuario;
import com.pdv.pdv.entities.enums.StatusPedidoEnum;
import com.pdv.pdv.repositories.PedidoRepository;
import com.pdv.pdv.repositories.ProdutoRepository;
import com.pdv.pdv.repositories.UsuarioRepository;
import com.pdv.pdv.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> getPedidosPorVendedor(@PathVariable Long idUsuario, @RequestParam(required = false) StatusPedidoEnum status) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario != null) {
            if (status != null) {
                return ResponseEntity.ok(pedidoRepository.findAllByStatusPedidoAndVendedorIdLoja(status, usuario.getIdLoja()));
            }
            return ResponseEntity.ok(pedidoRepository.findAllByVendedorIdLoja(usuario.getIdLoja()));
        }
        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> createUptadePedido(@RequestBody Pedido pedidoRequest) {
        Pedido pedido = null;

        if (pedidoRequest.getItensPedido() != null && !pedidoRequest.getItensPedido().isEmpty()){
            List<Long> idsProduto = pedidoRequest.getItensPedido().stream().map(itemPedido -> itemPedido.getProduto().getId()).collect(Collectors.toList());

            List<Produto> produtos = produtoRepository.findAllById(idsProduto);

            pedidoRequest.getItensPedido().forEach(itemPedido -> produtos.forEach(produto -> {
                if (itemPedido.getProduto().getId().equals(produto.getId())) {
                    itemPedido.setProduto(produto);
                }
            }));

            List<String> produtosSemEstoque = pedidoRequest.verificaProdutosSemEstoque();

            if (!produtosSemEstoque.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorMessage("PRODUTOS SEM ESTOQUE", produtosSemEstoque));
            }

            pedidoRequest.calcularValorTotal();
            pedidoRequest.removeProdutosEstoque();

            if (pedidoRequest.getId() == null) {
                pedidoRequest.setDataPedido(LocalDateTime.now());
            }
            pedido = pedidoRequest;
        } else {
            pedido = pedidoRepository.findById(pedidoRequest.getId()).orElse(null);
        }

        if (pedido != null) {
            pedido.setStatusPedido(pedidoRequest.getStatusPedido());
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoRepository.save(pedido));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("PEDIDO NÃO FOI CRIADO/ATUALIZADO", pedidoRequest));
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElse(null);

        if (pedido != null) {
            pedido.setStatusPedido(StatusPedidoEnum.CANCELADO);
            pedidoRepository.save(pedido);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Pedido não encontrado", "ID PEDIDO: " + idPedido));
    }
}
