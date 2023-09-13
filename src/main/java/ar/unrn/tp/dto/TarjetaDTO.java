package ar.unrn.tp.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TarjetaDTO {

    private Long id;
    private String numTarjeta;
    private String nombre;

    public TarjetaDTO(Long id, String numTarjeta, String nombre) {
        this.id = id;
        this.numTarjeta = numTarjeta;
        this.nombre = nombre;
    }
}
