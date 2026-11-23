package vista;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import modelo.Auto;
import modelo.Moto;
import modelo.Vehiculo;

final class GrillaVehiculo extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNAS = {
        "Patente", "Tipo", "Marca", "Modelo", "Año", "Puertas", "Combustible", "Cilindrada (cc)"
    };
    private List<Vehiculo> vehiculos = List.of();

    void cargar(List<Vehiculo> vehiculos) {
        this.vehiculos = List.copyOf(vehiculos);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return vehiculos.size();
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
            case 4, 5, 7 -> Integer.class;
            default -> String.class;
        };
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Vehiculo vehiculo = vehiculos.get(fila);
        return switch (columna) {
            case 0 -> vehiculo.getPatente();
            case 1 -> vehiculo instanceof Auto ? "Auto" : "Moto";
            case 2 -> vehiculo.getMarca();
            case 3 -> vehiculo.getModelo();
            case 4 -> vehiculo.getAnio();
            case 5 -> vehiculo instanceof Auto auto ? auto.getCantidadPuertas() : null;
            case 6 -> vehiculo instanceof Auto auto ? auto.getCombustible().name() : null;
            case 7 -> vehiculo instanceof Moto moto ? moto.getCilindrada() : null;
            default -> throw new IndexOutOfBoundsException(columna);
        };
    }
}
