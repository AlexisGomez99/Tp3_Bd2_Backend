package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.CacheVentaService;
import ar.unrn.tp.modelo.Venta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class CacheVentas implements CacheVentaService {

    Jedis jedis;

    private void setUpJedis() {
        this.jedis = new Jedis("localhost", 6379);
    }

    private void closeJedis() {
        this.jedis.close();
    }

    @Override
    public void persistirVenta(Venta venta) {

    }

    @Override
    public void persistirUltimasVentas(List<Venta> ventas, Long idCliente) {
        this.setUpJedis();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        for(Venta venta: ventas){
            try {
                String ventaJson = objectMapper.writeValueAsString(venta);
                this.jedis.lpush("cliente:" + idCliente.toString() + ":ventas", ventaJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        this.closeJedis();
    }

    @Override
    public void limpiarCache(Long idCliente) {
        this.setUpJedis();
        this.jedis.del("cliente:" + idCliente.toString() + ":ventas");
        this.closeJedis();
    }

    @Override
    public List<Venta> listarVentasDeUnCliente(Long idCliente) {
        this.setUpJedis();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;
        List<String> ventasJson = this.jedis.lrange("cliente:" + idCliente.toString() + ":ventas", 0, -1);
        List<Venta> ventas = new ArrayList<>();
        for (String ventaJson : ventasJson) {
            try {
                Venta venta = objectMapper.readValue(ventaJson, Venta.class);
                ventas.add(venta);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        this.closeJedis();
        return ventas;
    }
}
