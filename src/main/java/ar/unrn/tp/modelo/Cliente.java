package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.EmailException;
import ar.unrn.tp.excepciones.NotNumException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Cliente", uniqueConstraints = @UniqueConstraint(columnNames = "dni"))
public class Cliente {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    private String apellido;
    @Column(name ="dni", unique = true)
    private String dni;
    private String email;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Tarjeta> tarjetas;

    public Cliente(String nombre, String apellido, String dni, String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        if (dni == null || dni.isEmpty() || nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty() ) {
            throw new RuntimeException("Los datos proporcionados no son válidos");
        }
        if (!isNumeric(dni))
            throw new RuntimeException("No es un numero");
        Matcher mather = pattern.matcher(email);
        if (mather.find() == false)
            throw new RuntimeException("No es un email");
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
    }
    public void agregarTarjeta(Tarjeta tarjeta){
        this.tarjetas.add(tarjeta);
    }

    public Tarjeta obtenerTarjeta(int index){
        return tarjetas.get(index);
    }
    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
    public void setTarjeta(Tarjeta tarjeta) {
        if(tarjetas!=null) {
            agregarTarjeta(tarjeta);
        }else {
         this.tarjetas = new ArrayList<>();
         agregarTarjeta(tarjeta);
        }
    }

    public void setCliente(String nombre, String apellido, String dni, String email) {

        if (!getNombre().equalsIgnoreCase(nombre) && nombre != null)
            setNombre(nombre);
        if (!getApellido().equalsIgnoreCase(apellido) && apellido != null)
            setApellido(apellido);
        if (!getDni().equalsIgnoreCase(dni) && dni != null)
            setDni(dni);
        if (!getEmail().equalsIgnoreCase(email) && email != null)
            setEmail(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(getNombre(), cliente.getNombre()) && Objects.equals(getApellido(), cliente.getApellido()) && Objects.equals(getDni(), cliente.getDni()) && Objects.equals(getEmail(), cliente.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombre(), getApellido(), getDni(), getEmail());
    }
}
