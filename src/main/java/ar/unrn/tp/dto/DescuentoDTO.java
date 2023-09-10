package ar.unrn.tp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class DescuentoDTO {

    private Long id;
    private String tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double descuento;
    private String nombreDesc;

    public DescuentoDTO(Long id, String tipo, LocalDate fechaInicio, LocalDate fechaFin, double descuento, String nombreDesc) {
        this.id = id;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descuento = descuento;
        this.nombreDesc = nombreDesc;
    }
}
