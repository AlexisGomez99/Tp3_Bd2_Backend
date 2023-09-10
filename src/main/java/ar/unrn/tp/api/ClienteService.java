package ar.unrn.tp.api;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteService {

    void crearCliente(String nombre, String apellido, String dni, String email);

    // validar que sea un cliente existente
    void modificarCliente(Long idCliente, String nombre,String apellido, String dni, String email );
    // validar que sea un cliente existente
    void agregarTarjeta(Long idCliente, String nro, String marca);

    //Devuelve las tarjetas de un cliente específico
    List listarTarjetas(Long idCliente);

    void eliminarCliente(Long idCliente);
}
