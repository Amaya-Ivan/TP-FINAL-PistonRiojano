package modelo;

public enum EstadoOrden {
    PENDIENTE,
    EN_REPARACION,
    FINALIZADA,
    ENTREGADA;

    public boolean puedePasarA(EstadoOrden nuevoEstado) {
        return switch (this) {
            case PENDIENTE -> nuevoEstado == EN_REPARACION;
            case EN_REPARACION -> nuevoEstado == FINALIZADA;
            case FINALIZADA -> nuevoEstado == ENTREGADA;
            case ENTREGADA -> false;
        };
    }
}
