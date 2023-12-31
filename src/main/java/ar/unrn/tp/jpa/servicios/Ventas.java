package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.CacheVentaService;
import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.dto.*;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.modelo.*;
import org.junit.jupiter.api.AfterEach;

import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
@Service
public class Ventas implements VentaService {


    private final EntityManagerFactory emf;
    private CacheVentaService cacheVentaService;
    public Ventas(EntityManagerFactory emf, CacheVentaService cacheVentaService) {
        this.emf = emf;
        this.cacheVentaService= cacheVentaService;
    }

    @Override
    public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) throws Exception {
        inTransactionExecute((em) -> {

            List<Descuento> descuentos = new ArrayList<>();

            TypedQuery<DescTarjeta> t = em.createQuery("select p from DescTarjeta p", DescTarjeta.class);
            descuentos.addAll(t.getResultList());
            TypedQuery<DescProducto> p = em.createQuery("select p from DescProducto p", DescProducto.class);
            descuentos.addAll(p.getResultList());
            if(idTarjeta == null)
                throw new RuntimeException("Debe seleccionar una tarjeta.");
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
                        Calendar calendar = Calendar.getInstance();
                        int anioActual = calendar.get(Calendar.YEAR);
                        NextNumber numeroUnico=null;
                        try {
                            TypedQuery<NextNumber> query = em.createQuery("select n from NextNumber n where anio = :anioActual", NextNumber.class);
                            query.setParameter("anioActual", anioActual);
                            query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
                            numeroUnico = query.getSingleResult();

                        }catch(NoResultException e){
                            numeroUnico= new NextNumber( calendar.get(Calendar.YEAR), 0);
                        }
                        carrito.asignarNumeroUnico(numeroUnico.recuperarSiguiente()+"-"+anioActual);
                        venta = carrito.comprarListado();
                        em.persist(venta);
                        em.persist(numeroUnico);
                        this.cacheVentaService.limpiarCache(idCliente);
                    } catch (NotNullException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new RuntimeException("La tarjeta no pertenece al cliente");
                }

        });

    }

    @Override
    public double calcularMonto(List<Long> productos, Long idTarjeta) throws Exception {
        AtomicReference<Double> total= new AtomicReference<>((double) 0);


        inTransactionExecute((em) -> {
            double totalTransaccion=0;
            if(idTarjeta == null)
                throw new RuntimeException("Debe seleccionar una tarjeta.");

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
                        productoDTOS.add(new ProductoDTO(p.getId(),p.getCodigo(),p.getDescripcion(),new CategoriaDTO(p.getCategoria().getId(),p.getCategoria().getNombreCategoria()),p.getPrecio(),new MarcaDTO(p.getMarca().getId(),p.getMarca().getNombre()),0L));
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

    @Override
    public List<Venta> ventasRecientesDeCliente(Long idCliente) throws Exception {
        List<Venta> ventas = new ArrayList<>();
        List<Venta> ventasCacheadas;

        ventasCacheadas = this.cacheVentaService.listarVentasDeUnCliente(idCliente);
        if(ventasCacheadas.size() > 0)
            return ventasCacheadas;
        inTransactionExecute((em) -> {
            ventas.addAll(em.createQuery("SELECT v FROM Venta v JOIN FETCH v.listaProductos WHERE v.cliente.id = :idCliente ORDER BY v.fechaVenta DESC ", Venta.class)
                    .setParameter("idCliente", idCliente)
                    .setMaxResults(3)
                    .getResultList());
        });
        this.cacheVentaService.persistirUltimasVentas(ventas, idCliente);
        return ventas;
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
