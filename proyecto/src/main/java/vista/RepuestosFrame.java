package vista;

import dao.RepuestoDao;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import modelo.Repuesto;

public final class RepuestosFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    private final RepuestoDao repuestoDao;
    private final GrillaRepuesto grilla = new GrillaRepuesto();
    private final JTable tabla = new JTable(grilla);
    private final JTextField codigo = new JTextField();
    private final JTextField descripcion = new JTextField();
    private final JTextField precio = new JTextField();
    private final JTextField stock = new JTextField();
    private final JButton guardar = new JButton("Guardar cambios");
    private final JButton actualizar = new JButton("Actualizar lista");
    private final JLabel estado = new JLabel();
    private boolean ocupado;

    public RepuestosFrame(RepuestoDao repuestoDao) {
        super("Repuestos", true, true, true, true);
        this.repuestoDao = repuestoDao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(crearContenido());
        tabla.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting() && !ocupado) {
                cargarSeleccion();
            }
        });
        guardar.addActionListener(evento -> guardarCambios());
        actualizar.addActionListener(evento -> cargarRepuestos(codigo.getText()));
        cargarRepuestos("");
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBorder(BorderFactory.createEmptyBorder(14, 16, 12, 16));

        JPanel formulario = new JPanel(new BorderLayout(12, 0));
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Repuesto seleccionado"),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));
        codigo.setColumns(10);
        codigo.setEditable(false);
        descripcion.setEditable(false);
        JPanel valores = new JPanel(new GridLayout(1, 2, 10, 0));
        valores.add(campo("Precio unitario", precio));
        valores.add(campo("Stock", stock));
        formulario.add(campo("Código", codigo), BorderLayout.WEST);
        formulario.add(campo("Descripción", descripcion), BorderLayout.CENTER);
        formulario.add(valores, BorderLayout.EAST);
        precio.setColumns(12);
        stock.setColumns(8);
        contenido.add(formulario, BorderLayout.NORTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(26);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(290);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(135);
        NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
        DefaultTableCellRenderer importe = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void setValue(Object valor) {
                setText(valor == null ? "" : moneda.format(valor));
            }
        };
        importe.setHorizontalAlignment(SwingConstants.RIGHT);
        tabla.getColumnModel().getColumn(2).setCellRenderer(importe);
        contenido.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.add(estado, BorderLayout.WEST);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.add(guardar);
        acciones.add(actualizar);
        pie.add(acciones, BorderLayout.EAST);
        contenido.add(pie, BorderLayout.SOUTH);
        return contenido;
    }

    private JPanel campo(String texto, JTextField entrada) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setLabelFor(entrada);
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(entrada, BorderLayout.CENTER);
        return panel;
    }

    private void cargarRepuestos(String codigoSeleccionado) {
        consultar("Cargando repuestos…", repuestoDao::listar, lista -> {
            grilla.cargar(lista);
            tabla.clearSelection();
            for (int fila = 0; fila < grilla.getRowCount(); fila++) {
                if (grilla.getRepuesto(fila).getCodigo().equals(codigoSeleccionado)) {
                    int filaVisible = tabla.convertRowIndexToView(fila);
                    tabla.setRowSelectionInterval(filaVisible, filaVisible);
                    break;
                }
            }
            cargarSeleccion();
        });
    }

    private void cargarSeleccion() {
        Repuesto repuesto = seleccionado();
        codigo.setText(repuesto == null ? "" : repuesto.getCodigo());
        descripcion.setText(repuesto == null ? "" : repuesto.getDescripcion());
        precio.setText(repuesto == null ? "" : repuesto.getPrecioUnitario().toPlainString().replace('.', ','));
        stock.setText(repuesto == null ? "" : Integer.toString(repuesto.getStock()));
        actualizarControles();
    }

    private Repuesto seleccionado() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : grilla.getRepuesto(tabla.convertRowIndexToModel(fila));
    }

    private void guardarCambios() {
        Repuesto actual = seleccionado();
        if (actual == null) {
            return;
        }
        Repuesto editado;
        try {
            editado = new Repuesto(actual.getCodigo(), actual.getDescripcion(), leerPrecio(), leerStock());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Revisá los datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        consultar("Guardando cambios…", () -> repuestoDao.modificar(editado), guardado -> {
            if (!guardado) {
                JOptionPane.showMessageDialog(this, "El repuesto ya no está disponible.",
                        getTitle(), JOptionPane.WARNING_MESSAGE);
            }
            cargarRepuestos(editado.getCodigo());
        });
    }

    private BigDecimal leerPrecio() {
        String texto = precio.getText().strip();
        if (!texto.matches("[0-9]{1,10}([.,][0-9]{1,2})?")) {
            throw new IllegalArgumentException("Ingresá un precio de hasta 10 dígitos enteros y 2 decimales, sin separador de miles.");
        }
        return new BigDecimal(texto.replace(',', '.'));
    }

    private int leerStock() {
        try {
            int unidades = Integer.parseInt(stock.getText().strip());
            if (unidades >= 0) {
                return unidades;
            }
        } catch (NumberFormatException ex) {
            // Se informa el formato esperado tanto si hay texto como si el número queda fuera de rango.
        }
        throw new IllegalArgumentException("Ingresá el stock como un número entero de cero o más.");
    }

    private <T> void consultar(String mensaje, Callable<T> tarea, Consumer<T> resultado) {
        ocupado = true;
        estado.setText(mensaje);
        actualizarControles();
        TareaSwing.ejecutar(this, tarea, resultado, () -> {
            ocupado = false;
            actualizarControles();
        }, null);
    }

    private void actualizarControles() {
        boolean editable = !ocupado && tabla.getSelectedRow() >= 0;
        tabla.setEnabled(!ocupado);
        actualizar.setEnabled(!ocupado);
        precio.setEnabled(editable);
        stock.setEnabled(editable);
        guardar.setEnabled(editable);
        if (!ocupado) {
            int cantidad = grilla.getRowCount();
            estado.setText(cantidad == 1 ? "1 repuesto" : cantidad + " repuestos");
        }
    }
}
