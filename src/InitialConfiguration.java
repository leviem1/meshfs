import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Sun Oct 30 14:13:25 MDT 2016
 */



/**
 * Created by Mark Hedrick on 10/30/16.
 */
public class InitialConfiguration extends JFrame {
    public InitialConfiguration() {
        initComponents();
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });
        ButtonGroup modeRadios = new ButtonGroup();
        modeRadios.add(serverModeBtn);
        modeRadios.add(clientModeBtn);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Mark Hedrick
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        serverModeBtn = new JRadioButton();
        clientModeBtn = new JRadioButton();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("MeshFS - Initial Configuration");
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
                label1.setText("Please select the correct mode of operation:");

                //---- serverModeBtn ----
                serverModeBtn.setText("Server");

                //---- clientModeBtn ----
                clientModeBtn.setText("Client");

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(label1)
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(serverModeBtn)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(clientModeBtn)))
                            .addContainerGap(27, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(label1)
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(serverModeBtn)
                                .addComponent(clientModeBtn))
                            .addContainerGap(18, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
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
    private JRadioButton serverModeBtn;
    private JRadioButton clientModeBtn;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    public static void run() {
        JFrame initialConfiguration = new InitialConfiguration();
        CenterWindow.centerOnScreen(initialConfiguration);
        initialConfiguration.setVisible(true);
    }

    public void onOk() {
        if(clientModeBtn.isSelected()){
            ClientModeConfiguration.run();
        }
        else if(serverModeBtn.isSelected()){
            ServerModeConfiguration.run();
        }
        dispose();
    }
}
