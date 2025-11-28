package com.fgonzalez.prueba_ac.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BitacoraLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_producto", nullable = false)
    private Long idProducto;

    @Column(name = "accion", nullable = false, length = 50)
    private String accion;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }

}
