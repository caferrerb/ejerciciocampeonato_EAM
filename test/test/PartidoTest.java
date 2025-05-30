package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class PartidoTest {

    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private Jugador jugadorLocal1;
    private Jugador jugadorLocal2;
    private Jugador jugadorVisitante1;
    private Jugador jugadorVisitante2;
    private Partido partido;

    @BeforeEach
    void setUp() {
        equipoLocal = new Equipo("EL01", "Los Leones", "Zona Centro", "Coach Leo");
        equipoVisitante = new Equipo("EV01", "Las Aguilas", "Zona Sur", "Coach Aguila");

        jugadorLocal1 = new Jugador("JL1", "Juan Perez", "Delantero", 9, equipoLocal);
        jugadorLocal2 = new Jugador("JL2", "Pedro Gomez", "Defensa", 3, equipoLocal);
        jugadorVisitante1 = new Jugador("JV1", "Carlos Ruiz", "Delantero", 7, equipoVisitante);
        jugadorVisitante2 = new Jugador("JV2", "Maria Lopez", "Mediocampista", 8, equipoVisitante);

        equipoLocal.getJugadores().add(jugadorLocal1);
        equipoLocal.getJugadores().add(jugadorLocal2);
        equipoVisitante.getJugadores().add(jugadorVisitante1);
        equipoVisitante.getJugadores().add(jugadorVisitante2);

        partido = new Partido("P001", equipoLocal, equipoVisitante, "Estadio Olimpico", "Roberto Funes", LocalDateTime.now());
    }

    @Test
    @DisplayName("Test constructor initializes lists and attributes")
    void testConstructor() {
        assertNotNull(partido);
        assertEquals("P001", partido.getId());
        assertEquals(equipoLocal, partido.getEquipoLocal());
        assertEquals(equipoVisitante, partido.getEquipoVisitante());
        assertEquals("Estadio Olimpico", partido.getEstadio());
        assertEquals("Roberto Funes", partido.getArbitro());
        assertEquals(0, partido.getGolesLocal());
        assertEquals(0, partido.getGolesVisitante());
        assertNotNull(partido.getTarjetas());
        assertTrue(partido.getTarjetas().isEmpty());
        assertNotNull(partido.getFaltas());
        assertTrue(partido.getFaltas().isEmpty());
        assertNotNull(partido.getGoles());
        assertTrue(partido.getGoles().isEmpty());
    }

    @Test
    @DisplayName("Test registrarGol - Exito para equipo local")
    void testRegistrarGol_SuccessLocalTeam() {
        assertTrue(partido.registrarGol(jugadorLocal1, 10), "Should successfully register a goal for local team");
        assertEquals(1, partido.getGolesLocal(), "Local team goals should increase");
        assertEquals(1, partido.getGoles().size(), "Goals list should contain the new goal");
        assertEquals(jugadorLocal1, partido.getGoles().get(0).getJugador());
    }

    @Test
    @DisplayName("Test registrarGol - Exito para equipo visitante")
    void testRegistrarGol_SuccessVisitorTeam() {
        assertTrue(partido.registrarGol(jugadorVisitante1, 25), "Should successfully register a goal for visitor team");
        assertEquals(1, partido.getGolesVisitante(), "Visitor team goals should increase");
        assertEquals(1, partido.getGoles().size(), "Goals list should contain the new goal");
        assertEquals(jugadorVisitante1, partido.getGoles().get(0).getJugador());
    }

    @Test
    @DisplayName("Test registrarGol - Jugador no pertenece a ningún equipo del partido")
    void testRegistrarGol_PlayerNotInMatchTeams() {
        Equipo otroEquipo = new Equipo("EOO", "Otro Equipo", "Otro Barrio", "Otro Coach");
        Jugador jugadorExterno = new Jugador("JE1", "Jugador Externo", "Defensa", 5, otroEquipo);

        assertFalse(partido.registrarGol(jugadorExterno, 30), "Should not register goal if player is not in match teams");
        assertEquals(0, partido.getGolesLocal());
        assertEquals(0, partido.getGolesVisitante());
        assertTrue(partido.getGoles().isEmpty());
    }

    @Test
    @DisplayName("Test registrarGol - Parámetros inválidos (jugador nulo)")
    void testRegistrarGol_NullPlayer() {
        assertFalse(partido.registrarGol(null, 10), "Should not register goal with null player");
        assertEquals(0, partido.getGolesLocal());
        assertEquals(0, partido.getGolesVisitante());
    }

    @Test
    @DisplayName("Test registrarGol - Parámetros inválidos (minuto no positivo)")
    void testRegistrarGol_InvalidMinute() {
        assertFalse(partido.registrarGol(jugadorLocal1, 0), "Should not register goal with 0 minute");
        assertFalse(partido.registrarGol(jugadorLocal1, -5), "Should not register goal with negative minute");
        assertEquals(0, partido.getGolesLocal());
        assertEquals(0, partido.getGolesVisitante());
    }

    @Test
    @DisplayName("Test getEquipoJugador - Jugador del equipo local encontrado")
    void testObtenerEquipoJugador_LocalPlayerFound() {
        assertEquals(equipoLocal, partido.obtenerEquipoJugador("JL1"), "Should find local team for local player ID");
    }

    @Test
    @DisplayName("Test getEquipoJugador - Jugador del equipo visitante encontrado")
    void testObtenerEquipoJugador_VisitorPlayerFound() {
        assertEquals(equipoVisitante, partido.obtenerEquipoJugador("JV1"), "Should find visitor team for visitor player ID");
    }

    @Test
    @DisplayName("Test getEquipoJugador - Jugador no encontrado")
    void testObtenerEquipoJugador_PlayerNotFound() {
        assertNull(partido.obtenerEquipoJugador("J999"), "Should return null if player not found in either team");
    }

    @Test
    @DisplayName("Test getEquipoJugador - ID de jugador nulo o vacío")
    void testObtenerEquipoJugador_InvalidPlayerId() {
        assertNull(partido.obtenerEquipoJugador(null), "Should return null for null player ID");
        assertNull(partido.obtenerEquipoJugador(""), "Should return null for empty player ID");
    }

    @Test
    @DisplayName("Test registrarTarjetaPorIdJugador - Exito (Amarilla)")
    void testRegistrarTarjetaPorIdJugador_SuccessYellow() {
        assertTrue(partido.registrarTarjetaPorIdJugador(jugadorLocal1, "Amarilla", 15, "Falta"));
        assertEquals(1, partido.getTarjetas().size());
        assertEquals("Amarilla", partido.getTarjetas().get(0).getTipo());
        assertEquals(jugadorLocal1, partido.getTarjetas().get(0).getJugador());
    }

    @Test
    @DisplayName("Test registrarTarjetaPorIdJugador - Exito (Roja)")
    void testRegistrarTarjetaPorIdJugador_SuccessRed() {
        assertTrue(partido.registrarTarjetaPorIdJugador(jugadorVisitante1, "Roja", 70, "Expulsión"));
        assertEquals(1, partido.getTarjetas().size());
        assertEquals("Roja", partido.getTarjetas().get(0).getTipo());
        assertEquals(jugadorVisitante1, partido.getTarjetas().get(0).getJugador());
    }

    @Test
    @DisplayName("Test registrarTarjetaPorIdJugador - Jugador no pertenece a ningún equipo del partido")
    void testRegistrarTarjetaPorIdJugador_PlayerNotInMatchTeams() {
        Equipo otroEquipo = new Equipo("EOO", "Otro Equipo", "Otro Barrio", "Otro Coach");
        Jugador jugadorExterno = new Jugador("JE1", "Jugador Externo", "Defensa", 5, otroEquipo);
        assertFalse(partido.registrarTarjetaPorIdJugador(jugadorExterno, "Amarilla", 30, "Motivo"));
        assertTrue(partido.getTarjetas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarTarjetaPorIdJugador - Parámetros inválidos (tipo de tarjeta)")
    void testRegistrarTarjetaPorIdJugador_InvalidType() {
        assertFalse(partido.registrarTarjetaPorIdJugador(jugadorLocal1, "Azul", 10, "Motivo"));
        assertTrue(partido.getTarjetas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarTarjetaPorIdJugador - Parámetros nulos o vacíos")
    void testRegistrarTarjetaPorIdJugador_InvalidParameters() {
        assertFalse(partido.registrarTarjetaPorIdJugador(null, "Amarilla", 10, "Motivo"));
        assertFalse(partido.registrarTarjetaPorIdJugador(jugadorLocal1, null, 10, "Motivo"));
        assertFalse(partido.registrarTarjetaPorIdJugador(jugadorLocal1, "Amarilla", 0, "Motivo"));
        assertFalse(partido.registrarTarjetaPorIdJugador(jugadorLocal1, "Amarilla", 10, null));
        assertTrue(partido.getTarjetas().isEmpty());
    }

    @Test
    @DisplayName("Test calcularFaltasEquipo - Exito para equipo local")
    void testCalcularFaltasEquipo_SuccessLocalTeam() {
        // Asegúrate de que los jugadores tengan sus equipos asociados para que getJugadorQueCometio().getEquipo() funcione.
        // Esto ya se hace en setUp, pero es una buena verificación conceptual.
        partido.getFaltas().add(new Falta(jugadorLocal1, 10, "Defensa", jugadorVisitante1, null)); // Local commits
        partido.getFaltas().add(new Falta(jugadorLocal2, 20, "Medio", jugadorVisitante2, null)); // Local commits
        partido.getFaltas().add(new Falta(jugadorVisitante1, 30, "Ataque", jugadorLocal1, null)); // Visitor commits

        assertEquals(2, partido.calcularFaltasEquipo(equipoLocal.getId()), "Local team should have 2 fouls");
    }

    @Test
    @DisplayName("Test calcularFaltasEquipo - Exito para equipo visitante")
    void testCalcularFaltasEquipo_SuccessVisitorTeam() {
        partido.getFaltas().add(new Falta(jugadorLocal1, 10, "Defensa", jugadorVisitante1, null)); // Local commits
        partido.getFaltas().add(new Falta(jugadorVisitante1, 30, "Ataque", jugadorLocal1, null)); // Visitor commits
        partido.getFaltas().add(new Falta(jugadorVisitante2, 40, "Centro", jugadorLocal2, null)); // Visitor commits

        assertEquals(2, partido.calcularFaltasEquipo(equipoVisitante.getId()), "Visitor team should have 2 fouls");
    }

    @Test
    @DisplayName("Test calcularFaltasEquipo - Equipo sin faltas")
    void testCalcularFaltasEquipo_NoFouls() {
        assertEquals(0, partido.calcularFaltasEquipo(equipoLocal.getId()), "Local team should have 0 fouls if none registered");
    }

    @Test
    @DisplayName("Test calcularFaltasEquipo - Equipo no encontrado en el partido")
    void testCalcularFaltasEquipo_TeamNotFound() {
        Equipo equipoDesconocido = new Equipo("E999", "Equipo Desconocido", "Desconocido", "Desconocido");
        assertEquals(-1, partido.calcularFaltasEquipo(equipoDesconocido.getId()), "Should return -1 if team is not part of this match");
    }

    @Test
    @DisplayName("Test calcularFaltasEquipo - ID de equipo nulo o vacío")
    void testCalcularFaltasEquipo_InvalidTeamId() {
        assertEquals(-1, partido.calcularFaltasEquipo(null), "Should return -1 for null team ID");
        assertEquals(-1, partido.calcularFaltasEquipo(""), "Should return -1 for empty team ID");
    }

    @Test
    @DisplayName("Test calcularTarjetasEquipo - Exito para equipo local")
    void testCalcularTarjetasEquipo_SuccessLocalTeam() {
        partido.getTarjetas().add(new Tarjeta(jugadorLocal1, "Amarilla", 10, "Falta")); // Local gets card
        partido.getTarjetas().add(new Tarjeta(jugadorLocal2, "Roja", 20, "Juego brusco")); // Local gets card
        partido.getTarjetas().add(new Tarjeta(jugadorVisitante1, "Amarilla", 30, "Simulacion")); // Visitor gets card

        assertEquals(2, partido.calcularTarjetasEquipo(equipoLocal.getId()), "Local team should have 2 cards");
    }

    @Test
    @DisplayName("Test calcularTarjetasEquipo - Exito para equipo visitante")
    void testCalcularTarjetasEquipo_SuccessVisitorTeam() {
        partido.getTarjetas().add(new Tarjeta(jugadorLocal1, "Amarilla", 10, "Falta")); // Local gets card
        partido.getTarjetas().add(new Tarjeta(jugadorVisitante1, "Amarilla", 30, "Simulacion")); // Visitor gets card
        partido.getTarjetas().add(new Tarjeta(jugadorVisitante2, "Roja", 40, "Agresion")); // Visitor gets card

        assertEquals(2, partido.calcularTarjetasEquipo(equipoVisitante.getId()), "Visitor team should have 2 cards");
    }

    @Test
    @DisplayName("Test calcularTarjetasEquipo - Equipo sin tarjetas")
    void testCalcularTarjetasEquipo_NoCards() {
        assertEquals(0, partido.calcularTarjetasEquipo(equipoLocal.getId()), "Local team should have 0 cards if none registered");
    }

    @Test
    @DisplayName("Test calcularTarjetasEquipo - Equipo no encontrado en el partido")
    void testCalcularTarjetasEquipo_TeamNotFound() {
        Equipo equipoDesconocido = new Equipo("E999", "Equipo Desconocido", "Desconocido", "Desconocido");
        assertEquals(-1, partido.calcularTarjetasEquipo(equipoDesconocido.getId()), "Should return -1 if team is not part of this match");
    }

    @Test
    @DisplayName("Test calcularTarjetasEquipo - ID de equipo nulo o vacío")
    void testCalcularTarjetasEquipo_InvalidTeamId() {
        assertEquals(-1, partido.calcularTarjetasEquipo(null), "Should return -1 for null team ID");
        assertEquals(-1, partido.calcularTarjetasEquipo(""), "Should return -1 for empty team ID");
    }

    @Test
    @DisplayName("Test calcularGolesEquipo - Exito para equipo local")
    void testCalcularGolesEquipo_SuccessLocalTeam() {
        partido.setGolesLocal(3); // Set goals directly
        partido.setGolesVisitante(1); // Set goals directly
        assertEquals(3, partido.calcularGolesEquipo(equipoLocal.getId()), "Local team should have 3 goals");
    }

    @Test
    @DisplayName("Test calcularGolesEquipo - Exito para equipo visitante")
    void testCalcularGolesEquipo_SuccessVisitorTeam() {
        partido.setGolesLocal(1); // Set goals directly
        partido.setGolesVisitante(2); // Set goals directly
        assertEquals(2, partido.calcularGolesEquipo(equipoVisitante.getId()), "Visitor team should have 2 goals");
    }

    @Test
    @DisplayName("Test calcularGolesEquipo - Equipo sin goles")
    void testCalcularGolesEquipo_NoGoals() {
        assertEquals(0, partido.calcularGolesEquipo(equipoLocal.getId()), "Local team should have 0 goals if none registered");
    }

    @Test
    @DisplayName("Test calcularGolesEquipo - Equipo no encontrado en el partido")
    void testCalcularGolesEquipo_TeamNotFound() {
        Equipo equipoDesconocido = new Equipo("E999", "Equipo Desconocido", "Desconocido", "Desconocido");
        assertEquals(-1, partido.calcularGolesEquipo(equipoDesconocido.getId()), "Should return -1 if team is not part of this match");
    }

    @Test
    @DisplayName("Test calcularGolesEquipo - ID de equipo nulo o vacío")
    void testCalcularGolesEquipo_InvalidTeamId() {
        assertEquals(-1, partido.calcularGolesEquipo(null), "Should return -1 for null team ID");
        assertEquals(-1, partido.calcularGolesEquipo(""), "Should return -1 for empty team ID");
    }

    @Test
    @DisplayName("Test registrarFaltaPorIdJugador - Exito")
    void testRegistrarFaltaPorIdJugador_Success() {
        assertTrue(partido.registrarFaltaPorIdJugador(jugadorLocal1, jugadorVisitante1, 30, "Medio campo"), "Should successfully register a foul");
        assertEquals(1, partido.getFaltas().size(), "Fouls list should contain the new foul");
        assertEquals(jugadorLocal1, partido.getFaltas().get(0).getJugadorQueCometio());
        assertEquals(jugadorVisitante1, partido.getFaltas().get(0).getJugadorAfectado());
    }

    @Test
    @DisplayName("Test registrarFaltaPorIdJugador - Jugador que comete no pertenece al partido")
    void testRegistrarFaltaPorIdJugador_ComittingPlayerNotInMatch() {
        Equipo otroEquipo = new Equipo("EOO", "Otro Equipo", "Otro Barrio", "Otro Coach");
        Jugador jugadorExterno = new Jugador("JE1", "Jugador Externo", "Delantero", 5, otroEquipo);
        assertFalse(partido.registrarFaltaPorIdJugador(jugadorExterno, jugadorVisitante1, 30, "Medio campo"));
        assertTrue(partido.getFaltas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarFaltaPorIdJugador - Jugador afectado no pertenece al partido")
    void testRegistrarFaltaPorIdJugador_AffectedPlayerNotInMatch() {
        Equipo otroEquipo = new Equipo("EOO", "Otro Equipo", "Otro Barrio", "Otro Coach");
        Jugador jugadorExterno = new Jugador("JE1", "Jugador Externo", "Delantero", 5, otroEquipo);
        assertFalse(partido.registrarFaltaPorIdJugador(jugadorLocal1, jugadorExterno, 30, "Medio campo"));
        assertTrue(partido.getFaltas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarFaltaPorIdJugador - Jugador que comete y afectado son el mismo")
    void testRegistrarFaltaPorIdJugador_SamePlayers() {
        assertFalse(partido.registrarFaltaPorIdJugador(jugadorLocal1, jugadorLocal1, 30, "Medio campo"));
        assertTrue(partido.getFaltas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarFaltaPorIdJugador - Jugadores del mismo equipo")
    void testRegistrarFaltaPorIdJugador_SameTeamPlayers() {
        assertFalse(partido.registrarFaltaPorIdJugador(jugadorLocal1, jugadorLocal2, 30, "Medio campo"));
        assertTrue(partido.getFaltas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarFaltaPorIdJugador - Parámetros nulos o vacíos")
    void testRegistrarFaltaPorIdJugador_InvalidParameters() {
        assertFalse(partido.registrarFaltaPorIdJugador(null, jugadorVisitante1, 30, "Medio campo"));
        assertFalse(partido.registrarFaltaPorIdJugador(jugadorLocal1, null, 30, "Medio campo"));
        assertFalse(partido.registrarFaltaPorIdJugador(jugadorLocal1, jugadorVisitante1, 0, "Medio campo"));
        assertFalse(partido.registrarFaltaPorIdJugador(jugadorLocal1, jugadorVisitante1, 30, null));
        assertFalse(partido.registrarFaltaPorIdJugador(jugadorLocal1, jugadorVisitante1, 30, ""));
        assertTrue(partido.getFaltas().isEmpty());
    }

    @Test
    @DisplayName("Test calcularGanador - Equipo local gana")
    void testCalcularGanador_LocalWins() {
        partido.setGolesLocal(2); // Set local goals
        partido.setGolesVisitante(1); // Set visitor goals
        assertEquals(equipoLocal, partido.calcularGanador(), "Local team should be the winner");
    }

    @Test
    @DisplayName("Test calcularGanador - Equipo visitante gana")
    void testCalcularGanador_VisitorWins() {
        partido.setGolesLocal(1); // Set local goals
        partido.setGolesVisitante(3); // Set visitor goals
        assertEquals(equipoVisitante, partido.calcularGanador(), "Visitor team should be the winner");
    }

    @Test
    @DisplayName("Test calcularGanador - Empate")
    void testCalcularGanador_Draw() {
        partido.setGolesLocal(0); // Set local goals
        partido.setGolesVisitante(0); // Set visitor goals
        assertNull(partido.calcularGanador(), "Should return null for a draw");
    }

    @Test
    @DisplayName("Test toString")
    void testToString() {
        partido.setGolesLocal(2); // Set local goals
        partido.setGolesVisitante(1); // Set visitor goals
        assertEquals("Los Leones vs Las Aguilas (2-1)", partido.toString());
    }

    @Test
    @DisplayName("Test equals - Same ID")
    void testEquals_SameId() {
        Partido partido2 = new Partido("P001", equipoLocal, equipoVisitante, "Otro Estadio", "Otro Arbitro", LocalDateTime.now());
        assertTrue(partido.equals(partido2), "Matches with the same ID should be equal");
    }

    @Test
    @DisplayName("Test equals - Different ID")
    void testEquals_DifferentId() {
        Partido partido3 = new Partido("P002", equipoLocal, equipoVisitante, "Estadio", "Arbitro", LocalDateTime.now());
        assertFalse(partido.equals(partido3), "Matches with different IDs should not be equal");
    }

    @Test
    @DisplayName("Test equals - Null object")
    void testEquals_NullObject() {
        assertFalse(partido.equals(null), "Should return false when comparing with null");
    }

    @Test
    @DisplayName("Test equals - Different class")
    void testEquals_DifferentClass() {
        Object obj = new Object();
        assertFalse(partido.equals(obj), "Should return false when comparing with an object of a different class");
    }

    @Test
    @DisplayName("Test hashCode - Same ID")
    void testHashCode_SameId() {
        Partido partido2 = new Partido("P001", equipoLocal, equipoVisitante, "Otro Estadio", "Otro Arbitro", LocalDateTime.now());
        assertEquals(partido.hashCode(), partido2.hashCode(), "Hash codes should be equal for matches with the same ID");
    }

    @Test
    @DisplayName("Test hashCode - Different ID")
    void testHashCode_DifferentId() {
        Partido partido3 = new Partido("P002", equipoLocal, equipoVisitante, "Estadio", "Arbitro", LocalDateTime.now());
        assertNotEquals(partido.hashCode(), partido3.hashCode(), "Hash codes should not be equal for matches with different IDs");
    }
}