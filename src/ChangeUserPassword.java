import edu.vt.middleware.password.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The ChangeUserPassword is a window responsible for letting users change their password.
 *
 * @author Mark Hedrick
 * @version 1.0.0
 */

class ChangeUserPassword extends JFrame {

    private static JFrame changeUserPassword;
    private final JFrame sender;
    private final String userAccount;
    private final String serverAddress;
    private final int port;
    private final boolean previousRunType;

    private ChangeUserPassword(String userAccount, String serverAddress, int port, JFrame sender, boolean previousRunType) {
        this.userAccount = userAccount;
        this.serverAddress = serverAddress;
        this.port = port;
        this.previousRunType = previousRunType;
        this.sender = sender;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
        JPanel dialogPane = new JPanel();
        JPanel contentPanel = new JPanel();
        JLabel usernameLbl = new JLabel();
        usernameField = new JTextField();
        JLabel newPasswordLbl = new JLabel();
        newPasswordField = new JPasswordField();
        JLabel currPasswordLbl = new JLabel();
        currPasswordField = new JPasswordField();
        JLabel newPasswordConfirmLbl = new JLabel();
        newPasswordFieldConfirm = new JPasswordField();
        validPasswordLbl = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        JLabel titleLbl2 = new JLabel();

        //======== this ========
        setTitle("MeshFS - Change User Password");
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

                //---- newPasswordField ----
                newPasswordField.setFont(new Font("Arial", newPasswordField.getFont().getStyle(), newPasswordField.getFont().getSize() + 1));

                //---- currPasswordLbl ----
                currPasswordLbl.setText("Current Password:");
                currPasswordLbl.setFont(new Font("Arial", currPasswordLbl.getFont().getStyle(), currPasswordLbl.getFont().getSize() + 1));

                //---- currPasswordField ----
                currPasswordField.setFont(new Font("Arial", currPasswordField.getFont().getStyle(), currPasswordField.getFont().getSize() + 1));

                //---- newPasswordConfirmLbl ----
                newPasswordConfirmLbl.setText("Confirm:");
                newPasswordConfirmLbl.setFont(new Font("Arial", newPasswordConfirmLbl.getFont().getStyle(), newPasswordConfirmLbl.getFont().getSize() + 1));

                //---- newPasswordFieldConfirm ----
                newPasswordFieldConfirm.setFont(new Font("Arial", newPasswordFieldConfirm.getFont().getStyle(), newPasswordFieldConfirm.getFont().getSize() + 1));

                //---- validPasswordLbl ----
                validPasswordLbl.setText("        ");
                validPasswordLbl.setHorizontalAlignment(SwingConstants.CENTER);
                validPasswordLbl.setFont(new Font("Arial", validPasswordLbl.getFont().getStyle(), validPasswordLbl.getFont().getSize() + 1));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(contentPanelLayout.createParallelGroup()
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                        .addComponent(currPasswordLbl)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(currPasswordField))
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                        .addComponent(usernameLbl)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(usernameField))
                                                .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                                                        .addComponent(newPasswordConfirmLbl)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(newPasswordFieldConfirm, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(validPasswordLbl, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                        .addComponent(newPasswordLbl)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(newPasswordField, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)))
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
                                        .addGap(11, 11, 11)
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(newPasswordLbl)
                                                .addComponent(newPasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(newPasswordConfirmLbl, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(validPasswordLbl)
                                                .addComponent(newPasswordFieldConfirm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap(12, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.EAST);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                okButton.setEnabled(false);
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
        okButton.addActionListener(e -> onOk());

        currPasswordField
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
                                checkFields();
                            }
                        });
        newPasswordField
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
                                checkFields();
                            }
                        });
        newPasswordFieldConfirm
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
                                checkFields();
                            }
                        });
    }

    private void onOk() {
        if (String.valueOf(newPasswordField.getPassword())
                .equals(String.valueOf(newPasswordFieldConfirm.getPassword()))) {
            try {
                FileClient.changePassword(
                        serverAddress,
                        port,
                        userAccount,
                        String.valueOf(currPasswordField.getPassword()),
                        String.valueOf(newPasswordFieldConfirm.getPassword()));
                JOptionPane.showMessageDialog(
                        changeUserPassword,
                        "Password Updated Successfully!",
                        "MeshFS - Success",
                        JOptionPane.INFORMATION_MESSAGE);
                ClientModeConfiguration.run(changeUserPassword, serverAddress, previousRunType);
                dispose();
                sender.dispose();
            } catch (IncorrectCredentialException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        changeUserPassword,
                        "Current Password is Incorrect!",
                        "MeshFS - Failure",
                        JOptionPane.ERROR_MESSAGE);
                currPasswordField.setText("");
                newPasswordField.setText("");
                newPasswordFieldConfirm.setText("");
            } catch (IOException | MalformedRequestException e) {
                e.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(
                    changeUserPassword, "Password Mismatch", "MeshFS - Error!", JOptionPane.ERROR_MESSAGE);
            newPasswordField.setText("");
            newPasswordFieldConfirm.setText("");
        }
    }

    private void checkFields() {
        if (String.valueOf(newPasswordFieldConfirm.getPassword())
                .equals(String.valueOf(newPasswordField.getPassword()))) {
            if (checkPasswordStrength(newPasswordFieldConfirm.getPassword())) {
                validPasswordLbl.setText("valid");
                okButton.setEnabled(true);
                buttonBar.getRootPane().setDefaultButton(okButton);
            } else {
                validPasswordLbl.setText("invalid");
                okButton.setEnabled(false);
            }
        }
    }

    private boolean checkPasswordStrength(char[] password) {
        LengthRule lengthRule = new LengthRule(8, 32);
        WhitespaceRule whitespaceRule = new WhitespaceRule();
        CharacterCharacteristicsRule characterCharacteristicsRule = new CharacterCharacteristicsRule();
        characterCharacteristicsRule.getRules().add(new DigitCharacterRule(1));
        characterCharacteristicsRule.getRules().add(new NonAlphanumericCharacterRule(1));
        characterCharacteristicsRule.getRules().add(new UppercaseCharacterRule(1));
        characterCharacteristicsRule.getRules().add(new LowercaseCharacterRule(1));
        characterCharacteristicsRule.setNumberOfCharacteristics(4);
        AlphabeticalSequenceRule alphabeticalSequenceRule = new AlphabeticalSequenceRule();
        QwertySequenceRule qwertySequenceRule = new QwertySequenceRule();
        RepeatCharacterRegexRule repeatCharacterRegexRule = new RepeatCharacterRegexRule(4);
        java.util.List<Rule> passwordRulesList = new ArrayList<>();
        passwordRulesList.add(lengthRule);
        passwordRulesList.add(whitespaceRule);
        passwordRulesList.add(alphabeticalSequenceRule);
        passwordRulesList.add(qwertySequenceRule);
        passwordRulesList.add(repeatCharacterRegexRule);
        passwordRulesList.add(characterCharacteristicsRule);

        PasswordValidator passwordValidator = new PasswordValidator(passwordRulesList);
        PasswordData passwordData = new PasswordData(new Password(String.valueOf(password)));

        RuleResult ruleResult = passwordValidator.validate(passwordData);

        return ruleResult.isValid();
    }

    /**
     * The run method is responsible for spawning the window when called.
     *
     * @param sender          The window that called this one
     * @param userAccount     The user account that called this window
     * @param serverAddress   The address used to connect to the master server
     * @param port            The port number used to connect to the master server
     * @param parentSender    The parent of the parent that called this window
     * @param previousRunType This value is used to toggle how the window is displayed
     */

    public static void run(
            JFrame sender, String userAccount, String serverAddress, int port, JFrame parentSender, boolean previousRunType) {
        changeUserPassword = new ChangeUserPassword(userAccount, serverAddress, port, parentSender, previousRunType);
        CenterWindow.centerOnWindow(sender, changeUserPassword);
        changeUserPassword.setVisible(true);
    }

    private JTextField usernameField;
    private JPasswordField newPasswordField;
    private JPasswordField currPasswordField;
    private JPasswordField newPasswordFieldConfirm;
    private JLabel validPasswordLbl;
    private JPanel buttonBar;
    private JButton okButton;
    //GEN-END:variables

}
