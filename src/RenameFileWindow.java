import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Tue Jan 17 18:36:29 MST 2017
 */



/**
 * @author User #1
 */
public class RenameFileWindow extends JFrame {
    private String serverAddress;
    private int port;
    private String jsonObj;
    private JFrame caller;
    private String newName;
    private static JFrame renameFileWindow;
    public RenameFileWindow(String serverAddress, int port, String jsonObj, JFrame sender, String currentName) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.jsonObj = jsonObj;
        this.caller = sender;
        initComponents();
        frameListeners();
        currentNameValue.setText(currentName);

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

                //---- currentNameValue ----
                currentNameValue.setText("(file name)");

                //---- newNameLbl ----
                newNameLbl.setText("New Name:");

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
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    FileClient.renameFile(serverAddress, port, jsonObj, newNameValueField.getText());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
                caller.dispose();
                ClientBrowser.run(serverAddress, port, renameFileWindow);
            }
        });
    }

    public static void run(String serverAddress, int port, JFrame sender, String jsonObj, String currentName){
        JFrame renameFileWindow = new RenameFileWindow(serverAddress, port, jsonObj, sender, currentName);
        CenterWindow.centerOnScreen(renameFileWindow);
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
