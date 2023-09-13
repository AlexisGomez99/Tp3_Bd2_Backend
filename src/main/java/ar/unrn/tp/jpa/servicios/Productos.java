package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.dto.CategoriaDTO;
import ar.unrn.tp.dto.MarcaDTO;
import ar.unrn.tp.dto.ProductoDTO;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;
import org.junit.jupiter.api.AfterAll;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class Productos implements ProductoService {

    private final EntityManagerFactory emf;

    public Productos(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public void crearProducto(String codigo, String descripcion, double precio, Long IdCategoría, Long IdMarca) {
        inTransactionExecute((em) -> {
            Categoria categoria = em.find(Categoria.class,IdCategoría);
            Marca marca = em.find(Marca.class,IdMarca);
            Producto producto= null;
            if (marca != null && categoria != null) {

                try {
                    producto = new Producto(codigo, descripcion, precio, categoria, marca);
                } catch (NotNullException e) {
                    throw new RuntimeException(e);
                }

            }
            else
                throw new RuntimeException("La marca o categoria no es valida.");
            em.persist(producto);
        });
    }

    @Override
    public void modificarProducto(Long idProducto, String codigo, String descripcion, double precio, Long IdCategoría, Long IdMarca) {
        inTransactionExecute((em) -> {

            Producto producto = em.find(Producto.class, idProducto);

            if (producto!= null) {
                Categoria categoria = em.getReference(Categoria.class, IdCategoría);
                Marca marca = em.getReference(Marca.class, IdMarca);
                producto.modificarProducto(codigo,descripcion,precio,categoria,marca);
            }
            else
                throw new RuntimeException("El producto no existe");

            em.persist(producto);
        });
    }

    @Override
    public List listarProductos() {
        List<ProductoDTO> productoDTOS= new ArrayList<>();
        inTransactionExecute((em) -> {
            List<Producto> productos;
            TypedQuery<Producto> q = em.createQuery("select p from Producto p", Producto.class);
            productos = q.getResultList();
            for(Producto p: productos){
                productoDTOS.add(new ProductoDTO(p.getId(),p.getCodigo(),p.getDescripcion(),new CategoriaDTO(p.getCategoria().getId(),p.getCategoria().getNombreCategoria()),p.getPrecio(),new MarcaDTO(p.getMarca().getId(),p.getMarca().getNombre())));
            }

        });

        return productoDTOS;
    }
    public void inTransactionExecute(Consumer<EntityManager> bloqueDeCodigo) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            bloqueDeCodigo.accept(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @AfterAll
    public void tearDown() {
        emf.close();
    }
}
