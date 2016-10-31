import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
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
    public GreetingsWindow() {
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        toFront();
        configBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConfigure();
            }
        });
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void onConfigure(){
        InitialConfiguration.run();
    }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Mark Hedrick
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        scrollPane1 = new JScrollPane();
        textPane1 = new JTextPane();
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

            // JFormDesigner evaluation mark
            dialogPane.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), dialogPane.getBorder())); dialogPane.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- label1 ----
                label1.setText("Welcome to MeshFS");
                label1.setFont(new Font("Helvetica Neue", label1.getFont().getStyle(), label1.getFont().getSize() + 5));

                //======== scrollPane1 ========
                {

                    //---- textPane1 ----
                    textPane1.setText("Before you can begin using MeshFS, you must set it up to run in either Client or Server mode. If you plan on using MeshFS in Client mode, make sure you know the Server address, and authentication details. If you plan to run MeshFS in Server mode, please refer to our Help guide for more guidance. Thank you for using MeshFS");
                    textPane1.setEditable(false);
                    textPane1.setFont(new Font("Helvetica Neue", textPane1.getFont().getStyle() & ~Font.BOLD, textPane1.getFont().getSize()));
                    scrollPane1.setViewportView(textPane1);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(96, 96, 96)
                            .addComponent(label1)
                            .addContainerGap(105, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(label1)
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
                quitButton.setText("Quit");
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
    // Generated using JFormDesigner Evaluation license - Mark Hedrick
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JScrollPane scrollPane1;
    private JTextPane textPane1;
    private JPanel buttonBar;
    private JButton configBtn;
    private JButton quitButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void run() {
        JFrame greetingsWindow = new GreetingsWindow();
        CenterWindow.centerOnScreen(greetingsWindow);
        greetingsWindow.setVisible(true);
    }
}
