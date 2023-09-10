package ar.unrn.tp.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TarjetaDTO {

    private Long id;
    private String numTarjeta;
    private String nombre;
}
