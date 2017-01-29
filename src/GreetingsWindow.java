import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private boolean runType;

    private GreetingsWindow(boolean runType) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        toFront();

        if(Reporting.getSystemOS().contains("Windows")){
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        this.runType = runType;

        initComponents();
        frameListeners();

        dialogPane.getRootPane().setDefaultButton(configBtn);
        configBtn.requestFocus();
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
        quitButton = new JButton();
        configBtn = new JButton();

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
                titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 7));
                titleLbl.setHorizontalAlignment(SwingConstants.CENTER);

                //======== scrollPane1 ========
                {

                    //---- greetingsPane ----
                    greetingsPane.setText("Before using MeshFS, you will need to configure it to run in either Client or Server mode. If you plan on using MeshFS in Client mode, make sure you know the address of the server that you wish to connect to, as well as any authentication credentials (if required). If you plan on running MeshFS in Server mode, please refer to our Help guide for detailed setup instructions.  Thank you for choosing MeshFS!");
                    greetingsPane.setEditable(false);
                    greetingsPane.setFont(new Font("Arial", greetingsPane.getFont().getStyle(), greetingsPane.getFont().getSize() + 2));
                    scrollPane1.setViewportView(greetingsPane);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                                .addComponent(titleLbl, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
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

                //---- quitButton ----
                quitButton.setText("Quit");
                quitButton.setFont(new Font("Arial", quitButton.getFont().getStyle(), quitButton.getFont().getSize() + 1));
                buttonBar.add(quitButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- configBtn ----
                configBtn.setText("Configuration...");
                configBtn.setFont(new Font("Arial", configBtn.getFont().getStyle(), configBtn.getFont().getSize() + 1));
                buttonBar.add(configBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
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

    private void frameListeners(){
        configBtn.addActionListener(e -> {
            if(runType){
                ClientModeConfiguration.run(greetingsWindow, "");
                dispose();
            }
            else{
                onConfigure();
            }

        });
        quitButton.addActionListener(e -> {
            dispose();
            System.exit(0);
        });
    }

    private void onConfigure(){
        InitialConfiguration.run(greetingsWindow);
        dispose();
    }

    public static void run(boolean runType) {
        greetingsWindow = new GreetingsWindow(runType);
        CenterWindow.centerOnScreen(greetingsWindow);
        greetingsWindow.setVisible(true);
    }

    public static void run(boolean runType, JFrame sender) {
        greetingsWindow = new GreetingsWindow(runType);
        CenterWindow.centerOnWindow(sender, greetingsWindow);
        greetingsWindow.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel titleLbl;
    private JScrollPane scrollPane1;
    private JTextPane greetingsPane;
    private JPanel buttonBar;
    private JButton quitButton;
    private JButton configBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
