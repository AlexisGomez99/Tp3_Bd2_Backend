package ar.unrn.tp.dto;


import ar.unrn.tp.modelo.Producto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VentaDTO {
    private Long id;
    private LocalDate fechaVenta;
    private ClienteDTO cliente;
    private TarjetaDTO tarjeta;
    private List<ProductoDTO> listaProductos;
    private double totalPagado;
}
