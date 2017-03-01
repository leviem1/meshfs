import java.awt.*;
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
    public ChangeUserPassword(String userAccount) {
        this.userAccount = userAccount;
        setTitle("MeshFS - Change User Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);


        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
        initComponents();
        usernameField.setText(userAccount);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        usernameLbl = new JLabel();
        usernameField = new JTextField();
        passwordLbl = new JLabel();
        passwordConfirmLbl = new JLabel();
        label3 = new JLabel();
        passwordField = new JPasswordField();
        passwordFieldConfirm = new JPasswordField();
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

                //---- passwordLbl ----
                passwordLbl.setText("Password:");
                passwordLbl.setFont(new Font("Arial", passwordLbl.getFont().getStyle(), passwordLbl.getFont().getSize() + 1));

                //---- passwordConfirmLbl ----
                passwordConfirmLbl.setText("Password:");
                passwordConfirmLbl.setFont(new Font("Arial", passwordConfirmLbl.getFont().getStyle(), passwordConfirmLbl.getFont().getSize() + 1));

                //---- label3 ----
                label3.setText("(confirm)");
                label3.setHorizontalAlignment(SwingConstants.CENTER);
                label3.setFont(new Font("Arial", label3.getFont().getStyle(), label3.getFont().getSize() + 1));

                //---- passwordField ----
                passwordField.setFont(new Font("Arial", passwordField.getFont().getStyle(), passwordField.getFont().getSize() + 1));

                //---- passwordFieldConfirm ----
                passwordFieldConfirm.setFont(new Font("Arial", passwordFieldConfirm.getFont().getStyle(), passwordFieldConfirm.getFont().getSize() + 1));

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
                                    .addComponent(usernameField))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(label3, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(passwordConfirmLbl, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(passwordFieldConfirm, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(passwordLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)))
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
                                .addComponent(passwordLbl)
                                .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(14, 14, 14)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordConfirmLbl)
                                .addComponent(passwordFieldConfirm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(9, 9, 9)
                            .addComponent(label3)
                            .addContainerGap(47, Short.MAX_VALUE))
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

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel usernameLbl;
    private JTextField usernameField;
    private JLabel passwordLbl;
    private JLabel passwordConfirmLbl;
    private JLabel label3;
    private JPasswordField passwordField;
    private JPasswordField passwordFieldConfirm;
    private JPanel buttonBar;
    private JButton okButton;
    private JLabel titleLbl2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void run(JFrame sender, String userAccount) {
        changeUserPassword = new ChangeUserPassword(userAccount);
        CenterWindow.centerOnWindow(sender, changeUserPassword);
        changeUserPassword.setVisible(true);
    }
}
