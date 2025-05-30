package test;

import model.Campeonato;
import model.Equipo;
import model.Jugador;
import model.Partido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CampeonatoTest {

    private Campeonato campeonato;
    private Equipo equipoA;
    private Equipo equipoB;
    private Equipo equipoC;
    private Jugador jugador1A;
    private Jugador jugador2A;
    private Jugador jugador1B;
    private Jugador jugador2B;
    private Partido partido1;
    private Partido partido2;

    @BeforeEach
    void setUp() {
        campeonato = new Campeonato();

        // Setup Equipos
        equipoA = new Equipo("E001", "Equipo Alpha", "Barrio Norte", "Entrenador A");
        equipoB = new Equipo("E002", "Equipo Beta", "Barrio Sur", "Entrenador B");
        equipoC = new Equipo("E003", "Equipo Gamma", "Barrio Este", "Entrenador C");

        // Setup Jugadores
        jugador1A = new Jugador("J001", "Jugador 1A", "Delantero", 10, equipoA);
        jugador2A = new Jugador("J002", "Jugador 2A", "Defensa", 2, equipoA);
        jugador1B = new Jugador("J003", "Jugador 1B", "Delantero", 9, equipoB);
        jugador2B = new Jugador("J004", "Jugador 2B", "Mediocampista", 8, equipoB);

        equipoA.getJugadores().add(jugador1A);
        equipoA.getJugadores().add(jugador2A);
        equipoB.getJugadores().add(jugador1B);
        equipoB.getJugadores().add(jugador2B);

        // Add equipos to campeonato for testing
        campeonato.getEquipos().add(equipoA);
        campeonato.getEquipos().add(equipoB);
        campeonato.getEquipos().add(equipoC);

        // Setup Partidos
        partido1 = new Partido("P001", equipoA, equipoB, "Estadio Principal", "Árbitro Uno", LocalDateTime.now());
        partido2 = new Partido("P002", equipoA, equipoC, "Estadio Secundario", "Árbitro Dos", LocalDateTime.now());

        // Add partidos to campeonato for testing
        campeonato.getPartidos().add(partido1);
        campeonato.getPartidos().add(partido2);

        // Associate partidos with teams
        equipoA.getPartidos().add(partido1);
        equipoA.getPartidos().add(partido2);
        equipoB.getPartidos().add(partido1);
        equipoC.getPartidos().add(partido2);
    }

    @Test
    @DisplayName("Test constructor initializes empty lists")
    void testConstructor() {
        Campeonato newCampeonato = new Campeonato();
        assertNotNull(newCampeonato.getEquipos(), "Equipos list should not be null after construction");
        assertTrue(newCampeonato.getEquipos().isEmpty(), "Equipos list should be empty after construction");
        assertNotNull(newCampeonato.getPartidos(), "Partidos list should not be null after construction");
        assertTrue(newCampeonato.getPartidos().isEmpty(), "Partidos list should be empty after construction");
    }

    @Test
    @DisplayName("Test registrarEquipo - Exito")
    void testRegistrarEquipo_Success() {
        assertTrue(campeonato.registrarEquipo("E004", "Equipo Delta", "Barrio Oeste", "Entrenador D"), "Should successfully register a new team");
        assertNotNull(campeonato.buscarEquipoPorId("E004"), "Newly registered team should be found by ID");
        assertNotNull(campeonato.buscarEquipoPorNombre("Equipo Delta"), "Newly registered team should be found by name");
    }

    @Test
    @DisplayName("Test registrarEquipo - ID ya existe")
    void testRegistrarEquipo_IdExists() {
        assertFalse(campeonato.registrarEquipo("E001", "Otro Nombre", "Otro Barrio", "Otro Entrenador"), "Should not register team if ID already exists");
        assertEquals(3, campeonato.getEquipos().size(), "Number of teams should remain unchanged");
    }

    @Test
    @DisplayName("Test registrarEquipo - Nombre ya existe")
    void testRegistrarEquipo_NameExists() {
        assertFalse(campeonato.registrarEquipo("E005", "Equipo Alpha", "Otro Barrio", "Otro Entrenador"), "Should not register team if name already exists");
        assertEquals(3, campeonato.getEquipos().size(), "Number of teams should remain unchanged");
    }

    @Test
    @DisplayName("Test registrarEquipo - Parámetros nulos o vacíos")
    void testRegistrarEquipo_InvalidParameters() {
        assertFalse(campeonato.registrarEquipo(null, "Nombre", "Barrio", "Entrenador"), "Should not register with null ID");
        assertFalse(campeonato.registrarEquipo("E005", "", "Barrio", "Entrenador"), "Should not register with empty name");
        assertFalse(campeonato.registrarEquipo("E005", "Nombre", null, "Entrenador"), "Should not register with null barrio");
        assertFalse(campeonato.registrarEquipo("E005", "Nombre", "Barrio", ""), "Should not register with empty entrenador");
        assertEquals(3, campeonato.getEquipos().size(), "Number of teams should remain unchanged with invalid parameters");
    }

    @Test
    @DisplayName("Test crearPartido - Exito")
    void testCrearPartido_Success() {
        assertTrue(campeonato.crearPartido("P003", "E001", "E003", "Nuevo Estadio", "Árbitro Tres"), "Should successfully create a new match");
        assertNotNull(campeonato.buscarPartidoPorId("P003"), "Newly created match should be found by ID");
        assertEquals(3, campeonato.getPartidos().size(), "Number of matches should increase by one");
        assertTrue(equipoA.getPartidos().contains(campeonato.buscarPartidoPorId("P003")), "Equipo A should have the new match associated");
        assertTrue(equipoC.getPartidos().contains(campeonato.buscarPartidoPorId("P003")), "Equipo C should have the new match associated");
    }

    @Test
    @DisplayName("Test crearPartido - ID de partido ya existe")
    void testCrearPartido_IdExists() {
        assertFalse(campeonato.crearPartido("P001", "E001", "E003", "Estadio", "Arbitro"), "Should not create match if ID already exists");
        assertEquals(2, campeonato.getPartidos().size(), "Number of matches should remain unchanged");
    }

    @Test
    @DisplayName("Test crearPartido - Equipo local no encontrado")
    void testCrearPartido_LocalTeamNotFound() {
        assertFalse(campeonato.crearPartido("P003", "E999", "E002", "Estadio", "Arbitro"), "Should not create match if local team not found");
        assertEquals(2, campeonato.getPartidos().size(), "Number of matches should remain unchanged");
    }

    @Test
    @DisplayName("Test crearPartido - Equipo visitante no encontrado")
    void testCrearPartido_VisitorTeamNotFound() {
        assertFalse(campeonato.crearPartido("P003", "E001", "E999", "Estadio", "Arbitro"), "Should not create match if visitor team not found");
        assertEquals(2, campeonato.getPartidos().size(), "Number of matches should remain unchanged");
    }

    @Test
    @DisplayName("Test crearPartido - Equipo local y visitante son el mismo")
    void testCrearPartido_SameTeams() {
        assertFalse(campeonato.crearPartido("P003", "E001", "E001", "Estadio", "Arbitro"), "Should not create match if local and visitor teams are the same");
        assertEquals(2, campeonato.getPartidos().size(), "Number of matches should remain unchanged");
    }

    @Test
    @DisplayName("Test crearPartido - Parámetros nulos o vacíos")
    void testCrearPartido_InvalidParameters() {
        assertFalse(campeonato.crearPartido(null, "E001", "E002", "Estadio", "Arbitro"), "Should not create with null ID");
        assertFalse(campeonato.crearPartido("P003", "E001", "E002", "", "Arbitro"), "Should not create with empty stadium");
        assertFalse(campeonato.crearPartido("P003", "E001", "E002", "Estadio", null), "Should not create with null referee");
        assertEquals(2, campeonato.getPartidos().size(), "Number of matches should remain unchanged with invalid parameters");
    }

    @Test
    @DisplayName("Test registrarGol - Exito")
    void testRegistrarGol_Success() {
        assertTrue(campeonato.registrarGol("P001", "J001", 15), "Should successfully register a goal for Jugador 1A in Partido 1");
        assertEquals(1, partido1.getGolesLocal(), "Equipo A's goals should increase by 1");
        assertEquals(1, partido1.getGoles().size(), "Partido 1 should have one goal recorded");

        assertTrue(campeonato.registrarGol("P001", "J003", 20), "Should successfully register a goal for Jugador 1B in Partido 1");
        assertEquals(1, partido1.getGolesVisitante(), "Equipo B's goals should increase by 1");
        assertEquals(2, partido1.getGoles().size(), "Partido 1 should have two goals recorded");
    }

    @Test
    @DisplayName("Test registrarGol - Partido no encontrado")
    void testRegistrarGol_MatchNotFound() {
        assertFalse(campeonato.registrarGol("P999", "J001", 10), "Should not register goal if match not found");
    }

    @Test
    @DisplayName("Test registrarGol - Jugador no encontrado en el partido")
    void testRegistrarGol_PlayerNotFoundInMatch() {
        Jugador jugadorNoEnPartido = new Jugador("J999", "Jugador Fantasma", "Portero", 1, equipoC);
        equipoC.getJugadores().add(jugadorNoEnPartido); // Add to a team, but not involved in partido1
        assertFalse(campeonato.registrarGol("P001", "J999", 30), "Should not register goal if player is not in either team of the match");
        assertEquals(0, partido1.getGolesLocal());
        assertEquals(0, partido1.getGolesVisitante());
    }

    @Test
    @DisplayName("Test registrarGol - Parámetros inválidos")
    void testRegistrarGol_InvalidParameters() {
        assertFalse(campeonato.registrarGol(null, "J001", 10), "Should not register with null match ID");
        assertFalse(campeonato.registrarGol("P001", "", 10), "Should not register with empty player ID");
        assertFalse(campeonato.registrarGol("P001", "J001", 0), "Should not register with non-positive minute");
        assertFalse(campeonato.registrarGol("P001", "J001", -5), "Should not register with negative minute");
    }

    @Test
    @DisplayName("Test registrarTarjeta - Exito (Amarilla)")
    void testRegistrarTarjeta_SuccessYellow() {
        assertTrue(campeonato.registrarTarjeta("P001", "J001", "Amarilla", 25, "Falta táctica"), "Should successfully register a yellow card");
        assertEquals(1, partido1.getTarjetas().size(), "Partido 1 should have one card recorded");
        assertEquals("Amarilla", partido1.getTarjetas().get(0).getTipo());
    }

    @Test
    @DisplayName("Test registrarTarjeta - Exito (Roja)")
    void testRegistrarTarjeta_SuccessRed() {
        assertTrue(campeonato.registrarTarjeta("P001", "J003", "Roja", 70, "Doble amarilla"), "Should successfully register a red card");
        assertEquals(1, partido1.getTarjetas().size(), "Partido 1 should have one card recorded");
        assertEquals("Roja", partido1.getTarjetas().get(0).getTipo());
    }

    @Test
    @DisplayName("Test registrarTarjeta - Partido no encontrado")
    void testRegistrarTarjeta_MatchNotFound() {
        assertFalse(campeonato.registrarTarjeta("P999", "J001", "Amarilla", 10, "Motivo"), "Should not register card if match not found");
    }

    @Test
    @DisplayName("Test registrarTarjeta - Jugador no encontrado en el partido")
    void testRegistrarTarjeta_PlayerNotFoundInMatch() {
        Jugador jugadorNoEnPartido = new Jugador("J999", "Jugador Fantasma", "Portero", 1, equipoC);
        equipoC.getJugadores().add(jugadorNoEnPartido);
        assertFalse(campeonato.registrarTarjeta("P001", "J999", "Amarilla", 30, "Motivo"), "Should not register card if player is not in either team of the match");
        assertTrue(partido1.getTarjetas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarTarjeta - Tipo de tarjeta inválido")
    void testRegistrarTarjeta_InvalidType() {
        assertFalse(campeonato.registrarTarjeta("P001", "J001", "Azul", 40, "Motivo"), "Should not register card with invalid type");
        assertTrue(partido1.getTarjetas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarTarjeta - Parámetros inválidos")
    void testRegistrarTarjeta_InvalidParameters() {
        assertFalse(campeonato.registrarTarjeta(null, "J001", "Amarilla", 10, "Motivo"), "Should not register with null match ID");
        assertFalse(campeonato.registrarTarjeta("P001", "", "Amarilla", 10, "Motivo"), "Should not register with empty player ID");
        assertFalse(campeonato.registrarTarjeta("P001", "J001", null, 10, "Motivo"), "Should not register with null type");
        assertFalse(campeonato.registrarTarjeta("P001", "J001", "Amarilla", 0, "Motivo"), "Should not register with non-positive minute");
        assertFalse(campeonato.registrarTarjeta("P001", "J001", "Amarilla", 10, null), "Should not register with null motive");
    }

    @Test
    @DisplayName("Test registrarFalta - Exito")
    void testRegistrarFalta_Success() {
        assertTrue(campeonato.registrarFalta("P001", "J001", "J003", 50, "Medio campo", null), "Should successfully register a foul");
        assertEquals(1, partido1.getFaltas().size(), "Partido 1 should have one foul recorded");
        assertEquals(jugador1A, partido1.getFaltas().get(0).getJugadorQueCometio());
        assertEquals(jugador1B, partido1.getFaltas().get(0).getJugadorAfectado());
    }

    @Test
    @DisplayName("Test registrarFalta - Partido no encontrado")
    void testRegistrarFalta_MatchNotFound() {
        assertFalse(campeonato.registrarFalta("P999", "J001", "J003", 10, "Zona", null), "Should not register foul if match not found");
    }

    @Test
    @DisplayName("Test registrarFalta - Jugador que comete no encontrado en el partido")
    void testRegistrarFalta_ComittingPlayerNotFoundInMatch() {
        Jugador jugadorNoEnPartido = new Jugador("J999", "Jugador Fantasma", "Portero", 1, equipoC);
        equipoC.getJugadores().add(jugadorNoEnPartido);
        assertFalse(campeonato.registrarFalta("P001", "J999", "J003", 30, "Zona", null), "Should not register foul if committing player not in match");
        assertTrue(partido1.getFaltas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarFalta - Jugador afectado no encontrado en el partido")
    void testRegistrarFalta_AffectedPlayerNotFoundInMatch() {
        Jugador jugadorNoEnPartido = new Jugador("J999", "Jugador Fantasma", "Portero", 1, equipoC);
        equipoC.getJugadores().add(jugadorNoEnPartido);
        assertFalse(campeonato.registrarFalta("P001", "J001", "J999", 30, "Zona", null), "Should not register foul if affected player not in match");
        assertTrue(partido1.getFaltas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarFalta - Jugador que comete y afectado son el mismo")
    void testRegistrarFalta_SamePlayers() {
        assertFalse(campeonato.registrarFalta("P001", "J001", "J001", 30, "Zona", null), "Should not register foul if committing and affected players are the same");
        assertTrue(partido1.getFaltas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarFalta - Jugadores del mismo equipo")
    void testRegistrarFalta_SameTeamPlayers() {
        Jugador jugador3A = new Jugador("J005", "Jugador 3A", "Mediocampista", 7, equipoA);
        equipoA.getJugadores().add(jugador3A);
        assertFalse(campeonato.registrarFalta("P001", "J001", "J005", 30, "Zona", null), "Should not register foul if players are from the same team");
        assertTrue(partido1.getFaltas().isEmpty());
    }

    @Test
    @DisplayName("Test registrarFalta - Parámetros inválidos")
    void testRegistrarFalta_InvalidParameters() {
        assertFalse(campeonato.registrarFalta(null, "J001", "J003", 10, "Zona", null), "Should not register with null match ID");
        assertFalse(campeonato.registrarFalta("P001", "", "J003", 10, "Zona", null), "Should not register with empty committing player ID");
        assertFalse(campeonato.registrarFalta("P001", "J001", null, 10, "Zona", null), "Should not register with null affected player ID");
        assertFalse(campeonato.registrarFalta("P001", "J001", "J003", 0, "Zona", null), "Should not register with non-positive minute");
        assertFalse(campeonato.registrarFalta("P001", "J001", "J003", 10, "", null), "Should not register with empty zone");
    }

    @Test
    @DisplayName("Test getPuntosEquipo - Exito")
    void testGetPuntosEquipo_Success() {
        // Simulate some results for equipoA
        partido1.setGolesLocal(2);
        partido1.setGolesVisitante(1); // EquipoA wins partido1
        partido2.setGolesLocal(0);
        partido2.setGolesVisitante(0); // EquipoA draws partido2

        // EquipoA: 1 ganado (3 puntos) + 1 empatado (1 punto) = 4 puntos
        assertEquals(4, campeonato.getPuntosEquipo("E001"), "Equipo Alpha should have 4 points");
    }

    @Test
    @DisplayName("Test getPuntosEquipo - Equipo no encontrado")
    void testGetPuntosEquipo_TeamNotFound() {
        assertEquals(-1, campeonato.getPuntosEquipo("E999"), "Should return -1 if team not found");
    }

    @Test
    @DisplayName("Test getPuntosEquipo - Parámetro inválido")
    void testGetPuntosEquipo_InvalidParameter() {
        assertEquals(-1, campeonato.getPuntosEquipo(null), "Should return -1 for null ID");
        assertEquals(-1, campeonato.getPuntosEquipo(""), "Should return -1 for empty ID");
    }

    @Test
    @DisplayName("Test contarFaltasEquipo - Exito")
    void testContarFaltasEquipo_Success() {
        campeonato.registrarFalta("P001", "J001", "J003", 10, "Centro", null); // Equipo A commits
        campeonato.registrarFalta("P001", "J002", "J003", 20, "Defensa", null); // Equipo A commits
        campeonato.registrarFalta("P002", "J001", "J005", 30, "Ataque", null); // Equipo A commits (assuming J005 is from EquipoC)

        assertEquals(2, campeonato.contarFaltasEquipo("E001"), "Equipo Alpha should have 3 fouls");
        assertEquals(0, campeonato.contarFaltasEquipo("E002"), "Equipo Beta should have 0 fouls (none committed)");
    }

    @Test
    @DisplayName("Test contarFaltasEquipo - Equipo no encontrado")
    void testContarFaltasEquipo_TeamNotFound() {
        assertEquals(-1, campeonato.contarFaltasEquipo("E999"), "Should return -1 if team not found");
    }

    @Test
    @DisplayName("Test contarFaltasEquipo - Parámetro inválido")
    void testContarFaltasEquipo_InvalidParameter() {
        assertEquals(-1, campeonato.contarFaltasEquipo(null), "Should return -1 for null ID");
        assertEquals(-1, campeonato.contarFaltasEquipo(""), "Should return -1 for empty ID");
    }

    @Test
    @DisplayName("Test getGanadorPartido - Equipo local gana")
    void testGetGanadorPartido_LocalWins() {
        partido1.setGolesLocal(2); // Set local goals
        partido1.setGolesVisitante(1); // Set visitor goals
        assertEquals("Equipo Alpha", campeonato.getGanadorPartido("P001"), "Equipo Alpha should be the winner");
    }

    @Test
    @DisplayName("Test getGanadorPartido - Equipo visitante gana")
    void testGetGanadorPartido_VisitorWins() {
        partido1.setGolesLocal(1); // Set local goals
        partido1.setGolesVisitante(3); // Set visitor goals
        assertEquals("Equipo Beta", campeonato.getGanadorPartido("P001"), "Equipo Beta should be the winner");
    }

    @Test
    @DisplayName("Test getGanadorPartido - Empate")
    void testGetGanadorPartido_Draw() {
        partido1.setGolesLocal(2); // Set local goals
        partido1.setGolesVisitante(2); // Set visitor goals
        assertEquals("Empate", campeonato.getGanadorPartido("P001"), "Should return 'Empate' for a draw");
    }

    @Test
    @DisplayName("Test getGanadorPartido - Partido no encontrado")
    void testGetGanadorPartido_MatchNotFound() {
        assertEquals("Partido no encontrado", campeonato.getGanadorPartido("P999"), "Should return 'Partido no encontrado' if match not found");
    }

    @Test
    @DisplayName("Test getGanadorPartido - Parámetro inválido")
    void testGetGanadorPartido_InvalidParameter() {
        assertEquals("Parámetro inválido", campeonato.getGanadorPartido(null), "Should return 'Parámetro inválido' for null ID");
        assertEquals("Parámetro inválido", campeonato.getGanadorPartido(""), "Should return 'Parámetro inválido' for empty ID");
    }


    @Test
    @DisplayName("Test calculaGolesJugador - Exito")
    void testCalculaGolesJugador_Success() {
        campeonato.registrarGol("P001", "J001", 10); // Jugador 1A scores
        campeonato.registrarGol("P001", "J001", 20); // Jugador 1A scores again
        campeonato.registrarGol("P002", "J001", 30); // Jugador 1A scores in another match
        campeonato.registrarGol("P001", "J003", 15); // Jugador 1B scores

        assertEquals(3, campeonato.calculaGolesJugador("J001"), "Jugador 1A should have 3 goals");
        assertEquals(1, campeonato.calculaGolesJugador("J003"), "Jugador 1B should have 1 goal");
        assertEquals(0, campeonato.calculaGolesJugador("J002"), "Jugador 2A should have 0 goals");
    }

    @Test
    @DisplayName("Test calculaGolesJugador - Jugador sin goles")
    void testCalculaGolesJugador_NoGoals() {
        assertEquals(0, campeonato.calculaGolesJugador("J002"), "Jugador 2A should have 0 goals if no goals registered");
    }

    @Test
    @DisplayName("Test calculaGolesJugador - Parámetro inválido")
    void testCalculaGolesJugador_InvalidParameter() {
        assertEquals(0, campeonato.calculaGolesJugador(null), "Should return 0 for null ID");
        assertEquals(0, campeonato.calculaGolesJugador(""), "Should return 0 for empty ID");
    }

    @Test
    @DisplayName("Test calculaTarjetasJugador - Exito")
    void testCalculaTarjetasJugador_Success() {
        campeonato.registrarTarjeta("P001", "J001", "Amarilla", 10, "Falta"); // J001 gets a yellow
        campeonato.registrarTarjeta("P001", "J001", "Roja", 20, "Doble Amarilla"); // J001 gets a red
        campeonato.registrarTarjeta("P002", "J001", "Amarilla", 30, "Retraso"); // J001 gets another yellow
        campeonato.registrarTarjeta("P001", "J003", "Amarilla", 15, "Reclamo"); // J003 gets a yellow

        assertEquals(3, campeonato.calculaTarjetasJugador("J001"), "Jugador 1A should have 3 cards");
        assertEquals(1, campeonato.calculaTarjetasJugador("J003"), "Jugador 1B should have 1 card");
        assertEquals(0, campeonato.calculaTarjetasJugador("J002"), "Jugador 2A should have 0 cards");
    }

    @Test
    @DisplayName("Test calculaTarjetasJugador - Jugador sin tarjetas")
    void testCalculaTarjetasJugador_NoCards() {
        assertEquals(0, campeonato.calculaTarjetasJugador("J002"), "Jugador 2A should have 0 cards if no cards registered");
    }

    @Test
    @DisplayName("Test calculaTarjetasJugador - Parámetro inválido")
    void testCalculaTarjetasJugador_InvalidParameter() {
        assertEquals(0, campeonato.calculaTarjetasJugador(null), "Should return 0 for null ID");
        assertEquals(0, campeonato.calculaTarjetasJugador(""), "Should return 0 for empty ID");
    }

    @Test
    @DisplayName("Test calculaFaltasJugador - Exito")
    void testCalculaFaltasJugador_Success() {
        campeonato.registrarFalta("P001", "J001", "J003", 10, "Medio", null); // J001 commits
        campeonato.registrarFalta("P001", "J001", "J003", 20, "Ataque", null); // J001 commits
        campeonato.registrarFalta("P002", "J001", "J004", 30, "Defensa", null); // J001 commits
        campeonato.registrarFalta("P001", "J003", "J001", 15, "Defensa", null); // J003 commits

        assertEquals(2, campeonato.calculaFaltasJugador("J001"), "Jugador 1A should have 3 fouls");
        assertEquals(1, campeonato.calculaFaltasJugador("J003"), "Jugador 1B should have 1 foul");
        assertEquals(0, campeonato.calculaFaltasJugador("J002"), "Jugador 2A should have 0 fouls");
    }

    @Test
    @DisplayName("Test calculaFaltasJugador - Jugador sin faltas")
    void testCalculaFaltasJugador_NoFouls() {
        assertEquals(0, campeonato.calculaFaltasJugador("J002"), "Jugador 2A should have 0 fouls if no fouls registered");
    }

    @Test
    @DisplayName("Test calculaFaltasJugador - Parámetro inválido")
    void testCalculaFaltasJugador_InvalidParameter() {
        assertEquals(0, campeonato.calculaFaltasJugador(null), "Should return 0 for null ID");
        assertEquals(0, campeonato.calculaFaltasJugador(""), "Should return 0 for empty ID");
    }

    @Test
    @DisplayName("Test calculaPartidosJugadosJugador - Exito")
    void testCalculaPartidosJugadosJugador_Success() {
        // J001 is in EquipoA. EquipoA plays P001 and P002.
        assertEquals(2, campeonato.calculaPartidosJugadosJugador("J001"), "Jugador 1A should have played 2 matches");
        // J003 is in EquipoB. EquipoB plays P001.
        assertEquals(1, campeonato.calculaPartidosJugadosJugador("J003"), "Jugador 1B should have played 1 match");
        // J002 is in EquipoA. EquipoA plays P001 and P002.
        assertEquals(2, campeonato.calculaPartidosJugadosJugador("J002"), "Jugador 2A should have played 2 matches");
    }

    @Test
    @DisplayName("Test calculaPartidosJugadosJugador - Jugador no ha jugado")
    void testCalculaPartidosJugadosJugador_NoGamesPlayed() {
        // Create a player not assigned to any team in the championship
        Jugador jugadorNoEnPartido = new Jugador("J999", "Jugador Inactivo", "Banca", 99, null);
        // Or a player in a team that hasn't played any matches (e.g., if equipoC had no matches initially)
        Equipo equipoSinJugar = new Equipo("E005", "Equipo Sin Jugar", "Barrio Prueba", "Entrenador X");
        Jugador jugadorEquipoSinJugar = new Jugador("J006", "Jugador E5", "Portero", 1, equipoSinJugar);
        equipoSinJugar.getJugadores().add(jugadorEquipoSinJugar);
        campeonato.getEquipos().add(equipoSinJugar);

        assertEquals(0, campeonato.calculaPartidosJugadosJugador("J999"), "Player not in any team should have 0 games played");
        assertEquals(0, campeonato.calculaPartidosJugadosJugador("J006"), "Player in a team that hasn't played should have 0 games played");
    }

    @Test
    @DisplayName("Test calculaPartidosJugadosJugador - Parámetro inválido")
    void testCalculaPartidosJugadosJugador_InvalidParameter() {
        assertEquals(0, campeonato.calculaPartidosJugadosJugador(null), "Should return 0 for null ID");
        assertEquals(0, campeonato.calculaPartidosJugadosJugador(""), "Should return 0 for empty ID");
    }

    @Test
    @DisplayName("Test buscarEquipoPorId - Exito")
    void testBuscarEquipoPorId_Success() {
        assertEquals(equipoA, campeonato.buscarEquipoPorId("E001"), "Should find Equipo Alpha by ID");
    }

    @Test
    @DisplayName("Test buscarEquipoPorId - No encontrado")
    void testBuscarEquipoPorId_NotFound() {
        assertNull(campeonato.buscarEquipoPorId("E999"), "Should return null if team not found by ID");
    }

    @Test
    @DisplayName("Test buscarEquipoPorId - Parámetro inválido")
    void testBuscarEquipoPorId_InvalidParameter() {
        assertNull(campeonato.buscarEquipoPorId(null), "Should return null for null ID");
        assertNull(campeonato.buscarEquipoPorId(""), "Should return null for empty ID");
    }

    @Test
    @DisplayName("Test buscarEquipoPorNombre - Exito")
    void testBuscarEquipoPorNombre_Success() {
        assertEquals(equipoA, campeonato.buscarEquipoPorNombre("Equipo Alpha"), "Should find Equipo Alpha by name");
        assertEquals(equipoA, campeonato.buscarEquipoPorNombre("equipo alpha"), "Should find Equipo Alpha by name (case-insensitive)");
    }

    @Test
    @DisplayName("Test buscarEquipoPorNombre - No encontrado")
    void testBuscarEquipoPorNombre_NotFound() {
        assertNull(campeonato.buscarEquipoPorNombre("Equipo ZZZ"), "Should return null if team not found by name");
    }

    @Test
    @DisplayName("Test buscarEquipoPorNombre - Parámetro inválido")
    void testBuscarEquipoPorNombre_InvalidParameter() {
        assertNull(campeonato.buscarEquipoPorNombre(null), "Should return null for null name");
        assertNull(campeonato.buscarEquipoPorNombre(""), "Should return null for empty name");
    }

    @Test
    @DisplayName("Test buscarPartidoPorId - Exito")
    void testBuscarPartidoPorId_Success() {
        assertEquals(partido1, campeonato.buscarPartidoPorId("P001"), "Should find Partido 1 by ID");
    }

    @Test
    @DisplayName("Test buscarPartidoPorId - No encontrado")
    void testBuscarPartidoPorId_NotFound() {
        assertNull(campeonato.buscarPartidoPorId("P999"), "Should return null if match not found by ID");
    }

    @Test
    @DisplayName("Test buscarPartidoPorId - Parámetro inválido")
    void testBuscarPartidoPorId_InvalidParameter() {
        assertNull(campeonato.buscarPartidoPorId(null), "Should return null for null ID");
        assertNull(campeonato.buscarPartidoPorId(""), "Should return null for empty ID");
    }
}