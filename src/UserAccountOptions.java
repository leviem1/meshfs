import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

/**
 * @author Mark Hedrick
 */
class UserAccountOptions extends JFrame {
    private static JFrame userAccountOptions;
    private String userAccount;
    private String serverAddress;
    private int port;
    private JFrame parentSender;
    private boolean previousRunType;

    //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JLabel titleLbl2;
    private JButton closeBtn;
    private JLabel titleLbl;
    private JButton changePasswordBtn;
    private JButton deleteAccount;
    private JButton changeGroupBtn;
    //GEN-END:variables

    private UserAccountOptions(
            String userAccount, String serverAddress, int port, JFrame parentSender, boolean previousRunType) {
        this.userAccount = userAccount;
        this.serverAddress = serverAddress;
        this.port = port;
        this.parentSender = parentSender;
        this.previousRunType = previousRunType;


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
        initComponents();
        frameListeners();

        titleLbl.setText(
                "Welcome " + userAccount.substring(0, 1).toUpperCase() + userAccount.substring(1) + "!");
        setTitle("MeshFS - Account Options");
        String userType = null;
        try {
            userType = FileClient.getUserType(serverAddress, port, userAccount, MeshFS.properties.getProperty("uuid"));
        } catch (MalformedRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (userAccount.equals("guest")) {
            changePasswordBtn.setEnabled(false);
            changePasswordBtn.setToolTipText("Changing the guest password is not allowed");
            changeGroupBtn.setEnabled(false);
            changeGroupBtn.setToolTipText("Changing guest groups is not allowed");
            deleteAccount.setEnabled(false);
            deleteAccount.setToolTipText("Deleting the guest account is not allowed");

        } else if (!userType.equals("admin")) {
            changeGroupBtn.setEnabled(false);
            changeGroupBtn.setToolTipText("Changing guest groups is not allowed");
        }
        if (userAccount.equals("admin")) {
            deleteAccount.setEnabled(false);
            deleteAccount.setToolTipText("Deleting the admin account is not allowed");
        }
    }

    public static void run(JFrame sender, String userAccount, String serverAddress, int port, boolean previousRunType) {
        userAccountOptions = new UserAccountOptions(userAccount, serverAddress, port, sender, previousRunType);
        CenterWindow.centerOnWindow(sender, userAccountOptions);
        userAccountOptions.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        titleLbl2 = new JLabel();
        closeBtn = new JButton();
        titleLbl = new JLabel();
        changePasswordBtn = new JButton();
        deleteAccount = new JButton();
        changeGroupBtn = new JButton();

        //======== this ========
        setTitle("MeshFS - Account Control Center");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

            //---- titleLbl2 ----
            titleLbl2.setText("Account Control Center");
            titleLbl2.setFont(new Font("Arial", titleLbl2.getFont().getStyle(), titleLbl2.getFont().getSize() + 9));
            titleLbl2.setHorizontalAlignment(SwingConstants.CENTER);

            //---- closeBtn ----
            closeBtn.setText("Close");
            closeBtn.setFont(new Font("Arial", closeBtn.getFont().getStyle(), closeBtn.getFont().getSize() + 1));

            //---- titleLbl ----
            titleLbl.setText("Welcome <username>!");
            titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
            titleLbl.setHorizontalAlignment(SwingConstants.CENTER);

            //---- changePasswordBtn ----
            changePasswordBtn.setText("Change Password");
            changePasswordBtn.setFont(new Font("Arial", changePasswordBtn.getFont().getStyle(), changePasswordBtn.getFont().getSize() + 1));

            //---- deleteAccount ----
            deleteAccount.setText("Delete Account");
            deleteAccount.setFont(new Font("Arial", deleteAccount.getFont().getStyle(), deleteAccount.getFont().getSize() + 1));

            //---- changeGroupBtn ----
            changeGroupBtn.setText("Change Group");
            changeGroupBtn.setFont(new Font("Arial", changeGroupBtn.getFont().getStyle(), changeGroupBtn.getFont().getSize() + 1));

            GroupLayout dialogPaneLayout = new GroupLayout(dialogPane);
            dialogPane.setLayout(dialogPaneLayout);
            dialogPaneLayout.setHorizontalGroup(
                dialogPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(dialogPaneLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(titleLbl2, GroupLayout.PREFERRED_SIZE, 324, GroupLayout.PREFERRED_SIZE))
                    .addGroup(dialogPaneLayout.createSequentialGroup()
                        .addGroup(dialogPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(titleLbl, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(dialogPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(dialogPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(dialogPaneLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(closeBtn))
                                    .addGroup(GroupLayout.Alignment.LEADING, dialogPaneLayout.createSequentialGroup()
                                        .addGroup(dialogPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(changePasswordBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(changeGroupBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(deleteAccount, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())
            );
            dialogPaneLayout.setVerticalGroup(
                dialogPaneLayout.createParallelGroup()
                    .addGroup(dialogPaneLayout.createSequentialGroup()
                        .addComponent(titleLbl2)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(titleLbl)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dialogPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(changePasswordBtn)
                            .addComponent(deleteAccount))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(changeGroupBtn)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addComponent(closeBtn))
            );
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners() {
        changePasswordBtn.addActionListener(
                e -> {
                    ChangeUserPassword.run(
                            userAccountOptions, userAccount, serverAddress, port, parentSender, previousRunType);
                    dispose();
                });

        deleteAccount.addActionListener(
                e -> {
                    int confirmBtn = JOptionPane.YES_NO_OPTION;
                    int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you wish to delete your account?", "MeshFS - Delete Account", confirmBtn);
                    if (confirmResult == 0) {
                        try {
                            FileClient.deleteAccount(serverAddress, port, userAccount);
                            ClientModeConfiguration.run(userAccountOptions, serverAddress, previousRunType);
                            parentSender.dispose();
                            dispose();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        } catch (MalformedRequestException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        dispose();
                    }
                });

        closeBtn.addActionListener(e -> dispose());
        changeGroupBtn.addActionListener(
                e -> ChangeUserGroup.run(userAccountOptions, userAccount, serverAddress, port));
    }
}
