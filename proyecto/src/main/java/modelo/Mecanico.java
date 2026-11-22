package modelo;

import java.math.BigDecimal;
import java.util.Objects;

public final class Mecanico {
    private final int legajo;
    private final String nombre;
    private final String apellido;
    private final Especialidad especialidad;
    private final BigDecimal valorHora;

    public Mecanico(int legajo, String nombre, String apellido, Especialidad especialidad,
                    BigDecimal valorHora) {
        if (nombre == null || nombre.isBlank() || apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El nombre y el apellido son obligatorios.");
        }
        if (valorHora == null || valorHora.signum() < 0) {
            throw new IllegalArgumentException("El valor por hora debe ser cero o mayor.");
        }
        this.legajo = legajo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = Objects.requireNonNull(especialidad, "La especialidad es obligatoria.");
        this.valorHora = valorHora;
    }

    public int getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public BigDecimal getValorHora() {
        return valorHora;
    }
}
