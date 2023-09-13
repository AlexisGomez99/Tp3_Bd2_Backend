package ar.unrn.tp.web;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.dto.ProductoDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("productos")
public class ProductoController {

    @Autowired
    private ProductoService productos;

    public ProductoController() {
    }


    @PostMapping("/crear")
    @Operation(summary = "Agregar un Producto")
    public ResponseEntity<?> create(@RequestBody ProductoDTO productoDTO) {

        this.productos.crearProducto(productoDTO.getCodigo(),productoDTO.getDescripcion(),productoDTO.getPrecio(),productoDTO.getCategoria().getId(),productoDTO.getMarca().getId());
        return ResponseEntity.status(OK).body("Ok");
    }
    @PutMapping("/actualizar")
    @Operation(summary = "Modificar un Producto")
    public ResponseEntity<?> update(@RequestBody ProductoDTO productoDTO) {
        this.productos.modificarProducto(productoDTO.getId(),productoDTO.getCodigo(),productoDTO.getDescripcion(),productoDTO.getPrecio(),productoDTO.getCategoria().getId(),productoDTO.getMarca().getId());
        return ResponseEntity.status(OK).body("Ok");
    }
    @GetMapping("/listar")
    @Operation(summary = "Listar Productos")
    public ResponseEntity<?> list() {
        return ResponseEntity.status(OK).body(this.productos.listarProductos());
    }
}
