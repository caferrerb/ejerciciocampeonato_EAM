package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquipoTest {

    private Equipo equipoA;
    private Equipo equipoB;
    private Equipo equipoC; // For scenarios where equipoA plays against C
    private Jugador jugadorA1;
    private Jugador jugadorA2;
    private Jugador jugadorB1;
    private Jugador jugadorB2;
    private Partido partido1; // EquipoA vs EquipoB
    private Partido partido2; // EquipoA vs EquipoC

    @BeforeEach
    void setUp() {
        equipoA = new Equipo("E001", "Equipo Alpha", "Barrio Norte", "Entrenador A");
        equipoB = new Equipo("E002", "Equipo Beta", "Barrio Sur", "Entrenador B");
        equipoC = new Equipo("E003", "Equipo Gamma", "Barrio Este", "Entrenador C");

        jugadorA1 = new Jugador("JA1", "Jugador A1", "Delantero", 9, equipoA);
        jugadorA2 = new Jugador("JA2", "Jugador A2", "Defensa", 2, equipoA);
        jugadorB1 = new Jugador("JB1", "Jugador B1", "Delantero", 7, equipoB);
        jugadorB2 = new Jugador("JB2", "Jugador B2", "Mediocampista", 8, equipoB);

        equipoA.getJugadores().add(jugadorA1);
        equipoA.getJugadores().add(jugadorA2);
        equipoB.getJugadores().add(jugadorB1);
        equipoB.getJugadores().add(jugadorB2);

        partido1 = new Partido("P001", equipoA, equipoB, "Estadio Principal", "Arbitro 1", LocalDateTime.now());
        partido2 = new Partido("P002", equipoA, equipoC, "Estadio Secundario", "Arbitro 2", LocalDateTime.now());

        equipoA.getPartidos().add(partido1);
        equipoA.getPartidos().add(partido2);
        equipoB.getPartidos().add(partido1);
        equipoC.getPartidos().add(partido2);
    }

    @Test
    @DisplayName("Test constructor with valid parameters")
    void testConstructor_ValidParameters() {
        Equipo newEquipo = new Equipo("E004", "Equipo Test", "Test Barrio", "Test Coach");
        assertNotNull(newEquipo);
        assertEquals("E004", newEquipo.getId());
        assertEquals("Equipo Test", newEquipo.getNombre());
        assertEquals("Test Barrio", newEquipo.getBarrio());
        assertEquals("Test Coach", newEquipo.getNombreEntrenador());
        assertNotNull(newEquipo.getJugadores());
        assertTrue(newEquipo.getJugadores().isEmpty());
        assertNotNull(newEquipo.getPartidos());
        assertTrue(newEquipo.getPartidos().isEmpty());
    }

    @Test
    @DisplayName("Test constructor with null/empty parameters should throw IllegalArgumentException")
    void testConstructor_InvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> new Equipo(null, "Name", "Barrio", "Coach"));
        assertThrows(IllegalArgumentException.class, () -> new Equipo("ID", "", "Barrio", "Coach"));
        assertThrows(IllegalArgumentException.class, () -> new Equipo("ID", "Name", null, "Coach"));
        assertThrows(IllegalArgumentException.class, () -> new Equipo("ID", "Name", "Barrio", ""));
    }

    @Test
    @DisplayName("Test calcularPartidosGanados - Equipo gana un partido")
    void testCalcularPartidosGanados_OneWin() {
        partido1.setGolesLocal(2); // Equipo A gana
        partido1.setGolesVisitante(1); // Equipo B pierde
        assertEquals(1, equipoA.calcularPartidosGanados(), "Equipo A should have 1 win");
        assertEquals(0, equipoB.calcularPartidosGanados(), "Equipo B should have 0 wins");
    }

    @Test
    @DisplayName("Test calcularPartidosGanados - Equipo no gana partidos")
    void testCalcularPartidosGanados_NoWins() {
        partido1.setGolesLocal(1); // Equipo A empata
        partido1.setGolesVisitante(1); // Equipo B empata
        partido2.setGolesLocal(0); // Equipo A pierde
        partido2.setGolesVisitante(1); // Equipo C gana
        assertEquals(0, equipoA.calcularPartidosGanados(), "Equipo A should have 0 wins");
    }

    @Test
    @DisplayName("Test calcularPartidosGanados - Múltiples partidos")
    void testCalcularPartidosGanados_MultipleMatches() {
        partido1.setGolesLocal(3); // Equipo A gana
        partido1.setGolesVisitante(0); // Equipo B pierde
        partido2.setGolesLocal(2); // Equipo A gana
        partido2.setGolesVisitante(1); // Equipo C pierde
        assertEquals(2, equipoA.calcularPartidosGanados(), "Equipo A should have 2 wins");
        assertEquals(0, equipoB.calcularPartidosGanados(), "Equipo B should have 0 wins");
    }


    @Test
    @DisplayName("Test calculaPartidosEmpatados - Equipo no empata partidos")
    void testCalculaPartidosEmpatados_NoDraws() {
        partido1.setGolesLocal(2); // Equipo A gana
        partido1.setGolesVisitante(1); // Equipo B pierde
        partido2.setGolesLocal(0); // Equipo A pierde
        partido2.setGolesVisitante(1); // Equipo C gana
        assertEquals(0, equipoA.calculaPartidosEmpatados(), "Equipo A should have 0 draws");
    }

    @Test
    @DisplayName("Test calculaPartidosEmpatados - Múltiples partidos")
    void testCalculaPartidosEmpatados_MultipleMatches() {
        partido1.setGolesLocal(0); // Equipo A empata
        partido1.setGolesVisitante(0); // Equipo B empata
        partido2.setGolesLocal(1); // Equipo A empata
        partido2.setGolesVisitante(1); // Equipo C empata
        assertEquals(2, equipoA.calculaPartidosEmpatados(), "Equipo A should have 2 draws");
        assertEquals(1, equipoB.calculaPartidosEmpatados(), "Equipo B should have 1 draw");
    }

    @Test
    @DisplayName("Test calculaPartidosPerdidos - Equipo pierde un partido")
    void testCalculaPartidosPerdidos_OneLoss() {
        partido1.setGolesLocal(1); // Equipo A pierde
        partido1.setGolesVisitante(2); // Equipo B gana
        assertEquals(1, equipoA.calculaPartidosPerdidos(), "Equipo A should have 1 loss");
        assertEquals(0, equipoB.calculaPartidosPerdidos(), "Equipo B should have 0 losses");
    }

    @Test
    @DisplayName("Test calculaPartidosPerdidos - Equipo no pierde partidos")
    void testCalculaPartidosPerdidos_NoLosses() {
        partido1.setGolesLocal(2); // Equipo A gana
        partido1.setGolesVisitante(1); // Equipo B pierde
        partido2.setGolesLocal(1); // Equipo A empata
        partido2.setGolesVisitante(1); // Equipo C empata
        assertEquals(0, equipoA.calculaPartidosPerdidos(), "Equipo A should have 0 losses");
    }

    @Test
    @DisplayName("Test calculaPartidosPerdidos - Múltiples partidos")
    void testCalculaPartidosPerdidos_MultipleMatches() {
        partido1.setGolesLocal(1); // Equipo A pierde
        partido1.setGolesVisitante(2); // Equipo B gana
        partido2.setGolesLocal(0); // Equipo A pierde
        partido2.setGolesVisitante(1); // Equipo C gana
        assertEquals(2, equipoA.calculaPartidosPerdidos(), "Equipo A should have 2 losses");
        assertEquals(0, equipoB.calculaPartidosPerdidos(), "Equipo B should have 0 losses");
    }

    @Test
    @DisplayName("Test calculaGolesAFavor - Equipo local")
    void testCalculaGolesAFavor_LocalTeam() {
        partido1.setGolesLocal(3); // Equipo A local
        partido1.setGolesVisitante(1); // Equipo B visitante
        partido2.setGolesLocal(2); // Equipo A local
        partido2.setGolesVisitante(0); // Equipo C visitante
        assertEquals(5, equipoA.calculaGolesAFavor(), "Equipo A should have 5 goals for");
    }

    @Test
    @DisplayName("Test calculaGolesAFavor - Equipo visitante")
    void testCalculaGolesAFavor_VisitorTeam() {
        partido1.setGolesLocal(1); // Equipo A local
        partido1.setGolesVisitante(2); // Equipo B visitante
        assertEquals(2, equipoB.calculaGolesAFavor(), "Equipo B should have 2 goals for");
    }

    @Test
    @DisplayName("Test calculaGolesAFavor - Sin goles a favor")
    void testCalculaGolesAFavor_NoGoalsFor() {
        partido1.setGolesLocal(0); // Equipo A local
        partido1.setGolesVisitante(1); // Equipo B visitante
        partido2.setGolesLocal(0); // Equipo A local
        partido2.setGolesVisitante(2); // Equipo C visitante
        assertEquals(0, equipoA.calculaGolesAFavor(), "Equipo A should have 0 goals for");
    }

    @Test
    @DisplayName("Test calculaGolesEnContra - Equipo local")
    void testCalculaGolesEnContra_LocalTeam() {
        partido1.setGolesLocal(3); // Equipo A local
        partido1.setGolesVisitante(1); // Equipo B visitante
        partido2.setGolesLocal(2); // Equipo A local
        partido2.setGolesVisitante(0); // Equipo C visitante
        assertEquals(1, equipoA.calculaGolesEnContra(), "Equipo A should have 1 goal against");
    }

    @Test
    @DisplayName("Test calculaGolesEnContra - Equipo visitante")
    void testCalculaGolesEnContra_VisitorTeam() {
        partido1.setGolesLocal(1); // Equipo A local
        partido1.setGolesVisitante(2); // Equipo B visitante
        assertEquals(1, equipoB.calculaGolesEnContra(), "Equipo B should have 1 goal against");
    }

    @Test
    @DisplayName("Test calculaGolesEnContra - Sin goles en contra")
    void testCalculaGolesEnContra_NoGoalsAgainst() {
        partido1.setGolesLocal(1); // Equipo A local
        partido1.setGolesVisitante(0); // Equipo B visitante
        partido2.setGolesLocal(2); // Equipo A local
        partido2.setGolesVisitante(0); // Equipo C visitante
        assertEquals(0, equipoA.calculaGolesEnContra(), "Equipo A should have 0 goals against");
    }

    @Test
    @DisplayName("Test calculaPuntos - Equipo con victorias y empates")
    void testCalculaPuntos_WinsAndDraws() {
        partido1.setGolesLocal(2); // Equipo A gana
        partido1.setGolesVisitante(1); // Equipo B pierde
        partido2.setGolesLocal(0); // Equipo A empata
        partido2.setGolesVisitante(0); // Equipo C empata
        // 1 victoria (3 puntos) + 1 empate (1 punto) = 4 puntos
        assertEquals(4, equipoA.calculaPuntos(), "Equipo A should have 4 points");
    }

    @Test
    @DisplayName("Test calculaPuntos - Equipo solo con derrotas")
    void testCalculaPuntos_OnlyLosses() {
        partido1.setGolesLocal(1); // Equipo A pierde
        partido1.setGolesVisitante(2); // Equipo B gana
        partido2.setGolesLocal(0); // Equipo A pierde
        partido2.setGolesVisitante(1); // Equipo C gana
        assertEquals(0, equipoA.calculaPuntos(), "Equipo A should have 0 points");
    }

    @Test
    @DisplayName("Test calculaPuntos - Equipo sin partidos")
    void testCalculaPuntos_NoMatches() {
        Equipo equipoSinPartidos = new Equipo("E004", "Equipo Sin Partidos", "Barrio", "Entrenador");
        assertEquals(0, equipoSinPartidos.calculaPuntos(), "Equipo without matches should have 0 points");
    }

    @Test
    @DisplayName("Test calculaTotalTarjetas - Equipo con tarjetas")
    void testCalculaTotalTarjetas_WithCards() {
        // Assume partido1 has a list of cards
        partido1.getTarjetas().add(new Tarjeta(jugadorA1, "Amarilla", 10, "Falta")); // J A1
        partido1.getTarjetas().add(new Tarjeta(jugadorB1, "Roja", 20, "Expulsion")); // J B1
        partido2.getTarjetas().add(new Tarjeta(jugadorA2, "Amarilla", 30, "Mano")); // J A2
        partido2.getTarjetas().add(new Tarjeta(jugadorA1, "Amarilla", 40, "Perder tiempo")); // J A1

        assertEquals(3, equipoA.calculaTotalTarjetas(), "Equipo A should have 3 total cards");
        assertEquals(1, equipoB.calculaTotalTarjetas(), "Equipo B should have 1 total card");
        assertEquals(0, equipoC.calculaTotalTarjetas(), "Equipo C should have 0 total cards (no players from C got cards in this setup)");
    }

    @Test
    @DisplayName("Test calculaTotalTarjetas - Equipo sin tarjetas")
    void testCalculaTotalTarjetas_NoCards() {
        assertEquals(0, equipoA.calculaTotalTarjetas(), "Equipo A should have 0 total cards if none registered");
        assertEquals(0, equipoB.calculaTotalTarjetas(), "Equipo B should have 0 total cards if none registered");
    }

    @Test
    @DisplayName("Test contarFaltas - Equipo con faltas")
    void testContarFaltas_WithFouls() {
        partido1.getFaltas().add(new Falta(jugadorA1, 15, "Defensa", jugadorB1, null)); // JA1 commits
        partido1.getFaltas().add(new Falta(jugadorA2, 25, "Medio", jugadorB2, null)); // JA2 commits
        partido2.getFaltas().add(new Falta(jugadorA1, 35, "Ataque", new Jugador("JC1", "Jugador C1", "Defensa", 5, equipoC), null)); // JA1 commits
        partido1.getFaltas().add(new Falta(jugadorB1, 5, "Centro", jugadorA1, null)); // JB1 commits

        assertEquals(3, equipoA.contarFaltas(), "Equipo A should have 3 total fouls committed");
        assertEquals(1, equipoB.contarFaltas(), "Equipo B should have 1 total foul committed");
        assertEquals(0, equipoC.contarFaltas(), "Equipo C should have 0 total fouls committed (no players from C committed fouls)");
    }

    @Test
    @DisplayName("Test contarFaltas - Equipo sin faltas")
    void testContarFaltas_NoFouls() {
        assertEquals(0, equipoA.contarFaltas(), "Equipo A should have 0 total fouls if none registered");
        assertEquals(0, equipoB.contarFaltas(), "Equipo B should have 0 total fouls if none registered");
    }

    @Test
    @DisplayName("Test toString")
    void testToString() {
        assertEquals("Equipo Alpha", equipoA.toString());
        assertEquals("Equipo Beta", equipoB.toString());
    }

    @Test
    @DisplayName("Test equals - Same ID")
    void testEquals_SameId() {
        Equipo equipoA2 = new Equipo("E001", "Equipo Alpha Clone", "Barrio Fake", "Entrenador Z");
        assertTrue(equipoA.equals(equipoA2), "Teams with the same ID should be equal");
    }

    @Test
    @DisplayName("Test equals - Different ID")
    void testEquals_DifferentId() {
        assertFalse(equipoA.equals(equipoB), "Teams with different IDs should not be equal");
    }

    @Test
    @DisplayName("Test equals - Null object")
    void testEquals_NullObject() {
        assertFalse(equipoA.equals(null), "Should return false when comparing with null");
    }

    @Test
    @DisplayName("Test equals - Different class")
    void testEquals_DifferentClass() {
        Object obj = new Object();
        assertFalse(equipoA.equals(obj), "Should return false when comparing with an object of a different class");
    }

    @Test
    @DisplayName("Test hashCode - Same ID")
    void testHashCode_SameId() {
        Equipo equipoA2 = new Equipo("E001", "Equipo Alpha Clone", "Barrio Fake", "Entrenador Z");
        assertEquals(equipoA.hashCode(), equipoA2.hashCode(), "Hash codes should be equal for teams with the same ID");
    }

    @Test
    @DisplayName("Test hashCode - Different ID")
    void testHashCode_DifferentId() {
        assertNotEquals(equipoA.hashCode(), equipoB.hashCode(), "Hash codes should not be equal for teams with different IDs");
    }
}