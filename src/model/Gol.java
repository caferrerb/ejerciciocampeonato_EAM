// Archivo: model/Gol.java
package model;

import java.util.Objects;

public class Gol {
    private Jugador jugador;
    private int minuto;

    public Gol() {
    }

    public Gol(Jugador jugador, int minuto) {
        this.jugador = jugador;
        this.minuto = minuto;
    }

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }
    public int getMinuto() { return minuto; }
    public void setMinuto(int minuto) { this.minuto = minuto; }

    @Override
    public String toString() {
        return "Gol{" +
                "jugador=" + (jugador != null ? jugador.getNombreCompleto() : "N/A") +
                ", minuto=" + minuto +
                '}';
    }
}
