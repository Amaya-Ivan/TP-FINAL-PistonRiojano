package modelo;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Vehiculo {
    private final String patente;
    private final String marca;
    private final String modelo;
    private final int anio;
    private final Cliente cliente;

    protected Vehiculo(String patente, String marca, String modelo, int anio, Cliente cliente) {
        if (patente == null || patente.isBlank()) {
            throw new IllegalArgumentException("La patente es obligatoria.");
        }
        if (marca == null || marca.isBlank() || modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("La marca y el modelo son obligatorios.");
        }
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.cliente = Objects.requireNonNull(cliente, "El cliente es obligatorio.");
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public abstract BigDecimal coeficienteManoDeObra();
}
