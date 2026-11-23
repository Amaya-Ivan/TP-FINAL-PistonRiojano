package vista;

import dao.MecanicoDao;
import dao.OrdenTrabajoDao;
import dao.RepuestoDao;
import dao.VehiculoDao;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import modelo.DetalleRepuesto;
import modelo.EstadoOrden;
import modelo.Mecanico;
import modelo.OrdenTrabajo;
import modelo.Repuesto;
import modelo.StockInsuficienteException;
import modelo.Vehiculo;

public final class NuevaOrdenFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    private final VehiculoDao vehiculoDao;
    private final MecanicoDao mecanicoDao;
    private final RepuestoDao repuestoDao;
    private final OrdenTrabajoDao ordenDao;
    private final JComboBox<Vehiculo> vehiculos = new JComboBox<>();
    private final JComboBox<Mecanico> mecanicos = new JComboBox<>();
    private final JComboBox<Repuesto> repuestos = new JComboBox<>();
    private final JTextArea problema = new JTextArea(2, 30);
    private final JTextField horas = new JTextField("0", 8);
    private final JTextField cantidad = new JTextField("1", 8);
    private final JLabel fecha = new JLabel();
    private final JLabel manoDeObra = new JLabel("—");
    private final JLabel importeRepuestos = new JLabel();
    private final JLabel total = new JLabel("—");
    private final JLabel estado = new JLabel(" ");
    private final GrillaDetalleOrden grilla = new GrillaDetalleOrden();
    private final JTable tabla = new JTable(grilla);
    private final JButton actualizar = new JButton("Actualizar listas");
    private final JButton agregar = new JButton("Agregar");
    private final JButton quitar = new JButton("Quitar seleccionado");
    private final JButton guardar = new JButton("Guardar orden");
    private final NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
    private LocalDate fechaIngreso = LocalDate.now();
    private boolean ocupado;

    public NuevaOrdenFrame(VehiculoDao vehiculoDao, MecanicoDao mecanicoDao,
                            RepuestoDao repuestoDao, OrdenTrabajoDao ordenDao) {
        super("Nueva orden de trabajo", true, true, true, true);
        this.vehiculoDao = vehiculoDao;
        this.mecanicoDao = mecanicoDao;
        this.repuestoDao = repuestoDao;
        this.ordenDao = ordenDao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(crearContenido());
        configurarEventos();
        actualizarTotales();
        cargarListas(null);
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        contenido.add(crearFormulario(), BorderLayout.NORTH);
        contenido.add(crearPanelRepuestos(), BorderLayout.CENTER);
        contenido.add(crearPie(), BorderLayout.SOUTH);
        return contenido;
    }

    private JPanel crearFormulario() {
        configurarCombo(vehiculos, v -> v.getPatente() + " · " + v.getMarca() + " " + v.getModelo()
                + " · " + v.getCliente().getApellido());
        configurarCombo(mecanicos, m -> m.getApellido() + ", " + m.getNombre() + " (" + m.getLegajo() + ")");
        JPanel seleccion = new JPanel(new GridLayout(1, 2, 12, 0));
        seleccion.add(campo("Vehículo", vehiculos));
        seleccion.add(campo("Mecánico responsable", mecanicos));

        problema.setLineWrap(true);
        problema.setWrapStyleWord(true);
        JPanel datos = new JPanel(new BorderLayout(10, 0));
        datos.add(campo("Descripción del problema", new JScrollPane(problema)), BorderLayout.CENTER);
        JPanel horario = new JPanel(new GridLayout(2, 2, 8, 5));
        fecha.setText(FORMATO_FECHA.format(fechaIngreso));
        horario.add(new JLabel("Fecha de ingreso"));
        horario.add(fecha);
        JLabel etiquetaHoras = new JLabel("Horas de mano de obra");
        etiquetaHoras.setLabelFor(horas);
        horario.add(etiquetaHoras);
        horario.add(horas);
        datos.add(horario, BorderLayout.EAST);

        JPanel formulario = new JPanel(new BorderLayout(0, 9));
        formulario.setBorder(bordeSeccion("Datos de la orden"));
        formulario.add(seleccion, BorderLayout.NORTH);
        formulario.add(datos, BorderLayout.CENTER);
        return formulario;
    }

    private JPanel crearPanelRepuestos() {
        configurarCombo(repuestos, r -> r.getCodigo() + " · " + r.getDescripcion()
                + " · Stock " + r.getStock());
        JPanel agregarRepuesto = new JPanel(new BorderLayout(10, 0));
        agregarRepuesto.add(campo("Repuesto", repuestos), BorderLayout.CENTER);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 0));
        acciones.add(campo("Cantidad", cantidad));
        acciones.add(agregar);
        acciones.add(quitar);
        agregarRepuesto.add(acciones, BorderLayout.EAST);

        tabla.setRowHeight(25);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(340);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(75);
        DefaultTableCellRenderer importe = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void setValue(Object valor) {
                setText(valor == null ? "" : moneda.format(valor));
            }
        };
        importe.setHorizontalAlignment(SwingConstants.RIGHT);
        tabla.setDefaultRenderer(BigDecimal.class, importe);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setColumnHeaderView(tabla.getTableHeader());

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(bordeSeccion("Repuestos de la orden"));
        panel.add(agregarRepuesto, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPie() {
        JPanel resumen = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        resumen.add(new JLabel("Mano de obra"));
        resumen.add(manoDeObra);
        resumen.add(new JLabel("Repuestos"));
        resumen.add(importeRepuestos);
        JLabel etiquetaTotal = new JLabel("Total");
        etiquetaTotal.setFont(etiquetaTotal.getFont().deriveFont(Font.BOLD));
        total.setFont(total.getFont().deriveFont(Font.BOLD, 17f));
        resumen.add(etiquetaTotal);
        resumen.add(total);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.add(actualizar);
        botones.add(guardar);
        JPanel acciones = new JPanel(new BorderLayout(8, 0));
        acciones.add(estado, BorderLayout.CENTER);
        acciones.add(botones, BorderLayout.EAST);

        JPanel pie = new JPanel(new BorderLayout(0, 8));
        pie.add(resumen, BorderLayout.NORTH);
        pie.add(acciones, BorderLayout.SOUTH);
        return pie;
    }

    private javax.swing.border.Border bordeSeccion(String titulo) {
        return BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(titulo),
                BorderFactory.createEmptyBorder(3, 8, 7, 8));
    }

    private JPanel campo(String titulo, JComponent entrada) {
        JPanel panel = new JPanel(new BorderLayout(0, 3));
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setLabelFor(entrada);
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(entrada, BorderLayout.CENTER);
        return panel;
    }

    private <T> void configurarCombo(JComboBox<T> combo, Function<T, String> mostrar) {
        DefaultListCellRenderer renderizador = new DefaultListCellRenderer();
        combo.setRenderer((lista, valor, indice, seleccionado, foco) ->
                renderizador.getListCellRendererComponent(lista, valor == null ? "" : mostrar.apply(valor),
                        indice, seleccionado, foco));
    }

    private void configurarEventos() {
        vehiculos.addActionListener(evento -> actualizarTotales());
        mecanicos.addActionListener(evento -> actualizarTotales());
        DocumentListener cambios = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent evento) {
                actualizarTotales();
            }

            @Override
            public void removeUpdate(DocumentEvent evento) {
                actualizarTotales();
            }

            @Override
            public void changedUpdate(DocumentEvent evento) {
                actualizarTotales();
            }
        };
        problema.getDocument().addDocumentListener(cambios);
        horas.getDocument().addDocumentListener(cambios);
        tabla.getSelectionModel().addListSelectionListener(evento -> actualizarControles());
        actualizar.addActionListener(evento -> cargarListas(null));
        agregar.addActionListener(evento -> agregarRepuesto());
        quitar.addActionListener(evento -> quitarRepuesto());
        guardar.addActionListener(evento -> guardarOrden());
    }

    private void cargarListas(String mensajeFinal) {
        String patente = vehiculoElegido() == null ? null : vehiculoElegido().getPatente();
        Integer legajo = mecanicoElegido() == null ? null : mecanicoElegido().getLegajo();
        Repuesto actual = (Repuesto) repuestos.getSelectedItem();
        String codigo = actual == null ? null : actual.getCodigo();
        String mensaje = mensajeFinal == null ? "Cargando listas…" : mensajeFinal;
        ejecutar(mensaje, () -> new ListasOrden(vehiculoDao.listar(), mecanicoDao.listar(), repuestoDao.listar()),
                listas -> {
                    cargarCombo(vehiculos, listas.vehiculos(), Vehiculo[]::new, Vehiculo::getPatente, patente);
                    cargarCombo(mecanicos, listas.mecanicos(), Mecanico[]::new, Mecanico::getLegajo, legajo);
                    cargarCombo(repuestos, listas.repuestos(), Repuesto[]::new, Repuesto::getCodigo, codigo);
                    estado.setText(mensajeFinal != null ? mensajeFinal : " ");
                    if (mensajeFinal == null && (listas.vehiculos().isEmpty() || listas.mecanicos().isEmpty())) {
                        estado.setText("Cargá un vehículo y un mecánico para registrar una orden.");
                    }
                    actualizarControles();
                    actualizarTotales();
                });
    }

    private <T, K> void cargarCombo(JComboBox<T> combo, List<T> datos, java.util.function.IntFunction<T[]> arreglo,
                                   Function<T, K> clave, K seleccion) {
        combo.setModel(new DefaultComboBoxModel<>(datos.toArray(arreglo)));
        if (seleccion != null) {
            datos.stream().filter(dato -> seleccion.equals(clave.apply(dato)))
                    .findFirst().ifPresent(combo::setSelectedItem);
        }
    }

    private void agregarRepuesto() {
        try {
            Repuesto repuesto = (Repuesto) repuestos.getSelectedItem();
            if (repuesto == null) {
                throw new IllegalArgumentException("Seleccioná un repuesto.");
            }
            OrdenTrabajo orden = crearBorrador();
            orden.agregarRepuesto(repuesto, leerCantidad());
            grilla.cargar(orden.getDetalles());
            cantidad.setText("1");
            estado.setText(" ");
            actualizarTotales();
        } catch (IllegalArgumentException | StockInsuficienteException ex) {
            mostrarValidacion(ex.getMessage());
        }
    }

    private void quitarRepuesto() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        List<DetalleRepuesto> detalles = new ArrayList<>(grilla.getDetalles());
        detalles.remove(tabla.convertRowIndexToModel(fila));
        grilla.cargar(detalles);
        tabla.clearSelection();
        estado.setText(" ");
        actualizarTotales();
        actualizarControles();
    }

    private void guardarOrden() {
        OrdenTrabajo orden;
        try {
            orden = crearBorrador();
        } catch (IllegalArgumentException ex) {
            mostrarValidacion(ex.getMessage());
            return;
        }
        ejecutar("Guardando orden…", () -> ordenDao.crear(orden), numero -> {
            reiniciarFormulario();
            cargarListas("Orden N.º " + numero + " guardada.");
        });
    }

    private void reiniciarFormulario() {
        fechaIngreso = LocalDate.now();
        fecha.setText(FORMATO_FECHA.format(fechaIngreso));
        grilla.cargar(List.of());
        problema.setText("");
        horas.setText("0");
        cantidad.setText("1");
        actualizarTotales();
    }

    private OrdenTrabajo crearBorrador() {
        Vehiculo vehiculo = vehiculoElegido();
        Mecanico mecanico = mecanicoElegido();
        if (vehiculo == null || mecanico == null) {
            throw new IllegalArgumentException("Seleccioná el vehículo y el mecánico responsable.");
        }
        return new OrdenTrabajo(0, vehiculo, mecanico, fechaIngreso, problema.getText().strip(),
                leerHoras(), EstadoOrden.PENDIENTE, grilla.getDetalles());
    }

    private BigDecimal leerHoras() {
        String valor = horas.getText().strip();
        if (!valor.matches("[0-9]{1,6}([.,][0-9]{1,2})?")) {
            throw new IllegalArgumentException("Ingresá horas de cero o más, con hasta dos decimales.");
        }
        return new BigDecimal(valor.replace(',', '.'));
    }

    private int leerCantidad() {
        try {
            int unidades = Integer.parseInt(cantidad.getText().strip());
            if (unidades > 0) {
                return unidades;
            }
        } catch (NumberFormatException ex) {
            // Se muestra la indicación también cuando el valor queda fuera de rango.
        }
        throw new IllegalArgumentException("Ingresá una cantidad entera mayor que cero.");
    }

    private void actualizarTotales() {
        BigDecimal subtotalRepuestos = BigDecimal.ZERO;
        for (DetalleRepuesto detalle : grilla.getDetalles()) {
            subtotalRepuestos = subtotalRepuestos.add(detalle.calcularSubtotal());
        }
        importeRepuestos.setText(moneda.format(subtotalRepuestos));
        try {
            OrdenTrabajo orden = crearBorrador();
            manoDeObra.setText(moneda.format(orden.calcularManoDeObra()));
            total.setText(moneda.format(orden.calcularTotal()));
        } catch (IllegalArgumentException ex) {
            manoDeObra.setText("—");
            total.setText("—");
        }
    }

    private Vehiculo vehiculoElegido() {
        return (Vehiculo) vehiculos.getSelectedItem();
    }

    private Mecanico mecanicoElegido() {
        return (Mecanico) mecanicos.getSelectedItem();
    }

    private void mostrarValidacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Revisá los datos", JOptionPane.WARNING_MESSAGE);
    }

    private <T> void ejecutar(String mensaje, Callable<T> tarea, Consumer<T> resultado) {
        ocupado = true;
        estado.setText(mensaje);
        actualizarControles();
        TareaSwing.ejecutar(this, tarea, resultado, () -> {
            ocupado = false;
            if (estado.getText().endsWith("…")) {
                estado.setText(" ");
            }
            actualizarControles();
        }, null);
    }

    private void actualizarControles() {
        boolean habilitado = !ocupado;
        boolean puedeGuardar = habilitado && vehiculoElegido() != null && mecanicoElegido() != null;
        vehiculos.setEnabled(habilitado);
        mecanicos.setEnabled(habilitado);
        repuestos.setEnabled(habilitado);
        problema.setEnabled(habilitado);
        horas.setEnabled(habilitado);
        cantidad.setEnabled(habilitado);
        tabla.setEnabled(habilitado);
        actualizar.setEnabled(habilitado);
        guardar.setEnabled(puedeGuardar);
        agregar.setEnabled(puedeGuardar && repuestos.getSelectedItem() != null);
        quitar.setEnabled(habilitado && tabla.getSelectedRow() >= 0);
    }

    private record ListasOrden(List<Vehiculo> vehiculos, List<Mecanico> mecanicos, List<Repuesto> repuestos) {
    }
}
