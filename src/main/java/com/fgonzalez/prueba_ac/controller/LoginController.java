package com.fgonzalez.prueba_ac.controller;

import com.fgonzalez.prueba_ac.model.Usuario;
import com.fgonzalez.prueba_ac.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginData){
        return usuarioService.obtenerPorEmail(loginData.getEmail())
                .map(u ->{
                    if(!u.getPassword().equals(loginData.getPassword())){
                        return ResponseEntity.badRequest().body("Contraseña incorrecta");
                    }
                    u.setPassword(null);

                    return ResponseEntity.ok(u);

                }).orElse(ResponseEntity.status(404).body("Usuario no encontrado"));
    }
}
