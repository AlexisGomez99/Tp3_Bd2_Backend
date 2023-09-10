package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.CategoriaService;
import ar.unrn.tp.modelo.Categoria;
import org.junit.jupiter.api.AfterAll;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class Categorias implements CategoriaService {

    private final EntityManagerFactory emf;

    public Categorias(EntityManagerFactory emf) {
        this.emf=emf;
    }


    @Override
    public void crearCategoria(String nombre) {
        inTransactionExecute((em) -> {
            Categoria categoria= new Categoria(nombre);

            em.persist(categoria);
        });
    }

    @Override
    public void modificarCategoria(Long idCategoria, String nombre) {
        inTransactionExecute((em) -> {
            Categoria categoria = em.getReference(Categoria.class, idCategoria);
            if (!categoria.getNombreCategoria().equalsIgnoreCase(nombre) && nombre != null)
                categoria.setNombreCategoria(nombre);
            em.persist(categoria);
        });
    }

    @Override
    public List listarCategorias() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<Categoria> categorias;
        tx.begin();
        TypedQuery<Categoria> q = em.createQuery("select c from Categoria c", Categoria.class);
        categorias = q.getResultList();
        tx.commit();
        return categorias;
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
