package com.pdv.pdv.controllers;

import com.pdv.pdv.entities.Usuario;
import com.pdv.pdv.repositories.UsuarioRepository;
import com.pdv.pdv.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> getUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(usuarioRepository.findById(idUsuario));
    }

    @GetMapping("/login")
    public ResponseEntity<?> validaLogin(@PathParam(value = "login") String login, @PathParam(value = "senha") String senha) {
        Usuario usuario = usuarioRepository.findByUsuarioAndSenha(login, senha);

        if (usuario != null) {
            return ResponseEntity.ok().body(usuario);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long idUsuario) {
        usuarioRepository.deleteById(idUsuario);
        return ResponseEntity.ok(new ErrorMessage("Usuario deletado com sucesso", "ID Usuario: " + idUsuario));
    }
}
