package vista;

import java.math.BigDecimal;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Repuesto;

final class GrillaRepuesto extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNAS = {"Código", "Descripción", "Precio unitario", "Stock"};
    private List<Repuesto> repuestos = List.of();

    void cargar(List<Repuesto> repuestos) {
        this.repuestos = List.copyOf(repuestos);
        fireTableDataChanged();
    }

    Repuesto getRepuesto(int fila) {
        return repuestos.get(fila);
    }

    @Override
    public int getRowCount() {
        return repuestos.size();
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
            case 2 -> BigDecimal.class;
            case 3 -> Integer.class;
            default -> String.class;
        };
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Repuesto repuesto = getRepuesto(fila);
        return switch (columna) {
            case 0 -> repuesto.getCodigo();
            case 1 -> repuesto.getDescripcion();
            case 2 -> repuesto.getPrecioUnitario();
            case 3 -> repuesto.getStock();
            default -> throw new IndexOutOfBoundsException(columna);
        };
    }
}
