package vista;

import dao.RepuestoDao;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import modelo.Repuesto;

public final class RepuestosFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    private final RepuestoDao repuestoDao;
    private final GrillaRepuesto grilla = new GrillaRepuesto();
    private boolean ocupado;

    public RepuestosFrame(RepuestoDao repuestoDao) {
        this.repuestoDao = repuestoDao;
        initComponents();
        tabla.setModel(grilla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(290);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(135);
        configurarImportes();
        tabla.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting() && !ocupado) {
                cargarSeleccion();
            }
        });
        cargarRepuestos("");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formulario = new javax.swing.JPanel();
        etiquetaCodigo = new javax.swing.JLabel();
        codigo = new javax.swing.JTextField();
        etiquetaDescripcion = new javax.swing.JLabel();
        descripcion = new javax.swing.JTextField();
        etiquetaPrecio = new javax.swing.JLabel();
        precio = new javax.swing.JTextField();
        etiquetaStock = new javax.swing.JLabel();
        stock = new javax.swing.JTextField();
        lista = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        estado = new javax.swing.JLabel();
        guardar = new javax.swing.JButton();
        actualizar = new javax.swing.JButton();

        setTitle("Repuestos");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formulario.setBorder(javax.swing.BorderFactory.createTitledBorder("Repuesto seleccionado"));
        etiquetaCodigo.setText("Código");
        etiquetaCodigo.setLabelFor(codigo);
        codigo.setColumns(10);
        codigo.setEditable(false);
        etiquetaDescripcion.setText("Descripción");
        etiquetaDescripcion.setLabelFor(descripcion);
        descripcion.setEditable(false);
        etiquetaPrecio.setText("Precio unitario");
        etiquetaPrecio.setLabelFor(precio);
        precio.setColumns(12);
        etiquetaStock.setText("Stock");
        etiquetaStock.setLabelFor(stock);
        stock.setColumns(8);
        tabla.setModel(new javax.swing.table.DefaultTableModel(new Object[0][0], new String[] {"Código", "Descripción", "Precio unitario", "Stock"}));
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(26);
        guardar.setText("Guardar cambios");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });
        actualizar.setText("Actualizar lista");
        actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout formularioLayout = new javax.swing.GroupLayout(formulario);
        formulario.setLayout(formularioLayout);
        formularioLayout.setHorizontalGroup(
            formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(formularioLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(codigo, 0, 100, Short.MAX_VALUE))
            .addGap(12)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(descripcion, 0, 270, Short.MAX_VALUE))
            .addGap(12)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(precio, 0, 120, Short.MAX_VALUE))
            .addGap(12)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(stock, 0, 80, Short.MAX_VALUE))
            .addGap(8))
        );
        formularioLayout.setVerticalGroup(
            formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(formularioLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8))
        );
        lista.setViewportView(tabla);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(16)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(formulario, 0, 720, Short.MAX_VALUE)
                .addComponent(lista, 0, 720, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 12, Short.MAX_VALUE)
                    .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8)
                    .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(14)
            .addComponent(formulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(8)
            .addComponent(lista, 0, 260, Short.MAX_VALUE)
            .addGap(8)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        guardarCambios();
    }//GEN-LAST:event_guardarActionPerformed

    private void actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarActionPerformed
        cargarRepuestos(codigo.getText());
    }//GEN-LAST:event_actualizarActionPerformed

    private void configurarImportes() {
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar;
    private javax.swing.JTextField codigo;
    private javax.swing.JTextField descripcion;
    private javax.swing.JLabel estado;
    private javax.swing.JLabel etiquetaCodigo;
    private javax.swing.JLabel etiquetaDescripcion;
    private javax.swing.JLabel etiquetaPrecio;
    private javax.swing.JLabel etiquetaStock;
    private javax.swing.JPanel formulario;
    private javax.swing.JButton guardar;
    private javax.swing.JScrollPane lista;
    private javax.swing.JTextField precio;
    private javax.swing.JTextField stock;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
