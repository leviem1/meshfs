import javax.swing.*;

/**
 * Created by markhedrick on 10/15/16.
 */
public class ConfigurationWindow extends JFrame {

    private JPanel contentPane;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;

    public ConfigurationWindow() {
        setContentPane(contentPane);
        setTitle("MeshFS - Initial Configuration");
        setSize(300,200);
        setResizable(false);
        toFront();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void run() {
        JFrame window = new ConfigurationWindow();
        CenterWindow.centerOnScreen(window);
        window.setVisible(true);
    }
}
