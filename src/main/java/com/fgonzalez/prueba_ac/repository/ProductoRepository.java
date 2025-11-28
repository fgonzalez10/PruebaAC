package com.fgonzalez.prueba_ac.repository;

import com.fgonzalez.prueba_ac.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByMarcaContainingIgnoreCase(String marca);
}
