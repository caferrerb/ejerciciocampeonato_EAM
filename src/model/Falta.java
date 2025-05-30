// Archivo: model/Falta.java
package model;

import java.util.Objects;

public class Falta {
    private Jugador jugadorQueCometio;
    private int minuto;
    private String zonaCampo;
    private Jugador jugadorAfectado;
    private String tarjetaAsociada;

    public Falta() {
    }

    public Falta(Jugador jugadorQueCometio, int minuto, String zonaCampo, Jugador jugadorAfectado, String tarjetaAsociada) {
        this.jugadorQueCometio = jugadorQueCometio;
        this.minuto = minuto;
        this.zonaCampo = zonaCampo;
        this.jugadorAfectado = jugadorAfectado;
        this.tarjetaAsociada = tarjetaAsociada;
    }

    public Jugador getJugadorQueCometio() { return jugadorQueCometio; }
    public void setJugadorQueCometio(Jugador jugadorQueCometio) { this.jugadorQueCometio = jugadorQueCometio; }
    public int getMinuto() { return minuto; }
    public void setMinuto(int minuto) { this.minuto = minuto; }
    public String getZonaCampo() { return zonaCampo; }
    public void setZonaCampo(String zonaCampo) { this.zonaCampo = zonaCampo; }
    public Jugador getJugadorAfectado() { return jugadorAfectado; }
    public void setJugadorAfectado(Jugador jugadorAfectado) { this.jugadorAfectado = jugadorAfectado; }
    public String getTarjetaAsociada() { return tarjetaAsociada; }
    public void setTarjetaAsociada(String tarjetaAsociada) { this.tarjetaAsociada = tarjetaAsociada; }

    @Override
    public String toString() {
        return "Falta{" +
                "jugadorQueCometio=" + (jugadorQueCometio != null ? jugadorQueCometio.getNombreCompleto() : "N/A") +
                ", minuto=" + minuto +
                ", zonaCampo='" + zonaCampo + '\'' +
                ", jugadorAfectado=" + (jugadorAfectado != null ? jugadorAfectado.getNombreCompleto() : "N/A") +
                ", tarjetaAsociada='" + (tarjetaAsociada != null ? tarjetaAsociada : "Ninguna") + '\'' +
                '}';
    }
}