package com.pdv.pdv.controllers;

import com.pdv.pdv.entities.Pedido;
import com.pdv.pdv.entities.Produto;
import com.pdv.pdv.repositories.PedidoRepository;
import com.pdv.pdv.repositories.ProdutoRepository;
import com.pdv.pdv.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> post(@RequestBody Pedido pedido) {
        List<Long> idsProduto = pedido.getItensPedido().stream().map(itemPedido -> itemPedido.getProduto().getId()).collect(Collectors.toList());
        List<Produto> produtos = produtoRepository.findAllById(idsProduto);
        pedido.getItensPedido().forEach(itemPedido -> {
            produtos.forEach(produto -> {
                if (itemPedido.getProduto().getId().equals(produto.getId())){
                    itemPedido.setProduto(produto);
                }
            });
        });

        List<String> produtosSemEstoque = pedido.verificaProdutosSemEstoque();

        if (!produtosSemEstoque.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage("PRODUTOS SEM ESTOQUE", produtosSemEstoque));
        }

        pedido.calcularValorTotal();

        pedido.removeProdutosEstoque();
        pedidoRepository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
