package vista;

import dao.ClienteDao;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import modelo.Cliente;

public final class ClientesFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    private final ClienteDao clienteDao;
    private final GrillaCliente grilla = new GrillaCliente();
    private String dniOriginal;
    private boolean ocupado;

    public ClientesFrame(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
        initComponents();
        tabla.setModel(grilla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(145);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(220);
        tabla.getSelectionModel().addListSelectionListener(evento -> {
            if (!evento.getValueIsAdjusting() && !ocupado) {
                cargarSeleccion();
            }
        });
        cargarClientes();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        formulario = new javax.swing.JPanel();
        etiquetaDni = new javax.swing.JLabel();
        dni = new javax.swing.JTextField();
        etiquetaTelefono = new javax.swing.JLabel();
        telefono = new javax.swing.JTextField();
        etiquetaNombre = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        etiquetaApellido = new javax.swing.JLabel();
        apellido = new javax.swing.JTextField();
        etiquetaCorreo = new javax.swing.JLabel();
        correo = new javax.swing.JTextField();
        lista = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        estado = new javax.swing.JLabel();
        nuevo = new javax.swing.JButton();
        guardar = new javax.swing.JButton();
        eliminar = new javax.swing.JButton();
        actualizar = new javax.swing.JButton();

        setTitle("Clientes");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formulario.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del cliente"));
        etiquetaDni.setText("DNI");
        etiquetaDni.setLabelFor(dni);
        dni.setColumns(14);
        etiquetaTelefono.setText("Teléfono");
        etiquetaTelefono.setLabelFor(telefono);
        telefono.setColumns(16);
        etiquetaNombre.setText("Nombre");
        etiquetaNombre.setLabelFor(nombre);
        nombre.setColumns(20);
        etiquetaApellido.setText("Apellido");
        etiquetaApellido.setLabelFor(apellido);
        apellido.setColumns(20);
        etiquetaCorreo.setText("Correo (opcional)");
        etiquetaCorreo.setLabelFor(correo);
        correo.setColumns(28);
        tabla.setModel(new javax.swing.table.DefaultTableModel(new Object[0][0], new String[] {"DNI", "Nombre", "Apellido", "Teléfono", "Correo electrónico"}));
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(26);
        nuevo.setText("Nuevo");
        nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoActionPerformed(evt);
            }
        });
        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });
        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
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
                .addComponent(etiquetaDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(dni, 0, 150, Short.MAX_VALUE)
                .addComponent(nombre, 0, 150, Short.MAX_VALUE)
                .addComponent(correo, 0, 150, Short.MAX_VALUE))
            .addGap(18)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(telefono, 0, 150, Short.MAX_VALUE)
                .addComponent(apellido, 0, 150, Short.MAX_VALUE))
            .addGap(8))
        );
        formularioLayout.setVerticalGroup(
            formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(formularioLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(dni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(correo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8)
                    .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8)
                    .addComponent(eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoActionPerformed
        limpiarFormulario();
        dni.requestFocusInWindow();
    }//GEN-LAST:event_nuevoActionPerformed

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        guardarCliente();
    }//GEN-LAST:event_guardarActionPerformed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
        eliminarCliente();
    }//GEN-LAST:event_eliminarActionPerformed

    private void actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarActionPerformed
        cargarClientes();
    }//GEN-LAST:event_actualizarActionPerformed

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar;
    private javax.swing.JTextField apellido;
    private javax.swing.JTextField correo;
    private javax.swing.JTextField dni;
    private javax.swing.JButton eliminar;
    private javax.swing.JLabel estado;
    private javax.swing.JLabel etiquetaApellido;
    private javax.swing.JLabel etiquetaCorreo;
    private javax.swing.JLabel etiquetaDni;
    private javax.swing.JLabel etiquetaNombre;
    private javax.swing.JLabel etiquetaTelefono;
    private javax.swing.JPanel formulario;
    private javax.swing.JButton guardar;
    private javax.swing.JScrollPane lista;
    private javax.swing.JTextField nombre;
    private javax.swing.JButton nuevo;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField telefono;
    // End of variables declaration//GEN-END:variables
}
