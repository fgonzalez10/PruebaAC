package com.fgonzalez.prueba_ac.repository;

import com.fgonzalez.prueba_ac.model.BitacoraLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BitacoraLogRepository extends JpaRepository<BitacoraLog, Long> {

    List<BitacoraLog> findByIdProductoOrderByFechaDesc(Long idProducto);


}