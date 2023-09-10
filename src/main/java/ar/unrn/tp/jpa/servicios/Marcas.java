package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.MarcaService;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Marca;
import org.junit.jupiter.api.AfterAll;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class Marcas implements MarcaService {

    private final EntityManagerFactory emf;

    public Marcas(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public void crearMarca(String nombre) {
        inTransactionExecute((em) -> {
            Marca marca= new Marca(nombre);

            em.persist(marca);
        });
    }

    @Override
    public void modificarMarca(Long idMarca, String nombre) {
        inTransactionExecute((em) -> {
            Marca marca = em.getReference(Marca.class, idMarca);
            if (!marca.getNombre().equalsIgnoreCase(nombre) && nombre != null)
                marca.setNombre(nombre);
            em.persist(marca);
        });
    }

    @Override
    public List listarMarcas() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<Marca> marcas;
        tx.begin();
        TypedQuery<Marca> q = em.createQuery("select m from Marca m", Marca.class);
        marcas = q.getResultList();
        tx.commit();
        return marcas;
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
