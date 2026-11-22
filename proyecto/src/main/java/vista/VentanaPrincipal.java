package vista;

import dao.ClienteDao;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public final class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    private final JDesktopPane escritorio = new JDesktopPane();
    private ClientesFrame ventanaClientes;

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
        JMenu archivo = new JMenu("Archivo");
        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(evento -> dispose());
        archivo.add(salir);
        barra.add(archivo);

        JMenu clientes = new JMenu("Clientes");
        JMenuItem administrar = new JMenuItem("Administrar clientes");
        administrar.addActionListener(evento -> abrirClientes());
        clientes.add(administrar);
        barra.add(clientes);
        return barra;
    }

    private void abrirClientes() {
        if (ventanaClientes == null || ventanaClientes.isClosed()) {
            ventanaClientes = new ClientesFrame(new ClienteDao());
            escritorio.add(ventanaClientes);
            Dimension disponible = escritorio.getSize();
            ventanaClientes.setSize(Math.min(900, disponible.width), Math.min(580, disponible.height));
            ventanaClientes.setLocation(
                    (disponible.width - ventanaClientes.getWidth()) / 2,
                    (disponible.height - ventanaClientes.getHeight()) / 2);
            ventanaClientes.setVisible(true);
        }
        try {
            ventanaClientes.setIcon(false);
            ventanaClientes.moveToFront();
            ventanaClientes.setSelected(true);
        } catch (PropertyVetoException ex) {
            // Swing puede vetar un cambio de selección o el estado de minimizado.
        }
    }
}
