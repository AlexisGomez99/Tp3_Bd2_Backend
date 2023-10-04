package ar.unrn.tp.api;

import ar.unrn.tp.dto.ProductoDTO;
import ar.unrn.tp.excepciones.NotNullException;

import java.util.List;

public interface ProductoService {
    //validar que sea una categoría existente y que codigo no se repita
    void crearProducto(String codigo, String descripcion, double precio, Long
            IdCategoría,Long marca);
    //validar que sea un producto existente
    void modificarProducto(Long idProducto,String codigo, String descripcion, double precio, Long
            IdCategoría,Long marca);
    //Devuelve todos los productos
    List listarProductos();

    ProductoDTO findById(Long id);
}
