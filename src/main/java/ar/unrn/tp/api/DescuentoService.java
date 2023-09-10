package ar.unrn.tp.api;

import ar.unrn.tp.dto.DescuentoDTO;
import ar.unrn.tp.modelo.DescTarjeta;
import ar.unrn.tp.modelo.Descuento;

import java.time.LocalDate;
import java.util.List;

public interface DescuentoService {
    // validar que las fechas no se superpongan
    void crearDescuentoSobreTotal(String marcaTarjeta, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje);

    // validar que las fechas no se superpongan
    void crearDescuento(String marcaProducto, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje);

    List<DescuentoDTO> listarDescuentos();
}


