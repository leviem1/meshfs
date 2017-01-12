import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Sun Oct 30 13:52:07 MDT 2016
 */



/**
 * Created by Mark Hedrick on 10/30/16.
 */
public class GreetingsWindow extends JFrame {

    private static JFrame greetingsWindow;

    public GreetingsWindow() {
        initComponents();
        frameListeners();
        dialogPane.getRootPane().setDefaultButton(configBtn);
        configBtn.requestFocus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        toFront();
    }

    private void frameListeners(){
        configBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfigure();
            }
        });
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    private void onConfigure(){
        InitialConfiguration.run(greetingsWindow);
    }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        titleLbl = new JLabel();
        scrollPane1 = new JScrollPane();
        greetingsPane = new JTextPane();
        buttonBar = new JPanel();
        configBtn = new JButton();
        quitButton = new JButton();

        //======== this ========
        setTitle("MeshFS - Greetings");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- titleLbl ----
                titleLbl.setText("Welcome to MeshFS");
                titleLbl.setFont(new Font("Helvetica Neue", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));

                //======== scrollPane1 ========
                {

                    //---- greetingsPane ----
                    greetingsPane.setText("Before using MeshFS, you will need to configure it to run in either Client or Server mode. If you plan on using MeshFS in Client mode, make sure you know the address of the server that you wish to connect to, as well as any authentication credentials (if required). If you plan on running MeshFS in Server mode, please refer to our Help guide for detailed setup instructions.  Thank you for choosing MeshFS!");
                    greetingsPane.setEditable(false);
                    greetingsPane.setFont(new Font("Helvetica Neue", greetingsPane.getFont().getStyle() & ~Font.BOLD, greetingsPane.getFont().getSize()));
                    scrollPane1.setViewportView(greetingsPane);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(96, 96, 96)
                            .addComponent(titleLbl)
                            .addContainerGap(109, Short.MAX_VALUE))
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(titleLbl)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                            .addContainerGap())
                );
            }
            dialogPane.add(contentPanel, BorderLayout.NORTH);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());

                //---- configBtn ----
                configBtn.setText("Configuration...");
                configBtn.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
                buttonBar.add(configBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- quitButton ----
                quitButton.setText("Exit");
                quitButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
                buttonBar.add(quitButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel titleLbl;
    private JScrollPane scrollPane1;
    private JTextPane greetingsPane;
    private JPanel buttonBar;
    private JButton configBtn;
    private JButton quitButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void run() {
        greetingsWindow = new GreetingsWindow();
        CenterWindow.centerOnScreen(greetingsWindow);
        greetingsWindow.setVisible(true);

    }
}
