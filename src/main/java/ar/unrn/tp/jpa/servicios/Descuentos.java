package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.dto.DescuentoDTO;
import ar.unrn.tp.excepciones.DateException;
import ar.unrn.tp.modelo.*;
import org.junit.jupiter.api.AfterAll;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class Descuentos implements DescuentoService {

    private final EntityManagerFactory emf;

    public Descuentos(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void crearDescuentoSobreTotal(String marcaTarjeta, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje) {
        inTransactionExecute((em) -> {
            DescTarjeta descuento;
            try {
                descuento = new DescTarjeta(marcaTarjeta,fechaDesde,fechaHasta,porcentaje);
            } catch (DateException e) {
                throw new RuntimeException(e);
            }

            em.persist(descuento);
        });
    }

    @Override
    public void crearDescuento(String marcaProducto, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje) {
        inTransactionExecute((em) -> {
            Descuento descuento;
            try {
                descuento = new DescProducto(marcaProducto,fechaDesde,fechaHasta,porcentaje);
            } catch (DateException e) {
                throw new RuntimeException(e);
            }

            em.persist(descuento);
        });
    }

    @Override
    public List<DescuentoDTO> listarDescuentos() {
        List<DescuentoDTO> descuentoDTOS= new ArrayList<>();
        inTransactionExecute((em)->{
            List<DescTarjeta> descuentosT = new ArrayList<>();
            List<DescProducto> descuentosD = new ArrayList<>();
            LocalDate fechaActual= LocalDate.now();

            TypedQuery<DescTarjeta> t = em.createQuery("SELECT d FROM DescTarjeta d WHERE :fechaActual BETWEEN d.fechaInicio AND d.fechaFin", DescTarjeta.class);
            t.setParameter("fechaActual", fechaActual);
            descuentosT.addAll(t.getResultList());

            for(DescTarjeta d: descuentosT){
                descuentoDTOS.add(new DescuentoDTO(d.getId(),"Tarjeta",d.getFechaInicio(),d.getFechaFin(),d.getDescuento(),d.getTarjeta()));
            }

            TypedQuery<DescProducto> p = em.createQuery("select p from DescProducto p WHERE :fechaActual BETWEEN p.fechaInicio AND p.fechaFin", DescProducto.class);
            p.setParameter("fechaActual", fechaActual);
            descuentosD.addAll(p.getResultList());
            for(DescProducto d: descuentosD){
                descuentoDTOS.add(new DescuentoDTO(d.getId(),"Marca",d.getFechaInicio(),d.getFechaFin(),d.getDescuento(),d.getMarca()));
            }

        });
        return descuentoDTOS;
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
