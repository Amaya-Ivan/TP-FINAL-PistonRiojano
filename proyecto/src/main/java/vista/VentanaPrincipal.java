package vista;

import dao.ClienteDao;
import dao.MecanicoDao;
import dao.OrdenTrabajoDao;
import dao.RepuestoDao;
import dao.VehiculoDao;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public final class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    private ClientesFrame ventanaClientes;
    private VehiculosFrame ventanaVehiculos;
    private RepuestosFrame ventanaRepuestos;
    private NuevaOrdenFrame ventanaNuevaOrden;
    private ConsultaOrdenesFrame ventanaConsultaOrdenes;

    public VentanaPrincipal() {
        initComponents();
        setMinimumSize(new Dimension(680, 480));
        setSize(1050, 690);
        setLocationRelativeTo(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        escritorio = new javax.swing.JDesktopPane();
        barraMenu = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        opcionSalir = new javax.swing.JMenuItem();
        menuClientes = new javax.swing.JMenu();
        opcionClientes = new javax.swing.JMenuItem();
        menuVehiculos = new javax.swing.JMenu();
        opcionVehiculos = new javax.swing.JMenuItem();
        menuRepuestos = new javax.swing.JMenu();
        opcionRepuestos = new javax.swing.JMenuItem();
        menuOrdenes = new javax.swing.JMenu();
        opcionNuevaOrden = new javax.swing.JMenuItem();
        opcionConsultarOrdenes = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pistón Riojano");

        escritorio.setBackground(new java.awt.Color(226, 231, 237));
        escritorio.setLayout(null);

        menuArchivo.setText("Archivo");

        opcionSalir.setText("Salir");
        opcionSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionSalirActionPerformed(evt);
            }
        });
        menuArchivo.add(opcionSalir);

        barraMenu.add(menuArchivo);

        menuClientes.setText("Clientes");

        opcionClientes.setText("Administrar clientes");
        opcionClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionClientesActionPerformed(evt);
            }
        });
        menuClientes.add(opcionClientes);

        barraMenu.add(menuClientes);

        menuVehiculos.setText("Vehículos");

        opcionVehiculos.setText("Registrar y listar vehículos");
        opcionVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionVehiculosActionPerformed(evt);
            }
        });
        menuVehiculos.add(opcionVehiculos);

        barraMenu.add(menuVehiculos);

        menuRepuestos.setText("Repuestos");

        opcionRepuestos.setText("Consultar y actualizar repuestos");
        opcionRepuestos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionRepuestosActionPerformed(evt);
            }
        });
        menuRepuestos.add(opcionRepuestos);

        barraMenu.add(menuRepuestos);

        menuOrdenes.setText("Órdenes");

        opcionNuevaOrden.setText("Nueva orden de trabajo");
        opcionNuevaOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionNuevaOrdenActionPerformed(evt);
            }
        });
        menuOrdenes.add(opcionNuevaOrden);

        opcionConsultarOrdenes.setText("Consultar órdenes");
        opcionConsultarOrdenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opcionConsultarOrdenesActionPerformed(evt);
            }
        });
        menuOrdenes.add(opcionConsultarOrdenes);

        barraMenu.add(menuOrdenes);

        setJMenuBar(barraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void opcionSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionSalirActionPerformed
        dispose();
    }//GEN-LAST:event_opcionSalirActionPerformed

    private void opcionClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionClientesActionPerformed
        abrirClientes();
    }//GEN-LAST:event_opcionClientesActionPerformed

    private void opcionVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionVehiculosActionPerformed
        abrirVehiculos();
    }//GEN-LAST:event_opcionVehiculosActionPerformed

    private void opcionRepuestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionRepuestosActionPerformed
        abrirRepuestos();
    }//GEN-LAST:event_opcionRepuestosActionPerformed

    private void opcionNuevaOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionNuevaOrdenActionPerformed
        abrirNuevaOrden();
    }//GEN-LAST:event_opcionNuevaOrdenActionPerformed

    private void opcionConsultarOrdenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opcionConsultarOrdenesActionPerformed
        abrirConsultaOrdenes();
    }//GEN-LAST:event_opcionConsultarOrdenesActionPerformed

    private void abrirClientes() {
        if (ventanaClientes == null || ventanaClientes.isClosed()) {
            ventanaClientes = new ClientesFrame(new ClienteDao());
        }
        mostrar(ventanaClientes, 900, 580);
    }

    private void abrirVehiculos() {
        if (ventanaVehiculos == null || ventanaVehiculos.isClosed()) {
            ventanaVehiculos = new VehiculosFrame(new ClienteDao(), new VehiculoDao());
        }
        mostrar(ventanaVehiculos, 900, 580);
    }

    private void abrirRepuestos() {
        if (ventanaRepuestos == null || ventanaRepuestos.isClosed()) {
            ventanaRepuestos = new RepuestosFrame(new RepuestoDao());
        }
        mostrar(ventanaRepuestos, 850, 540);
    }

    private void abrirNuevaOrden() {
        if (ventanaNuevaOrden == null || ventanaNuevaOrden.isClosed()) {
            ventanaNuevaOrden = new NuevaOrdenFrame(
                    new VehiculoDao(), new MecanicoDao(), new RepuestoDao(), new OrdenTrabajoDao());
        }
        mostrar(ventanaNuevaOrden, 970, 600);
    }

    private void abrirConsultaOrdenes() {
        if (ventanaConsultaOrdenes == null || ventanaConsultaOrdenes.isClosed()) {
            ventanaConsultaOrdenes = new ConsultaOrdenesFrame(new OrdenTrabajoDao());
        } else {
            ventanaConsultaOrdenes.recargar();
        }
        mostrar(ventanaConsultaOrdenes, 900, 540);
    }

    private void mostrar(JInternalFrame ventana, int ancho, int alto) {
        if (ventana.getDesktopPane() == null) {
            escritorio.add(ventana);
            Dimension disponible = escritorio.getSize();
            ventana.setSize(Math.min(ancho, disponible.width), Math.min(alto, disponible.height));
            ventana.setLocation(
                    (disponible.width - ventana.getWidth()) / 2,
                    (disponible.height - ventana.getHeight()) / 2);
            ventana.setVisible(true);
        }
        try {
            ventana.setIcon(false);
            ventana.moveToFront();
            ventana.setSelected(true);
        } catch (PropertyVetoException ex) {
            // Swing puede vetar un cambio de selección o el estado de minimizado.
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JDesktopPane escritorio;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenu menuClientes;
    private javax.swing.JMenu menuOrdenes;
    private javax.swing.JMenu menuRepuestos;
    private javax.swing.JMenu menuVehiculos;
    private javax.swing.JMenuItem opcionClientes;
    private javax.swing.JMenuItem opcionConsultarOrdenes;
    private javax.swing.JMenuItem opcionNuevaOrden;
    private javax.swing.JMenuItem opcionRepuestos;
    private javax.swing.JMenuItem opcionSalir;
    private javax.swing.JMenuItem opcionVehiculos;
    // End of variables declaration//GEN-END:variables
}
