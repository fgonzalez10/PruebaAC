package com.fgonzalez.prueba_ac.service;

import com.fgonzalez.prueba_ac.model.Rol;
import com.fgonzalez.prueba_ac.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidacionService {

    @Autowired
    private UsuarioService usuarioService;

    public void validarAdmin(Long usuarioId) {
        Usuario usuario = usuarioService.obtenerPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getRol().equals(Rol.ADMIN)) {
            throw new RuntimeException("No tienes permisos para realizar esta acción");
        }
    }
}
