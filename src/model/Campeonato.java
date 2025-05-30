// Archivo: model/Campeonato.java
package model;
import java.util.ArrayList;
import java.util.List;



/**
 * Representa un campeonato de fútbol, gestionando equipos y partidos.
 * Proporciona métodos para registrar entidades, crear partidos y obtener estadísticas generales.
 */
public class Campeonato {
    private List<Equipo> equipos;
    private List<Partido> partidos;

    /**
     * Constructor para crear una nueva instancia de Campeonato.
     * Inicializa las listas de equipos y partidos vacías.
     */
    public Campeonato() {
        this.equipos = new ArrayList<>();
        this.partidos = new ArrayList<>();
    }

    // Getters y Setters
    public List<Equipo> getEquipos() { return equipos; }
    public void setEquipos(List<Equipo> equipos) { this.equipos = equipos; }
    public List<Partido> getPartidos() { return partidos; }
    public void setPartidos(List<Partido> partidos) { this.partidos = partidos; }

    /**
     * Registra un nuevo equipo en el campeonato.
     * El equipo se crea con los datos proporcionados y se añade a la lista de equipos.
     * Si el ID del equipo ya existe, se retorna false.
     * Si el nombre del equipo ya existe, se retorna false.
     *
     * <p>Sugerencia de reutilización: {@link #buscarEquipoPorId(String)}, {@link #buscarEquipoPorNombre(String)}</p>
     *
     * @param id El identificador único del equipo. No debe ser nulo ni vacío.
     * @param nombre El nombre del equipo. No debe ser nulo ni vacío.
     * @param barrio El barrio del equipo. No debe ser nulo ni vacío.
     * @param nombreEntrenador El nombre del entrenador del equipo. No debe ser nulo ni vacío.
     * @return true si el equipo se registra correctamente, false si el ID o nombre ya existen, o si los parámetros son inválidos.
     */
    public boolean registrarEquipo(String id, String nombre, String barrio, String nombreEntrenador) {
        return true;
    }

    /**
     * Crea un nuevo partido y lo añade al campeonato.
     * El equipo visitante no puede ser el mismo que el local.
     *
     * <p>Sugerencia de reutilización: {@link #buscarEquipoPorId(String)}, {@link #buscarPartidoPorId(String)}</p>
     *
     * @param id El identificador único del partido. No debe ser nulo ni vacío.
     * @param idEquipoLocal El ID del equipo local. Debe corresponder a un equipo ya registrado.
     * @param idEquipoVisitante El ID del equipo visitante. Debe corresponder a un equipo ya registrado.
     * @param estadio El nombre del estadio donde se juega el partido. No debe ser nulo ni vacío.
     * @param arbitro El nombre del árbitro del partido. No debe ser nulo ni vacío.
     * @return true si el partido se crea correctamente, false si el ID del partido ya existe,
     * si los equipos no se encuentran, o si el equipo local es el mismo que el visitante.
     */
    public boolean crearPartido(String id, String idEquipoLocal, String idEquipoVisitante, String estadio, String arbitro) {
        return true;
    }

    /**
     * Registra un gol en un partido específico.
     * El idPartido debe ser un partido existente.
     * El idJugador debe ser un jugador existente en alguno de los equipos del partido.
     * El minuto debe ser un valor positivo.
     *
     * <p>Sugerencia de reutilización: {@link #buscarPartidoPorId(String)}, {@link Partido#obtenerEquipoJugador(String)}</p>
     *
     * @param idPartido El ID del partido donde se registró el gol. No debe ser nulo ni vacío.
     * @param idJugador El ID del jugador que anotó el gol. Debe corresponder a un jugador en uno de los equipos del partido.
     * @param minuto El minuto en el que se anotó el gol. Debe ser un valor positivo.
     * @return true si el gol se registra correctamente, false si el ID del partido o jugador no se encuentra, o si el minuto es inválido.
     */
    public boolean registrarGol(String idPartido, String idJugador, int minuto) {
       return true;
    }

