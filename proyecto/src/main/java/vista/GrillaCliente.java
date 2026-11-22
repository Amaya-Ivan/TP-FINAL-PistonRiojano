package vista;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Cliente;

public final class GrillaCliente extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNAS = {"DNI", "Nombre", "Apellido", "Teléfono", "Correo electrónico"};

    private List<Cliente> clientes = List.of();

    public void cargar(List<Cliente> clientes) {
        this.clientes = List.copyOf(clientes);
        fireTableDataChanged();
    }

    public Cliente getCliente(int fila) {
        return clientes.get(fila);
    }

    @Override
    public int getRowCount() {
        return clientes.size();
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
        return String.class;
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Cliente cliente = getCliente(fila);
        return switch (columna) {
            case 0 -> cliente.getDni();
            case 1 -> cliente.getNombre();
            case 2 -> cliente.getApellido();
            case 3 -> cliente.getTelefono();
            case 4 -> cliente.getEmail();
            default -> throw new IndexOutOfBoundsException(columna);
        };
    }
}
