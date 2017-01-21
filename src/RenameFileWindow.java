import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/*
 * Created by JFormDesigner on Tue Jan 17 18:36:29 MST 2017
 */



/**
 * @author Mark Hedrick
 */
public class RenameFileWindow extends JFrame {
    private String serverAddress;
    private int port;
    private String jsonObj;
    private JFrame caller;
    private String newName;
    private static JFrame renameFileWindow;
    private String userAccount;
    private RenameFileWindow(String serverAddress, int port, String jsonObj, JFrame sender, String currentName, String userAccount) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.jsonObj = jsonObj;
        this.caller = sender;
        this.userAccount = userAccount;
        initComponents();
        frameListeners();
        currentNameValue.setText(currentName);
        okButton.setEnabled(false);
        if(Reporting.getSystemOS().contains("Windows")){
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        currentNameLbl = new JLabel();
        currentNameValue = new JLabel();
        newNameLbl = new JLabel();
        newNameValueField = new JTextField();
        buttonBar = new JPanel();
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
                currentNameLbl.setFont(new Font("Arial", currentNameLbl.getFont().getStyle(), currentNameLbl.getFont().getSize() + 1));

                //---- currentNameValue ----
                currentNameValue.setText("(file name)");
                currentNameValue.setFont(new Font("Arial", currentNameValue.getFont().getStyle(), currentNameValue.getFont().getSize() + 1));

                //---- newNameLbl ----
                newNameLbl.setText("New Name:");
                newNameLbl.setFont(new Font("Arial", newNameLbl.getFont().getStyle(), newNameLbl.getFont().getSize() + 1));

                //---- newNameValueField ----
                newNameValueField.setFont(new Font("Arial", newNameValueField.getFont().getStyle(), newNameValueField.getFont().getSize() + 1));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(currentNameLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(currentNameValue))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(newNameLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(newNameValueField, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap(8, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(currentNameLbl)
                                .addComponent(currentNameValue))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(newNameLbl)
                                .addComponent(newNameValueField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(5, Short.MAX_VALUE))
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
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners(){
        newNameValueField.getDocument().addDocumentListener(new DocumentListener() {
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

                if(!(currentNameValue.getText().equals(newNameValueField.getText()))){
                    if (newNameValueField.getText().isEmpty()){
                        okButton.setEnabled(false);
                    }else{
                        okButton.setEnabled(true);
                        buttonBar.getRootPane().setDefaultButton(okButton);
                    }
                }else{
                    okButton.setEnabled(false);
                }
            }
        });
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    FileClient.renameFile(serverAddress, port, jsonObj, newNameValueField.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
                caller.dispose();
                ClientBrowser.run(serverAddress, port, renameFileWindow, userAccount);
            }
        });
    }

    public static void run(String serverAddress, int port, JFrame sender, String jsonObj, String currentName, String userAccount){
        JFrame renameFileWindow = new RenameFileWindow(serverAddress, port, jsonObj, sender, currentName, userAccount);
        CenterWindow.centerOnWindow(sender, renameFileWindow);
        renameFileWindow.setVisible(true);

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel currentNameLbl;
    private JLabel currentNameValue;
    private JLabel newNameLbl;
    private JTextField newNameValueField;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
