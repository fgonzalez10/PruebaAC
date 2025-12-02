package com.fgonzalez.prueba_ac.repository;

import com.fgonzalez.prueba_ac.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByEmailContainingIgnoreCase(String email);
    boolean existsByEmail(String email);

}
