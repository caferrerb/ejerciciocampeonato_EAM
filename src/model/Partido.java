// Archivo: model/Partido.java
package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Partido {
    private String id;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private String estadio;
    private String arbitro;
    private int golesLocal;
    private int golesVisitante;
    private List<Tarjeta> tarjetas;
    private List<Falta> faltas;
    private List<Gol> goles;

    public Partido() {
        this.tarjetas = new ArrayList<>();
        this.faltas = new ArrayList<>();
        this.goles = new ArrayList<>();
    }

    public Partido(String id, Equipo equipoLocal, Equipo equipoVisitante, String estadio, String arbitro, LocalDateTime fechaHora) {
        this.id = id;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.estadio = estadio;
        this.arbitro = arbitro;
        this.golesLocal = 0;
        this.golesVisitante = 0;
        this.tarjetas = new ArrayList<>();
        this.faltas = new ArrayList<>();
        this.goles = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Equipo getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(Equipo equipoLocal) { this.equipoLocal = equipoLocal; }
    public Equipo getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(Equipo equipoVisitante) { this.equipoVisitante = equipoVisitante; }
    public String getEstadio() { return estadio; }
    public void setEstadio(String estadio) { this.estadio = estadio; }
    public String getArbitro() { return arbitro; }
    public void setArbitro(String arbitro) { this.arbitro = arbitro; }
    public int getGolesLocal() { return golesLocal; }
    public void setGolesLocal(int golesLocal) { this.golesLocal = golesLocal; }
    public int getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(int golesVisitante) { this.golesVisitante = golesVisitante; }
    public List<Tarjeta> getTarjetas() { return tarjetas; }
    public void setTarjetas(List<Tarjeta> tarjetas) { this.tarjetas = tarjetas; }
    public List<Falta> getFaltas() { return faltas; }
    public void setFaltas(List<Falta> faltas) { this.faltas = faltas; }
    public List<Gol> getGoles() { return goles; }
    public void setGoles(List<Gol> goles) { this.goles = goles; }

    /**
     * Registra un gol para un jugador específico.
     * Si el jugador no pertenece a ninguno de los equipos participantes en el partido, se retorna false.
     * Si el jugador es del equipo local, se aumenta el número de goles del equipo local en 1.
     * Si el jugador es del equipo visitante, se aumenta el número de goles del equipo visitante en 1.
     *
     * <p>Sugerencia de reutilización: {@link #obtenerEquipoJugador(String)}</p>
     *
     * @param jugador El jugador que anotó el gol.
     * @param minuto El minuto en el que se anotó el gol.
     * @return true si el gol se registra correctamente, false en caso contrario.
     */
    public boolean registrarGol(Jugador jugador, int minuto) {
        if (jugador == null || minuto <= 0) {
            System.err.println("Error al registrar gol: Jugador nulo o minuto inválido.");
            return false;
        }

        // Verificar si el jugador pertenece a alguno de los equipos del partido
        // Reutiliza getEquipoJugador para verificar si el jugador está en el partido
        if (obtenerEquipoJugador(jugador.getId()) == null) {
            System.err.println("Error al registrar gol: El jugador no pertenece a ninguno de los equipos del partido.");
            return false;
        }

        // Registrar el gol
        Gol nuevoGol = new Gol(jugador, minuto);
        this.goles.add(nuevoGol);

        // Aumentar el contador de goles del equipo correspondiente
        if (jugador.getEquipo() != null) {
            if (jugador.getEquipo().equals(equipoLocal)) {
                golesLocal++;
            } else if (jugador.getEquipo().equals(equipoVisitante)) {
                golesVisitante++;
            } else {
                System.err.println("Advertencia: El jugador anotador no pertenece a los equipos de este partido. Gol registrado pero no sumado a equipos.");
            }
        }
        return true;
    }

    /**
     * Devuelve el equipo al que pertenece el jugador.
     * Verifica que el jugador pertenezca a alguno de los equipos participantes en el partido, ya sea local o visitante.
     * @param idJugador El ID del jugador.
     * @return El equipo al que pertenece el jugador, o null si el jugador no se encuentra en ninguno de los equipos del partido.
     */
    public Equipo obtenerEquipoJugador(String idJugador) {
        return null;
    }

    /**
     * Registra una tarjeta para un jugador específico.
     *
     * <p>Sugerencia de reutilización: {@link #obtenerEquipoJugador(String)}</p>
     *
     * @param jugador El jugador que recibe la tarjeta.
     * @param tipo El tipo de tarjeta (amarilla o roja).
     * @param minuto El minuto en el que se recibe la tarjeta.
     * @param motivo El motivo de la tarjeta.
     */
    public boolean registrarTarjetaPorIdJugador(Jugador jugador, String tipo, int minuto, String motivo) {
        return false;
    }

    /**
     * Calcula el número total de faltas de un equipo en un partido.
     * * @param idEquipo El ID del equipo.
     * si es el id retorna el numero de faltas del equipo local,
     * si es el id del equipo visitante retorna el numero de faltas del equipo visitante,
     * si no existe retorna -1
     *
     * <p>Sugerencia de reutilización: {@link #getEquipoLocal()}, {@link #getEquipoVisitante()}, {@link Falta#getJugadorQueCometio()}</p>
     *
     * @return El número total de faltas del equipo.
     */
    public int calcularFaltasEquipo(String idEquipo){
       return 0;
    }


    /**
     * Calcula el número total de tarjetas de un equipo en un partido.
     * * @param idEquipo El ID del equipo.
     * si es el id retorna el numero de tarjetas del equipo local,
     * si es el id del equipo visitante retorna el numero de tarjetas del equipo visitante,
     * si no existe retorna -1
     *
     * <p>Sugerencia de reutilización: {@link #getEquipoLocal()}, {@link #getEquipoVisitante()}, {@link Tarjeta#getJugador()}</p>
     *
     * @return El número total de tarjetas del equipo.
     */
    public int calcularTarjetasEquipo(String idEquipo){
       return 0;
    }

    /**
     * Calcula el número total de goles de un equipo en un partido.
     * * @param idEquipo El ID del equipo.
     * si es el id retorna el numero de goles del equipo local,
     * si es el id del equipo visitante retorna el numero de goles del equipo visitante,
     * si no existe retorna -1
     *
     * <p>Sugerencia de reutilización: {@link #getEquipoLocal()}, {@link #getEquipoVisitante()}</p>
     *
     * @return El número total de goles del equipo.
     */
    public int calcularGolesEquipo(String idEquipo){
       return 0;
    }

    /**
     * Registra una falta para un jugador específico.
     *
     * <p>Sugerencia de reutilización: {@link #obtenerEquipoJugador(String)}</p>
     *
     * @param jugadorComete El jugador que comete la falta.
     * @param jugadorAfectado El jugador que sufre la falta.
     * @param minuto El minuto en el que se comete la falta.
     * @param zona La zona en la que se comete la falta.
     */
    public boolean registrarFaltaPorIdJugador(Jugador jugadorComete, Jugador jugadorAfectado, int minuto, String zona) {
        return false;
    }

    /**
     * Calcula el equipo ganador del partido.
     * @return El equipo ganador.
     */
    public Equipo calcularGanador() {
        return null;
    }

    @Override
    public String toString() {
        return equipoLocal.getNombre() + " vs " + equipoVisitante.getNombre() + " (" + golesLocal + "-" + golesVisitante + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partido partido = (Partido) o;
        return Objects.equals(id, partido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
