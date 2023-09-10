package ar.unrn.tp.web;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.dto.ClienteDTO;
import ar.unrn.tp.dto.TarjetaDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("clientes")
public class ClienteController {

    @Autowired
    private ClienteService clientes;

    public ClienteController(){

    }

    @PostMapping("/crear")
    @Operation(summary = "Agregar un Cliente")
    public ResponseEntity<?> create(@RequestBody ClienteDTO cliente) {
        this.clientes.crearCliente(cliente.getNombre(),cliente.getApellido(),cliente.getDni(),cliente.getEmail());
        return ResponseEntity.status(OK).body("Ok");
    }

    @PutMapping("/actualizar")
    @Operation(summary = "Modificar un Cliente")
    public ResponseEntity<?> update(@RequestBody ClienteDTO cliente) {
        this.clientes.modificarCliente(cliente.getId(),cliente.getNombre(),cliente.getApellido(),cliente.getDni(),cliente.getEmail());
        return ResponseEntity.status(OK).body("Ok");
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Eliminar un Cliente")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        this.clientes.eliminarCliente(id);
        return ResponseEntity.status(OK).body("Ok");
    }

    @PutMapping("/agregar-tarjeta/{idCliente}")
    @Operation(summary = "Agregar tarjeta a un Cliente")
    public ResponseEntity<?> addCard(@PathVariable Long idCliente,@RequestBody TarjetaDTO tarjeta) {
        this.clientes.agregarTarjeta(idCliente,tarjeta.getNumTarjeta(),tarjeta.getNombre());
        return ResponseEntity.status(OK).body("Ok");
    }

    @GetMapping("/listar-tarjeta/{idCliente}")
    @Operation(summary = "Listar tarjetas de un Cliente")
    public ResponseEntity<?> listCards(@PathVariable Long idCliente) {
        return ResponseEntity.status(OK).body(this.clientes.listarTarjetas(idCliente));
    }


}
