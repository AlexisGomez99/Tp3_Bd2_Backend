package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.dto.*;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.modelo.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterEach;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
@Service
public class Ventas implements VentaService {


    private final EntityManagerFactory emf;

    public Ventas(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) throws Exception {
        inTransactionExecute((em) -> {

            List<Descuento> descuentos = new ArrayList<>();

            TypedQuery<DescTarjeta> t = em.createQuery("select p from DescTarjeta p", DescTarjeta.class);
            descuentos.addAll(t.getResultList());
            TypedQuery<DescProducto> p = em.createQuery("select p from DescProducto p", DescProducto.class);
            descuentos.addAll(p.getResultList());

            Cliente cliente = em.find(Cliente.class,idCliente);
            Tarjeta tarjeta = em.find(Tarjeta.class,idTarjeta);
            if (cliente == null) {
                throw new RuntimeException("El cliente no existe");
            }
            if (tarjeta == null){
                throw new RuntimeException("No existe la tarjeta solicitada");
            }
            if (productos==null || productos.isEmpty()) {
                throw new RuntimeException("No hay productos para esta lista");
            }
            boolean existeTarjeta=false;

                for (Tarjeta card : cliente.getTarjetas()) {
                    if (card.getNumTarjeta().equalsIgnoreCase(tarjeta.getNumTarjeta())) {
                        existeTarjeta = true;
                        break;
                    }
                }
                if (existeTarjeta) {
                    List<Producto> productoRecuperados = new ArrayList<>();
                    for (Long i : productos) {
                        Producto prod = em.getReference(Producto.class, i);
                        productoRecuperados.add(prod);
                    }
                    Carrito carrito = new Carrito(descuentos);
                    carrito.asociarTarjeta(tarjeta);
                    carrito.asociarCliente(cliente);
                    carrito.agregarProductos(productoRecuperados);
                    Venta venta;
                    try {
                        venta = carrito.comprarListado();
                        em.persist(venta);
                    } catch (NotNullException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new RuntimeException("La tarjeta no pertenece al cliente");
                }

        });

    }

    @Override
    public double calcularMonto(List<Long> productos,@NotNull Long idTarjeta) throws Exception {
        AtomicReference<Double> total= new AtomicReference<>((double) 0);


        inTransactionExecute((em) -> {
            double totalTransaccion=0;
            Tarjeta tarjeta = em.find(Tarjeta.class, idTarjeta);
            if (tarjeta == null){
                throw new RuntimeException("La tarjeta solicitada no existe");
            }
            if (productos==null || productos.isEmpty()) {
                throw new RuntimeException("No hay productos para esta lista");
            }

            List<Descuento> descuentos = new ArrayList<>();

            TypedQuery<DescTarjeta> t = em.createQuery("select p from DescTarjeta p", DescTarjeta.class);
            descuentos.addAll(t.getResultList());
            TypedQuery<DescProducto> p = em.createQuery("select p from DescProducto p", DescProducto.class);
            descuentos.addAll(p.getResultList());



            List<Producto> productoRecuperados = new ArrayList<>();
            for (Long i : productos) {
                Producto prod = em.getReference(Producto.class, i);
                productoRecuperados.add(prod);
            }
            Carrito carrito = new Carrito(descuentos);
            carrito.asociarTarjeta(tarjeta);
            carrito.agregarProductos(productoRecuperados);


            try {
                totalTransaccion = carrito.calcularDescuento();
            } catch (NotNullException e) {
                throw new RuntimeException(e);
            }
            total.set(totalTransaccion);
        });

        return total.get();
    }

    @Override
    public List<VentaDTO> ventas() {
        List<VentaDTO> ventaDTOS= new ArrayList<>();
        try {
            inTransactionExecute((em) -> {
                List<Venta> ventas;
                TypedQuery<Venta> q = em.createQuery("select v from Venta v", Venta.class);
                ventas = q.getResultList();
                List<ProductoDTO> productoDTOS= new ArrayList<>();
                for(Venta v: ventas){
                    for(ProductoVendido p: v.getListaProductos()){
                        productoDTOS.add(new ProductoDTO(p.getId(),p.getCodigo(),p.getDescripcion(),new CategoriaDTO(p.getCategoria().getId(),p.getCategoria().getNombreCategoria()),p.getPrecio(),new MarcaDTO(p.getMarca().getId(),p.getMarca().getNombre())));
                    }
                    TarjetaDTO tarjetaDTO= new TarjetaDTO(v.getTarjeta().getId(),v.getTarjeta().getNumTarjeta(),v.getTarjeta().getNombre());
                    ClienteDTO clienteDTO= new ClienteDTO(v.getCliente().getId(),v.getCliente().getNombre(),v.getCliente().getApellido(),v.getCliente().getDni(),v.getCliente().getEmail());
                    clienteDTO.agregarTarjeta(tarjetaDTO);
                    ventaDTOS.add(new VentaDTO(v.getId(),v.getFechaVenta(),clienteDTO,tarjetaDTO,productoDTOS,v.getTotalPagado()));

                }

            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ventaDTOS;
    }



    public void inTransactionExecute(Consumer<EntityManager> bloqueDeCodigo) throws Exception {
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
            if (em != null && em.isOpen())
                em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        emf.close();
    }
}
