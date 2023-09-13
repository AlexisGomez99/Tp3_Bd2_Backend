package ar.unrn.tp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaDTO {

    private Long id;
    private String nombreCategoria;

    public CategoriaDTO(Long id, String nombreCategoria) {
        this.id = id;
        this.nombreCategoria = nombreCategoria;
    }
}
