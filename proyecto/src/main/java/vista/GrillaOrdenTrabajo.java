package vista;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.EstadoOrden;
import modelo.Mecanico;
import modelo.OrdenTrabajo;

final class GrillaOrdenTrabajo extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNAS = {"N.º de orden", "Fecha", "Patente", "Mecánico", "Estado", "Total"};
    private List<OrdenTrabajo> ordenes = List.of();

    void cargar(List<OrdenTrabajo> ordenes) {
        this.ordenes = List.copyOf(ordenes);
        fireTableDataChanged();
    }

    OrdenTrabajo getOrden(int fila) {
        return ordenes.get(fila);
    }

    @Override
    public int getRowCount() {
        return ordenes.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int columna) {
        return COLUMNAS[columna];
    }

    @Override
    public Class<?> getColumnClass(int columna) {
        return switch (columna) {
            case 0 -> Long.class;
            case 1 -> LocalDate.class;
            case 4 -> EstadoOrden.class;
            case 5 -> BigDecimal.class;
            default -> String.class;
        };
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        OrdenTrabajo orden = getOrden(fila);
        Mecanico mecanico = orden.getMecanico();
        return switch (columna) {
            case 0 -> orden.getNumero();
            case 1 -> orden.getFechaIngreso();
            case 2 -> orden.getVehiculo().getPatente();
            case 3 -> mecanico.getApellido() + ", " + mecanico.getNombre();
            case 4 -> orden.getEstado();
            case 5 -> orden.calcularTotal();
            default -> throw new IndexOutOfBoundsException(columna);
        };
    }
}
