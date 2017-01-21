import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.NumberFormat;
import java.util.HashMap;
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

    private static JFrame clientModeConfiguration;
    private String usernameFinal = "";

    private ClientModeConfiguration() {
        initComponents();
        frameListeners();
        okButton.setEnabled(false);
        bindAnonymouslyCheckBox.setSelected(true);
        usernameField.setEnabled(false);
        passwordField.setEnabled(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        if(Reporting.getSystemOS().contains("Windows")){
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
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
                serverAddressLbl.setFont(new Font("Arial", serverAddressLbl.getFont().getStyle(), serverAddressLbl.getFont().getSize() + 1));

                //---- serverPortLbl ----
                serverPortLbl.setText("Server Port:");
                serverPortLbl.setFont(new Font("Arial", serverPortLbl.getFont().getStyle(), serverPortLbl.getFont().getSize() + 1));

                //---- usernameLbl ----
                usernameLbl.setText("Username:");
                usernameLbl.setFont(new Font("Arial", usernameLbl.getFont().getStyle(), usernameLbl.getFont().getSize() + 1));

                //---- passwordLbl ----
                passwordLbl.setText("Password:");
                passwordLbl.setFont(new Font("Arial", passwordLbl.getFont().getStyle(), passwordLbl.getFont().getSize() + 1));

                //---- bindAnonLbl ----
                bindAnonLbl.setText("Bind as Guest:");
                bindAnonLbl.setFont(new Font("Arial", bindAnonLbl.getFont().getStyle(), bindAnonLbl.getFont().getSize() + 1));

                //---- serverAddressField ----
                serverAddressField.setFont(new Font("Arial", serverAddressField.getFont().getStyle(), serverAddressField.getFont().getSize() + 1));

                //---- serverPortField ----
                serverPortField.setFont(new Font("Arial", serverPortField.getFont().getStyle(), serverPortField.getFont().getSize() + 1));
                serverPortField.setText("5704");

                //---- usernameField ----
                usernameField.setFont(new Font("Arial", usernameField.getFont().getStyle(), usernameField.getFont().getSize() + 1));

                //---- passwordField ----
                passwordField.setFont(new Font("Arial", passwordField.getFont().getStyle(), passwordField.getFont().getSize() + 1));

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
                                    .addGap(0, 221, Short.MAX_VALUE))
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
                                    .addContainerGap(56, Short.MAX_VALUE))))
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
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- titleLbl ----
            titleLbl.setText("Client Connection Settings");
            titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
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

    private void onOk(){
        String password = String.valueOf(passwordField.getPassword());
        String username = usernameField.getText();
        if(!(FileClient.ping(serverAddressField.getText(), Integer.parseInt(serverPortField.getText())))){
            JOptionPane.showMessageDialog(null, "Server Offline!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
            serverAddressField.setText("");
            serverPortField.setText("5704");
            usernameField.setText("");
            passwordField.setText("");
            bindAnonymouslyCheckBox.setSelected(false);
            return;
        }
        try{
            if(!(bindAnonymouslyCheckBox.isSelected())){
                connectAsUser(usernameField.getText(), String.valueOf(passwordField.getPassword()));
                if(!(usernameFinal.equals(""))){
                    FileClient.receiveFile(serverAddressField.getText(), Integer.parseInt(serverPortField.getText()), ".catalog.json", ".catalog.json");
                    ClientBrowser.run(serverAddressField.getText(), Integer.parseInt(serverPortField.getText()), clientModeConfiguration, usernameFinal);
                    dispose();
                }
            }else{
                usernameFinal = "guest";
                FileClient.receiveFile(serverAddressField.getText(), Integer.parseInt(serverPortField.getText()), ".catalog.json", ".catalog.json");
                ClientBrowser.run(serverAddressField.getText(), Integer.parseInt(serverPortField.getText()), clientModeConfiguration, usernameFinal);
                dispose();
            }
        }catch(IOException ioe){
        }
    }

    private void bindAnonymously(String mode){
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

    private void connectAsUser(String username, String password){
        try {
            FileClient.receiveFile(serverAddressField.getText(), Integer.parseInt(serverPortField.getText()), ".auth", ".auth");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(".auth"));
            HashMap<String, String> credentials = (HashMap<String, String>)ois.readObject();
            for (HashMap.Entry<String,String> entry : credentials.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals(username)){
                    for(int i = 0; i < username.length()-1; i=i+2){
                        try{
                            password += username.charAt(i);
                        }catch(IndexOutOfBoundsException ioobe){
                        }
                    }
                    MessageDigest messageDigest = null;
                    try {
                        messageDigest = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    messageDigest.update(password.getBytes(),0, password.length());
                    String enc = new BigInteger(1,messageDigest.digest()).toString(128);
                    if(!(value.equals(enc))){
                        return;
                    }
                    else if(value.equals(enc)){
                        usernameFinal = username;
                        return;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkFields(JTextField field) {
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