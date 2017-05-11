import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * @author Mark Hedrick
 */
class ClientModeConfiguration extends JFrame {

    private static JFrame clientModeConfiguration;
    private final boolean runType;
    private String usernameFinal = "";

    //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel serverAddressLbl;
    private JLabel serverPortLbl;
    private JLabel passwordLbl;
    private JFormattedTextField serverPortField;
    private JPasswordField passwordField;
    private JLabel usernameLbl;
    private JTextField usernameField;
    private JCheckBox bindAnonymouslyCheckBox;
    private JComboBox serverAddressField;
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

        serverAddressField.setSelectedItem(serverAddress);
        serverAddressField.setModel(new DefaultComboBoxModel(MeshFS.multicastServer.getFoundMasters().toArray()));
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
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        serverAddressLbl = new JLabel();
        serverPortLbl = new JLabel();
        passwordLbl = new JLabel();
        serverPortField = new JFormattedTextField(numberFormat);
        passwordField = new JPasswordField();
        usernameLbl = new JLabel();
        usernameField = new JTextField();
        bindAnonymouslyCheckBox = new JCheckBox();
        serverAddressField = new JComboBox();
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

                //---- bindAnonymouslyCheckBox ----
                bindAnonymouslyCheckBox.setText("Login as Guest");
                bindAnonymouslyCheckBox.setFont(new Font("Arial", bindAnonymouslyCheckBox.getFont().getStyle(), bindAnonymouslyCheckBox.getFont().getSize() + 1));
                bindAnonymouslyCheckBox.setHorizontalAlignment(SwingConstants.LEFT);
                bindAnonymouslyCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);

                //---- serverAddressField ----
                serverAddressField.setEditable(true);
                serverAddressField.setFont(new Font("Arial", serverAddressField.getFont().getStyle(), serverAddressField.getFont().getSize() + 1));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(serverAddressLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(serverAddressField, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(bindAnonymouslyCheckBox))))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(serverPortField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(serverPortLbl)
                                .addComponent(serverAddressLbl)
                                .addComponent(serverAddressField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(usernameLbl)
                                .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(bindAnonymouslyCheckBox))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
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
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0.0};

                //---- backBtn ----
                backBtn.setText("Back");
                backBtn.setFont(new Font("Arial", backBtn.getFont().getStyle() & ~Font.ITALIC, backBtn.getFont().getSize() + 1));
                buttonBar.add(backBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("Connect");
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle() & ~Font.ITALIC, okButton.getFont().getSize() + 1));
                okButton.setEnabled(false);
                buttonBar.add(okButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
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
        ((JTextField)serverAddressField
                .getEditor()
                .getEditorComponent())
                .getDocument()
                .addDocumentListener(clientConnectionSettingsListener);
        serverAddressField.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                serverAddressField.setModel(new DefaultComboBoxModel(MeshFS.multicastServer.getFoundMasters().toArray()));
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        serverPortField
                .getDocument()
                .addDocumentListener(clientConnectionSettingsListener);
        passwordField
                .getDocument()
                .addDocumentListener(clientConnectionSettingsListener);
        usernameField
                .getDocument()
                .addDocumentListener(clientConnectionSettingsListener);
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

    DocumentListener clientConnectionSettingsListener =  new DocumentListener() {

        public void changedUpdate(DocumentEvent e) {
            changed();
        }

        public void removeUpdate(DocumentEvent e) {
            changed();
        }

        public void insertUpdate(DocumentEvent e) {
            changed();
        }

        private void changed() {
            if(!(serverAddressField.getSelectedItem().toString().isEmpty())){
                if(!(serverPortField.getText().isEmpty())) {
                    if (!(usernameField.getText().isEmpty())) {
                        if (!(String.valueOf(passwordField.getPassword()).isEmpty())) {
                            okButton.setEnabled(true);
                            buttonBar.getRootPane().setDefaultButton(okButton);
                        }else{
                            okButton.setEnabled(false);
                        }
                    }else{
                        okButton.setEnabled(false);
                    }
                }else{
                    okButton.setEnabled(false);
                }
            }else{
                okButton.setEnabled(false);
            }
        }
    };

    private void onOk() {
        int pingTime = FileClient.ping(serverAddressField.getSelectedItem().toString(), Integer.parseInt(serverPortField.getText()));
        if (pingTime == -1) {
            JOptionPane.showMessageDialog(
                    clientModeConfiguration, "Server Offline!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
            serverAddressField.setSelectedItem("");
            serverPortField.setText("5704");
            usernameField.setText("guest");
            passwordField.setText("guest");
            bindAnonymouslyCheckBox.setSelected(true);
            okButton.setEnabled(false);
            return;
        }
        try {
            String uuid = connectAsUser(usernameField.getText(), String.valueOf(passwordField.getPassword()));
            System.out.println(uuid);
            if(uuid.equals("-1")){
                System.out.println("Error!");
                JOptionPane.showMessageDialog(
                        clientModeConfiguration, "Login Failure!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                usernameField.requestFocus();
                usernameField.setText("");
                passwordField.setText("");
                usernameField.setEnabled(true);
                passwordField.setEnabled(true);
            }
            File catalog = File.createTempFile(".catalog", ".json");
            if (!(usernameFinal.equals(""))) {
                FileClient.receiveFile(
                        serverAddressField.getSelectedItem().toString(),
                        Integer.parseInt(serverPortField.getText()),
                        ".catalog.json",
                        catalog.getAbsolutePath());
                ClientBrowser.run(
                        serverAddressField.getSelectedItem().toString(),
                        Integer.parseInt(serverPortField.getText()),
                        clientModeConfiguration,
                        usernameFinal,
                        catalog,
                        runType);
                dispose();
            }
        } catch (IOException ignored) {
        } catch (MalformedRequestException e) {
            e.printStackTrace();
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

    private String connectAsUser(String username, String password) {
        try {
            String uuid = FileClient.loginAsUser(
                    serverAddressField.getSelectedItem().toString(),
                    Integer.parseInt(serverPortField.getText()),
                    username,
                    Crypt.generateEncryptedAuth(username, password));
            return uuid;

        } catch (IOException | MalformedRequestException e) {
            e.printStackTrace();
        }
        return "-1";
    }

}
