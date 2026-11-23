package vista;

import dao.OrdenTrabajoDao;
import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import modelo.EstadoOrden;
import modelo.OrdenTrabajo;

public final class ConsultaOrdenesFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    private final OrdenTrabajoDao ordenDao;
    private final GrillaOrdenTrabajo grilla = new GrillaOrdenTrabajo();
    private final JTable tabla = new JTable(grilla);
    private final JComboBox<FiltroEstado> filtro = new JComboBox<>();
    private final JButton actualizar = new JButton("Actualizar lista");
    private final JButton avanzar = new JButton("Avanzar estado");
    private final JLabel estado = new JLabel(" ");
    private boolean ocupado;

    public ConsultaOrdenesFrame(OrdenTrabajoDao ordenDao) {
        super("Consultar órdenes", true, true, true, true);
        this.ordenDao = ordenDao;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        filtro.addItem(new FiltroEstado(null));
        for (EstadoOrden opcion : EstadoOrden.values()) {
            filtro.addItem(new FiltroEstado(opcion));
        }
        setContentPane(crearContenido());
        filtro.addActionListener(evento -> recargar());
        actualizar.addActionListener(evento -> recargar());
        avanzar.addActionListener(evento -> avanzarEstado());
        tabla.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                actualizarControles();
            }
        });
        recargar();
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBorder(BorderFactory.createEmptyBorder(14, 16, 12, 16));

        JLabel etiqueta = new JLabel("Estado");
        etiqueta.setLabelFor(filtro);
        JPanel campoFiltro = new JPanel(new BorderLayout(10, 0));
        campoFiltro.add(etiqueta, BorderLayout.WEST);
        campoFiltro.add(filtro, BorderLayout.CENTER);
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.add(campoFiltro, BorderLayout.WEST);
        encabezado.add(actualizar, BorderLayout.EAST);
        contenido.add(encabezado, BorderLayout.NORTH);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(26);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(185);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(126);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(120);
        tabla.setDefaultRenderer(LocalDate.class,
                renderizar(valor -> FORMATO_FECHA.format((LocalDate) valor), SwingConstants.CENTER));
        tabla.setDefaultRenderer(EstadoOrden.class,
                renderizar(valor -> nombreEstado((EstadoOrden) valor), SwingConstants.LEFT));
        NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
        tabla.setDefaultRenderer(BigDecimal.class, renderizar(moneda::format, SwingConstants.RIGHT));
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setColumnHeaderView(tabla.getTableHeader());
        contenido.add(scroll, BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout(10, 0));
        pie.add(estado, BorderLayout.CENTER);
        pie.add(avanzar, BorderLayout.EAST);
        contenido.add(pie, BorderLayout.SOUTH);
        return contenido;
    }

    private DefaultTableCellRenderer renderizar(Function<Object, String> formato, int alineacion) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void setValue(Object valor) {
                setText(valor == null ? "" : formato.apply(valor));
            }
        };
        renderer.setHorizontalAlignment(alineacion);
        return renderer;
    }

    void recargar() {
        if (!ocupado) {
            OrdenTrabajo seleccion = ordenSeleccionada();
            cargarOrdenes(seleccion == null ? null : seleccion.getNumero(), null);
        }
    }

    private void cargarOrdenes(Long numeroSeleccionado, String confirmacion) {
        FiltroEstado opcion = (FiltroEstado) filtro.getSelectedItem();
        tabla.clearSelection();
        grilla.cargar(List.of());
        ejecutar("Cargando órdenes…", () -> ordenDao.listar(opcion.estado()), ordenes -> {
            grilla.cargar(ordenes);
            if (numeroSeleccionado != null) {
                for (int fila = 0; fila < grilla.getRowCount(); fila++) {
                    if (grilla.getOrden(fila).getNumero() == numeroSeleccionado) {
                        int filaVisible = tabla.convertRowIndexToView(fila);
                        tabla.setRowSelectionInterval(filaVisible, filaVisible);
                        break;
                    }
                }
            }
            estado.setText(confirmacion == null ? cantidadOrdenes() : confirmacion + " · " + cantidadOrdenes());
            actualizarControles();
        });
    }

    private void avanzarEstado() {
        OrdenTrabajo seleccion = ordenSeleccionada();
        EstadoOrden siguiente = seleccion == null ? null : siguienteEstado(seleccion.getEstado());
        if (siguiente == null) {
            return;
        }
        long numero = seleccion.getNumero();
        ejecutar("Actualizando estado…", () -> ordenDao.cambiarEstado(numero, seleccion.getEstado(), siguiente),
                cambiado -> cargarOrdenes(numero, cambiado
                        ? "Orden N.º " + numero + ": " + nombreEstado(siguiente)
                        : "La orden cambió. Se actualizó la lista."));
    }

    private OrdenTrabajo ordenSeleccionada() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : grilla.getOrden(tabla.convertRowIndexToModel(fila));
    }

    private EstadoOrden siguienteEstado(EstadoOrden actual) {
        return Arrays.stream(EstadoOrden.values()).filter(actual::puedePasarA).findFirst().orElse(null);
    }

    private static String nombreEstado(EstadoOrden estado) {
        return switch (estado) {
            case PENDIENTE -> "Pendiente";
            case EN_REPARACION -> "En reparación";
            case FINALIZADA -> "Finalizada";
            case ENTREGADA -> "Entregada";
        };
    }

    private <T> void ejecutar(String mensaje, Callable<T> tarea, Consumer<T> resultado) {
        ocupado = true;
        estado.setText(mensaje);
        actualizarControles();
        TareaSwing.ejecutar(this, tarea, resultado, () -> {
            ocupado = false;
            estado.setText(cantidadOrdenes());
            actualizarControles();
        }, null);
    }

    private String cantidadOrdenes() {
        int cantidad = grilla.getRowCount();
        return cantidad == 1 ? "1 orden" : cantidad + " órdenes";
    }

    private void actualizarControles() {
        OrdenTrabajo seleccion = ordenSeleccionada();
        EstadoOrden siguiente = seleccion == null ? null : siguienteEstado(seleccion.getEstado());
        tabla.setEnabled(!ocupado);
        filtro.setEnabled(!ocupado);
        actualizar.setEnabled(!ocupado);
        avanzar.setEnabled(!ocupado && siguiente != null);
        avanzar.setText(siguiente == null ? "Avanzar estado"
                : "Avanzar a " + nombreEstado(siguiente).toLowerCase(Locale.ROOT));
    }

    private record FiltroEstado(EstadoOrden estado) {
        @Override
        public String toString() {
            return estado == null ? "Todos" : nombreEstado(estado);
        }
    }
}
