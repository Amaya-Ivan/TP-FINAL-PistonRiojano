package vista;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Aplicacion {
    private Aplicacion() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            usarNimbus();
            new VentanaPrincipal().setVisible(true);
        });
    }

    private static void usarNimbus() {
        for (UIManager.LookAndFeelInfo estilo : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(estilo.getName())) {
                try {
                    UIManager.setLookAndFeel(estilo.getClassName());
                } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
                    // Swing conserva su apariencia predeterminada.
                }
                return;
            }
        }
    }
}
