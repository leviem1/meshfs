import com.jgoodies.forms.factories.DefaultComponentFactory;
import org.apache.commons.lang3.RandomStringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import static java.lang.Math.toIntExact;

/**
 * @author Mark Hedrick
 */
@SuppressWarnings("unchecked")
class ServerModeConfiguration extends JFrame {

    private static JFrame serverModeConfiguration;
    private final DefaultListModel model;
    private final ArrayList<UserAccounts> accountsEnc;
    private final ArrayList<UserAccounts> accountsPlain;
    private ArrayList<UserAccounts> accountsImported;
    private String out;

    //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JTabbedPane serverSettingPane;
    private JPanel networkTab;
    private JLabel serverNetworkInterfaceLbl;
    private JTextField masterServerField;
    private JLabel serverPortLbl;
    private JFormattedTextField serverPortField;
    private JCheckBox isMasterBox;
    private JLabel serverThreadsLbl;
    private JFormattedTextField serverThreadsField;
    private JLabel serverTimeoutLbl;
    private JFormattedTextField serverTimeoutField;
    private JLabel timeUnitLbl;
    private JFormattedTextField numStripesField;
    private JLabel stripesLbl;
    private JFormattedTextField numStripeCopiesField;
    private JLabel stripedCopiesLbl;
    private JFormattedTextField numWholeField;
    private JLabel wholeCopiesLbl;
    private JButton importConfigBtn;
    private JButton backupConfigBtn;
    private JLabel importDescriptionLbl;
    private JLabel exportDescriptionLbl;
    private JComponent configurationToolsLblSep;
    private JLabel helpIcon;
    private JPanel storageTab;
    private JLabel repositoryLbl;
    private JTextField repoPathField;
    private JLabel minFreeSpaceLbl;
    private JFormattedTextField minSpaceField;
    private JLabel label1;
    private JSlider spaceSldr;
    private JLabel freeSpaceLbl;
    private JButton browseBtn;
    private JPanel userAccounts;
    private JTextArea textArea1;
    private JLabel usernameLbl;
    private JTextField usernameValueField;
    private JLabel passwordLbl;
    private JPasswordField passwordValueField;
    private JLabel groupLbl;
    private JComboBox groupBox;
    private JButton groupManagerBtn;
    private JComboBox accountTypeBox;
    private JLabel accountTypeLbl;
    private JCheckBox allowGuestBox;
    private JButton removeUserBtn;
    private JButton submitBtn;
    private JScrollPane scrollPane2;
    private JList userAccountDataList;
    private JPanel buttonBar;
    private JButton backBtn;
    private JButton resetConfigBtn;
    private JButton okButton;
    private JLabel titleLbl;
    //GEN-END:variables

    private ServerModeConfiguration() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        model = new DefaultListModel();
        accountsEnc = new ArrayList<>();
        accountsPlain = new ArrayList<>();
        accountsImported = new ArrayList<>();

        initComponents();
        frameListeners();

