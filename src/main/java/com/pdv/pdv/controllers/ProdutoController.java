package com.pdv.pdv.controllers;

import com.pdv.pdv.entities.Produto;
import com.pdv.pdv.entities.Usuario;
import com.pdv.pdv.repositories.ProdutoRepository;
import com.pdv.pdv.repositories.UsuarioRepository;
import com.pdv.pdv.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> getProdutosPorLoja(@PathVariable Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario != null){
            return ResponseEntity.ok(produtoRepository.findAllByIdLoja(usuario.getIdLoja()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("USUARIO NAO ENCONTRADO", "ID USUARIO: " + idUsuario));
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT}, value = "/{idUsuario}")
    public ResponseEntity<?> post(@RequestBody Produto produtoRequest, @PathVariable Long idUsuario){

        if (produtoRequest.getIdLoja() == null) {
            Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

            if (usuario != null){
                produtoRequest.setIdLoja(usuario.getIdLoja());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("USUARIO NAO ENCONTRADO", "ID USUARIO: " + idUsuario));
            }
        }

        Produto produto = produtoRequest;

        if (produtoRequest.getId() != null) {
            Produto produtoUpdate = produtoRepository.findById(produtoRequest.getId()).orElse(null);

            if (produtoUpdate != null) {
                produto = new Produto();
                produto.setQuantidade(produtoRequest.getQuantidade() != null ? produtoRequest.getQuantidade() : produtoUpdate.getQuantidade());
                produto.setTipoProduto(produtoUpdate.getTipoProduto());
                produto.setIdLoja(produtoUpdate.getIdLoja());
                produto.setId(produtoUpdate.getId());
                produto.setCor(produtoUpdate.getCor());
                produto.setPreco(produtoRequest.getPreco() != null ? produtoRequest.getPreco() : produtoUpdate.getPreco());
                produto.setDescricao(produtoUpdate.getDescricao());
                produto.setNome(produtoUpdate.getNome());
                produto.setTamanho(produtoUpdate.getTamanho());
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
    }

    @DeleteMapping("/{idProduto}")
    public ResponseEntity<?> deletarProduto(@PathVariable Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto).orElse(null);

        if (produto == null) {
            return ResponseEntity.notFound().build();
        }

        produto.setTipoProduto("DELETADO");
        return ResponseEntity.ok().body(produtoRepository.save(produto));
    }
}
