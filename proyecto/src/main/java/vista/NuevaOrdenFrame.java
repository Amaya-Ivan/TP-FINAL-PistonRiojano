package vista;

import dao.MecanicoDao;
import dao.OrdenTrabajoDao;
import dao.RepuestoDao;
import dao.VehiculoDao;
import java.awt.Font;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
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
    private final GrillaDetalleOrden grilla = new GrillaDetalleOrden();
    private final NumberFormat moneda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-AR"));
    private LocalDate fechaIngreso = LocalDate.now();
    private boolean ocupado;

    public NuevaOrdenFrame(VehiculoDao vehiculoDao, MecanicoDao mecanicoDao,
                            RepuestoDao repuestoDao, OrdenTrabajoDao ordenDao) {
        this.vehiculoDao = vehiculoDao;
        this.mecanicoDao = mecanicoDao;
        this.repuestoDao = repuestoDao;
        this.ordenDao = ordenDao;
        initComponents();
        configurarContenido();
        configurarEventos();
        actualizarTotales();
        cargarListas(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formulario = new javax.swing.JPanel();
        etiquetaVehiculos = new javax.swing.JLabel();
        vehiculos = new javax.swing.JComboBox<>();
        etiquetaMecanicos = new javax.swing.JLabel();
        mecanicos = new javax.swing.JComboBox<>();
        etiquetaProblema = new javax.swing.JLabel();
        descripcion = new javax.swing.JScrollPane();
        problema = new javax.swing.JTextArea();
        etiquetaFecha = new javax.swing.JLabel();
        fecha = new javax.swing.JLabel();
        etiquetaHoras = new javax.swing.JLabel();
        horas = new javax.swing.JTextField();
        panelRepuestos = new javax.swing.JPanel();
        etiquetaRepuestos = new javax.swing.JLabel();
        repuestos = new javax.swing.JComboBox<>();
        etiquetaCantidad = new javax.swing.JLabel();
        cantidad = new javax.swing.JTextField();
        agregar = new javax.swing.JButton();
        quitar = new javax.swing.JButton();
        lista = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        etiquetaManoDeObra = new javax.swing.JLabel();
        manoDeObra = new javax.swing.JLabel();
        etiquetaImporteRepuestos = new javax.swing.JLabel();
        importeRepuestos = new javax.swing.JLabel();
        etiquetaTotal = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        estado = new javax.swing.JLabel();
        actualizar = new javax.swing.JButton();
        guardar = new javax.swing.JButton();

        setTitle("Nueva orden de trabajo");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formulario.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la orden"));
        etiquetaVehiculos.setText("Vehículo");
        etiquetaVehiculos.setLabelFor(vehiculos);
        etiquetaMecanicos.setText("Mecánico responsable");
        etiquetaMecanicos.setLabelFor(mecanicos);
        etiquetaProblema.setText("Descripción del problema");
        problema.setRows(2);
        problema.setColumns(30);
        problema.setLineWrap(true);
        problema.setWrapStyleWord(true);
        etiquetaProblema.setLabelFor(problema);
        etiquetaFecha.setText("Fecha de ingreso");
        fecha.setText("—");
        etiquetaHoras.setText("Horas de mano de obra");
        etiquetaHoras.setLabelFor(horas);
        horas.setColumns(8);
        horas.setText("0");
        panelRepuestos.setBorder(javax.swing.BorderFactory.createTitledBorder("Repuestos de la orden"));
        etiquetaRepuestos.setText("Repuesto");
        etiquetaRepuestos.setLabelFor(repuestos);
        etiquetaCantidad.setText("Cantidad");
        etiquetaCantidad.setLabelFor(cantidad);
        cantidad.setColumns(8);
        cantidad.setText("1");
        agregar.setText("Agregar");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });
        quitar.setText("Quitar seleccionado");
        quitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitarActionPerformed(evt);
            }
        });
        tabla.setModel(new javax.swing.table.DefaultTableModel(new Object[0][0], new String[] {"Repuesto", "Cantidad", "Precio unitario", "Subtotal"}));
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(25);
        etiquetaManoDeObra.setText("Mano de obra");
        manoDeObra.setText("—");
        etiquetaImporteRepuestos.setText("Repuestos");
        importeRepuestos.setText("—");
        etiquetaTotal.setText("Total");
        total.setText("—");
        estado.setText(" ");
        actualizar.setText("Actualizar listas");
        actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarActionPerformed(evt);
            }
        });
        guardar.setText("Guardar orden");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        descripcion.setViewportView(problema);
        javax.swing.GroupLayout formularioLayout = new javax.swing.GroupLayout(formulario);
        formulario.setLayout(formularioLayout);
        formularioLayout.setHorizontalGroup(
            formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(formularioLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(formularioLayout.createSequentialGroup()
                    .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(etiquetaVehiculos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(vehiculos, 0, 340, Short.MAX_VALUE))
                    .addGap(12)
                    .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(etiquetaMecanicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(mecanicos, 0, 335, Short.MAX_VALUE)))
                .addGroup(formularioLayout.createSequentialGroup()
                    .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(etiquetaProblema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(descripcion, 0, 460, Short.MAX_VALUE))
                    .addGap(12)
                    .addGroup(formularioLayout.createSequentialGroup()
                        .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(etiquetaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(etiquetaHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10)
                        .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(horas, 0, 88, Short.MAX_VALUE)))))
            .addGap(8))
        );
        formularioLayout.setVerticalGroup(
            formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(formularioLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaVehiculos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaMecanicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(vehiculos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(mecanicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(formularioLayout.createSequentialGroup()
                    .addComponent(etiquetaProblema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6)
                    .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(formularioLayout.createSequentialGroup()
                    .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(etiquetaFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(8)
                    .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(etiquetaHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(horas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGap(8))
        );
        lista.setViewportView(tabla);
        javax.swing.GroupLayout panelRepuestosLayout = new javax.swing.GroupLayout(panelRepuestos);
        panelRepuestos.setLayout(panelRepuestosLayout);
        panelRepuestosLayout.setHorizontalGroup(
            panelRepuestosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(panelRepuestosLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(panelRepuestosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelRepuestosLayout.createSequentialGroup()
                    .addGroup(panelRepuestosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(etiquetaRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(repuestos, 0, 380, Short.MAX_VALUE))
                    .addGap(10)
                    .addGroup(panelRepuestosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(etiquetaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(8)
                    .addComponent(agregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8)
                    .addComponent(quitar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(lista, 0, 700, Short.MAX_VALUE))
            .addGap(8))
        );
        panelRepuestosLayout.setVerticalGroup(
            panelRepuestosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(panelRepuestosLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(panelRepuestosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(panelRepuestosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(repuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(agregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(quitar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addComponent(lista, 0, 142, Short.MAX_VALUE)
            .addGap(8))
        );
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(16)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(formulario, 0, 740, Short.MAX_VALUE)
                .addComponent(panelRepuestos, 0, 740, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(etiquetaManoDeObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12)
                    .addComponent(manoDeObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12)
                    .addComponent(etiquetaImporteRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12)
                    .addComponent(importeRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12)
                    .addComponent(etiquetaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(12)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 12, Short.MAX_VALUE)
                    .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8)
                    .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(14)
            .addComponent(formulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(8)
            .addComponent(panelRepuestos, 0, 230, Short.MAX_VALUE)
            .addGap(8)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaManoDeObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(manoDeObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaImporteRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(importeRepuestos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        agregarRepuesto();
    }//GEN-LAST:event_agregarActionPerformed

    private void quitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitarActionPerformed
        quitarRepuesto();
    }//GEN-LAST:event_quitarActionPerformed

    private void actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarActionPerformed
        cargarListas(null);
    }//GEN-LAST:event_actualizarActionPerformed

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        guardarOrden();
    }//GEN-LAST:event_guardarActionPerformed

    private void configurarContenido() {
        configurarCombo(vehiculos, v -> v.getPatente() + " · " + v.getMarca() + " " + v.getModelo()
                + " · " + v.getCliente().getApellido());
        configurarCombo(mecanicos, m -> m.getApellido() + ", " + m.getNombre() + " (" + m.getLegajo() + ")");
        configurarCombo(repuestos, r -> r.getCodigo() + " · " + r.getDescripcion() + " · Stock " + r.getStock());
        fecha.setText(FORMATO_FECHA.format(fechaIngreso));
        etiquetaTotal.setFont(etiquetaTotal.getFont().deriveFont(Font.BOLD));
        total.setFont(total.getFont().deriveFont(Font.BOLD, 17f));
        tabla.setModel(grilla);
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
        lista.setColumnHeaderView(tabla.getTableHeader());
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar;
    private javax.swing.JButton agregar;
    private javax.swing.JTextField cantidad;
    private javax.swing.JScrollPane descripcion;
    private javax.swing.JLabel estado;
    private javax.swing.JLabel etiquetaCantidad;
    private javax.swing.JLabel etiquetaFecha;
    private javax.swing.JLabel etiquetaHoras;
    private javax.swing.JLabel etiquetaImporteRepuestos;
    private javax.swing.JLabel etiquetaManoDeObra;
    private javax.swing.JLabel etiquetaMecanicos;
    private javax.swing.JLabel etiquetaProblema;
    private javax.swing.JLabel etiquetaRepuestos;
    private javax.swing.JLabel etiquetaTotal;
    private javax.swing.JLabel etiquetaVehiculos;
    private javax.swing.JLabel fecha;
    private javax.swing.JPanel formulario;
    private javax.swing.JButton guardar;
    private javax.swing.JTextField horas;
    private javax.swing.JLabel importeRepuestos;
    private javax.swing.JScrollPane lista;
    private javax.swing.JLabel manoDeObra;
    private javax.swing.JComboBox<Mecanico> mecanicos;
    private javax.swing.JPanel panelRepuestos;
    private javax.swing.JTextArea problema;
    private javax.swing.JButton quitar;
    private javax.swing.JComboBox<Repuesto> repuestos;
    private javax.swing.JTable tabla;
    private javax.swing.JLabel total;
    private javax.swing.JComboBox<Vehiculo> vehiculos;
    // End of variables declaration//GEN-END:variables
}
