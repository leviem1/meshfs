import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/*
 * Created by JFormDesigner on Sun Oct 30 15:17:23 MDT 2016
 */



/**
 * Created by Mark Hedrick on 10/30/16.
 */
public class ClientModeConfiguration extends JFrame{
    public ClientModeConfiguration() {
        initComponents();
        frameListeners();
        okButton.setEnabled(false);
        bindAnonymouslyCheckBox.setSelected(true);
        usernameField.setEnabled(false);
        passwordField.setEnabled(false);
        setResizable(false);

    }

    private void initComponents() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        serverAddressLbl = new JLabel();
        serverPortLbl = new JLabel();
        usernameLbl = new JLabel();
        passwordLbl = new JLabel();
        bindAnonLbl = new JLabel();
        bindAnonymouslyCheckBox = new JCheckBox();
        serverAddressField = new JTextField();
        serverPortField = new JFormattedTextField(numberFormat);
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        buttonBar = new JPanel();
        okButton = new JButton();
        titleLbl = new JLabel();

        //======== this ========
        setTitle("MeshFS - Client Configuration");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- serverAddressLbl ----
                serverAddressLbl.setText("Server Address:");
                serverAddressLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                //---- serverPortLbl ----
                serverPortLbl.setText("Server Port:");
                serverPortLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                //---- usernameLbl ----
                usernameLbl.setText("Username:");
                usernameLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                //---- passwordLbl ----
                passwordLbl.setText("Password:");
                passwordLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                //---- bindAnonLbl ----
                bindAnonLbl.setText("Bind Anonymously:");
                bindAnonLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                //---- serverAddressField ----
                serverAddressField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                //---- serverPortField ----
                serverPortField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
                serverPortField.setText("5704");

                //---- usernameField ----
                usernameField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                //---- passwordField ----
                passwordField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(bindAnonLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(bindAnonymouslyCheckBox)
                                    .addGap(0, 229, Short.MAX_VALUE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGroup(contentPanelLayout.createParallelGroup()
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(serverAddressLbl)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(serverAddressField, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(serverPortLbl)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(serverPortField, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(contentPanelLayout.createSequentialGroup()
                                                .addComponent(passwordLbl)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(passwordField))
                                            .addGroup(contentPanelLayout.createSequentialGroup()
                                                .addComponent(usernameLbl)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))))
                                    .addContainerGap(64, Short.MAX_VALUE))))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(serverAddressLbl)
                                .addComponent(serverAddressField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(14, 14, 14)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(serverPortLbl)
                                .addComponent(serverPortField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(bindAnonLbl)
                                .addComponent(bindAnonymouslyCheckBox))
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(usernameLbl)
                                .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordLbl)
                                .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                okButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- titleLbl ----
            titleLbl.setText("Client Connection Settings");
            titleLbl.setFont(new Font("Helvetica Neue", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
            titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPane.add(titleLbl, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners(){
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });
        bindAnonymouslyCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(bindAnonymouslyCheckBox.isSelected()){
                    bindAnonymously("yes");
                }
                else{
                    bindAnonymously("no");
                }
            }
        });
        serverAddressField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            public void changed() {
                if (!(checkFields(serverAddressField))) {
                    okButton.setEnabled(false);
                }else {
                    okButton.setEnabled(true);
                    buttonBar.getRootPane().setDefaultButton(okButton);
                }

            }
        });
        serverPortField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            public void changed() {
                if (serverPortField.getText().isEmpty() == true){
                    okButton.setEnabled(false);
                }else {
                    okButton.setEnabled(true);
                }
            }
        });
        passwordField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            public void changed() {
                if (passwordField.getPassword().length == 0){
                    okButton.setEnabled(false);
                }else {
                    okButton.setEnabled(true);
                }
            }
        });
        usernameField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            public void removeUpdate(DocumentEvent e) {
                changed();
            }
            public void insertUpdate(DocumentEvent e) {
                changed();
            }
            public void changed() {
                if (!(checkFields(usernameField))) {
                    okButton.setEnabled(false);
                }else {
                    okButton.setEnabled(true);
                }

            }
        });
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel serverAddressLbl;
    private JLabel serverPortLbl;
    private JLabel usernameLbl;
    private JLabel passwordLbl;
    private JLabel bindAnonLbl;
    private JCheckBox bindAnonymouslyCheckBox;
    private JTextField serverAddressField;
    private JFormattedTextField serverPortField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel buttonBar;
    private JButton okButton;
    private JLabel titleLbl;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void onOk(){
        if(!(FileClient.ping(serverAddressField.getText(), Integer.parseInt(serverPortField.getText())))){
            JOptionPane.showMessageDialog(null, "Server Offline!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
            serverAddressField.setText("");
            serverPortField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            bindAnonymouslyCheckBox.setSelected(false);
        }
        try{
            //FileClient.receiveFile(serverAddressField.getText(), Integer.parseInt(serverPortField.getText()));
            FileClient.receiveFile(serverAddressField.getText(), Integer.parseInt(serverPortField.getText()), "catalog.json", ".catalog.json");
        }catch(IOException ioe){
            System.out.println("error");
        }
        ClientBrowser.run(serverAddressField.getText(), Integer.parseInt(serverPortField.getText()));
        dispose();
    }

    public void bindAnonymously(String mode){
        if(mode.equals("yes")){
            usernameField.setEnabled(false);
            usernameField.setText("");
            passwordField.setEnabled(false);
            passwordField.setText("");
        }
        else if(mode.equals("no")){
            usernameField.setEnabled(true);
            passwordField.setEnabled(true);
        }

    }

    public boolean checkFields(JTextField field) {
        if (field.getText().isEmpty() == true){
            return false;
        }else{
            return true;
        }
    }

    public static void run(JFrame sender) {
        JFrame clientModeConfiguration = new ClientModeConfiguration();
        CenterWindow.centerOnWindow(sender, clientModeConfiguration);
        clientModeConfiguration.setVisible(true);
    }
}