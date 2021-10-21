package com.pdv.pdv.controllers;

import com.pdv.pdv.entities.Usuario;
import com.pdv.pdv.repositories.UsuarioRepository;
import com.pdv.pdv.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
