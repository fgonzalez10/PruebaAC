package com.fgonzalez.prueba_ac.controller;

import com.fgonzalez.prueba_ac.model.Usuario;
import com.fgonzalez.prueba_ac.service.UsuarioService;
import com.fgonzalez.prueba_ac.service.ValidacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ValidacionService validacionService;

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> obtenerPorEmail(@PathVariable String email) {
        return usuarioService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email-contain")
    public List<Usuario> buscarPorEmailContain(@RequestParam String email) {
        return usuarioService.findByEmailContainingIgnoreCase(email);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario usuario, @RequestParam Long idUsuario) {
        try {
            validacionService.validarAdmin(idUsuario);
            Usuario nuevoUsuario = usuarioService.crear(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario usuario, @RequestParam Long idUsuario) {
        try {
            validacionService.validarAdmin(idUsuario);
            Usuario actualizado = usuarioService.actualizar(id, usuario);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @RequestParam Long idUsuario) {
        try {
            validacionService.validarAdmin(idUsuario);
            usuarioService.eliminar(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
