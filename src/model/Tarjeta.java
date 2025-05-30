// Archivo: model/Tarjeta.java
package model;

import java.util.Objects;

public class Tarjeta {
    private Jugador jugador;
    private String tipo;
    private int minuto;
    private String motivo;

    public Tarjeta(Jugador jugador, String tipo, int minuto, String motivo) {
        this.jugador = jugador;
        this.tipo = tipo;
        this.minuto = minuto;
        this.motivo = motivo;
    }

    public Tarjeta() {
    }

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getMinuto() { return minuto; }
    public void setMinuto(int minuto) { this.minuto = minuto; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    @Override
    public String toString() {
        return "Tarjeta{" +
                "jugador=" + (jugador != null ? jugador.getNombreCompleto() : "N/A") +
                ", tipo='" + tipo + '\'' +
                ", minuto=" + minuto +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}