package com.fgonzalez.prueba_ac.controller;

import com.fgonzalez.prueba_ac.model.BitacoraLog;
import com.fgonzalez.prueba_ac.model.Producto;
import com.fgonzalez.prueba_ac.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {


        @Autowired
        private ProductoService productoService;

        @GetMapping
        public ResponseEntity<List<Producto>> obtenerTodos() {
            return ResponseEntity.ok(productoService.obtenerTodos());
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
            return productoService.obtenerPorId(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping("/buscar/nombre")
        public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String q) {
            return ResponseEntity.ok(productoService.buscarPorNombre(q));
        }

        @GetMapping("/buscar/marca")
        public ResponseEntity<List<Producto>> buscarPorMarca(@RequestParam String q) {
            return ResponseEntity.ok(productoService.buscarPorMarca(q));
        }

        @PostMapping
        public ResponseEntity<?> crear(@RequestBody Producto producto,
                                       @RequestParam Long usuarioId) {
            try {
                Producto nuevoProducto = productoService.crear(producto, usuarioId);
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> actualizar(@PathVariable Long id,
                                            @RequestBody Producto producto,
                                            @RequestParam Long usuarioId) {
            try {
                Producto actualizado = productoService.actualizar(id, producto, usuarioId);
                return ResponseEntity.ok(actualizado);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> eliminar(@PathVariable Long id,
                                          @RequestParam Long usuarioId) {
            try {
                productoService.eliminar(id, usuarioId);
                return ResponseEntity.ok("Producto eliminado correctamente");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        @GetMapping("/{id}/historial")
        public ResponseEntity<List<BitacoraLog>> obtenerHistorial(@PathVariable Long id) {
            return ResponseEntity.ok(productoService.obtenerHistorial(id));
        }

}