    /**
     * Registra una tarjeta (amarilla o roja) en un partido específico.
     * El idPartido debe ser un partido existente.
     * El idJugador debe ser un jugador existente en alguno de los equipos del partido.
     * El tipo debe ser "Amarilla" o "Roja".
     * El minuto debe ser un valor positivo.
     *
     * <p>Sugerencia de reutilización: {@link #buscarPartidoPorId(String)}, {@link Partido#obtenerEquipoJugador(String)}</p>
     *
     * @param idPartido El ID del partido donde se mostró la tarjeta. No debe ser nulo ni vacío.
     * @param idJugador El ID del jugador que recibió la tarjeta. Debe pertenecer a uno de los equipos del partido.
     * @param tipo El tipo de tarjeta (ej. "Amarilla", "Roja"). No debe ser nulo ni vacío.
     * @param minuto El minuto en el que se mostró la tarjeta. Debe ser un valor positivo.
     * @param motivo El motivo por el cual se mostró la tarjeta. No debe ser nulo ni vacío.
     * @return true si la tarjeta se registra correctamente, false si el ID del partido o jugador no se encuentra, o si el tipo o minuto son inválidos.
     */
    public boolean registrarTarjeta(String idPartido, String idJugador, String tipo, int minuto, String motivo) {
        return true;
    }

    /**
     * Registra una falta en un partido específico.
     * El idPartido debe ser un partido existente.
     * El idJugadorComete debe ser un jugador existente en alguno de los equipos del partido y no puede ser el mismo que el idJugadorAfectado y no deben pertenecer al mismo equipo.
     * El idJugadorAfectado debe ser un jugador existente en alguno de los equipos del partido y no puede ser el mismo que el idJugadorComete y no deben pertenecer al mismo equipo.
     * El minuto debe ser un valor positivo.
     *
     * <p>Sugerencia de reutilización: {@link #buscarPartidoPorId(String)}, {@link Partido#obtenerEquipoJugador(String)}</p>
     *
     * @param idPartido El ID del partido donde ocurrió la falta. No debe ser nulo ni vacío.
     * @param idJugadorComete El ID del jugador que cometió la falta. Debe pertenecer a uno de los equipos del partido.
     * @param idJugadorAfectado El ID del jugador afectado por la falta. Debe pertenecer a uno de los equipos del partido.
     * @param minuto El minuto en el que se cometió la falta. Debe ser un valor positivo.
     * @param zona La zona del campo donde ocurrió la falta. No debe ser nulo ni vacío.
     * @param tarjeta El tipo de tarjeta asociada a la falta (ej. "Amarilla", "Roja"), o null si no hay tarjeta.
     * @return true si la falta se registra correctamente, false si el ID del partido o de los jugadores no se encuentra, si la zona es inválida, o si el minuto es inválido.
     */
    public boolean registrarFalta(String idPartido, String idJugadorComete, String idJugadorAfectado, int minuto, String zona, String tarjeta) {
        return true;
    }

    /**
     * Obtiene los puntos de un equipo específico en el campeonato.
     * El idEquipo debe ser un equipo existente en el campeonato. si no existe retorna -1
     *
     * <p>Sugerencia de reutilización: {@link #buscarEquipoPorId(String)}, {@link Equipo#calculaPuntos()}</p>
     *
     * @param idEquipo El ID del equipo del que se desean obtener los puntos. No debe ser nulo ni vacío.
     * @return Los puntos totales del equipo, o -1 si el equipo no se encuentra en el campeonato.
     * @throws IllegalArgumentException Si el ID del equipo es nulo o vacío.
     */
    public int getPuntosEquipo(String idEquipo) {
       return 0;
    }


