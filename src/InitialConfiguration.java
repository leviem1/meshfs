import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Mark Hedrick
 */
class InitialConfiguration extends JFrame {

    private static JFrame initialConfiguration;

    private InitialConfiguration() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if(Reporting.getSystemOS().contains("Windows")){
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        initComponents();
        frameListeners();


        modeSelectionBox.addItem("none");

        modeSelectionBox.addItem("Client Mode");

        modeSelectionBox.addItem("Server Mode");

        okButton.setEnabled(false);
        modeSelectionBox.requestFocus();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        JPanel dialogPane = new JPanel();
        contentPanel = new JPanel();
        JLabel titleLbl = new JLabel();
        JLabel modeLbl = new JLabel();
        modeSelectionBox = new JComboBox();
        JPanel buttonBar = new JPanel();
        backBtn = new JButton();
        okButton = new JButton();

        //======== this ========
        setTitle("MeshFS - Initial Configuration");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

            //======== contentPanel ========
            {

                //---- titleLbl ----
                titleLbl.setText("Mode");
                titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
                titleLbl.setHorizontalAlignment(SwingConstants.CENTER);

                //---- modeLbl ----
                modeLbl.setText("Please select the desired mode of operation:");
                modeLbl.setFont(new Font("Arial", modeLbl.getFont().getStyle(), modeLbl.getFont().getSize() + 1));
                modeLbl.setHorizontalAlignment(SwingConstants.CENTER);

                //---- modeSelectionBox ----
                modeSelectionBox.setFont(new Font("Arial", modeSelectionBox.getFont().getStyle(), modeSelectionBox.getFont().getSize() + 1));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addComponent(titleLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGap(84, 84, 84)
                            .addComponent(modeSelectionBox, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(modeLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())
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

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] {0, 0, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0};

                //---- backBtn ----
                backBtn.setText("Back");
                backBtn.setFont(new Font("Arial", backBtn.getFont().getStyle(), backBtn.getFont().getSize() + 1));
                buttonBar.add(backBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }

            GroupLayout dialogPaneLayout = new GroupLayout(dialogPane);
            dialogPane.setLayout(dialogPaneLayout);
            dialogPaneLayout.setHorizontalGroup(
                dialogPaneLayout.createParallelGroup()
                    .addGroup(GroupLayout.Alignment.TRAILING, dialogPaneLayout.createSequentialGroup()
                        .addGroup(dialogPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addGroup(GroupLayout.Alignment.LEADING, dialogPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(buttonBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(contentPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10))
            );
            dialogPaneLayout.setVerticalGroup(
                dialogPaneLayout.createParallelGroup()
                    .addGroup(dialogPaneLayout.createSequentialGroup()
                        .addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonBar, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
            );
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
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
        backBtn.addActionListener(e -> {
            dispose();
            GreetingsWindow.run(false, initialConfiguration);
        });
    }

    private void onOk() {
        if(modeSelectionBox.getSelectedItem().toString().equals("Server Mode")){
            ServerModeConfiguration.run(initialConfiguration);
            dispose();
        }
        else if(modeSelectionBox.getSelectedItem().toString().equals("Client Mode")){
            ClientModeConfiguration.run(initialConfiguration, "", true);
            dispose();
        }
    }

    public static void run(JFrame sender) {
        initialConfiguration = new InitialConfiguration();
        CenterWindow.centerOnWindow(sender, initialConfiguration);
        initialConfiguration.setVisible(true);
    }

    private JPanel contentPanel;
    private JComboBox modeSelectionBox;
    private JButton backBtn;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
