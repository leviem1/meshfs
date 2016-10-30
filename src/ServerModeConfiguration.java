import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;

import static java.lang.String.valueOf;
/*
 * Created by JFormDesigner on Sun Oct 30 15:58:43 MDT 2016
 */



/**
 * Created by Mark Hedrick on 10/30/16.
 */
public class ServerModeConfiguration extends JFrame {
    public ServerModeConfiguration() {
        initComponents();
        serverAddress.setText(Reporting.getIpAddress());
        freeSpace.setText("(Free Space: " + valueOf(Reporting.getSystemStorage()/1073741824) + " GB)");
        browseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int rVal = fileChooser.showOpenDialog(null);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    repoPathTextField.setText(fileChooser.getSelectedFile().toString());
                    repoPathTextField.setToolTipText(fileChooser.getSelectedFile().toString());
                }
            }
        });
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Mark Hedrick
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        serverAddress = new JTextField();
        label2 = new JLabel();
        serverPort = new JFormattedTextField(NumberFormat.getInstance());
        label3 = new JLabel();
        label4 = new JLabel();
        numStripes = new JFormattedTextField(NumberFormat.getInstance());
        label5 = new JLabel();
        numStripeCopies = new JFormattedTextField(NumberFormat.getInstance());
        label6 = new JLabel();
        numWhole = new JFormattedTextField(NumberFormat.getInstance());
        label7 = new JLabel();
        minSpace = new JFormattedTextField(NumberFormat.getInstance());
        label8 = new JLabel();
        repoPathTextField = new JTextField();
        browseBtn = new JButton();
        freeSpace = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("MeshFS - Server Configuration");
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
                label1.setText("Server IP Address:");

                //---- label2 ----
                label2.setText("Server Port:");

                //---- serverPort ----
                serverPort.setText("5704");

                //---- label3 ----
                label3.setText("Number of:");

                //---- label4 ----
                label4.setText("Stripes:");

                //---- numStripes ----
                numStripes.setText("3");

                //---- label5 ----
                label5.setText("Striped Copies:");

                //---- numStripeCopies ----
                numStripeCopies.setText("2");

                //---- label6 ----
                label6.setText("Whole Copies:");

                //---- numWhole ----
                numWhole.setText("2");

                //---- label7 ----
                label7.setText("Minimum Free Space:");

                //---- minSpace ----
                minSpace.setText("0");

                //---- label8 ----
                label8.setText("Repository:");

                //---- browseBtn ----
                browseBtn.setText("Browse");

                //---- freeSpace ----
                freeSpace.setText("(free space)");

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(label8)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(repoPathTextField, GroupLayout.PREFERRED_SIZE, 329, GroupLayout.PREFERRED_SIZE)
                                    .addGap(12, 12, 12)
                                    .addComponent(browseBtn))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(label1)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(serverAddress, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(label3)
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(label2)
                                            .addGap(12, 12, 12)
                                            .addGroup(contentPanelLayout.createParallelGroup()
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                    .addComponent(label4)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(numStripes))
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                    .addComponent(label5)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(numStripeCopies))
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                    .addComponent(serverPort, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                    .addComponent(label6)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(numWhole))))
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(label7)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(minSpace)))
                                    .addGap(18, 18, 18)
                                    .addComponent(freeSpace, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label1)
                                .addComponent(serverAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(serverPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label2))
                            .addGap(18, 18, 18)
                            .addComponent(label3)
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label4)
                                .addComponent(numStripes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(numStripeCopies, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label5))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(numWhole, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label6))
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(minSpace, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label7)
                                .addComponent(freeSpace))
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label8)
                                .addComponent(browseBtn)
                                .addComponent(repoPathTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(26, Short.MAX_VALUE))
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
    private JTextField serverAddress;
    private JLabel label2;
    private JFormattedTextField serverPort;
    private JLabel label3;
    private JLabel label4;
    private JFormattedTextField numStripes;
    private JLabel label5;
    private JFormattedTextField numStripeCopies;
    private JLabel label6;
    private JFormattedTextField numWhole;
    private JLabel label7;
    private JFormattedTextField minSpace;
    private JLabel label8;
    private JTextField repoPathTextField;
    private JButton browseBtn;
    private JLabel freeSpace;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void run() {
        JFrame serverModeConfiguration = new ServerModeConfiguration();
        CenterWindow.centerOnScreen(serverModeConfiguration);
        serverModeConfiguration.setVisible(true);
    }

    public void onOk(){
        dispose();
    }
}
