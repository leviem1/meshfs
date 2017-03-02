import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Tue Feb 28 20:04:44 MST 2017
 */



/**
 * @author User #1
 */
public class ChangeUserPassword extends JFrame {
    private static JFrame changeUserPassword;
    private String userAccount;
    private String serverAddress;
    private int port;

    public ChangeUserPassword(String userAccount, String serverAddress, int port) {
        this.userAccount = userAccount;
        this.serverAddress = serverAddress;
        this.port = port;


        setTitle("MeshFS - Change User Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);


        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
        initComponents();
        frameListeners();
        usernameField.setText(userAccount);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        usernameLbl = new JLabel();
        usernameField = new JTextField();
        newPasswordLbl = new JLabel();
        newPasswordConfirmLbl = new JLabel();
        label3 = new JLabel();
        newPasswordField = new JPasswordField();
        newPasswordFieldConfirm = new JPasswordField();
        currPasswordLbl = new JLabel();
        currPasswordField = new JPasswordField();
        buttonBar = new JPanel();
        okButton = new JButton();
        titleLbl2 = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- usernameLbl ----
                usernameLbl.setText("Username:");
                usernameLbl.setFont(new Font("Arial", usernameLbl.getFont().getStyle(), usernameLbl.getFont().getSize() + 1));

                //---- usernameField ----
                usernameField.setEditable(false);
                usernameField.setEnabled(false);
                usernameField.setFont(new Font("Arial", usernameField.getFont().getStyle(), usernameField.getFont().getSize() + 1));

                //---- newPasswordLbl ----
                newPasswordLbl.setText("New Password:");
                newPasswordLbl.setFont(new Font("Arial", newPasswordLbl.getFont().getStyle(), newPasswordLbl.getFont().getSize() + 1));

                //---- newPasswordConfirmLbl ----
                newPasswordConfirmLbl.setText("New Password:");
                newPasswordConfirmLbl.setFont(new Font("Arial", newPasswordConfirmLbl.getFont().getStyle(), newPasswordConfirmLbl.getFont().getSize() + 1));

                //---- label3 ----
                label3.setText("(confirm)");
                label3.setHorizontalAlignment(SwingConstants.CENTER);
                label3.setFont(new Font("Arial", label3.getFont().getStyle(), label3.getFont().getSize() + 1));

                //---- newPasswordField ----
                newPasswordField.setFont(new Font("Arial", newPasswordField.getFont().getStyle(), newPasswordField.getFont().getSize() + 1));

                //---- newPasswordFieldConfirm ----
                newPasswordFieldConfirm.setFont(new Font("Arial", newPasswordFieldConfirm.getFont().getStyle(), newPasswordFieldConfirm.getFont().getSize() + 1));

                //---- currPasswordLbl ----
                currPasswordLbl.setText("Current Password:");
                currPasswordLbl.setFont(new Font("Arial", currPasswordLbl.getFont().getStyle(), currPasswordLbl.getFont().getSize() + 1));

                //---- currPasswordField ----
                currPasswordField.setFont(new Font("Arial", currPasswordField.getFont().getStyle(), currPasswordField.getFont().getSize() + 1));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(usernameLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(usernameField, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(currPasswordLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(currPasswordField, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(newPasswordLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(newPasswordField, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label3, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(newPasswordConfirmLbl))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(newPasswordFieldConfirm, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)))
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(usernameLbl)
                                .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(currPasswordLbl)
                                .addComponent(currPasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(newPasswordLbl)
                                .addComponent(newPasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(newPasswordConfirmLbl)
                                .addComponent(newPasswordFieldConfirm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(label3)
                            .addContainerGap(29, Short.MAX_VALUE))
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

            //---- titleLbl2 ----
            titleLbl2.setText("Change User Password");
            titleLbl2.setFont(new Font("Arial", titleLbl2.getFont().getStyle(), titleLbl2.getFont().getSize() + 9));
            titleLbl2.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPane.add(titleLbl2, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners() {
        okButton.addActionListener(
                e -> onOk());
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel usernameLbl;
    private JTextField usernameField;
    private JLabel newPasswordLbl;
    private JLabel newPasswordConfirmLbl;
    private JLabel label3;
    private JPasswordField newPasswordField;
    private JPasswordField newPasswordFieldConfirm;
    private JLabel currPasswordLbl;
    private JPasswordField currPasswordField;
    private JPanel buttonBar;
    private JButton okButton;
    private JLabel titleLbl2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void run(JFrame sender, String userAccount, String serverAddress, int port) {
        changeUserPassword = new ChangeUserPassword(userAccount, serverAddress, port);
        CenterWindow.centerOnWindow(sender, changeUserPassword);
        changeUserPassword.setVisible(true);
    }

    public void onOk(){
        if(String.valueOf(newPasswordField.getPassword()).equals(String.valueOf(newPasswordFieldConfirm.getPassword()))){
            try {
                FileClient.changePassword(serverAddress, port, userAccount, String.valueOf(currPasswordField.getPassword()), String.valueOf(newPasswordFieldConfirm.getPassword()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(changeUserPassword, "New Password Mismatch", "MeshFS - Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
