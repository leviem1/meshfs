import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.HashMap;

/**
 * @author Mark Hedrick
 */
class ClientModeConfiguration extends JFrame {

    private static JFrame clientModeConfiguration;
    private final boolean runType;
    private String usernameFinal = "";
    //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel serverAddressLbl;
    private JLabel serverPortLbl;
    private JLabel passwordLbl;
    private JTextField serverAddressField;
    private JFormattedTextField serverPortField;
    private JPasswordField passwordField;
    private JLabel usernameLbl;
    private JTextField usernameField;
    private JCheckBox bindAnonymouslyCheckBox;
    private JLabel bindAnonLbl;
    private JPanel buttonBar;
    private JButton backBtn;
    private JButton okButton;
    private JLabel titleLbl;
    //GEN-END:variables

    private ClientModeConfiguration(String serverAddress, boolean runType) {
        this.runType = runType;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        initComponents();
        frameListeners();

        serverAddressField.setText(serverAddress);
        if (serverAddress.equals("")) {
            okButton.setEnabled(false);
        } else {
            okButton.setEnabled(true);
        }
    }

    public static void run(JFrame sender, String serverAddress, boolean runType) {
        clientModeConfiguration = new ClientModeConfiguration(serverAddress, runType);
        if (!(sender == null)) {
            CenterWindow.centerOnWindow(sender, clientModeConfiguration);
        } else {
            CenterWindow.centerOnScreen(clientModeConfiguration);
        }
        clientModeConfiguration.setVisible(true);
    }

    private void initComponents() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        //GEN-BEGIN:initComponents

        dialogPane = new JPanel();
        contentPanel = new JPanel();
        serverAddressLbl = new JLabel();
        serverPortLbl = new JLabel();
        passwordLbl = new JLabel();
        serverAddressField = new JTextField();
        serverPortField = new JFormattedTextField(numberFormat);
        passwordField = new JPasswordField();
        usernameLbl = new JLabel();
        usernameField = new JTextField();
        bindAnonymouslyCheckBox = new JCheckBox();
        bindAnonLbl = new JLabel();
        buttonBar = new JPanel();
        backBtn = new JButton();
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
                serverAddressLbl.setText("Master Address:");
                serverAddressLbl.setFont(new Font("Arial", serverAddressLbl.getFont().getStyle() & ~Font.ITALIC, serverAddressLbl.getFont().getSize() + 1));

                //---- serverPortLbl ----
                serverPortLbl.setText("Server Port:");
                serverPortLbl.setFont(new Font("Arial", serverPortLbl.getFont().getStyle() & ~Font.ITALIC, serverPortLbl.getFont().getSize() + 1));

                //---- passwordLbl ----
                passwordLbl.setText("Password:");
                passwordLbl.setFont(new Font("Arial", passwordLbl.getFont().getStyle() & ~Font.ITALIC, passwordLbl.getFont().getSize() + 1));

                //---- serverAddressField ----
                serverAddressField.setFont(new Font("Arial", serverAddressField.getFont().getStyle() & ~Font.ITALIC, serverAddressField.getFont().getSize() + 1));

                //---- serverPortField ----
                serverPortField.setFont(new Font("Arial", serverPortField.getFont().getStyle() & ~Font.ITALIC, serverPortField.getFont().getSize() + 1));
                serverPortField.setText("5704");

                //---- passwordField ----
                passwordField.setFont(new Font("Arial", passwordField.getFont().getStyle() & ~Font.ITALIC, passwordField.getFont().getSize() + 1));

                //---- usernameLbl ----
                usernameLbl.setText("Username:");
                usernameLbl.setFont(new Font("Arial", usernameLbl.getFont().getStyle() & ~Font.ITALIC, usernameLbl.getFont().getSize() + 1));

                //---- usernameField ----
                usernameField.setFont(new Font("Arial", usernameField.getFont().getStyle() & ~Font.ITALIC, usernameField.getFont().getSize() + 1));

