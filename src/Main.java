import javax.swing.SwingUtilities;

/**
 * Ponto de entrada do sistema BibliotecaManager.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal());
    }
}
