/**
 * Created by muniz on 10/11/16.
 */

import javax.swing.*;


public class WelcomeWindow extends JFrame {

    private JPanel contentPane;
    private JButton configure;

    public WelcomeWindow() {
        setContentPane(contentPane);
        setTitle("This is a test title");
        setSize(300,200);
        setResizable(false);
        toFront();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void run() {
        JFrame window = new WelcomeWindow();
        CenterWindow.centerOnScreen(window);
        window.setVisible(true);
    }
}