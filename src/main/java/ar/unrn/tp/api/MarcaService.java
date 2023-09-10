package ar.unrn.tp.api;

import java.util.List;

public interface MarcaService {
    void crearMarca(String nombre);
    void modificarMarca(Long idMarca,String nombre);
    List listarMarcas();
}
