/**
 * Created by muniz on 10/11/16.
 */

import javax.swing.*;


public class WelcomeWindow extends JFrame {

    private JPanel contentPane;

    public WelcomeWindow() {
        setContentPane(contentPane);
        setTitle("This is a test title");
        setSize(300,200);
    }

    public static void run() {
        JFrame window = new WelcomeWindow();
        CenterWindow.centerOnScreen(window);
        window.setVisible(true);
    }
}