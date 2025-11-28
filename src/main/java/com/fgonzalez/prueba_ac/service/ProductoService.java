package com.fgonzalez.prueba_ac.service;

import com.fgonzalez.prueba_ac.model.BitacoraLog;
import com.fgonzalez.prueba_ac.model.Producto;
import com.fgonzalez.prueba_ac.model.Rol;
import com.fgonzalez.prueba_ac.model.Usuario;
import com.fgonzalez.prueba_ac.repository.ProductoRepository;
import com.fgonzalez.prueba_ac.repository.BitacoraLogRepository;
import org.springdoc.core.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private BitacoraLogRepository bitacoraLogRepository;

    @Autowired
    private ValidacionService validacionService;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarPorMarca(String marca) {
        return productoRepository.findByMarcaContainingIgnoreCase(marca);
    }

    public Producto crear(Producto producto, Long usuarioId) {
        validacionService.validarAdmin(usuarioId);
        if (productoRepository.existsBySku(producto.getSku())) {
            throw new RuntimeException("El SKU ya existe");
        }
        Producto nuevoProducto = productoRepository.save(producto);
        crearLog(nuevoProducto.getId(), usuarioId, "CREATE",
                "Producto creado: " + nuevoProducto.getNombre());
        return nuevoProducto;
    }

    public Producto actualizar(Long id, Producto producto, Long usuarioId) {
        validacionService.validarAdmin(usuarioId);
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        String cambios = construirDescripcionCambios(productoExistente, producto);

        productoExistente.setSku(producto.getSku());
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setMarca(producto.getMarca());

        Producto actualizado = productoRepository.save(productoExistente);

        crearLog(actualizado.getId(), usuarioId, "UPDATE", cambios);

        return actualizado;
    }

    public void eliminar(Long id, Long usuarioId) {
        validacionService.validarAdmin(usuarioId);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        crearLog(id, usuarioId, "DELETE",
                "Producto eliminado: " + producto.getNombre() + " (SKU: " + producto.getSku() + ")");

        productoRepository.deleteById(id);
    }

    public List<BitacoraLog> obtenerHistorial(Long productoId) {
        return bitacoraLogRepository.findByIdProductoOrderByFechaDesc(productoId);
    }

    private void crearLog(Long productoId, Long usuarioId, String accion, String descripcion) {
        BitacoraLog log = new BitacoraLog();
        log.setIdProducto(productoId);
        log.setIdUsuario(usuarioId);
        log.setAccion(accion);
        log.setDescripcion(descripcion);
        bitacoraLogRepository.save(log);
    }

    private String construirDescripcionCambios(Producto anterior, Producto nuevo) {
        StringBuilder cambios = new StringBuilder("Cambios: ");

        if (!anterior.getNombre().equals(nuevo.getNombre())) {
            cambios.append("Nombre: '").append(anterior.getNombre())
                    .append("' → '").append(nuevo.getNombre()).append("'; ");
        }
        if (!anterior.getPrecio().equals(nuevo.getPrecio())) {
            cambios.append("Precio: $").append(anterior.getPrecio())
                    .append(" → $").append(nuevo.getPrecio()).append("; ");
        }
        if (!anterior.getMarca().equals(nuevo.getMarca())) {
            cambios.append("Marca: '").append(anterior.getMarca())
                    .append("' → '").append(nuevo.getMarca()).append("'; ");
        }

        return cambios.toString();
    }



}
