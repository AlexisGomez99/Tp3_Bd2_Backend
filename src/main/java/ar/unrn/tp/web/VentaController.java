package ar.unrn.tp.web;

import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.dto.ProductoDTO;
import ar.unrn.tp.dto.VentaDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("ventas")
public class VentaController {

    @Autowired
    private VentaService ventas;

    public VentaController() {
    }


    @PostMapping("/crear")
    @Operation(summary = "Agregar una Venta")
    public ResponseEntity<?> create(@RequestBody VentaDTO ventaDTO) throws Exception {
        List<Long> prods = ventaDTO.getListaProductos().stream()
                .map(ProductoDTO::getId)
                .collect(Collectors.toList());

        this.ventas.realizarVenta(ventaDTO.getCliente().getId(),prods,ventaDTO.getTarjeta().getId());
        return ResponseEntity.status(OK).body("Ok");
    }

    @PutMapping("/actualizar")
    @Operation(summary = "Modificar una Venta")
    public ResponseEntity<?> update(@RequestBody VentaDTO ventaDTO) throws Exception {
        List<Long> prods = ventaDTO.getListaProductos().stream()
                .map(ProductoDTO::getId)
                .collect(Collectors.toList());
        return ResponseEntity.status(OK).body(this.ventas.calcularMonto(prods,ventaDTO.getTarjeta().getId()));
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar Ventas")
    public ResponseEntity<?> list() {
        return ResponseEntity.status(OK).body(this.ventas.ventas());
    }
}
