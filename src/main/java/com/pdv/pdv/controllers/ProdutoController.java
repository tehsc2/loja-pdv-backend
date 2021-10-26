package com.pdv.pdv.controllers;

import com.pdv.pdv.entities.Produto;
import com.pdv.pdv.entities.Usuario;
import com.pdv.pdv.entities.enums.StatusPedidoEnum;
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
    public ResponseEntity<?> post(@RequestBody Produto produto, @PathVariable Long idUsuario){

        if (produto.getIdLoja() == null) {
            Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

            if (usuario != null){
                produto.setIdLoja(usuario.getIdLoja());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("USUARIO NAO ENCONTRADO", "ID USUARIO: " + idUsuario));
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
    }

    @DeleteMapping("/{idProduto}")
    public ResponseEntity<?> deletarProduto(@PathVariable Long idProduto) {
        produtoRepository.deleteById(idProduto);
        return ResponseEntity.ok().build();
    }
}
