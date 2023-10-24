package ar.unrn.tp.api;

import ar.unrn.tp.modelo.Venta;

import java.util.List;

public interface CacheVentaService {

    void persistirVenta(Venta venta);

    void persistirUltimasVentas(List<Venta> ventas, Long idCliente);

    void limpiarCache(Long idCliente);

    List<Venta> listarVentasDeUnCliente(Long idCliente);

}
