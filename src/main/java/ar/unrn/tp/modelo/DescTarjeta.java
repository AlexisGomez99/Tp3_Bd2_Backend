package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.DateException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class DescTarjeta extends Descuento {

    private String tarjeta;


    public DescTarjeta(String tarjeta, LocalDate fechaInicio, LocalDate fechaFin, double descuento) throws DateException {
        super(fechaInicio,fechaFin,descuento);
        this.tarjeta = tarjeta;
    }


    @Override
    public double aplicarDescuento(List<Producto> productos, Tarjeta tarjeta) {
        LocalDate hoy= LocalDate.now();
        double total=0;
        if (hoy.isBefore(this.fechaFin) && hoy.isAfter(this.fechaInicio) && tarjeta.equals(this.tarjeta)){
            total = getTotal(productos);
            total = total * this.descuento;
        }
        return total;
    }


    public double getTotal(List<Producto> productos) {
        double total = 0;
        if(productos != null) {
            for (Producto prod : productos) {
                total = total + prod.getPrecio();
            }
        }
        return total;
    }

}
