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

    private static JFrame initialConfiguration;

    private InitialConfiguration() {
        initComponents();
        modeSelectionBox.addItem("none");
        modeSelectionBox.addItem("Client Mode");
        modeSelectionBox.addItem("Server Mode");
        frameListeners();
        okButton.setEnabled(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        if(Reporting.getSystemOS().contains("Windows")){
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        titleLbl = new JLabel();
        modeLbl = new JLabel();
        modeSelectionBox = new JComboBox();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("MeshFS - Initial Configuration");
        Container contentPane = getContentPane();

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- titleLbl ----
                titleLbl.setText("Mode");
                titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
                titleLbl.setHorizontalAlignment(SwingConstants.CENTER);

                //---- modeLbl ----
                modeLbl.setText("Please select the correct mode of operation:");
                modeLbl.setFont(new Font("Arial", modeLbl.getFont().getStyle(), modeLbl.getFont().getSize() + 1));
                modeLbl.setHorizontalAlignment(SwingConstants.CENTER);

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addComponent(titleLbl, GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(modeLbl, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(84, 84, 84)
                            .addComponent(modeSelectionBox, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(88, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addComponent(titleLbl)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(modeLbl)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                            .addComponent(modeSelectionBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(dialogPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(dialogPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners(){
        okButton.addActionListener(e -> onOk());
        modeSelectionBox.addActionListener(e -> {
            if(modeSelectionBox.getSelectedItem().toString().equals("none")){
                okButton.setEnabled(false);
            }else{
                okButton.setEnabled(true);
            }
            contentPanel.getRootPane().setDefaultButton(okButton);
            contentPanel.requestFocus();
        });
    }

    private void onOk() {
        if(modeSelectionBox.getSelectedItem().toString().equals("Server Mode")){
            ServerModeConfiguration.run(initialConfiguration);
            dispose();
        }
        else if(modeSelectionBox.getSelectedItem().toString().equals("Client Mode")){
            ClientModeConfiguration.run(initialConfiguration, "");
            dispose();
        }
    }

    public static void run(JFrame sender) {
        initialConfiguration = new InitialConfiguration();
        CenterWindow.centerOnWindow(sender, initialConfiguration);
        initialConfiguration.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel titleLbl;
    private JLabel modeLbl;
    private JComboBox modeSelectionBox;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
