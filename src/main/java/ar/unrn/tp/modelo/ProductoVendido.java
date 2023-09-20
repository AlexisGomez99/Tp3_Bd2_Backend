package ar.unrn.tp.modelo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductoVendido {
    @Id
    @GeneratedValue
    private Long id;
    private String codigo;
    private String descripcion;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Categoria categoria;
    private double precio;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Marca marca;

    public ProductoVendido(String codigo, String descripcion, Categoria categoria, double precio, Marca marca) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.marca = marca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductoVendido)) return false;
        ProductoVendido that = (ProductoVendido) o;
        return Double.compare(that.getPrecio(), getPrecio()) == 0 && getCodigo().equals(that.getCodigo()) && getDescripcion().equals(that.getDescripcion()) && getCategoria().equals(that.getCategoria()) && getMarca().equals(that.getMarca());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodigo(), getDescripcion(), getCategoria(), getPrecio(), getMarca());
    }

}
