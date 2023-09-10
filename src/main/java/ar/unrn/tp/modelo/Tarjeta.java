package ar.unrn.tp.modelo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Tarjeta {

    @Id
    @GeneratedValue
    private Long id;
    private String numTarjeta;
    private String nombre;

    public Tarjeta(String numTarjeta,String nombre) {
        this.numTarjeta = numTarjeta;
        this.nombre = nombre;
    }

    public boolean equals(String tarjeta){
        boolean x= false;
        if (this.nombre.equalsIgnoreCase(tarjeta))
            x= true;
        return x;

    }

}