    /**
     * Cuenta el número total de faltas cometidas por un equipo específico en todos sus partidos.
     *
     * <p>Sugerencia de reutilización: {@link #buscarEquipoPorId(String)}, {@link Equipo#contarFaltas()}</p>
     *
     * @param idEquipo El ID del equipo del que se desean contar las faltas. No debe ser nulo ni vacío.
     * @return El número total de faltas cometidas por el equipo, o -1 si el equipo no se encuentra.
     * @throws IllegalArgumentException Si el ID del equipo es nulo o vacío.
     */
    public int contarFaltasEquipo(String idEquipo) {
       return 0;
    }

    /**
     * Obtiene el nombre del equipo ganador de un partido específico.
     *
     * <p>Sugerencia de reutilización: {@link #buscarPartidoPorId(String)}, {@link Partido#calcularGanador()}</p>
     *
     * @param idPartido El ID del partido del que se desea obtener el ganador. No debe ser nulo ni vacío.
     * @return El nombre del equipo ganador, "Empate" si el partido terminó en empate, o "Partido no encontrado" si el ID no corresponde a ningún partido.
     * @throws IllegalArgumentException Si el ID del partido es nulo o vacío.
     */
    public String getGanadorPartido(String idPartido) {
        return "";
    }

    /**
     * Genera la tabla de posiciones actual del campeonato.
     * Los equipos se ordenan primero por puntos (descendente), luego por goles a favor (descendente),
     * y finalmente por goles en contra (ascendente) para desempate.
     *
     * @return Una lista de arrays de objetos, donde cada array representa una fila de la tabla de posiciones
     * con los datos del equipo (Nombre, Puntos, Ganados, Empatados, Perdidos, Goles Favor, Goles Contra, Tarjetas, Faltas).
     */
    public List<Object[]> getTablaDePosiciones() {
        List<Object[]> tabla = new ArrayList<>();
        // Note: The previous logic uses Java 8 Streams for sorting.
        // Reimplementing with for-loop for sorting can be complex (e.g., Bubble Sort, Insertion Sort).
        // For simplicity and to maintain current functionality, I will use a temporary list and Collections.sort
        // or a custom loop-based sort if truly necessary, but sorting with Comparator is generally preferred.
        // Assuming 'Collections.sort' is acceptable even without Streams as it still uses basic loop internally.

        // Create a mutable copy to sort
        List<Equipo> equiposOrdenados = new ArrayList<>(equipos);

        // Manual sorting using nested loops (Bubble Sort for demonstration as requested)
        for (int i = 0; i < equiposOrdenados.size() - 1; i++) {
            for (int j = 0; j < equiposOrdenados.size() - i - 1; j++) {
                Equipo eq1 = equiposOrdenados.get(j);
                Equipo eq2 = equiposOrdenados.get(j + 1);

                // Primary sort: Puntos (descending)
                if (eq1.calculaPuntos() < eq2.calculaPuntos()) {
                    // Swap
                    Equipo temp = equiposOrdenados.get(j);
                    equiposOrdenados.set(j, equiposOrdenados.get(j + 1));
                    equiposOrdenados.set(j + 1, temp);
                } else if (eq1.calculaPuntos() == eq2.calculaPuntos()) {
                    // Secondary sort: Goles a Favor (descending)
                    if (eq1.calculaGolesAFavor() < eq2.calculaGolesAFavor()) {
                        // Swap
                        Equipo temp = equiposOrdenados.get(j);
                        equiposOrdenados.set(j, equiposOrdenados.get(j + 1));
                        equiposOrdenados.set(j + 1, temp);
                    } else if (eq1.calculaGolesAFavor() == eq2.calculaGolesAFavor()) {
                        // Tertiary sort: Goles en Contra (ascending)
                        if (eq1.calculaGolesEnContra() > eq2.calculaGolesEnContra()) {
                            // Swap
                            Equipo temp = equiposOrdenados.get(j);
                            equiposOrdenados.set(j, equiposOrdenados.get(j + 1));
                            equiposOrdenados.set(j + 1, temp);
                        }
                    }
                }
            }
        }


        for (int i = 0; i < equiposOrdenados.size(); i++) {
            Equipo equipo = equiposOrdenados.get(i);
            tabla.add(new Object[]{
                    equipo.getNombre(),
                    equipo.calculaPuntos(),
                    equipo.calcularPartidosGanados(),
                    equipo.calculaPartidosEmpatados(),
                    equipo.calculaPartidosPerdidos(),
                    equipo.calculaGolesAFavor(),
                    equipo.calculaGolesEnContra(),
                    equipo.calculaTotalTarjetas(),
                    equipo.contarFaltas()
            });
        }
        return tabla;
    }

