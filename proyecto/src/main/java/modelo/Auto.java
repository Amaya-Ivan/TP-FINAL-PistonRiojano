package modelo;

import java.math.BigDecimal;
import java.util.Objects;

public final class Auto extends Vehiculo {
    private static final BigDecimal COEFICIENTE_DIESEL = new BigDecimal("1.20");

    private final int cantidadPuertas;
    private final TipoCombustible combustible;

    public Auto(String patente, String marca, String modelo, int anio, Cliente cliente,
                int cantidadPuertas, TipoCombustible combustible) {
        super(patente, marca, modelo, anio, cliente);
        if (cantidadPuertas <= 0) {
            throw new IllegalArgumentException("La cantidad de puertas debe ser mayor que cero.");
        }
        this.cantidadPuertas = cantidadPuertas;
        this.combustible = Objects.requireNonNull(combustible, "El combustible es obligatorio.");
    }

    public int getCantidadPuertas() {
        return cantidadPuertas;
    }

    public TipoCombustible getCombustible() {
        return combustible;
    }

    @Override
    public BigDecimal coeficienteManoDeObra() {
        return combustible == TipoCombustible.DIESEL ? COEFICIENTE_DIESEL : BigDecimal.ONE;
    }
}
