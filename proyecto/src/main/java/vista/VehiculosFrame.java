package vista;

import dao.ClienteDao;
import dao.VehiculoDao;
import java.awt.CardLayout;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
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
    private final GrillaVehiculo grilla = new GrillaVehiculo();
    private boolean ocupado;
    private boolean cambiandoClientes;

    public VehiculosFrame(ClienteDao clienteDao, VehiculoDao vehiculoDao) {
        this.clienteDao = clienteDao;
        this.vehiculoDao = vehiculoDao;
        initComponents();
        configurarClienteYCombustible();
        tabla.setModel(grilla);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(105);
        cargarClientes();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        etiquetaClientes = new javax.swing.JLabel();
        clientes = new javax.swing.JComboBox<>();
        actualizar = new javax.swing.JButton();
        formulario = new javax.swing.JPanel();
        datosComunes = new javax.swing.JPanel();
        etiquetaPatente = new javax.swing.JLabel();
        patente = new javax.swing.JTextField();
        etiquetaMarca = new javax.swing.JLabel();
        marca = new javax.swing.JTextField();
        etiquetaModelo = new javax.swing.JLabel();
        modelo = new javax.swing.JTextField();
        etiquetaAnio = new javax.swing.JLabel();
        anio = new javax.swing.JTextField();
        etiquetaTipo = new javax.swing.JLabel();
        tipo = new javax.swing.JComboBox<>();
        datosTipo = new javax.swing.JPanel();
        camposAuto = new javax.swing.JPanel();
        etiquetaPuertas = new javax.swing.JLabel();
        puertas = new javax.swing.JTextField();
        etiquetaCombustible = new javax.swing.JLabel();
        combustible = new javax.swing.JComboBox<>();
        camposMoto = new javax.swing.JPanel();
        etiquetaCilindrada = new javax.swing.JLabel();
        cilindrada = new javax.swing.JTextField();
        lista = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        estado = new javax.swing.JLabel();
        guardar = new javax.swing.JButton();

        setTitle("Vehículos");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        etiquetaClientes.setText("Cliente");
        etiquetaClientes.setLabelFor(clientes);
        clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientesActionPerformed(evt);
            }
        });
        actualizar.setText("Actualizar clientes");
        actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarActionPerformed(evt);
            }
        });
        formulario.setBorder(javax.swing.BorderFactory.createTitledBorder("Registrar vehículo"));
        etiquetaPatente.setText("Patente");
        etiquetaPatente.setLabelFor(patente);
        etiquetaMarca.setText("Marca");
        etiquetaMarca.setLabelFor(marca);
        etiquetaModelo.setText("Modelo");
        etiquetaModelo.setLabelFor(modelo);
        etiquetaAnio.setText("Año");
        etiquetaAnio.setLabelFor(anio);
        etiquetaTipo.setText("Tipo de vehículo");
        etiquetaTipo.setLabelFor(tipo);
        tipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"Auto", "Moto"}));
        tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoActionPerformed(evt);
            }
        });
        etiquetaPuertas.setText("Cantidad de puertas");
        etiquetaPuertas.setLabelFor(puertas);
        etiquetaCombustible.setText("Combustible");
        etiquetaCombustible.setLabelFor(combustible);
        etiquetaCilindrada.setText("Cilindrada (cc)");
        etiquetaCilindrada.setLabelFor(cilindrada);
        tabla.setModel(new javax.swing.table.DefaultTableModel(new Object[0][0], new String[] {"Patente", "Tipo", "Marca", "Modelo", "Año", "Puertas", "Combustible", "Cilindrada (cc)"}));
        tabla.setAutoCreateRowSorter(true);
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(26);
        guardar.setText("Guardar vehículo");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout datosComunesLayout = new javax.swing.GroupLayout(datosComunes);
        datosComunes.setLayout(datosComunesLayout);
        datosComunesLayout.setHorizontalGroup(
            datosComunesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(datosComunesLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(datosComunesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaPatente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(patente, 0, 155, Short.MAX_VALUE))
            .addGap(12)
            .addGroup(datosComunesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(marca, 0, 155, Short.MAX_VALUE))
            .addGap(12)
            .addGroup(datosComunesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(modelo, 0, 155, Short.MAX_VALUE))
            .addGap(12)
            .addGroup(datosComunesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(anio, 0, 155, Short.MAX_VALUE))
            .addGap(8))
        );
        datosComunesLayout.setVerticalGroup(
            datosComunesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(datosComunesLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(datosComunesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaPatente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(datosComunesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(patente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(anio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8))
        );
        datosTipo.setLayout(new java.awt.CardLayout());
        datosTipo.add(camposAuto, "Auto");
        javax.swing.GroupLayout camposAutoLayout = new javax.swing.GroupLayout(camposAuto);
        camposAuto.setLayout(camposAutoLayout);
        camposAutoLayout.setHorizontalGroup(
            camposAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(camposAutoLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(camposAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaPuertas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(puertas, 0, 140, Short.MAX_VALUE))
            .addGap(12)
            .addGroup(camposAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaCombustible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(combustible, 0, 140, Short.MAX_VALUE))
            .addGap(8))
        );
        camposAutoLayout.setVerticalGroup(
            camposAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(camposAutoLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(camposAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaPuertas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(etiquetaCombustible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(camposAutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(puertas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(combustible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8))
        );
        datosTipo.add(camposMoto, "Moto");
        javax.swing.GroupLayout camposMotoLayout = new javax.swing.GroupLayout(camposMoto);
        camposMoto.setLayout(camposMotoLayout);
        camposMotoLayout.setHorizontalGroup(
            camposMotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(camposMotoLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(camposMotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaCilindrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cilindrada, 0, 295, Short.MAX_VALUE))
            .addGap(8))
        );
        camposMotoLayout.setVerticalGroup(
            camposMotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(camposMotoLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(camposMotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaCilindrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addGroup(camposMotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cilindrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8))
        );
        javax.swing.GroupLayout formularioLayout = new javax.swing.GroupLayout(formulario);
        formulario.setLayout(formularioLayout);
        formularioLayout.setHorizontalGroup(
            formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(formularioLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(datosComunes, 0, 700, Short.MAX_VALUE)
                .addGroup(formularioLayout.createSequentialGroup()
                    .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(etiquetaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tipo, 0, 200, Short.MAX_VALUE))
                    .addGap(12)
                    .addComponent(datosTipo, 0, 420, Short.MAX_VALUE)))
            .addGap(8))
        );
        formularioLayout.setVerticalGroup(
            formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(formularioLayout.createSequentialGroup()
            .addGap(4)
            .addComponent(datosComunes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(8)
            .addGroup(formularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(formularioLayout.createSequentialGroup()
                    .addGap(8)
                    .addComponent(etiquetaTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8)
                    .addComponent(tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(datosTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(4))
        );
        lista.setViewportView(tabla);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(16)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(etiquetaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10)
                    .addComponent(clientes, 0, 400, Short.MAX_VALUE)
                    .addGap(10)
                    .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(formulario, 0, 720, Short.MAX_VALUE)
                .addComponent(lista, 0, 720, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 12, Short.MAX_VALUE)
                    .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addGap(14)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(etiquetaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(clientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(8)
            .addComponent(formulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(8)
            .addComponent(lista, 0, 260, Short.MAX_VALUE)
            .addGap(8)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientesActionPerformed
        if (!cambiandoClientes) {
            limpiarFormulario();
            cargarVehiculos();
        }
    }//GEN-LAST:event_clientesActionPerformed

    private void actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarActionPerformed
        cargarClientes();
    }//GEN-LAST:event_actualizarActionPerformed

    private void tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoActionPerformed
        mostrarTipo();
    }//GEN-LAST:event_tipoActionPerformed

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        guardarVehiculo();
    }//GEN-LAST:event_guardarActionPerformed

    private void configurarClienteYCombustible() {
        combustible.setModel(new DefaultComboBoxModel<>(TipoCombustible.values()));
        DefaultListCellRenderer renderizador = new DefaultListCellRenderer();
        clientes.setRenderer((lista, cliente, indice, seleccionado, foco) ->
                renderizador.getListCellRendererComponent(lista, cliente == null ? "" :
                        cliente.getApellido() + ", " + cliente.getNombre() + " (" + cliente.getDni() + ")",
                        indice, seleccionado, foco));
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar;
    private javax.swing.JTextField anio;
    private javax.swing.JPanel camposAuto;
    private javax.swing.JPanel camposMoto;
    private javax.swing.JTextField cilindrada;
    private javax.swing.JComboBox<Cliente> clientes;
    private javax.swing.JComboBox<TipoCombustible> combustible;
    private javax.swing.JPanel datosComunes;
    private javax.swing.JPanel datosTipo;
    private javax.swing.JLabel estado;
    private javax.swing.JLabel etiquetaAnio;
    private javax.swing.JLabel etiquetaCilindrada;
    private javax.swing.JLabel etiquetaClientes;
    private javax.swing.JLabel etiquetaCombustible;
    private javax.swing.JLabel etiquetaMarca;
    private javax.swing.JLabel etiquetaModelo;
    private javax.swing.JLabel etiquetaPatente;
    private javax.swing.JLabel etiquetaPuertas;
    private javax.swing.JLabel etiquetaTipo;
    private javax.swing.JPanel formulario;
    private javax.swing.JButton guardar;
    private javax.swing.JScrollPane lista;
    private javax.swing.JTextField marca;
    private javax.swing.JTextField modelo;
    private javax.swing.JTextField patente;
    private javax.swing.JTextField puertas;
    private javax.swing.JTable tabla;
    private javax.swing.JComboBox<String> tipo;
    // End of variables declaration//GEN-END:variables
}
