package ar.unrn.tp.web;

import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.dto.DescuentoDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("descuentos")
public class DescuentoController {

    @Autowired
    private DescuentoService descuentos;

    public DescuentoController(){}


    @PostMapping("/crear-descuento-tarjeta")
    @Operation(summary = "Agregar un Descuento sobre total")
    public ResponseEntity<?> createAboutTotal(@RequestBody DescuentoDTO descuentoDTO) {
        this.descuentos.crearDescuentoSobreTotal(descuentoDTO.getNombreDesc(),descuentoDTO.getFechaInicio(),descuentoDTO.getFechaFin(), descuentoDTO.getDescuento());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/crear-descuento-producto")
    @Operation(summary = "Agregar un Descuento sobre producto")
    public ResponseEntity<?> createAboutProd(@RequestBody DescuentoDTO descuentoDTO) {
        this.descuentos.crearDescuento(descuentoDTO.getNombreDesc(),descuentoDTO.getFechaInicio(),descuentoDTO.getFechaFin(), descuentoDTO.getDescuento());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar Descuentos")
    public ResponseEntity<?> list(){
        return ResponseEntity.status(OK).body(this.descuentos.listarDescuentos());
    }

}
