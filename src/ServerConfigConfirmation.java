import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author Mark Hedrick
 */
class ServerConfigConfirmation extends JFrame {
    private static JFrame serverConfigConfirmation;
    private final ArrayList accountDetails;
    private Properties properties;


    //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JScrollPane configValuesPane;
    private JTextPane configValues;
    private JLabel serverIdentifierLbl;
    private JTextField serverIdentifierField;
    private JPanel buttonBar;
    private JButton backBtn;
    private JButton okButton;
    private JLabel titleLbl;
    //GEN-END:variables

    private ServerConfigConfirmation(
            String content, ArrayList accountDetails, Properties properties, boolean isMaster) {
        this.properties = properties;
        this.accountDetails = accountDetails;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        initComponents();
        frameListeners();

        if (isMaster) {
            serverIdentifierField.setEditable(false);
            serverIdentifierLbl.setEnabled(false);
            serverIdentifierField.setText(MeshFS.properties.getProperty("uuid"));
        }

        configValues.setContentType("text/html");
        configValues.setText(content);
        configValues.setCaretPosition(0);
    }

    public static void run(
            JFrame sender,
            String content,
            ArrayList accountDetails,
            Properties properties,
            boolean isMaster) {
        serverConfigConfirmation =
                new ServerConfigConfirmation(content, accountDetails, properties, isMaster);
        CenterWindow.centerOnWindow(sender, serverConfigConfirmation);
        serverConfigConfirmation.setVisible(true);
    }

    private void initComponents() {
        //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        configValuesPane = new JScrollPane();
        configValues = new JTextPane();
        serverIdentifierLbl = new JLabel();
        serverIdentifierField = new JTextField();
        buttonBar = new JPanel();
        backBtn = new JButton();
        okButton = new JButton();
        titleLbl = new JLabel();

        //======== this ========
        setTitle("MeshFS - Server Confirmation");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- label1 ----
                label1.setText("Confirm the following values before the server launches:");
                label1.setHorizontalAlignment(SwingConstants.CENTER);
                label1.setFont(new Font("Arial", label1.getFont().getStyle(), label1.getFont().getSize() + 1));

                //======== configValuesPane ========
                {
                    configValuesPane.setViewportView(configValues);
                }

                //---- serverIdentifierLbl ----
                serverIdentifierLbl.setText("Master ID:");
                serverIdentifierLbl.setFont(new Font("Arial", serverIdentifierLbl.getFont().getStyle(), serverIdentifierLbl.getFont().getSize() + 1));

                //---- serverIdentifierField ----
                serverIdentifierField.setFont(new Font("Arial", serverIdentifierField.getFont().getStyle(), serverIdentifierField.getFont().getSize() + 1));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(contentPanelLayout.createParallelGroup()
                                                .addComponent(label1, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                                                .addComponent(configValuesPane)
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                        .addComponent(serverIdentifierLbl)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(serverIdentifierField, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)))
                                        .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(label1)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(configValuesPane, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(serverIdentifierLbl)
                                                .addComponent(serverIdentifierField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 0, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{0.0, 1.0, 0.0};

                //---- backBtn ----
                backBtn.setText("Back");
                backBtn.setFont(new Font("Arial", backBtn.getFont().getStyle(), backBtn.getFont().getSize() + 1));
                buttonBar.add(backBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("Launch Server");
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- titleLbl ----
            titleLbl.setText("Config Parameters");
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
        okButton.addActionListener(
                e -> {
                    try {
                        onOk();
                    } catch (IOException ignored) {
                    }
                });
        backBtn.addActionListener(
                e -> {
                    ServerModeConfiguration.run(serverConfigConfirmation);
                    dispose();
                });

        serverIdentifierField
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
                                if (serverIdentifierField.getText().isEmpty()) {
                                    okButton.setEnabled(false);
                                } else if (!serverIdentifierField.getText().isEmpty()) {
                                    okButton.setEnabled(true);
                                }
                            }
                        });
    }

    private void onOk() throws IOException {
        properties.setProperty("uuid", serverIdentifierField.getText());
        try {
            ServerModeConfiguration.writeConfig(properties);
            JOptionPane.showMessageDialog(
                    serverConfigConfirmation,
                    "Configuration was saved!",
                    "MeshFS - Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    serverConfigConfirmation,
                    "Error when saving configuration!",
                    "MeshFS - Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        FileOutputStream fos =
                new FileOutputStream(MeshFS.properties.getProperty("repository") + ".auth");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(accountDetails);
        oos.flush();

        final String javaBin =
                System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar;
        try {
            currentJar =
                    new File(MeshFS.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (!currentJar.getName().endsWith(".jar")) {
                return;
            }
            final ArrayList<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-server");
            command.add("-cp");
            command.add(
                    MeshFS.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            command.add("-jar");
            command.add(currentJar.getPath());
            command.add("-nogui");
            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            dispose();
            System.exit(0);
        } catch (URISyntaxException ignored) {
        } finally {
            oos.close();
        }
    }
}
