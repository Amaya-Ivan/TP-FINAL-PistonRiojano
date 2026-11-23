package vista;

import dao.ClienteDao;
import dao.VehiculoDao;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import modelo.Auto;
import modelo.Cliente;
import modelo.Moto;
import modelo.TipoCombustible;
import modelo.Vehiculo;

public final class VehiculosFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;
    private static final String AUTO = "Auto";
    private static final String MOTO = "Moto";

    private final ClienteDao clienteDao;
    private final VehiculoDao vehiculoDao;
    private final JComboBox<Cliente> clientes = new JComboBox<>();
    private final JComboBox<String> tipo = new JComboBox<>(new String[]{AUTO, MOTO});
    private final JTextField patente = new JTextField();
    private final JTextField marca = new JTextField();
    private final JTextField modelo = new JTextField();
    private final JTextField anio = new JTextField();
    private final JTextField puertas = new JTextField();
    private final JComboBox<TipoCombustible> combustible = new JComboBox<>(TipoCombustible.values());
    private final JTextField cilindrada = new JTextField();
    private final JPanel datosTipo = new JPanel(new CardLayout());
    private final JButton actualizar = new JButton("Actualizar clientes");
    private final JButton guardar = new JButton("Guardar vehículo");
    private final GrillaVehiculo grilla = new GrillaVehiculo();
    private final JLabel estado = new JLabel();
    private boolean ocupado;
    private boolean cambiandoClientes;

    public VehiculosFrame(ClienteDao clienteDao, VehiculoDao vehiculoDao) {
        super("Vehículos", true, true, true, true);
        this.clienteDao = clienteDao;
        this.vehiculoDao = vehiculoDao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(crearContenido());
        tipo.addActionListener(evento -> mostrarTipo());
        clientes.addActionListener(evento -> {
            if (!cambiandoClientes) {
                limpiarFormulario();
                cargarVehiculos();
            }
        });
        actualizar.addActionListener(evento -> cargarClientes());
        guardar.addActionListener(evento -> guardarVehiculo());
        cargarClientes();
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBorder(BorderFactory.createEmptyBorder(14, 16, 12, 16));

        DefaultListCellRenderer renderizador = new DefaultListCellRenderer();
        clientes.setRenderer((lista, cliente, indice, seleccionado, foco) ->
                renderizador.getListCellRendererComponent(lista, cliente == null ? "" :
                        cliente.getApellido() + ", " + cliente.getNombre() + " (" + cliente.getDni() + ")",
                        indice, seleccionado, foco));
        JPanel titular = new JPanel(new BorderLayout(10, 0));
        JLabel etiquetaCliente = new JLabel("Cliente");
        etiquetaCliente.setLabelFor(clientes);
        titular.add(etiquetaCliente, BorderLayout.WEST);
        titular.add(clientes, BorderLayout.CENTER);
        titular.add(actualizar, BorderLayout.EAST);

        JPanel datosComunes = new JPanel(new GridLayout(1, 4, 10, 0));
        datosComunes.add(campo("Patente", patente));
        datosComunes.add(campo("Marca", marca));
        datosComunes.add(campo("Modelo", modelo));
        datosComunes.add(campo("Año", anio));
        JPanel camposAuto = new JPanel(new GridLayout(1, 2, 10, 0));
        camposAuto.add(campo("Cantidad de puertas", puertas));
        camposAuto.add(campo("Combustible", combustible));
        datosTipo.add(camposAuto, AUTO);
        datosTipo.add(campo("Cilindrada (cc)", cilindrada), MOTO);

        JPanel filaTipo = new JPanel(new GridLayout(1, 2, 10, 0));
        filaTipo.add(campo("Tipo de vehículo", tipo));
        filaTipo.add(datosTipo);
        JPanel formulario = new JPanel(new GridLayout(2, 1, 0, 9));
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Registrar vehículo"),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));
        formulario.add(datosComunes);
        formulario.add(filaTipo);

        JPanel encabezado = new JPanel(new BorderLayout(0, 10));
        encabezado.add(titular, BorderLayout.NORTH);
        encabezado.add(formulario, BorderLayout.CENTER);
        contenido.add(encabezado, BorderLayout.NORTH);

        JTable tabla = new JTable(grilla);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(26);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(105);
        contenido.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.add(estado, BorderLayout.WEST);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        acciones.add(guardar);
        pie.add(acciones, BorderLayout.EAST);
        contenido.add(pie, BorderLayout.SOUTH);
        return contenido;
    }

    private JPanel campo(String texto, JComponent entrada) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setLabelFor(entrada);
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(entrada, BorderLayout.CENTER);
        return panel;
    }

    private void cargarClientes() {
        Cliente anterior = (Cliente) clientes.getSelectedItem();
        consultar("Cargando clientes…", clienteDao::listar, lista -> {
            cambiarModeloClientes(lista, anterior);
            cargarVehiculos();
        }, null);
    }

    private void cambiarModeloClientes(List<Cliente> lista, Cliente anterior) {
        cambiandoClientes = true;
        try {
            clientes.setModel(new DefaultComboBoxModel<>(lista.toArray(Cliente[]::new)));
            if (anterior != null) {
                lista.stream().filter(cliente -> cliente.getDni().equals(anterior.getDni()))
                        .findFirst().ifPresent(clientes::setSelectedItem);
            }
        } finally {
            cambiandoClientes = false;
        }
    }

    private void cargarVehiculos() {
        Cliente seleccionado = (Cliente) clientes.getSelectedItem();
        grilla.cargar(List.of());
        if (seleccionado == null) {
            actualizarControles();
            return;
        }
        consultar("Cargando vehículos…", () -> vehiculoDao.listarPorCliente(seleccionado.getDni()), lista -> {
            grilla.cargar(lista);
            actualizarControles();
        }, null);
    }

    private void guardarVehiculo() {
        Vehiculo nuevoVehiculo;
        try {
            Cliente titular = (Cliente) clientes.getSelectedItem();
            if (titular == null) {
                throw new IllegalArgumentException("Seleccioná un cliente.");
            }
            int modeloAnio = enteroPositivo(anio.getText(), "el año");
            String identificador = patente.getText().strip().toUpperCase(Locale.ROOT);
            String fabricante = marca.getText().strip();
            String nombreModelo = modelo.getText().strip();
            if (AUTO.equals(tipo.getSelectedItem())) {
                nuevoVehiculo = new Auto(identificador, fabricante, nombreModelo, modeloAnio, titular,
                        enteroPositivo(puertas.getText(), "la cantidad de puertas"),
                        (TipoCombustible) combustible.getSelectedItem());
            } else {
                nuevoVehiculo = new Moto(identificador, fabricante, nombreModelo, modeloAnio, titular,
                        enteroPositivo(cilindrada.getText(), "la cilindrada"));
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Revisá los datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        consultar("Guardando vehículo…", () -> vehiculoDao.crear(nuevoVehiculo), guardado -> {
            if (guardado) {
                limpiarFormulario();
                cargarVehiculos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el vehículo.",
                        getTitle(), JOptionPane.WARNING_MESSAGE);
            }
        }, "Ya existe un vehículo con esa patente.");
    }

    private void limpiarFormulario() {
        patente.setText("");
        marca.setText("");
        modelo.setText("");
        anio.setText("");
        puertas.setText("");
        cilindrada.setText("");
    }

    private int enteroPositivo(String texto, String campo) {
        try {
            int valor = Integer.parseInt(texto.strip());
            if (valor > 0) {
                return valor;
            }
        } catch (NumberFormatException ex) {
            // La misma indicación sirve si el valor falta o está fuera de rango.
        }
        throw new IllegalArgumentException("Ingresá " + campo + " como un número entero mayor que cero.");
    }

    private void mostrarTipo() {
        ((CardLayout) datosTipo.getLayout()).show(datosTipo, (String) tipo.getSelectedItem());
        actualizarControles();
    }

    private <T> void consultar(String mensaje, Callable<T> tarea, Consumer<T> resultado, String mensajeDuplicado) {
        ocupado = true;
        estado.setText(mensaje);
        actualizarControles();
        TareaSwing.ejecutar(this, tarea, resultado, () -> {
            ocupado = false;
            actualizarControles();
        }, mensajeDuplicado);
    }

    private void actualizarControles() {
        boolean clienteElegido = clientes.getSelectedItem() != null;
        boolean editable = !ocupado && clienteElegido;
        boolean esAuto = AUTO.equals(tipo.getSelectedItem());
        actualizar.setEnabled(!ocupado);
        clientes.setEnabled(editable);
        patente.setEnabled(editable);
        marca.setEnabled(editable);
        modelo.setEnabled(editable);
        anio.setEnabled(editable);
        tipo.setEnabled(editable);
        puertas.setEnabled(editable && esAuto);
        combustible.setEnabled(editable && esAuto);
        cilindrada.setEnabled(editable && !esAuto);
        guardar.setEnabled(editable);
        if (!ocupado) {
            int cantidad = grilla.getRowCount();
            estado.setText(clienteElegido ? (cantidad == 1 ? "1 vehículo" : cantidad + " vehículos")
                    : "No hay clientes disponibles");
        }
    }
}