        submitBtn.setEnabled(false);
        removeUserBtn.setEnabled(false);
        backupConfigBtn.setEnabled(false);
        okButton.setEnabled(false);
        freeSpaceLbl.setText("(Free Space: " + (Reporting.getSystemStorage() / 1073741824L) + " GB)");
        spaceSldr.setMaximum(toIntExact(Reporting.getSystemStorage() / 1073741824L) - 10);
        spaceSldr.setMinimum(0);
        helpIcon.setIcon(new ImageIcon(MeshFS.class.getResource("help_icon.png")));
        helpIcon.setToolTipText(
                "<html>Selecting this option will designate<br>this computer as the master MeshFS<br>server</html>");
        repoPathField.setEnabled(false);
        userAccountDataList.setBorder(new EmptyBorder(10, 10, 10, 10));
        accountTypeBox.addItem("user");
        accountTypeBox.addItem("admin");

    }

    static void writeConfig(Properties properties) {
        ConfigParser.write(properties);
    }

    public static void run(JFrame sender) {
        serverModeConfiguration = new ServerModeConfiguration();
        CenterWindow.centerOnWindow(sender, serverModeConfiguration);
        serverModeConfiguration.setVisible(true);
    }

    private void initComponents() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String pw = RandomStringUtils.random(8, characters);
        String value = "<html>Username: admin <br>Password: " + pw + "<br>Group: none<br>Type: admin</html>";
        model.add(0, value);
        //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        dialogPane = new JPanel();
        serverSettingPane = new JTabbedPane();
        networkTab = new JPanel();
        serverNetworkInterfaceLbl = new JLabel();
        masterServerField = new JTextField();
        serverPortLbl = new JLabel();
        serverPortField = new JFormattedTextField(numberFormat);
        isMasterBox = new JCheckBox();
        serverThreadsLbl = new JLabel();
        serverThreadsField = new JFormattedTextField(numberFormat);
        serverTimeoutLbl = new JLabel();
        serverTimeoutField = new JFormattedTextField(numberFormat);
        timeUnitLbl = new JLabel();
        numStripesField = new JFormattedTextField(numberFormat);
        stripesLbl = new JLabel();
        numStripeCopiesField = new JFormattedTextField(numberFormat);
        stripedCopiesLbl = new JLabel();
        numWholeField = new JFormattedTextField(numberFormat);
        wholeCopiesLbl = new JLabel();
        importConfigBtn = new JButton();
        backupConfigBtn = new JButton();
        importDescriptionLbl = new JLabel();
        exportDescriptionLbl = new JLabel();
        configurationToolsLblSep = compFactory.createSeparator("Configuration Tools");
        helpIcon = new JLabel();
        storageTab = new JPanel();
        repositoryLbl = new JLabel();
        repoPathField = new JTextField(System.getProperty("user.dir") + File.separator + "repo");
        minFreeSpaceLbl = new JLabel();
        minSpaceField = new JFormattedTextField(numberFormat);
        label1 = new JLabel();
        spaceSldr = new JSlider();
        freeSpaceLbl = new JLabel();
        browseBtn = new JButton();
        userAccounts = new JPanel();
        textArea1 = new JTextArea();
        usernameLbl = new JLabel();
        usernameValueField = new JTextField();
        passwordLbl = new JLabel();
        passwordValueField = new JPasswordField();
        groupLbl = new JLabel();
        groupBox = new JComboBox();
        groupManagerBtn = new JButton();
        accountTypeBox = new JComboBox();
        accountTypeLbl = new JLabel();
        allowGuestBox = new JCheckBox();
        removeUserBtn = new JButton();
        submitBtn = new JButton();
        scrollPane2 = new JScrollPane();
        userAccountDataList = new JList(model);
        buttonBar = new JPanel();
        backBtn = new JButton();
        resetConfigBtn = new JButton();
        okButton = new JButton();
        titleLbl = new JLabel();

        //======== this ========
        setTitle("MeshFS - Server Configuration");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== serverSettingPane ========
            {
                serverSettingPane.setFont(new Font("Arial", serverSettingPane.getFont().getStyle() & ~Font.BOLD, serverSettingPane.getFont().getSize() + 1));

                //======== networkTab ========
                {
                    networkTab.setBackground(new Color(229, 229, 229));

                    //---- serverNetworkInterfaceLbl ----
                    serverNetworkInterfaceLbl.setText("Master Server:");
                    serverNetworkInterfaceLbl.setFont(new Font("Arial", serverNetworkInterfaceLbl.getFont().getStyle() & ~Font.BOLD, serverNetworkInterfaceLbl.getFont().getSize() + 1));

                    //---- masterServerField ----
                    masterServerField.setText("127.0.0.1");
                    masterServerField.setFont(new Font("Arial", masterServerField.getFont().getStyle() & ~Font.BOLD, masterServerField.getFont().getSize() + 1));
                    masterServerField.setEnabled(false);

                    //---- serverPortLbl ----
                    serverPortLbl.setText("Network Port:");
                    serverPortLbl.setFont(new Font("Arial", serverPortLbl.getFont().getStyle() & ~Font.BOLD, serverPortLbl.getFont().getSize() + 1));

                    //---- serverPortField ----
                    serverPortField.setText("5704");
                    serverPortField.setFont(new Font("Arial", serverPortField.getFont().getStyle() & ~Font.BOLD, serverPortField.getFont().getSize() + 1));

                    //---- isMasterBox ----
                    isMasterBox.setText("Is Master:");
                    isMasterBox.setFont(new Font("Arial", isMasterBox.getFont().getStyle() & ~Font.BOLD, isMasterBox.getFont().getSize() + 1));
                    isMasterBox.setToolTipText("Check this box if this computer is the master server");
                    isMasterBox.setHorizontalTextPosition(SwingConstants.LEADING);
                    isMasterBox.setSelected(true);

                    //---- serverThreadsLbl ----
                    serverThreadsLbl.setText("Network Threads:");
                    serverThreadsLbl.setFont(new Font("Arial", serverThreadsLbl.getFont().getStyle() & ~Font.BOLD, serverThreadsLbl.getFont().getSize() + 1));

                    //---- serverThreadsField ----
                    serverThreadsField.setText("16");
                    serverThreadsField.setFont(new Font("Arial", serverThreadsField.getFont().getStyle() & ~Font.BOLD, serverThreadsField.getFont().getSize() + 1));

                    //---- serverTimeoutLbl ----
                    serverTimeoutLbl.setText("Server Timeout:");
                    serverTimeoutLbl.setFont(new Font("Arial", serverTimeoutLbl.getFont().getStyle() & ~Font.BOLD, serverTimeoutLbl.getFont().getSize() + 1));

                    //---- serverTimeoutField ----
                    serverTimeoutField.setText("5");
                    serverTimeoutField.setFont(new Font("Arial", serverTimeoutField.getFont().getStyle() & ~Font.BOLD, serverTimeoutField.getFont().getSize() + 1));
                    serverTimeoutField.setColumns(2);

                    //---- timeUnitLbl ----
                    timeUnitLbl.setText("seconds");
                    timeUnitLbl.setFont(new Font("Arial", timeUnitLbl.getFont().getStyle() & ~Font.BOLD, timeUnitLbl.getFont().getSize() - 1));

                    //---- numStripesField ----
                    numStripesField.setText("3");
                    numStripesField.setFont(new Font("Arial", numStripesField.getFont().getStyle() & ~Font.BOLD, numStripesField.getFont().getSize() + 1));

                    //---- stripesLbl ----
                    stripesLbl.setText("Number of Stripes:");
                    stripesLbl.setFont(new Font("Arial", stripesLbl.getFont().getStyle() & ~Font.BOLD, stripesLbl.getFont().getSize() + 1));

                    //---- numStripeCopiesField ----
                    numStripeCopiesField.setText("2");
                    numStripeCopiesField.setFont(new Font("Arial", numStripeCopiesField.getFont().getStyle() & ~Font.BOLD, numStripeCopiesField.getFont().getSize() + 1));

                    //---- stripedCopiesLbl ----
                    stripedCopiesLbl.setText("Number of Striped Copies:");
                    stripedCopiesLbl.setFont(new Font("Arial", stripedCopiesLbl.getFont().getStyle() & ~Font.BOLD, stripedCopiesLbl.getFont().getSize() + 1));

                    //---- numWholeField ----
                    numWholeField.setText("2");
                    numWholeField.setFont(new Font("Arial", numWholeField.getFont().getStyle() & ~Font.BOLD, numWholeField.getFont().getSize() + 1));

                    //---- wholeCopiesLbl ----
                    wholeCopiesLbl.setText("Number of Whole Copies:");
                    wholeCopiesLbl.setFont(new Font("Arial", wholeCopiesLbl.getFont().getStyle() & ~Font.BOLD, wholeCopiesLbl.getFont().getSize() + 1));

                    //---- importConfigBtn ----
                    importConfigBtn.setText("Import Existing Configuration");
                    importConfigBtn.setFont(new Font("Arial", importConfigBtn.getFont().getStyle() & ~Font.BOLD, importConfigBtn.getFont().getSize() + 1));

                    //---- backupConfigBtn ----
                    backupConfigBtn.setText("Backup Current Configuration");
                    backupConfigBtn.setFont(new Font("Arial", backupConfigBtn.getFont().getStyle() & ~Font.BOLD, backupConfigBtn.getFont().getSize() + 1));

                    //---- importDescriptionLbl ----
                    importDescriptionLbl.setText("(Select an existing configuration file)");
                    importDescriptionLbl.setFont(new Font("Arial", importDescriptionLbl.getFont().getStyle(), importDescriptionLbl.getFont().getSize() - 1));

                    //---- exportDescriptionLbl ----
                    exportDescriptionLbl.setText("(Backup current configuration settings)");
                    exportDescriptionLbl.setFont(new Font("Arial", exportDescriptionLbl.getFont().getStyle(), exportDescriptionLbl.getFont().getSize() - 1));

                    //---- configurationToolsLblSep ----
                    configurationToolsLblSep.setFont(new Font("Arial", Font.PLAIN, configurationToolsLblSep.getFont().getSize() + 1));

                    GroupLayout networkTabLayout = new GroupLayout(networkTab);
                    networkTab.setLayout(networkTabLayout);
                    networkTabLayout.setHorizontalGroup(
                            networkTabLayout.createParallelGroup()
                                    .addGroup(networkTabLayout.createSequentialGroup()
                                            .addGroup(networkTabLayout.createParallelGroup()
                                                    .addGroup(networkTabLayout.createSequentialGroup()
                                                            .addContainerGap()
                                                            .addGroup(networkTabLayout.createParallelGroup()
                                                                    .addComponent(configurationToolsLblSep, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                    .addGroup(networkTabLayout.createSequentialGroup()
                                                                            .addComponent(serverNetworkInterfaceLbl)
                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                            .addComponent(masterServerField, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE)
                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                            .addComponent(isMasterBox)
                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                            .addComponent(helpIcon, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                                                            .addGap(0, 135, Short.MAX_VALUE))
                                                                    .addGroup(networkTabLayout.createSequentialGroup()
                                                                            .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                                                    .addGroup(GroupLayout.Alignment.LEADING, networkTabLayout.createSequentialGroup()
                                                                                            .addComponent(serverPortLbl)
                                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                            .addComponent(serverPortField))
                                                                                    .addGroup(networkTabLayout.createParallelGroup()
                                                                                            .addGroup(networkTabLayout.createSequentialGroup()
                                                                                                    .addComponent(serverThreadsLbl)
                                                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                                    .addComponent(serverThreadsField, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE))
                                                                                            .addGroup(networkTabLayout.createSequentialGroup()
                                                                                                    .addComponent(serverTimeoutLbl)
                                                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                                    .addComponent(serverTimeoutField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                                    .addComponent(timeUnitLbl))))
                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                                                                            .addGroup(networkTabLayout.createParallelGroup()
                                                                                    .addGroup(GroupLayout.Alignment.TRAILING, networkTabLayout.createSequentialGroup()
                                                                                            .addComponent(stripesLbl)
                                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                            .addComponent(numStripesField, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
                                                                                    .addGroup(GroupLayout.Alignment.TRAILING, networkTabLayout.createSequentialGroup()
                                                                                            .addComponent(stripedCopiesLbl)
                                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                            .addComponent(numStripeCopiesField, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))
                                                                                    .addGroup(GroupLayout.Alignment.TRAILING, networkTabLayout.createSequentialGroup()
                                                                                            .addComponent(wholeCopiesLbl)
                                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                            .addComponent(numWholeField, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE))))))
                                                    .addGroup(networkTabLayout.createSequentialGroup()
                                                            .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                    .addComponent(backupConfigBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                    .addComponent(importConfigBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addGroup(networkTabLayout.createParallelGroup()
                                                                    .addComponent(importDescriptionLbl)
                                                                    .addComponent(exportDescriptionLbl))
                                                            .addGap(0, 0, Short.MAX_VALUE)))
                                            .addContainerGap())
                    );
                    networkTabLayout.setVerticalGroup(
                            networkTabLayout.createParallelGroup()
                                    .addGroup(networkTabLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(serverNetworkInterfaceLbl)
                                                            .addComponent(masterServerField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(isMasterBox))
                                                    .addComponent(helpIcon, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(serverPortLbl)
                                                    .addComponent(numStripesField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(stripesLbl)
                                                    .addComponent(serverPortField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(serverThreadsLbl)
                                                    .addComponent(serverThreadsField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(numStripeCopiesField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(stripedCopiesLbl))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(serverTimeoutLbl)
                                                    .addComponent(timeUnitLbl)
                                                    .addComponent(serverTimeoutField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(numWholeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(wholeCopiesLbl))
                                            .addGap(18, 18, 18)
                                            .addComponent(configurationToolsLblSep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(importDescriptionLbl)
                                                    .addComponent(importConfigBtn))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(backupConfigBtn)
                                                    .addComponent(exportDescriptionLbl))
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                }
                serverSettingPane.addTab("Network", networkTab);

                //======== storageTab ========
                {
                    storageTab.setBackground(new Color(229, 229, 229));

                    //---- repositoryLbl ----
                    repositoryLbl.setText("Repository:");
                    repositoryLbl.setFont(new Font("Arial", repositoryLbl.getFont().getStyle(), repositoryLbl.getFont().getSize() + 1));

                    //---- repoPathField ----
                    repoPathField.setFont(new Font("Arial", repoPathField.getFont().getStyle(), repoPathField.getFont().getSize() + 1));

                    //---- minFreeSpaceLbl ----
                    minFreeSpaceLbl.setText("Reserved Space:");
                    minFreeSpaceLbl.setFont(new Font("Arial", minFreeSpaceLbl.getFont().getStyle(), minFreeSpaceLbl.getFont().getSize() + 1));

                    //---- minSpaceField ----
                    minSpaceField.setText("0");
                    minSpaceField.setFont(new Font("Arial", minSpaceField.getFont().getStyle(), minSpaceField.getFont().getSize() + 1));
                    minSpaceField.setEnabled(false);

                    //---- label1 ----
                    label1.setText("GB");
                    label1.setFont(new Font("Arial", label1.getFont().getStyle(), label1.getFont().getSize() + 1));

                    //---- spaceSldr ----
                    spaceSldr.setEnabled(false);

                    //---- freeSpaceLbl ----
                    freeSpaceLbl.setText("(free space)");
                    freeSpaceLbl.setFont(new Font("Arial", freeSpaceLbl.getFont().getStyle(), freeSpaceLbl.getFont().getSize() + 1));

                    //---- browseBtn ----
                    browseBtn.setText("Browse...");
                    browseBtn.setFont(new Font("Arial", browseBtn.getFont().getStyle(), browseBtn.getFont().getSize() + 1));

                    GroupLayout storageTabLayout = new GroupLayout(storageTab);
                    storageTab.setLayout(storageTabLayout);
                    storageTabLayout.setHorizontalGroup(
                            storageTabLayout.createParallelGroup()
                                    .addGroup(storageTabLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(storageTabLayout.createParallelGroup()
                                                    .addGroup(storageTabLayout.createSequentialGroup()
                                                            .addComponent(minFreeSpaceLbl)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                            .addComponent(minSpaceField, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(label1)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(spaceSldr, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                                                    .addGroup(storageTabLayout.createSequentialGroup()
                                                            .addComponent(freeSpaceLbl, GroupLayout.PREFERRED_SIZE, 351, GroupLayout.PREFERRED_SIZE)
                                                            .addGap(0, 257, Short.MAX_VALUE))
                                                    .addGroup(storageTabLayout.createSequentialGroup()
                                                            .addComponent(repositoryLbl)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(repoPathField, GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(browseBtn)))
                                            .addContainerGap())
                    );
                    storageTabLayout.setVerticalGroup(
                            storageTabLayout.createParallelGroup()
                                    .addGroup(storageTabLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(storageTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(repositoryLbl)
                                                    .addComponent(browseBtn)
                                                    .addComponent(repoPathField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(storageTabLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addGroup(storageTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(minFreeSpaceLbl)
                                                            .addComponent(minSpaceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(label1))
                                                    .addComponent(spaceSldr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(freeSpaceLbl)
                                            .addContainerGap(151, Short.MAX_VALUE))
                    );
                }
                serverSettingPane.addTab("Storage", storageTab);

                //======== userAccounts ========
                {
                    userAccounts.setBackground(new Color(229, 229, 229));

                    //---- usernameLbl ----
                    usernameLbl.setText("Username:");
                    usernameLbl.setFont(new Font("Arial", usernameLbl.getFont().getStyle(), usernameLbl.getFont().getSize() + 1));

                    //---- usernameValueField ----
                    usernameValueField.setFont(new Font("Arial", usernameValueField.getFont().getStyle(), usernameValueField.getFont().getSize() + 1));

                    //---- passwordLbl ----
                    passwordLbl.setText("Password:");
                    passwordLbl.setFont(new Font("Arial", passwordLbl.getFont().getStyle(), passwordLbl.getFont().getSize() + 1));

                    //---- passwordValueField ----
                    passwordValueField.setFont(new Font("Arial", passwordValueField.getFont().getStyle(), passwordValueField.getFont().getSize() + 1));

                    //---- groupLbl ----
                    groupLbl.setText("Group:");
                    groupLbl.setFont(new Font("Arial", groupLbl.getFont().getStyle(), groupLbl.getFont().getSize() + 1));

                    //---- groupManagerBtn ----
                    groupManagerBtn.setText("Groups...");
                    groupManagerBtn.setFont(new Font("Arial", groupManagerBtn.getFont().getStyle(), groupManagerBtn.getFont().getSize() + 1));

                    //---- accountTypeLbl ----
                    accountTypeLbl.setText("Account Type:");
                    accountTypeLbl.setFont(new Font("Arial", accountTypeLbl.getFont().getStyle(), accountTypeLbl.getFont().getSize() + 1));

                    //---- allowGuestBox ----
                    allowGuestBox.setText("Enable Guest");
                    allowGuestBox.setHorizontalTextPosition(SwingConstants.LEFT);
                    allowGuestBox.setFont(new Font("Arial", allowGuestBox.getFont().getStyle(), allowGuestBox.getFont().getSize() + 1));
                    allowGuestBox.setSelected(true);

                    //---- removeUserBtn ----
                    removeUserBtn.setText("Delete User");
                    removeUserBtn.setFont(new Font("Arial", removeUserBtn.getFont().getStyle(), removeUserBtn.getFont().getSize() + 1));

                    //---- submitBtn ----
                    submitBtn.setText("Add User");
                    submitBtn.setFont(new Font("Arial", submitBtn.getFont().getStyle(), submitBtn.getFont().getSize() + 1));

                    //======== scrollPane2 ========
                    {

                        //---- userAccountDataList ----
                        userAccountDataList.setFont(new Font("Arial", userAccountDataList.getFont().getStyle(), userAccountDataList.getFont().getSize() + 1));
                        scrollPane2.setViewportView(userAccountDataList);
                    }

                    GroupLayout userAccountsLayout = new GroupLayout(userAccounts);
                    userAccounts.setLayout(userAccountsLayout);
                    userAccountsLayout.setHorizontalGroup(
                            userAccountsLayout.createParallelGroup()
                                    .addGroup(userAccountsLayout.createSequentialGroup()
                                            .addGroup(userAccountsLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(groupManagerBtn, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(GroupLayout.Alignment.TRAILING, userAccountsLayout.createParallelGroup()
                                                            .addGroup(userAccountsLayout.createSequentialGroup()
                                                                    .addGap(238, 238, 238)
                                                                    .addComponent(textArea1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(userAccountsLayout.createSequentialGroup()
                                                                    .addContainerGap()
                                                                    .addComponent(usernameLbl)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(usernameValueField, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(userAccountsLayout.createSequentialGroup()
                                                                    .addContainerGap()
                                                                    .addComponent(passwordLbl)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(passwordValueField, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(userAccountsLayout.createSequentialGroup()
                                                                    .addContainerGap()
                                                                    .addComponent(groupLbl, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(groupBox, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)))
                                                    .addGroup(userAccountsLayout.createSequentialGroup()
                                                            .addContainerGap()
                                                            .addGroup(userAccountsLayout.createParallelGroup()
                                                                    .addGroup(userAccountsLayout.createSequentialGroup()
                                                                            .addComponent(accountTypeLbl)
                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                            .addComponent(accountTypeBox))
                                                                    .addGroup(userAccountsLayout.createSequentialGroup()
                                                                            .addComponent(allowGuestBox)
                                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                            .addComponent(submitBtn)))))
                                            .addGap(12, 12, 12)
                                            .addGroup(userAccountsLayout.createParallelGroup()
                                                    .addGroup(userAccountsLayout.createSequentialGroup()
                                                            .addGap(0, 0, Short.MAX_VALUE)
                                                            .addComponent(removeUserBtn))
                                                    .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
                                            .addContainerGap())
                    );
                    userAccountsLayout.setVerticalGroup(
                            userAccountsLayout.createParallelGroup()
                                    .addGroup(userAccountsLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(userAccountsLayout.createParallelGroup()
                                                    .addGroup(userAccountsLayout.createSequentialGroup()
                                                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
                                                            .addGap(12, 12, 12)
                                                            .addGroup(userAccountsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(removeUserBtn)
                                                                    .addComponent(allowGuestBox)
                                                                    .addComponent(submitBtn)))
                                                    .addGroup(userAccountsLayout.createSequentialGroup()
                                                            .addGroup(userAccountsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(usernameLbl)
                                                                    .addComponent(usernameValueField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addGroup(userAccountsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(passwordLbl)
                                                                    .addComponent(passwordValueField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addGroup(userAccountsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(groupLbl)
                                                                    .addComponent(groupBox, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(groupManagerBtn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addGroup(userAccountsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(accountTypeLbl, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(accountTypeBox, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))))
                                            .addGap(55, 55, 55)
                                            .addComponent(textArea1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE))
                    );
                }
                serverSettingPane.addTab("User Accounts", userAccounts);
            }
            dialogPane.add(serverSettingPane, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 309, 0, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{0.0, 1.0, 0.0, 0.0};

                //---- backBtn ----
                backBtn.setText("Back");
                backBtn.setFont(new Font("Arial", backBtn.getFont().getStyle(), backBtn.getFont().getSize() + 1));
                buttonBar.add(backBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- resetConfigBtn ----
                resetConfigBtn.setText("Reset to Defaults");
                resetConfigBtn.setFont(new Font("Arial", resetConfigBtn.getFont().getStyle() & ~Font.BOLD, resetConfigBtn.getFont().getSize() + 1));
                buttonBar.add(resetConfigBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("Continue...");
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle() & ~Font.BOLD, okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- titleLbl ----
            titleLbl.setText("Server Settings");
            titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
            titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPane.add(titleLbl, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(667, 415);
        setLocationRelativeTo(getOwner());
        //GEN-END:initComponents
    }

    private void frameListeners() {
        browseBtn.addActionListener(
                e -> {
                    final JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                    fileChooser.setDialogTitle("Choose Repository");
                    int rVal = fileChooser.showOpenDialog(null);
                    if (rVal == JFileChooser.APPROVE_OPTION) {
                        repoPathField.setText(fileChooser.getSelectedFile().toString());
                        repoPathField.setToolTipText(fileChooser.getSelectedFile().toString());
                    }
                });
        okButton.addActionListener(e -> onOk());
        importConfigBtn.addActionListener(e -> importConfig());
        backupConfigBtn.addActionListener(e -> backupConfig());
        masterServerField
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
                                if (masterServerField.getText().isEmpty()) {
                                    backupConfigBtn.setEnabled(false);
                                    okButton.setEnabled(false);
                                } else {
                                    backupConfigBtn.setEnabled(true);
                                    okButton.setEnabled(true);
                                }
                            }
                        });
        spaceSldr.addChangeListener(ce -> minSpaceField.setText(String.valueOf(spaceSldr.getValue())));
        minSpaceField.addKeyListener(
                new KeyAdapter() {
                    public void keyReleased(KeyEvent ke) {
                        spaceSldr.setValue(0);
                        spaceSldr.setValue(Integer.parseInt(minSpaceField.getText()));
                    }
                });
        masterServerField
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
                                if (masterServerField.getText().equals("127.0.0.1")) {
                                    return;
                                }
                                if (masterServerField.getText().isEmpty()) {
                                    backupConfigBtn.setEnabled(false);
                                    okButton.setEnabled(false);
                                } else {
                                    backupConfigBtn.setEnabled(true);
                                    okButton.setEnabled(true);
                                }
                            }
                        });
        serverPortField
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
                                if (!(checkFields(serverPortField))) {
                                    backupConfigBtn.setEnabled(false);
                                    okButton.setEnabled(false);
                                } else {
                                    backupConfigBtn.setEnabled(true);
                                    okButton.setEnabled(true);
                                }
                            }
                        });
        serverThreadsField
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
                                if (!(checkFields(serverThreadsField))) {
                                    backupConfigBtn.setEnabled(false);
                                    okButton.setEnabled(false);
                                } else {
                                    backupConfigBtn.setEnabled(true);
                                    okButton.setEnabled(true);
                                }
                            }
                        });
        serverTimeoutField
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
                                if (!(checkFields(serverTimeoutField))) {
                                    backupConfigBtn.setEnabled(false);
                                    okButton.setEnabled(false);
                                } else {
                                    backupConfigBtn.setEnabled(true);
                                    okButton.setEnabled(true);
                                }
                            }
                        });
        numStripesField
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
                                if (!(checkFields(numStripesField))) {
                                    backupConfigBtn.setEnabled(false);
                                    okButton.setEnabled(false);
                                } else {
                                    backupConfigBtn.setEnabled(true);
                                    okButton.setEnabled(true);
                                }
                            }
                        });
        numStripeCopiesField
                .getDocument()
                .addDocumentListener(serverModeConfigRefreshListener);
        numWholeField
                .getDocument()
                .addDocumentListener(serverModeConfigRefreshListener);
        repoPathField
                .getDocument()
                .addDocumentListener(serverModeConfigRefreshListener);
        minSpaceField
                .getDocument()
                .addDocumentListener(serverModeConfigRefreshListener);

        resetConfigBtn.addActionListener(e -> resetConfig());
        submitBtn.addActionListener(
                e -> {
                    String password = String.valueOf(passwordValueField.getPassword());
                    String group = "none";
                    if (groupBox.getSelectedItem() != null) {
                        group = groupBox.getSelectedItem().toString();
                    }
                    String value =
                            "<html>Username: "
                                    + usernameValueField.getText()
                                    + "<br>Password: "
                                    + password
                                    + "<br>Group: "
                                    + group
                                    + "<br>Type: "
                                    + accountTypeBox.getSelectedItem().toString()
                                    + "</html>";
                    int index = userAccountDataList.getModel().getSize();
                    if (usernameValueField.getText().equals("guest")) {
                        JOptionPane.showMessageDialog(
                                serverModeConfiguration,
                                "The username \"guest\" is reserved!",
                                "MeshFS - Error",
                                JOptionPane.ERROR_MESSAGE);
                        usernameValueField.setText("");
                        passwordValueField.setText("");
                        usernameValueField.requestFocus();
                        return;
                    }
                    if (userAccountDataList.getModel().getSize() > 0) {
                        for (int i = 0; i < userAccountDataList.getModel().getSize(); i++) {
                            if (userAccountDataList
                                    .getModel()
                                    .getElementAt(i)
                                    .toString()
                                    .substring(
                                            userAccountDataList.getModel().getElementAt(i).toString().indexOf("Username:")
                                                    + 10,
                                            userAccountDataList.getModel().getElementAt(i).toString().indexOf("<br>"))
                                    .equals(usernameValueField.getText())) {
                                usernameValueField.setText("");
                                passwordValueField.setText("");
                                JOptionPane.showMessageDialog(
                                        serverModeConfiguration,
                                        "Duplicate users cannot be created!",
                                        "MeshFS - Error",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            } else if (!(userAccountDataList
                                    .getModel()
                                    .getElementAt(i)
                                    .toString()
                                    .equals(value))) {

                                model.add(index, value);
                                usernameValueField.setText("");
                                passwordValueField.setText("");
                                return;
                            }
                        }
                    } else {

                        model.add(index, value);
                        usernameValueField.setText("");
                        passwordValueField.setText("");
                    }
                });
        removeUserBtn.addActionListener(
                e -> {
                    int index = userAccountDataList.getSelectedIndex();
                    if (index > -1) {
                        model.remove(index);
                    }

                    userAccountDataList.setModel(model);
                });
        passwordValueField
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
                                if (!(String.valueOf(passwordValueField.getPassword())).isEmpty()) {
                                    submitBtn.setEnabled(true);
                                    userAccounts.getRootPane().setDefaultButton(submitBtn);
                                } else {
                                    submitBtn.setEnabled(false);
                                    userAccounts.getRootPane().setDefaultButton(submitBtn);
                                }
                            }
                        });
        userAccountDataList.addListSelectionListener(
                listSelectionEvent -> {
                    if (userAccountDataList.isSelectionEmpty()) {
                        removeUserBtn.setEnabled(false);
                    } else {
                        removeUserBtn.setEnabled(true);
                    }
                });
        isMasterBox.addActionListener(
                actionEvent -> {
                    if (isMasterBox.isSelected()) {
                        setConfigMode("full");
                    } else {
                        setConfigMode("easy");
                    }
                });
        backBtn.addActionListener(
                actionEvent -> {
                    InitialConfiguration.run(serverModeConfiguration);
                    dispose();
                });
        groupManagerBtn.addActionListener(
                actionEvent -> GroupManager.run(serverModeConfiguration, groupBox));

    }

    DocumentListener serverModeConfigRefreshListener = new DocumentListener() {

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
            if (!(checkFields(minSpaceField))) {
                backupConfigBtn.setEnabled(false);
                okButton.setEnabled(false);
            } else {
                backupConfigBtn.setEnabled(true);
                okButton.setEnabled(true);
            }
        }
    };

    private void onOk() {

        try {
            File repoDirectory = new File(String.valueOf(repoPathField.getText()));
            if (!repoDirectory.exists()) {
                repoDirectory.mkdirs();
            }
        } catch (Exception z) {
            JOptionPane.showMessageDialog(
                    serverModeConfiguration,
                    "There was an error applying the Configuration!",
                    "MeshFS - Error",
                    JOptionPane.WARNING_MESSAGE);
            z.printStackTrace();
        }
        generateAuthObjs();
        ServerConfigConfirmation.run(
                this,
                "<html><font face=\"arial\"><center><b>Master Address:</b> "
                        + masterServerField.getText()
                        + "<br><br><b>Timeout:</b> "
                        + String.valueOf(serverTimeoutField.getText())
                        + " seconds<br><br><b>Port:</b> "
                        + String.valueOf(serverPortField.getText())
                        + "<br><br><b>File Copies / Stripes / Striped Copies</b>: "
                        + String.valueOf(numWholeField.getText())
                        + "/"
                        + String.valueOf(numStripesField.getText())
                        + "/"
                        + String.valueOf(numStripeCopiesField.getText())
                        + "<br><br><b>Repository:</b> "
                        + String.valueOf(repoPathField.getText())
                        + "<br><br><b>Minimum Space:</b> "
                        + String.valueOf(Integer.parseInt(minSpaceField.getText()))
                        + " GB<br><br><b>Network Threads:</b> "
                        + String.valueOf(serverThreadsField.getText())
                        + "<br><br><b>Accounts:</b><br>"
                        + out
                        + "</center></font></html>",
                accountsEnc,
                getConfigProperties(),
                isMasterBox.isSelected());
        dispose();
    }

    private void importConfig() {
        Properties properties = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Choose Existing Configuration");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MeshFS Properties", "mfsarchive");
        fileChooser.setFileFilter(filter);
        int rVal = fileChooser.showOpenDialog(null);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            try {
                FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile().toString());
                ObjectInputStream ois = new ObjectInputStream(fis);
                ArrayList<Object> importedObjects;

                importedObjects = (ArrayList<Object>) ois.readObject();
                properties = (Properties) importedObjects.get(0);

                accountsImported = (ArrayList<UserAccounts>) importedObjects.get(1);
                isMasterBox.setSelected((boolean) importedObjects.get(2));
                ois.close();
                fis.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            assert properties != null;
            masterServerField.setText(properties.getProperty("masterIP"));
            serverPortField.setText(properties.getProperty("portNumber"));
            numStripesField.setText(properties.getProperty("numStripes"));
            numStripeCopiesField.setText(properties.getProperty("numStripeCopy"));
            numWholeField.setText(properties.getProperty("numWholeCopy"));
            minSpaceField.setText(properties.getProperty("minSpace"));
            repoPathField.setText(properties.getProperty("repository"));
            serverThreadsField.setText(properties.getProperty("serverThreads"));
            serverTimeoutField.setText(properties.getProperty("timeout"));
            userAccountDataList.removeAll();
            model.removeAllElements();
            allowGuestBox.setSelected(false);

            for (UserAccounts userAccounts : accountsImported) {
                String username = userAccounts.getUsername();
                String password = userAccounts.getPassword();
                String type = userAccounts.getAccountType();
                String group = userAccounts.getGroups().toString().replace("[", "").replace("]", "");
                String value = "";
                if (!(username.equals("guest"))) {
                    value = "<html>Username: " + username + "<br>Password: " + password + "Group: " + group + "<br>Type: " + type + "</html>";

                } else if (username.equals("guest")) {
                    allowGuestBox.setSelected(true);
                }
                if (!(value.equals(""))) {
                    model.add(userAccountDataList.getModel().getSize(), value);
                }
            }
        }
    }

    private void backupConfig() {
        Properties currentProperties = getConfigProperties();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Choose Backup Location");
        fileChooser.setAcceptAllFileFilterUsed(false);
        int rVal = fileChooser.showOpenDialog(null);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            try {
                FileOutputStream fos =
                        new FileOutputStream(
                                fileChooser.getSelectedFile().toString()
                                        + File.separator
                                        + "backup_config_"
                                        + Reporting.getSystemDate()
                                        + ".mfsarchive");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                generateAuthObjs();
                ArrayList<Object> outputObjects = new ArrayList<>();
                outputObjects.add(currentProperties);
                outputObjects.add(accountsPlain);
                outputObjects.add(isMasterBox.isSelected());
                oos.writeObject(outputObjects);
                oos.flush();
                JOptionPane.showMessageDialog(
                        serverModeConfiguration,
                        "Backup Successful!",
                        "MeshFS - Success",
                        JOptionPane.INFORMATION_MESSAGE);
                oos.close();
                fos.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        serverModeConfiguration, "Backup Failed!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void resetConfig() {
        masterServerField.setText(MeshFS.properties.getProperty("masterIP"));
        serverPortField.setText(MeshFS.properties.getProperty("portNumber"));
        numStripesField.setText(MeshFS.properties.getProperty("numStripes"));
        numStripeCopiesField.setText(MeshFS.properties.getProperty("numStripeCopy"));
        numWholeField.setText(MeshFS.properties.getProperty("numWholeCopy"));
        minSpaceField.setText(MeshFS.properties.getProperty("minSpace"));
        repoPathField.setText(MeshFS.properties.getProperty("repository"));
        serverThreadsField.setText(MeshFS.properties.getProperty("serverThreads"));
        serverTimeoutField.setText(MeshFS.properties.getProperty("timeout"));
        userAccountDataList.removeAll();
        model.removeAllElements();
        groupBox.removeAllItems();
        serverSettingPane.setSelectedIndex(0);

    }

    private Properties getConfigProperties() {
        Properties configProperties = new Properties();
        configProperties.setProperty("numStripes", String.valueOf(numStripesField.getText()));
        configProperties.setProperty("numStripeCopy", String.valueOf(numStripeCopiesField.getText()));
        configProperties.setProperty("numWholeCopy", String.valueOf(numWholeField.getText()));
        configProperties.setProperty("minSpace", minSpaceField.getText());
        configProperties.setProperty("masterIP", masterServerField.getText());
        configProperties.setProperty("portNumber", String.valueOf(serverPortField.getText()));
        configProperties.setProperty("repository", String.valueOf(repoPathField.getText()));
        configProperties.setProperty("serverThreads", String.valueOf(serverThreadsField.getText()));
        configProperties.setProperty("timeout", String.valueOf(serverTimeoutField.getText()));
        configProperties.setProperty("uuid", MeshFS.properties.getProperty("uuid"));
        return configProperties;
    }

    private boolean checkFields(JFormattedTextField field) {
        return !field.getText().isEmpty();
    }

    private void generateAuthObjs() {
        out = "";
        for (int i = 0; i < userAccountDataList.getModel().getSize(); i++) {
            String user =
                    userAccountDataList
                            .getModel()
                            .getElementAt(i)
                            .toString()
                            .substring(
                                    userAccountDataList.getModel().getElementAt(i).toString().indexOf("Username:")
                                            + 10,
                                    userAccountDataList.getModel().getElementAt(i).toString().indexOf("<br>"));
            String passOrig =
                    userAccountDataList
                            .getModel()
                            .getElementAt(i)
                            .toString()
                            .substring(
                                    userAccountDataList.getModel().getElementAt(i).toString().indexOf("Password:")
                                            + 10,
                                    userAccountDataList.getModel().getElementAt(i).toString().indexOf("<br>Group:"));
            String group =
                    userAccountDataList
                            .getModel()
                            .getElementAt(i)
                            .toString()
                            .substring(
                                    userAccountDataList.getModel().getElementAt(i).toString().indexOf("Group:")
                                            + 7,
                                    userAccountDataList.getModel().getElementAt(i).toString().indexOf("Type:"));
            String type =
                    userAccountDataList
                            .getModel()
                            .getElementAt(i)
                            .toString()
                            .substring(
                                    userAccountDataList.getModel().getElementAt(i).toString().indexOf("Type:")
                                            + 6,
                                    userAccountDataList.getModel().getElementAt(i).toString().indexOf("</html>"));

            out += "Username: <i>" + user + "</i>, Password: <i>" + passOrig + "</i>, Group: <i>" + group.toLowerCase() + "</i>, Type: <i>" + type + "<br>";
            accountsEnc.add(new UserAccounts(user, Crypt.generateEncryptedPass(user, passOrig), type, new ArrayList<>(Collections.singletonList(group.substring(0, group.lastIndexOf("<br>"))))));
            accountsPlain.add(new UserAccounts(user, passOrig, type, new ArrayList<>(Collections.singletonList(group.substring(0, group.lastIndexOf("<br>"))))));
        }
        if (out.equals("")) {
            out = "(none)";
        }
        if (allowGuestBox.isSelected()) {
            out += "Username: <i>guest</i>, Password: <i>N/A</i>" + "</i>, Group: <i>none</i>, Type: <i>User" + "<br>";
            accountsEnc.add(new UserAccounts("guest", Crypt.generateEncryptedPass("guest", "guest"), "user", new ArrayList<>(Collections.singletonList("guest"))));
            accountsPlain.add(new UserAccounts("guest", "guest", "user", new ArrayList<>(Collections.singletonList("guest"))));
        }
    }

    private void setConfigMode(String mode) {
        if (mode.equals("full")) {
            masterServerField.setEnabled(true);
            serverPortField.setEnabled(true);
            serverThreadsField.setEnabled(true);
            serverTimeoutField.setEnabled(true);
            numStripesField.setEnabled(true);
            numStripeCopiesField.setEnabled(true);
            numWholeField.setEnabled(true);
            usernameValueField.setEnabled(true);
            passwordValueField.setEnabled(true);
            submitBtn.setEnabled(true);
            userAccountDataList.setEnabled(true);
            removeUserBtn.setEnabled(true);
            allowGuestBox.setEnabled(true);
            masterServerField.setEnabled(false);
            masterServerField.setText("127.0.0.1");
            browseBtn.setEnabled(true);
            spaceSldr.setEnabled(false);
            minSpaceField.setEnabled(false);
            serverSettingPane.addTab("User Accounts", userAccounts);

        } else if (mode.equals("easy")) {
            masterServerField.setEnabled(true);
            serverPortField.setEnabled(true);
            serverThreadsField.setEnabled(false);
            serverTimeoutField.setEnabled(false);
            numStripesField.setEnabled(false);
            numStripeCopiesField.setEnabled(false);
            numWholeField.setEnabled(false);
            usernameValueField.setEnabled(false);
            passwordValueField.setEnabled(false);
            submitBtn.setEnabled(false);
            userAccountDataList.setEnabled(false);
            removeUserBtn.setEnabled(false);
            allowGuestBox.setEnabled(false);
            userAccountDataList.removeAll();
            masterServerField.setText("");
            browseBtn.setEnabled(true);
            spaceSldr.setEnabled(true);
            minSpaceField.setEnabled(true);
            serverSettingPane.removeTabAt(2);
        }
    }
}
