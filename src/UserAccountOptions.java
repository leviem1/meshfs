import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;
/*
 * Created by JFormDesigner on Tue Feb 28 13:20:55 MST 2017
 */



/**
 * @author User #1
 */
public class UserAccountOptions extends JFrame {
    private static JFrame userAccountOptions;
    private String userAccount;
    private String serverAddress;
    private int port;
    private JFrame parentSender;

    public UserAccountOptions(String userAccount, String serverAddress, int port, JFrame parentSender) {
        this.userAccount = userAccount;
        this.serverAddress = serverAddress;
        this.port = port;
        this.parentSender = parentSender;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
        initComponents();
        frameListeners();
        titleLbl.setText("Welcome " + userAccount.substring(0,1).toUpperCase() + userAccount.substring(1) + "!");
        setTitle("MeshFS - Account Options");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        titleLbl2 = new JLabel();
        titleLbl = new JLabel();
        buttonBar = new JPanel();
        closeBtn = new JButton();
        button1 = new JButton();
        changePasswordBtn = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

            //---- titleLbl2 ----
            titleLbl2.setText("Account Control Center");
            titleLbl2.setFont(new Font("Arial", titleLbl2.getFont().getStyle(), titleLbl2.getFont().getSize() + 9));
            titleLbl2.setHorizontalAlignment(SwingConstants.CENTER);

            //---- titleLbl ----
            titleLbl.setText("Welcome <username>!");
            titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
            titleLbl.setHorizontalAlignment(SwingConstants.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                //---- closeBtn ----
                closeBtn.setText("Close");
                closeBtn.setFont(new Font("Arial", closeBtn.getFont().getStyle(), closeBtn.getFont().getSize() + 1));
                buttonBar.add(closeBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }

            //---- button1 ----
            button1.setText("Delete Account");
            button1.setFont(new Font("Arial", button1.getFont().getStyle(), button1.getFont().getSize() + 1));

            //---- changePasswordBtn ----
            changePasswordBtn.setText("Change Password");
            changePasswordBtn.setFont(new Font("Arial", changePasswordBtn.getFont().getStyle(), changePasswordBtn.getFont().getSize() + 1));

            GroupLayout dialogPaneLayout = new GroupLayout(dialogPane);
            dialogPane.setLayout(dialogPaneLayout);
            dialogPaneLayout.setHorizontalGroup(
                dialogPaneLayout.createParallelGroup()
                    .addGroup(dialogPaneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(dialogPaneLayout.createParallelGroup()
                            .addComponent(titleLbl2, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                            .addComponent(titleLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(dialogPaneLayout.createSequentialGroup()
                                .addComponent(buttonBar, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(dialogPaneLayout.createSequentialGroup()
                                .addComponent(changePasswordBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                                .addComponent(button1))))
            );
            dialogPaneLayout.setVerticalGroup(
                dialogPaneLayout.createParallelGroup()
                    .addGroup(dialogPaneLayout.createSequentialGroup()
                        .addComponent(titleLbl2)
                        .addGap(18, 18, 18)
                        .addComponent(titleLbl)
                        .addGap(18, 18, 18)
                        .addGroup(dialogPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(changePasswordBtn)
                            .addComponent(button1))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(buttonBar, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
            );
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(345, 245);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners() {
        changePasswordBtn.addActionListener(
                e -> {
                    ChangeUserPassword.run(userAccountOptions, userAccount, serverAddress, port, parentSender);
                    dispose();
                });
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JLabel titleLbl2;
    private JLabel titleLbl;
    private JPanel buttonBar;
    private JButton closeBtn;
    private JButton button1;
    private JButton changePasswordBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void run(JFrame sender, String userAccount, String serverAddress, int port) {
        userAccountOptions = new UserAccountOptions(userAccount, serverAddress, port, sender);
        CenterWindow.centerOnWindow(sender, userAccountOptions);
        userAccountOptions.setVisible(true);
    }
}
