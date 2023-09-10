package ar.unrn.tp.dto;

import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Marca;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductoDTO {
    private Long id;
    private String codigo;
    private String descripcion;
    private CategoriaDTO categoria;
    private double precio;
    private MarcaDTO marca;
}