                //---- bindAnonLbl ----
                bindAnonLbl.setText("Bind as Guest:");
                bindAnonLbl.setFont(new Font("Arial", bindAnonLbl.getFont().getStyle() & ~Font.ITALIC, bindAnonLbl.getFont().getSize() + 1));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                                    .addComponent(serverAddressLbl)
                                    .addGap(12, 12, 12)
                                    .addComponent(serverAddressField)
                                    .addGap(13, 13, 13)
                                    .addComponent(serverPortLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(serverPortField, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(GroupLayout.Alignment.LEADING, contentPanelLayout.createSequentialGroup()
                                            .addComponent(passwordLbl)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(passwordField))
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(usernameLbl)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)))
                                    .addGap(12, 12, 12)
                                    .addComponent(bindAnonLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(bindAnonymouslyCheckBox)
                                    .addContainerGap(12, Short.MAX_VALUE))))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(serverPortField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(serverPortLbl)
                                .addComponent(serverAddressField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(serverAddressLbl))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(usernameLbl)
                                .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(bindAnonLbl)
                                .addComponent(bindAnonymouslyCheckBox))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordLbl)
                                .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(14, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0};

                //---- backBtn ----
                backBtn.setText("Back");
                backBtn.setFont(new Font("Arial", backBtn.getFont().getStyle() & ~Font.ITALIC, backBtn.getFont().getSize() + 1));
                buttonBar.add(backBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("Connect");
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle() & ~Font.ITALIC, okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
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
        //GEN-END:initComponents
    }

    private void frameListeners() {
        okButton.addActionListener(e -> onOk());
        bindAnonymouslyCheckBox.addActionListener(
                e -> {
                    if (bindAnonymouslyCheckBox.isSelected()) {
                        bindAnonymously(true);
                    } else {
                        bindAnonymously(false);
                    }
                });
        serverAddressField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

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
                                if (!(bindAnonymouslyCheckBox.isSelected())) {
                                    if (usernameField.getText().isEmpty()) {
                                        return;
                                    }
                                    if (passwordField.getPassword().length == 0) {
                                        return;
                                    }
                                }
                                if (!(checkFields(serverAddressField))) {
                                    okButton.setEnabled(false);
                                } else {
                                    okButton.setEnabled(true);
                                    buttonBar.getRootPane().setDefaultButton(okButton);
                                }
                            }
                        });
        serverPortField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

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
                                if (serverPortField.getText().isEmpty()) {
                                    okButton.setEnabled(false);
                                } else {
                                    okButton.setEnabled(true);
                                }
                            }
                        });
        passwordField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

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
                                if (serverAddressField.getText().isEmpty() || serverPortField.getText().isEmpty()) {
                                    return;
                                }
                                if (passwordField.getPassword().length == 0) {
                                    okButton.setEnabled(false);
                                } else {
                                    okButton.setEnabled(true);
                                }
                            }
                        });
        usernameField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

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
                                if (serverAddressField.getText().isEmpty() || serverPortField.getText().isEmpty()) {
                                    return;
                                }
                                if (!(checkFields(usernameField))) {
                                    okButton.setEnabled(false);
                                } else {
                                    okButton.setEnabled(true);
                                }
                            }
                        });
        backBtn.addActionListener(
                e -> {
                    if (runType) {
                        InitialConfiguration.run(clientModeConfiguration);
                    } else {
                        GreetingsWindow.run(false, clientModeConfiguration);
                    }

                    dispose();
                });
    }

    private void onOk() {
        if (!(FileClient.ping(
                serverAddressField.getText(), Integer.parseInt(serverPortField.getText())))) {
            JOptionPane.showMessageDialog(
                    clientModeConfiguration, "Server Offline!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
            serverAddressField.setText("");
            serverPortField.setText("5704");
            usernameField.setText("guest");
            passwordField.setText("guest");
            bindAnonymouslyCheckBox.setSelected(true);
            okButton.setEnabled(false);
            return;
        }
        try {
            connectAsUser(usernameField.getText(), String.valueOf(passwordField.getPassword()));
            File catalog = File.createTempFile(".catalog", ".json");
            if (!(usernameFinal.equals(""))) {
                FileClient.receiveFile(
                        serverAddressField.getText(),
                        Integer.parseInt(serverPortField.getText()),
                        ".catalog.json",
                        catalog.getAbsolutePath());
                ClientBrowser.run(
                        serverAddressField.getText(),
                        Integer.parseInt(serverPortField.getText()),
                        clientModeConfiguration,
                        usernameFinal,
                        catalog);
                dispose();
            }
        } catch (IOException ignored) {
        }
    }

    private void bindAnonymously(boolean value) {
        if (value) {
            usernameField.setText("guest");
            passwordField.setText("guest");
            usernameField.setEnabled(false);
            passwordField.setEnabled(false);
            buttonBar.getRootPane().setDefaultButton(okButton);
            okButton.requestFocus();
        } else {
            usernameField.requestFocus();
            usernameField.setText("");
            passwordField.setText("");
            usernameField.setEnabled(true);
            passwordField.setEnabled(true);
        }
    }

    private void connectAsUser(String username, String password) {
        try {
            File auth = File.createTempFile(".auth", "");
            auth.deleteOnExit();
            FileClient.receiveFile(
                    serverAddressField.getText(),
                    Integer.parseInt(serverPortField.getText()),
                    ".auth",
                    auth.getAbsolutePath());
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(auth.getAbsolutePath()));
            @SuppressWarnings("unchecked")
            HashMap<String, String> credentials = (HashMap<String, String>) ois.readObject();
            for (HashMap.Entry<String, String> entry : credentials.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equals(username)) {
                    for (int i = 0; i < username.length() - 1; i = i + 2) {
                        try {
                            password += username.charAt(i);
                        } catch (IndexOutOfBoundsException ignored) {
                        }
                    }
                    MessageDigest messageDigest = null;
                    try {
                        messageDigest = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    assert messageDigest != null;
                    messageDigest.update(password.getBytes(), 0, password.length());
                    String enc = new BigInteger(1, messageDigest.digest()).toString(128);
                    if (!(value.equals(enc))) {
                        return;
                    } else if (value.equals(enc)) {
                        usernameFinal = username;
                        return;
                    }
                }
            }
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkFields(JTextField field) {
        return !field.getText().isEmpty();
    }
}
