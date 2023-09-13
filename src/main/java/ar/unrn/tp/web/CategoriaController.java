package ar.unrn.tp.web;


import ar.unrn.tp.api.CategoriaService;
import ar.unrn.tp.dto.CategoriaDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categorias;

    public CategoriaController() {

    }

    @PostMapping("/crear")
    @Operation(summary = "Agregar una Categoria")
    public ResponseEntity<?> create(@RequestBody CategoriaDTO categoriaDTO) {
        this.categorias.crearCategoria(categoriaDTO.getNombreCategoria());
        return ResponseEntity.status(OK).body("Ok");
    }

    @PutMapping("/actualizar")
    @Operation(summary = "Modificar una Categoria")
    public ResponseEntity<?> update(@RequestBody CategoriaDTO categoriaDTO) {
        this.categorias.modificarCategoria(categoriaDTO.getId(),categoriaDTO.getNombreCategoria());
        return ResponseEntity.status(OK).body("Ok");
    }
    @GetMapping("/listar")
    @Operation(summary = "Listar una Categoria")
    public ResponseEntity<?> list() {
        this.categorias.listarCategorias();
        return ResponseEntity.status(OK).body(this.categorias.listarCategorias());
    }
}
