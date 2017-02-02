import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Mark Hedrick
 */
class RenameFileWindow extends JFrame {
    private final String serverAddress;
    private final int port;
    private final String jsonObj;
    private final String userAccount;
    private final String originalName;
    private JLabel currentNameValue;
    private JTextField newNameValueField;
    private JPanel buttonBar;
    private JButton okButton;
    private File catalogFile;

    private RenameFileWindow(
            String serverAddress, int port, String jsonObj, String currentName, String userAccount, File catalogFile) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        this.serverAddress = serverAddress;
        this.port = port;
        this.jsonObj = jsonObj;
        this.userAccount = userAccount;
        this.originalName = currentName;
        this.catalogFile = catalogFile;

        initComponents();
        frameListeners();

        String currentNameTruncated = currentName;
        if (currentName.length() > 33) {
            currentNameTruncated = currentName.substring(0, 30) + "...";
        }

        int beginningExtension = currentName.lastIndexOf(".");

        currentNameValue.setText(currentNameTruncated);
        currentNameValue.setToolTipText(currentName);
        newNameValueField.setText(currentName);
        newNameValueField.setSelectionStart(0);
        newNameValueField.setSelectionEnd(beginningExtension);
        okButton.setEnabled(false);
    }

    public static void run(
            String serverAddress,
            int port,
            JFrame sender,
            String jsonObj,
            String currentName,
            String userAccount,
            File catalogFile) {
        JFrame renameFileWindow =
                new RenameFileWindow(serverAddress, port, jsonObj, currentName, userAccount, catalogFile);
        CenterWindow.centerOnWindow(sender, renameFileWindow);
        renameFileWindow.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        JPanel dialogPane = new JPanel();
        JPanel contentPanel = new JPanel();
        JLabel currentNameLbl = new JLabel();
        currentNameValue = new JLabel();
        JLabel newNameLbl = new JLabel();
        newNameValueField = new JTextField();
        buttonBar = new JPanel();
        JButton cancelBtn = new JButton();
        okButton = new JButton();

        //======== this ========
        setTitle("MeshFS - Rename");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- currentNameLbl ----
                currentNameLbl.setText("Current Name:");
                currentNameLbl.setFont(
                        new Font(
                                "Arial",
                                currentNameLbl.getFont().getStyle(),
                                currentNameLbl.getFont().getSize() + 1));

                //---- currentNameValue ----
                currentNameValue.setText("(file name)");
                currentNameValue.setFont(
                        new Font(
                                "Arial",
                                currentNameValue.getFont().getStyle(),
                                currentNameValue.getFont().getSize() + 1));

                //---- newNameLbl ----
                newNameLbl.setText("New Name:");
                newNameLbl.setFont(
                        new Font("Arial", newNameLbl.getFont().getStyle(), newNameLbl.getFont().getSize() + 1));

                //---- newNameValueField ----
                newNameValueField.setFont(
                        new Font(
                                "Arial",
                                newNameValueField.getFont().getStyle(),
                                newNameValueField.getFont().getSize() + 1));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout
                                .createParallelGroup()
                                .addGroup(
                                        contentPanelLayout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(
                                                        contentPanelLayout
                                                                .createParallelGroup()
                                                                .addGroup(
                                                                        contentPanelLayout
                                                                                .createSequentialGroup()
                                                                                .addComponent(currentNameLbl)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(
                                                                                        currentNameValue,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        231,
                                                                                        Short.MAX_VALUE))
                                                                .addGroup(
                                                                        contentPanelLayout
                                                                                .createSequentialGroup()
                                                                                .addComponent(newNameLbl)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(
                                                                                        newNameValueField,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        251,
                                                                                        Short.MAX_VALUE)))
                                                .addContainerGap()));
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout
                                .createParallelGroup()
                                .addGroup(
                                        contentPanelLayout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(
                                                        contentPanelLayout
                                                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(currentNameLbl)
                                                                .addComponent(currentNameValue))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(
                                                        contentPanelLayout
                                                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(newNameLbl)
                                                                .addComponent(
                                                                        newNameValueField,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE))
                                                .addContainerGap(5, Short.MAX_VALUE)));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 0, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{0.0, 1.0, 0.0};

                //---- cancelBtn ----
                cancelBtn.setText("Cancel");
                cancelBtn.setFont(
                        new Font("Arial", cancelBtn.getFont().getStyle(), cancelBtn.getFont().getSize() + 1));
                buttonBar.add(
                        cancelBtn,
                        new GridBagConstraints(
                                0,
                                0,
                                1,
                                1,
                                0.0,
                                0.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 5),
                                0,
                                0));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setFont(
                        new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                buttonBar.add(
                        okButton,
                        new GridBagConstraints(
                                2,
                                0,
                                1,
                                1,
                                0.0,
                                0.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0),
                                0,
                                0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners() {
        newNameValueField
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

                                if (!(originalName.equals(newNameValueField.getText()))) {
                                    if (newNameValueField.getText().isEmpty()) {
                                        okButton.setEnabled(false);
                                    } else {
                                        String newJsonPath = jsonObj.substring(0, jsonObj.lastIndexOf("/"));
                                        JSONObject catalogJson = JSONManipulator.getJSONObject(catalogFile.getAbsolutePath());
                                        Map<String, String> folderMap =
                                                JSONManipulator.getMapOfFolderContents(
                                                        catalogJson, newJsonPath, userAccount);
                                        for (Map.Entry<String, String> entry : folderMap.entrySet()) {
                                            if (entry.getValue().equals("file")) {
                                                if (entry.getKey().equals(newNameValueField.getText())) {
                                                    okButton.setEnabled(false);
                                                    return;
                                                }
                                            }
                                        }
                                        okButton.setEnabled(true);
                                        buttonBar.getRootPane().setDefaultButton(okButton);
                                    }
                                } else {
                                    okButton.setEnabled(false);
                                }
                            }
                        });
        okButton.addActionListener(
                e -> {
                    try {
                        FileClient.renameFile(serverAddress, port, jsonObj, newNameValueField.getText());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    dispose();
                });
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
