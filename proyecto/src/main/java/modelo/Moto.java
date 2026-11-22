package modelo;

import java.math.BigDecimal;

public final class Moto extends Vehiculo {
    private static final BigDecimal COEFICIENTE_HASTA_250_CC = new BigDecimal("0.80");

    private final int cilindrada;

    public Moto(String patente, String marca, String modelo, int anio, Cliente cliente,
                int cilindrada) {
        super(patente, marca, modelo, anio, cliente);
        if (cilindrada <= 0) {
            throw new IllegalArgumentException("La cilindrada debe ser mayor que cero.");
        }
        this.cilindrada = cilindrada;
    }

    public int getCilindrada() {
        return cilindrada;
    }

    @Override
    public BigDecimal coeficienteManoDeObra() {
        return cilindrada <= 250 ? COEFICIENTE_HASTA_250_CC : BigDecimal.ONE;
    }
}
