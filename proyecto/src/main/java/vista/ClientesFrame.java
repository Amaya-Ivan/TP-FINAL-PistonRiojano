package vista;

import dao.ClienteDao;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import modelo.Cliente;

public final class ClientesFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    private final ClienteDao clienteDao;
    private final GrillaCliente grilla = new GrillaCliente();
    private final JTable tabla = new JTable(grilla);
    private final JTextField dni = new JTextField(14);
    private final JTextField nombre = new JTextField(20);
    private final JTextField apellido = new JTextField(20);
    private final JTextField telefono = new JTextField(16);
    private final JTextField correo = new JTextField(28);
    private final JButton nuevo = new JButton("Nuevo");
    private final JButton guardar = new JButton("Guardar");
    private final JButton eliminar = new JButton("Eliminar");
    private final JButton actualizar = new JButton("Actualizar lista");
    private final JLabel estado = new JLabel();
    private String dniOriginal;
    private boolean ocupado;

    public ClientesFrame(ClienteDao clienteDao) {
        super("Clientes", true, true, true, true);
        this.clienteDao = clienteDao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(crearContenido());
        configurarEventos();
        cargarClientes();
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout(0, 14));
        contenido.setBorder(BorderFactory.createEmptyBorder(14, 16, 12, 16));
        contenido.add(crearFormulario(), BorderLayout.NORTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(26);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(145);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(220);
        contenido.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.add(estado, BorderLayout.WEST);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.add(nuevo);
        acciones.add(guardar);
        acciones.add(eliminar);
        acciones.add(actualizar);
        pie.add(acciones, BorderLayout.EAST);
        contenido.add(pie, BorderLayout.SOUTH);
        return contenido;
    }

    private JPanel crearFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Datos del cliente"),
                BorderFactory.createEmptyBorder(2, 6, 6, 6)));
        agregarCampo(formulario, "DNI", dni, 0, 0);
        agregarCampo(formulario, "Teléfono", telefono, 2, 0);
        agregarCampo(formulario, "Nombre", nombre, 0, 1);
        agregarCampo(formulario, "Apellido", apellido, 2, 1);
        agregarCampo(formulario, "Correo (opcional)", correo, 0, 2);
        return formulario;
    }

    private void agregarCampo(JPanel formulario, String texto, JComponent campo, int columna, int fila) {
        GridBagConstraints posicion = new GridBagConstraints();
        posicion.gridx = columna;
        posicion.gridy = fila;
        posicion.insets = new Insets(4, 7, 4, 7);
        posicion.anchor = GridBagConstraints.LINE_START;
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setLabelFor(campo);
        formulario.add(etiqueta, posicion);

        posicion.gridx++;
        posicion.weightx = 1;
        posicion.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(campo, posicion);
    }

    private void configurarEventos() {
        nuevo.addActionListener(evento -> {
            limpiarFormulario();
            dni.requestFocusInWindow();
        });
        guardar.addActionListener(evento -> guardarCliente());
        eliminar.addActionListener(evento -> eliminarCliente());
        actualizar.addActionListener(evento -> cargarClientes());
        tabla.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting() && !ocupado) {
                cargarSeleccion();
            }
        });
    }

    private void cargarClientes() {
        ejecutar("Cargando clientes…", clienteDao::listar, clientes -> {
            grilla.cargar(clientes);
            limpiarFormulario();
        });
    }

    private void guardarCliente() {
        Cliente cliente;
        try {
            String documento = dni.getText().strip();
            if (!documento.matches("[0-9]+")) {
                throw new IllegalArgumentException("Ingresá el DNI usando solo números.");
            }
            cliente = new Cliente(documento, nombre.getText().strip(), apellido.getText().strip(),
                    telefono.getText().strip(), correo.getText().strip());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Revisá los datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String anterior = dniOriginal;
        ejecutar("Guardando cliente…",
                () -> anterior == null ? clienteDao.crear(cliente) : clienteDao.modificar(anterior, cliente),
                guardado -> {
                    if (!guardado) {
                        JOptionPane.showMessageDialog(this, "El cliente ya no está disponible.",
                                "Clientes", JOptionPane.WARNING_MESSAGE);
                    }
                    limpiarFormulario();
                    cargarClientes();
                });
    }

    private void eliminarCliente() {
        if (dniOriginal == null) {
            return;
        }
        String documento = dniOriginal;
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Querés eliminar al cliente con DNI " + documento + "?",
                "Eliminar cliente", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            ejecutar("Eliminando cliente…", () -> clienteDao.eliminar(documento), eliminado -> {
                if (!eliminado) {
                    JOptionPane.showMessageDialog(this, "El cliente ya no está disponible.",
                            "Clientes", JOptionPane.WARNING_MESSAGE);
                }
                limpiarFormulario();
                cargarClientes();
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            limpiarFormulario();
            return;
        }
        Cliente cliente = grilla.getCliente(tabla.convertRowIndexToModel(fila));
        dniOriginal = cliente.getDni();
        dni.setText(cliente.getDni());
        nombre.setText(cliente.getNombre());
        apellido.setText(cliente.getApellido());
        telefono.setText(cliente.getTelefono());
        correo.setText(cliente.getEmail());
        actualizarControles();
    }

    private void limpiarFormulario() {
        dniOriginal = null;
        tabla.clearSelection();
        dni.setText("");
        nombre.setText("");
        apellido.setText("");
        telefono.setText("");
        correo.setText("");
        actualizarControles();
    }

    private <T> void ejecutar(String mensaje, Callable<T> tarea, Consumer<T> alCompletar) {
        ocupado = true;
        estado.setText(mensaje);
        actualizarControles();
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return tarea.call();
            }

            @Override
            protected void done() {
                ocupado = false;
                if (isClosed()) {
                    return;
                }
                actualizarControles();
                try {
                    alCompletar.accept(get());
                } catch (InterruptedException ex) {
                    mostrarError("Se interrumpió la operación.");
                } catch (ExecutionException ex) {
                    Throwable causa = ex.getCause();
                    String detalle = causa.getMessage();
                    if (causa instanceof SQLException sql && "23505".equals(sql.getSQLState())) {
                        detalle = "Ya existe un cliente con ese DNI.";
                    }
                    mostrarError(detalle != null ? detalle : "No se pudo completar la operación.");
                }
            }
        }.execute();
    }

    private void actualizarControles() {
        boolean disponible = !ocupado;
        tabla.setEnabled(disponible);
        nuevo.setEnabled(disponible);
        guardar.setEnabled(disponible);
        eliminar.setEnabled(disponible && dniOriginal != null);
        actualizar.setEnabled(disponible);
        dni.setEnabled(disponible);
        nombre.setEnabled(disponible);
        apellido.setEnabled(disponible);
        telefono.setEnabled(disponible);
        correo.setEnabled(disponible);
        if (disponible) {
            int cantidad = grilla.getRowCount();
            estado.setText(cantidad == 1 ? "1 cliente" : cantidad + " clientes");
        }
    }

    private void mostrarError(String mensaje) {
        estado.setText("Operación incompleta");
        JOptionPane.showMessageDialog(this, mensaje, "Clientes", JOptionPane.ERROR_MESSAGE);
    }
}
