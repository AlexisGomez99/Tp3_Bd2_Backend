package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.NotNullException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Objects;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "Producto", uniqueConstraints = @UniqueConstraint(columnNames = "codigo"))
public class Producto {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "codigo", unique = true)
    private String codigo;
    private String descripcion;

    @OneToOne
    private Categoria categoria;
    private double precio;
    @OneToOne
    private Marca marca;

    public Producto(String codigo, String descripcion, double precio, Categoria categoria , Marca marca) throws NotNullException {
        if (descripcion == null)
            throw new NotNullException("descripcion");
        if (precio < 0)
            throw new NotNullException("precio");
        if (categoria == null)
            throw new NotNullException("categoria");
        if (marca == null)
            throw new NotNullException("marca");

        this.codigo = codigo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.marca = marca;
    }

    public boolean esDeMarca(String marcaN) {
        boolean x=false;
        if(this.marca.getNombre().equalsIgnoreCase(marcaN)){
            x = true;
        }
        return x;
    }

    public void modificarProducto(String codigo, String descripcion, double precio, Categoria categoria, Marca marca) {
        if (!getCodigo().equalsIgnoreCase(codigo) && codigo != null)
            setCodigo(codigo);
        if (!getDescripcion().equalsIgnoreCase(descripcion) && descripcion != null)
            setDescripcion(descripcion);
        if (getPrecio() == precio && precio > 0)
            setPrecio(precio);
        if (!categoria.equals(getCategoria()) && categoria != null) {
            setCategoria(categoria);
        }
        if (!marca.equals(getCategoria()) && marca != null) {
            setMarca(marca);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return Double.compare(producto.getPrecio(), getPrecio()) == 0 && getCodigo().equals(producto.getCodigo()) && getDescripcion().equals(producto.getDescripcion()) && getCategoria().equals(producto.getCategoria()) && getMarca().equals(producto.getMarca());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodigo(), getDescripcion(), getCategoria(), getPrecio(), getMarca());
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria=" + categoria +
                ", precio=" + precio +
                ", marca=" + marca +
                '}';
    }
}
