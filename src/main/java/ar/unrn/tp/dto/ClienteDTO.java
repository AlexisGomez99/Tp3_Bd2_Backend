package ar.unrn.tp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClienteDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private List<TarjetaDTO> tarjetas;

    public ClienteDTO(Long id, String nombre, String apellido, String dni, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        tarjetas= new ArrayList<>();
    }

    public void agregarTarjeta(TarjetaDTO tarjeta){
        this.tarjetas.add(tarjeta);
    }
}
