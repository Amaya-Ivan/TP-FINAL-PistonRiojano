package vista;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

final class TareaSwing {
    private TareaSwing() {
    }

    static <T> void ejecutar(JInternalFrame ventana, Callable<T> tarea, Consumer<T> alCompletar,
                             Runnable habilitarControles, String mensajeDuplicado) {
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return tarea.call();
            }

            @Override
            protected void done() {
                if (ventana.isClosed()) {
                    return;
                }
                habilitarControles.run();
                try {
                    alCompletar.accept(get());
                } catch (InterruptedException ex) {
                    mostrarError(ventana, "Se interrumpió la operación.");
                } catch (ExecutionException ex) {
                    Throwable causa = ex.getCause();
                    String mensaje = causa.getMessage();
                    if (mensajeDuplicado != null && causa instanceof SQLException sql
                            && "23505".equals(sql.getSQLState())) {
                        mensaje = mensajeDuplicado;
                    }
                    mostrarError(ventana, mensaje == null ? "No se pudo completar la operación." : mensaje);
                }
            }
        }.execute();
    }

    private static void mostrarError(JInternalFrame ventana, String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, ventana.getTitle(), JOptionPane.ERROR_MESSAGE);
    }
}
