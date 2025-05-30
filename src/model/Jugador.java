// Archivo: model/Jugador.java
package model;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Jugador {
    private String id;
    private String nombreCompleto;
    private String posicion;
    private int numeroCamiseta;
    private Equipo equipo;

    public Jugador() {
    }

    public Jugador(String id, String nombreCompleto, String posicion, int numeroCamiseta, Equipo equipo) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
        this.equipo = equipo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombreCompleto() { return nombreCompleto; } // Corregido: nombreComplepleto a nombreCompleto
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
    public int getNumeroCamiseta() { return numeroCamiseta; }
    public void setNumeroCamiseta(int numeroCamiseta) { this.numeroCamiseta = numeroCamiseta; }
    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    @Override
    public String toString() {
        return nombreCompleto + " (" + (equipo != null ? equipo.getNombre() : "N/A") + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(id, jugador.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}