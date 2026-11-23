package vista;

import dao.ClienteDao;
import dao.RepuestoDao;
import dao.VehiculoDao;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public final class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    private final JDesktopPane escritorio = new JDesktopPane();
    private ClientesFrame ventanaClientes;
    private VehiculosFrame ventanaVehiculos;
    private RepuestosFrame ventanaRepuestos;

    public VentanaPrincipal() {
        super("Pistón Riojano");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(680, 480));
        setSize(1050, 690);
        setLocationRelativeTo(null);
        escritorio.setBackground(new Color(226, 231, 237));
        setContentPane(escritorio);
        setJMenuBar(crearMenu());
    }

    private JMenuBar crearMenu() {
        JMenuBar barra = new JMenuBar();
        agregarMenu(barra, "Archivo", "Salir", this::dispose);
        agregarMenu(barra, "Clientes", "Administrar clientes", this::abrirClientes);
        agregarMenu(barra, "Vehículos", "Registrar y listar vehículos", this::abrirVehiculos);
        agregarMenu(barra, "Repuestos", "Consultar y actualizar repuestos", this::abrirRepuestos);
        return barra;
    }

    private void agregarMenu(JMenuBar barra, String titulo, String texto, Runnable accion) {
        JMenu menu = new JMenu(titulo);
        JMenuItem opcion = new JMenuItem(texto);
        opcion.addActionListener(evento -> accion.run());
        menu.add(opcion);
        barra.add(menu);
    }

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
}
