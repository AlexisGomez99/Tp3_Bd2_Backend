package ar.unrn.tp.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VentaDTO {
    private Long id;
    private LocalDateTime fechaVenta;
    private ClienteDTO cliente;
    private TarjetaDTO tarjeta;
    private String codigoUnico;
    private List<ProductoDTO> listaProductos;
    private double totalPagado;

    public VentaDTO(Long id, LocalDateTime fechaVenta, ClienteDTO cliente, TarjetaDTO tarjeta, List<ProductoDTO> listaProductos, double totalPagado) {
        this.id = id;
        this.fechaVenta = fechaVenta;
        this.cliente = cliente;
        this.tarjeta = tarjeta;
        this.listaProductos = listaProductos;
        this.totalPagado = totalPagado;
    }


}
