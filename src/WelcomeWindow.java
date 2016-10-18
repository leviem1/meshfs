/**
 * Created by muniz on 10/11/16.
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WelcomeWindow extends JFrame {

    private JPanel contentPane;
    private JButton configureBtn;

    public WelcomeWindow() {
        setContentPane(contentPane);
        setTitle("MeshFS - Welcome");
        setSize(300,200);
        setResizable(false);
        toFront();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configureBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfigure();
            }
        });
    }

    public static void run() {
        JFrame window = new WelcomeWindow();
        CenterWindow.centerOnScreen(window);
        window.setVisible(true);
    }

    private void onConfigure() {
        JFrame ConfigurationWindow = new ConfigurationWindow();
        CenterWindow.centerOnScreen(ConfigurationWindow);
        ConfigurationWindow.setVisible(true);
        dispose();
    }


}