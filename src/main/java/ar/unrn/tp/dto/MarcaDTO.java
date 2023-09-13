package ar.unrn.tp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class MarcaDTO {
    private Long id;
    private String nombre;

    public MarcaDTO(Long id, String nombre) {
        this.id=id;
        this.nombre=nombre;
    }
}
