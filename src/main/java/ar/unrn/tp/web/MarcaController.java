package ar.unrn.tp.web;

import ar.unrn.tp.api.MarcaService;
import ar.unrn.tp.dto.CategoriaDTO;
import ar.unrn.tp.dto.MarcaDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("marcas")
public class MarcaController {

    @Autowired
    private MarcaService marcas;

    public MarcaController() {
    }


    @PostMapping("/crear")
    @Operation(summary = "Agregar una Marca")
    public ResponseEntity<?> create(@RequestBody MarcaDTO marcaDTO) {
        this.marcas.crearMarca(marcaDTO.getNombre());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PutMapping("/actualizar")
    @Operation(summary = "Modificar una Categoria")
    public ResponseEntity<?> update(@RequestBody CategoriaDTO categoriaDTO) {
        this.marcas.modificarMarca(categoriaDTO.getId(),categoriaDTO.getNombreCategoria());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @GetMapping("/listar")
    @Operation(summary = "Listar Marcas")
    public ResponseEntity<?> list() {
        return ResponseEntity.status(OK).body(this.marcas.listarMarcas());
    }
}
