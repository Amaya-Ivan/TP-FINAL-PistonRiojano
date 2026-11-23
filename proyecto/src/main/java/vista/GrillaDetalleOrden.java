package vista;

import java.math.BigDecimal;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.DetalleRepuesto;

final class GrillaDetalleOrden extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNAS = {"Repuesto", "Cantidad", "Precio unitario", "Subtotal"};
    private List<DetalleRepuesto> detalles = List.of();

    void cargar(List<DetalleRepuesto> detalles) {
        this.detalles = List.copyOf(detalles);
        fireTableDataChanged();
    }

    List<DetalleRepuesto> getDetalles() {
        return detalles;
    }

    @Override
    public int getRowCount() {
        return detalles.size();
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
            case 0 -> String.class;
            case 1 -> Integer.class;
            default -> BigDecimal.class;
        };
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        DetalleRepuesto detalle = detalles.get(fila);
        return switch (columna) {
            case 0 -> detalle.getRepuesto().getCodigo() + " · " + detalle.getRepuesto().getDescripcion();
            case 1 -> detalle.getCantidad();
            case 2 -> detalle.getPrecioUnitarioAplicado();
            case 3 -> detalle.calcularSubtotal();
            default -> throw new IndexOutOfBoundsException(columna);
        };
    }
}
