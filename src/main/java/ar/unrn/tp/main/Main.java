package ar.unrn.tp.main;


import ar.unrn.tp.api.*;
import ar.unrn.tp.jpa.servicios.*;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("objectdb:myDbTestFile.tmp;drop");

        ClienteService clientes= new Clientes(emf);

        clientes.crearCliente("Alexis","Gómez","42456256","ralexisge@gmail.com");
        clientes.crearCliente("Santiago","Fernandez","43456825","sjf@gmail.com");

        clientes.modificarCliente(1L,null,"Espinoza",null,null);

        clientes.agregarTarjeta(1L,"2020 5645 2356 2354","Visa");
        clientes.agregarTarjeta(1L,"6061 2682 3265 5632","MemeCard");
        clientes.agregarTarjeta(1L,"2525 2323 6524 6541","Uala");
        clientes.agregarTarjeta(2L,"6061 2682 7275 7565","MemeCard");

        CategoriaService categorias = new Categorias(emf);

        categorias.crearCategoria("ROPA DEPORTIVA");
        categorias.crearCategoria("ROPA DE INVIERNO");
        categorias.crearCategoria("ROPA DE VERANO");
        categorias.crearCategoria("CALZADO DEPORTIVO");
        categorias.crearCategoria("CALZADO DE INVIERNO");
        categorias.crearCategoria("CALZADO DE VERANO");

        categorias.modificarCategoria(7L, "ROPA DE DEPORTE");
        categorias.modificarCategoria(10L,"ROPA DE DEPORTE");

        ProductoService productos= new Productos(emf);
        MarcaService marcas= new Marcas(emf);
        marcas.crearMarca("Nike");
        marcas.crearMarca("Adiddas");
        marcas.crearMarca("Puma");
        productos.crearProducto("1","Buena calidad remera",500, 7L,13L);
        productos.crearProducto("2","Buena calidad zapatillas",300, 10L,13L);
        productos.modificarProducto(16L,"3","Remera",500, 8L,15L);

        DescuentoService descuentos = new Descuentos(emf);

        descuentos.crearDescuento("Nike", LocalDate.now().minusDays(3),LocalDate.now().plusMonths(1),0.05);
        descuentos.crearDescuentoSobreTotal("Visa", LocalDate.now().minusDays(3),LocalDate.now().plusMonths(1),0.08);

        VentaService ventas= new Ventas(emf);
        List<Long> prods= new ArrayList<>();
        prods.add(16L);
        ventas.realizarVenta(1L,prods,3L);


    }
}
