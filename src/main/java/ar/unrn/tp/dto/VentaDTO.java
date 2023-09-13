package ar.unrn.tp.dto;


import ar.unrn.tp.modelo.Producto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class VentaDTO {
    private Long id;
    private LocalDate fechaVenta;
    private ClienteDTO cliente;
    private TarjetaDTO tarjeta;
    private List<ProductoDTO> listaProductos;
    private double totalPagado;

    public VentaDTO(Long id, LocalDate fechaVenta, ClienteDTO cliente, TarjetaDTO tarjeta, List<ProductoDTO> listaProductos, double totalPagado) {
        this.id = id;
        this.fechaVenta = fechaVenta;
        this.cliente = cliente;
        this.tarjeta = tarjeta;
        this.listaProductos = listaProductos;
        this.totalPagado = totalPagado;
    }
}