    /**
     * Calcula el número total de goles anotados por un jugador específico en todos los partidos del campeonato.
     *
     * <p>Sugerencia de reutilización: {@link #buscarPartidoPorId(String)} (indirectamente para obtener partidos), {@link Gol#getJugador()}</p>
     *
     * @param idJugador El ID del jugador del que se desean contar los goles. No debe ser nulo ni vacío.
     * @return El número total de goles anotados por el jugador.
     * @throws IllegalArgumentException Si el ID del jugador es nulo o vacío.
     */
    public int calculaGolesJugador(String idJugador) {

        return 0;
    }

    /**
     * Calcula el número total de tarjetas (amarillas y rojas) recibidas por un jugador específico
     * en todos los partidos del campeonato.
     *
     * <p>Sugerencia de reutilización: {@link #buscarPartidoPorId(String)} (indirectamente para obtener partidos), {@link Tarjeta#getJugador()}</p>
     *
     * @param idJugador El ID del jugador del que se desean contar las tarjetas. No debe ser nulo ni vacío.
     * @return El número total de tarjetas recibidas por el jugador.
     * @throws IllegalArgumentException Si el ID del jugador es nulo o vacío.
     */
    public int calculaTarjetasJugador(String idJugador) {
       return  0;
    }

    /**
     * Calcula el número total de faltas cometidas por un jugador específico
     * en todos los partidos del campeonato.
     *
     * <p>Sugerencia de reutilización: {@link #buscarPartidoPorId(String)} (indirectamente para obtener partidos), {@link Falta#getJugadorQueCometio()}</p>
     *
     * @param idJugador El ID del jugador del que se desean contar las faltas. No debe ser nulo ni vacío.
     * @return El número total de faltas cometidas por el jugador.
     * @throws IllegalArgumentException Si el ID del jugador es nulo o vacío.
     */
    public int calculaFaltasJugador(String idJugador) {

        return 0;
    }

    /**
     * Calcula el número de partidos en los que un jugador específico ha participado.
     * Un jugador se considera que ha participado si su equipo (local o visitante) jugó el partido y él es parte de ese equipo.
     *
     * <p>Sugerencia de reutilización: {@link #buscarPartidoPorId(String)} (indirectamente para obtener partidos), {@link Partido#getEquipoLocal()}, {@link Partido#getEquipoVisitante()}</p>
     *
     * @param idJugador El ID del jugador del que se desean contar los partidos jugados. No debe ser nulo ni vacío.
     * @return El número total de partidos jugados por el jugador.
     * @throws IllegalArgumentException Si el ID del jugador es nulo o vacío.
     */
    public int calculaPartidosJugadosJugador(String idJugador) {
        return 0;
    }

    /**
     * Busca un equipo por su ID.
     * @param id El ID del equipo a buscar.
     * @return El equipo encontrado, o null si no existe.
     */
    public Equipo buscarEquipoPorId(String id) {
        return null;
    }

    /**
     * Busca un equipo por su nombre.
     * @param nombre El nombre del equipo a buscar.
     * @return El equipo encontrado, o null si no existe.
     */
    public Equipo buscarEquipoPorNombre(String nombre) {
        return null;
    }

    /**
     * Busca un partido por su ID.
     * @param id El ID del partido a buscar.
     * @return El partido encontrado, o null si no existe.
     */
    public Partido buscarPartidoPorId(String id) {
        return null;
    }
}



