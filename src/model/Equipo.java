package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Equipo {
    private String id;
    private String nombre;
    private String barrio;
    private String nombreEntrenador;
    private List<Jugador> jugadores;
    private List<Partido> partidos;

    public Equipo() {
        this.jugadores = new ArrayList<>();
        this.partidos = new ArrayList<>();
    }

    public Equipo(String id, String nombre, String barrio, String nombreEntrenador) {
        // Validación de parámetros nulos/vacíos (según el constructor original)
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del equipo no puede ser nulo o vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del equipo no puede ser nulo o vacío.");
        }
        if (barrio == null || barrio.trim().isEmpty()) {
            throw new IllegalArgumentException("El barrio del equipo no puede ser nulo o vacío.");
        }
        if (nombreEntrenador == null || nombreEntrenador.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del entrenador no puede ser nulo o vacío.");
        }

        this.id = id;
        this.nombre = nombre;
        this.barrio = barrio;
        this.nombreEntrenador = nombreEntrenador;
        this.jugadores = new ArrayList<>();
        this.partidos = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    public String getNombreEntrenador() { return nombreEntrenador; }
    public void setNombreEntrenador(String nombreEntrenador) { this.nombreEntrenador = nombreEntrenador; }
    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }
    public List<Partido> getPartidos() { return partidos; }
    public void setPartidos(List<Partido> partidos) { this.partidos = partidos; }

    /**
     * Calcula el número de partidos ganados.
     *
     * <p>Sugerencia de reutilización: {@link Partido#calcularGanador()}</p>
     *
     * @return El número de partidos ganados.
     */
    public int calcularPartidosGanados() {
        int ganados = 0;

        return ganados;
    }

    /**
     * Calcula el número de partidos empatados.
     * @return El número de partidos empatados.
     */
    public int calculaPartidosEmpatados() {
        int empatados = 0;

        return empatados;
    }

    /**
     * Calcula el número de partidos perdidos.
     *
     * <p>Sugerencia de reutilización: {@link Partido#calcularGanador()}</p>
     *
     * @return El número de partidos perdidos.
     */
    public int calculaPartidosPerdidos() {
        int perdidos = 0;

        return perdidos;
    }

    /**
     * Calcula el número de goles a favor.
     * @return El número de goles a favor.
     */
    public int calculaGolesAFavor() {
        int golesAFavor = 0;

        return golesAFavor;
    }

    /**
     * Calcula el número de goles en contra.
     * @return El número de goles en contra.
     */
    public int calculaGolesEnContra() {
        int golesEnContra = 0;

        return golesEnContra;
    }

    /**
     * Calcula el número de puntos.
     * Las reglas de puntos son:
     * 3 puntos por partido ganado.
     * 1 punto por partido empatado.
     * 0 puntos por partido perdido.
     *
     * <p>Sugerencia de reutilización: {@link #calcularPartidosGanados()}, {@link #calculaPartidosEmpatados()}</p>
     *
     * @return El número de puntos.
     */
    public int calculaPuntos() {
return 0;
    }

    /**
     * Calcula el número total de tarjetas amarillas y rojas en los partidos del equipo.
     * @return El número total de tarjetas.
     */
    public int calculaTotalTarjetas() {
        int totalTarjetas = 0;
        return totalTarjetas;
    }

    /**
     * Calcula el número total de faltas en los partidos del equipo.
     * @return El número total de faltas.
     */
    public int contarFaltas() {
        int totalFaltas = 0;
        return totalFaltas;
    }

    /**
     * Convierte el equipo a una representación en forma de cadena.
     * @return El nombre del equipo.
     */
    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Compara el equipo con otro objeto.
     * @param o El objeto a comparar.
     * @return true si el objeto es igual al equipo, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        return Objects.equals(id, equipo.id);
    }

    /**
     * Calcula el hash del equipo.
     * @return El hash del equipo.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}