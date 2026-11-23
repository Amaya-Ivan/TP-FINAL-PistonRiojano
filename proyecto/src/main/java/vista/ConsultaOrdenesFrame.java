package vista;

import dao.OrdenTrabajoDao;
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
import javax.swing.JInternalFrame;
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
    private boolean ocupado;

    public ConsultaOrdenesFrame(OrdenTrabajoDao ordenDao) {
        this.ordenDao = ordenDao;
        initComponents();
        filtro.addItem(new FiltroEstado(null));
        for (EstadoOrden opcion : EstadoOrden.values()) {
            filtro.addItem(new FiltroEstado(opcion));
        }
        configurarTabla();
        filtro.addActionListener(evento -> recargar());
        tabla.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting()) {
                actualizarControles();
            }
        });
        recargar();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        etiquetaFiltro = new javax.swing.JLabel();
        filtro = new javax.swing.JComboBox<>();
        actualizar = new javax.swing.JButton();
        lista = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        estado = new javax.swing.JLabel();
        avanzar = new javax.swing.JButton();

        setTitle("Consultar órdenes");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        etiquetaFiltro.setText("Estado");
        etiquetaFiltro.setLabelFor(filtro);
        actualizar.setText("Actualizar lista");
        actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarActionPerformed(evt);
            }
        });
        tabla.setModel(new javax.swing.table.DefaultTableModel(new Object[0][0], new String[] {"N.º de orden", "Fecha", "Patente", "Mecánico", "Estado", "Total"}));
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(26);
        estado.setText(" ");
        avanzar.setText("Avanzar estado");
        avanzar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                avanzarActionPerformed(evt);
            }
        });

        lista.setViewportView(tabla);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(16)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(etiquetaFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 16, Short.MAX_VALUE)
                    .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(lista, 0, 720, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 12, Short.MAX_VALUE)
                    .addComponent(avanzar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(14)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addComponent(lista, 0, 260, Short.MAX_VALUE)
            .addGap(8)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(avanzar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarActionPerformed
        recargar();
    }//GEN-LAST:event_actualizarActionPerformed

    private void avanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_avanzarActionPerformed
        avanzarEstado();
    }//GEN-LAST:event_avanzarActionPerformed

    private void configurarTabla() {
        tabla.setModel(grilla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(185);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(126);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(120);
        tabla.setDefaultRenderer(LocalDate.class,
                renderizar(valor -> FORMATO_FECHA.format((LocalDate) valor), SwingConstants.CENTER));
        tabla.setDefaultRenderer(EstadoOrden.class,
                renderizar(valor -> nombreEstado((EstadoOrden) valor), SwingConstants.LEFT));
        NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
        tabla.setDefaultRenderer(BigDecimal.class, renderizar(moneda::format, SwingConstants.RIGHT));
        lista.setColumnHeaderView(tabla.getTableHeader());
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar;
    private javax.swing.JButton avanzar;
    private javax.swing.JLabel estado;
    private javax.swing.JLabel etiquetaFiltro;
    private javax.swing.JComboBox<FiltroEstado> filtro;
    private javax.swing.JScrollPane lista;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
