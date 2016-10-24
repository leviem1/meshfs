import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mark Hedrick on 10/15/16.
 */
public class ConfigurationWindow extends JFrame {

    private JPanel contentPane;
    private JRadioButton clientBtn;
    private JRadioButton serverBtn;
    private JLabel radioID;

    public ConfigurationWindow() {
        setContentPane(contentPane);
        setTitle("MeshFS - Initial Configuration");
        setSize(300,200);
        setResizable(false);
        toFront();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ButtonGroup mode = new ButtonGroup();
        mode.add(clientBtn);
        mode.add(serverBtn);

        clientBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClient();
            }
        });
        serverBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onServer();
            }
        });
    }

    public static void run() {
        JFrame window = new ConfigurationWindow();
        CenterWindow.centerOnScreen(window);
        window.setVisible(true);
    }

    public void onClient(){
        radioID.setText("client");
    }

    public void onServer(){
        radioID.setText("server");
    }
}
