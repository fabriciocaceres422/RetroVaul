package es.izquierdofp.retrovault;

import com.formdev.flatlaf.FlatLightLaf;
import es.izquierdofp.retrovault.datos.DatabaseManager;
import es.izquierdofp.retrovault.vista.VentanaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        aplicarTema();
        DatabaseManager.inicializar();
        SwingUtilities.invokeLater(Main::mostrarVentana);
    }


    private static void aplicarTema() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Tema  no disponible: " + ex.getMessage());
        }
    }

    private static void mostrarVentana() {
        new VentanaPrincipal().setVisible(true);
    }
}
