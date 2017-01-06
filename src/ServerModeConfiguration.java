import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import static java.lang.String.valueOf;
import static java.lang.Math.toIntExact;
/**
 * Created by Mark Hedrick on 10/30/16.
 */
public class ServerModeConfiguration extends JFrame {
    public ServerModeConfiguration() {
        initComponents();
        windowListeners();
        freeSpaceLbl.setText("(Free Space: " + valueOf(Reporting.getSystemStorage()/1073741824) + " GB)");
        spaceSldr.setMaximum(toIntExact(Reporting.getSystemStorage()/1073741824)-10);
        spaceSldr.setMinimum(0);
    }

    private void initComponents() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        serverPortLbl = new JLabel();
        serverPortField = new JFormattedTextField(numberFormat);
        serverThreadsLbl = new JLabel();
        serverThreadsField = new JFormattedTextField(numberFormat);
        serverTimeoutLbl = new JLabel();
        serverTimeoutField = new JFormattedTextField(numberFormat);
        stripesLbl = new JLabel();
        numStripesField = new JFormattedTextField(numberFormat);
        stripedCopiesLbl = new JLabel();
        numStripeCopiesField = new JFormattedTextField(numberFormat);
        wholeCopiesLbl = new JLabel();
        numWholeField = new JFormattedTextField(numberFormat);
        serverNetworkInterfaceLbl = new JLabel();
        scrollPane1 = new JScrollPane();
        ipJListField = new JList(ipJList().toArray());
        panel2 = new JPanel();
        repositoryLbl = new JLabel();
        repoPathField = new JTextField(System.getProperty("user.dir"));
        minFreeSpaceLbl = new JLabel();
        minSpaceField = new JFormattedTextField(numberFormat);
        label1 = new JLabel();
        spaceSldr = new JSlider();
        freeSpaceLbl = new JLabel();
        browseBtn = new JButton();
        buttonBar = new JPanel();
        importConfigBtn = new JButton();
        backupConfigBtn = new JButton();
        hSpacer1 = new JPanel(null);
        okButton = new JButton();

        //======== this ========
        setTitle("MeshFS - Server Configuration");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== tabbedPane1 ========
            {

                //======== panel1 ========
                {
                    panel1.setBackground(new Color(229, 229, 229));

                    //---- serverPortLbl ----
                    serverPortLbl.setText("Network Port:");
                    serverPortLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- serverPortField ----
                    serverPortField.setText("5704");
                    serverPortField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- serverThreadsLbl ----
                    serverThreadsLbl.setText("Network Threads:");
                    serverThreadsLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- serverThreadsField ----
                    serverThreadsField.setText("16");

                    //---- serverTimeoutLbl ----
                    serverTimeoutLbl.setText("Server Timeout:");
                    serverTimeoutLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- serverTimeoutField ----
                    serverTimeoutField.setText("90");

                    //---- stripesLbl ----
                    stripesLbl.setText("Stripes:");
                    stripesLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- numStripesField ----
                    numStripesField.setText("3");
                    numStripesField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- stripedCopiesLbl ----
                    stripedCopiesLbl.setText("Striped Copies:");
                    stripedCopiesLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- numStripeCopiesField ----
                    numStripeCopiesField.setText("2");
                    numStripeCopiesField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- wholeCopiesLbl ----
                    wholeCopiesLbl.setText("Whole Copies:");
                    wholeCopiesLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- numWholeField ----
                    numWholeField.setText("2");
                    numWholeField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- serverNetworkInterfaceLbl ----
                    serverNetworkInterfaceLbl.setText("Network Interface:");
                    serverNetworkInterfaceLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //======== scrollPane1 ========
                    {
                        scrollPane1.setViewportView(ipJListField);
                    }

                    GroupLayout panel1Layout = new GroupLayout(panel1);
                    panel1.setLayout(panel1Layout);
                    panel1Layout.setHorizontalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(serverPortLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(serverPortField, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(stripedCopiesLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(numStripeCopiesField, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(GroupLayout.Alignment.LEADING, panel1Layout.createSequentialGroup()
                                                .addComponent(serverTimeoutLbl)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(serverTimeoutField, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(GroupLayout.Alignment.LEADING, panel1Layout.createSequentialGroup()
                                                .addComponent(stripesLbl)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(numStripesField, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(GroupLayout.Alignment.LEADING, panel1Layout.createSequentialGroup()
                                                .addComponent(serverThreadsLbl)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(serverThreadsField, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(panel1Layout.createParallelGroup()
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(wholeCopiesLbl)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(numWholeField, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
                                            .addComponent(serverNetworkInterfaceLbl)))
                                    .addComponent(scrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(3, Short.MAX_VALUE))
                    );
                    panel1Layout.setVerticalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(serverPortLbl)
                                    .addComponent(serverPortField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(stripedCopiesLbl)
                                    .addComponent(numStripeCopiesField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(serverThreadsLbl)
                                    .addComponent(serverThreadsField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(wholeCopiesLbl)
                                    .addComponent(numWholeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(serverTimeoutLbl)
                                    .addComponent(serverTimeoutField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(serverNetworkInterfaceLbl))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(stripesLbl)
                                        .addComponent(numStripesField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                }
                tabbedPane1.addTab("Network", panel1);

                //======== panel2 ========
                {
                    panel2.setBackground(new Color(229, 229, 229));

                    //---- repositoryLbl ----
                    repositoryLbl.setText("Repository:");
                    repositoryLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- repoPathField ----
                    repoPathField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- minFreeSpaceLbl ----
                    minFreeSpaceLbl.setText("Reserved Space:");
                    minFreeSpaceLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- minSpaceField ----
                    minSpaceField.setText("0");
                    minSpaceField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- label1 ----
                    label1.setText("GB");

                    //---- freeSpaceLbl ----
                    freeSpaceLbl.setText("(free space)");
                    freeSpaceLbl.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    //---- browseBtn ----
                    browseBtn.setText("Browse...");
                    browseBtn.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));

                    GroupLayout panel2Layout = new GroupLayout(panel2);
                    panel2.setLayout(panel2Layout);
                    panel2Layout.setHorizontalGroup(
                        panel2Layout.createParallelGroup()
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel2Layout.createParallelGroup()
                                    .addGroup(panel2Layout.createSequentialGroup()
                                        .addComponent(repositoryLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(repoPathField, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(browseBtn))
                                    .addGroup(panel2Layout.createSequentialGroup()
                                        .addComponent(minFreeSpaceLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(minSpaceField, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(label1)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spaceSldr, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))
                                    .addGroup(panel2Layout.createSequentialGroup()
                                        .addComponent(freeSpaceLbl, GroupLayout.PREFERRED_SIZE, 351, GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 80, Short.MAX_VALUE)))
                                .addContainerGap())
                    );
                    panel2Layout.setVerticalGroup(
                        panel2Layout.createParallelGroup()
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(repositoryLbl)
                                    .addComponent(repoPathField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(browseBtn))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(minFreeSpaceLbl)
                                        .addComponent(minSpaceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label1))
                                    .addComponent(spaceSldr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(freeSpaceLbl)
                                .addContainerGap(76, Short.MAX_VALUE))
                    );
                }
                tabbedPane1.addTab("Storage", panel2);
            }
            dialogPane.add(tabbedPane1, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 309, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 0.0};

                //---- importConfigBtn ----
                importConfigBtn.setText("Import...");
                importConfigBtn.setFont(new Font("Helvetica Neue", importConfigBtn.getFont().getStyle() & ~Font.ITALIC, importConfigBtn.getFont().getSize()));
                buttonBar.add(importConfigBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- backupConfigBtn ----
                backupConfigBtn.setText("Backup...");
                buttonBar.add(backupConfigBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
                buttonBar.add(hSpacer1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
                buttonBar.add(okButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
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

    private void windowListeners(){
        browseBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                fileChooser.setDialogTitle("Choose Respository");
                int rVal = fileChooser.showOpenDialog(null);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    repoPathField.setText(fileChooser.getSelectedFile().toString());
                    repoPathField.setToolTipText(fileChooser.getSelectedFile().toString());
                }
            }
        });
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });
        importConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importConfig();
            }
        });
        backupConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backupConfig();
            }
        });
        ipJListField.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                JList l = (JList)e.getSource();
                ListModel m = l.getModel();
                int index = l.locationToIndex(e.getPoint());
                if( index>-1 ) {
                    l.setToolTipText(m.getElementAt(index).toString());
                }
            }
        });
        spaceSldr.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                minSpaceField.setText(String.valueOf(spaceSldr.getValue()));

            }
        });
        minSpaceField.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent ke) {
                spaceSldr.setValue(0);
                spaceSldr.setValue(Integer.parseInt(minSpaceField.getText()));
            }
        });
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JLabel serverPortLbl;
    private JFormattedTextField serverPortField;
    private JLabel serverThreadsLbl;
    private JFormattedTextField serverThreadsField;
    private JLabel serverTimeoutLbl;
    private JFormattedTextField serverTimeoutField;
    private JLabel stripesLbl;
    private JFormattedTextField numStripesField;
    private JLabel stripedCopiesLbl;
    private JFormattedTextField numStripeCopiesField;
    private JLabel wholeCopiesLbl;
    private JFormattedTextField numWholeField;
    private JLabel serverNetworkInterfaceLbl;
    private JScrollPane scrollPane1;
    private JList ipJListField;
    private JPanel panel2;
    private JLabel repositoryLbl;
    private JTextField repoPathField;
    private JLabel minFreeSpaceLbl;
    private JFormattedTextField minSpaceField;
    private JLabel label1;
    private JSlider spaceSldr;
    private JLabel freeSpaceLbl;
    private JButton browseBtn;
    private JPanel buttonBar;
    private JButton importConfigBtn;
    private JButton backupConfigBtn;
    private JPanel hSpacer1;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void run(JFrame sender) {
        JFrame serverModeConfiguration = new ServerModeConfiguration();
        CenterWindow.centerOnWindow(sender, serverModeConfiguration);
        serverModeConfiguration.setVisible(true);
    }

    private ArrayList<String> ipJList(){
        ArrayList<String> x = new ArrayList<>();
        for(int i = 0; i < Reporting.getIpAddress().size(); i++){
            x.add(Reporting.getIpAddress().get(i).get(0).toString() + " (" + Reporting.getIpAddress().get(i).get(1).toString() + ")");
        }
        return x;
    }

    public void onOk(){
        try{
            File repoDirectory = new File(String.valueOf(repoPathField.getText()));
            if (!repoDirectory.exists()) {
                repoDirectory.mkdirs();
            }
            System.out.println(String.valueOf(repoPathField.getText()));
            writeConfig();
            JOptionPane.showMessageDialog(null, "Configuration was saved!", "MeshFS - Success", JOptionPane.INFORMATION_MESSAGE);

        }catch (Exception z) {
            JOptionPane.showMessageDialog(null, "There was an error applying the Configuration!", "MeshFS - Error", JOptionPane.WARNING_MESSAGE);
            z.printStackTrace();
        }
        dispose();
    }

    public void importConfig() {
        Properties properties = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Choose Existing Configuration");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MeshFS Properties", "properties");
        fileChooser.setFileFilter(filter);
        int rVal = fileChooser.showOpenDialog(null);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            try {
                properties = ConfigParser.reader(fileChooser.getSelectedFile().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverPortField.setText(properties.getProperty("portNumber"));
            numStripesField.setText(properties.getProperty("numStripes"));
            numStripeCopiesField.setText(properties.getProperty("numStripeCopy"));
            numWholeField.setText(properties.getProperty("numWholeCopy"));
            minSpaceField.setText(properties.getProperty("minSpace"));
            repoPathField.setText(properties.getProperty("repository"));
            serverThreadsField.setText(properties.getProperty("serverThreads"));
            serverTimeoutField.setText(properties.getProperty("serverTimeout"));
        }
    }

    public void backupConfig(){
        Properties currentProperties = getConfigProperties();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Choose Backup Location");
        fileChooser.setAcceptAllFileFilterUsed(false);
        int rVal = fileChooser.showOpenDialog(null);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            try {
                ConfigParser.backup(currentProperties, fileChooser.getSelectedFile().toString());
                JOptionPane.showMessageDialog(null, "Backup Successful!", "MeshFS - Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Backup Failed!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void writeConfig(){
        ConfigParser.write(getConfigProperties());
    }

    public Properties getConfigProperties(){
        Properties configProperties = new Properties();
        configProperties.setProperty("numStripes", String.valueOf(numStripesField.getText()));
        configProperties.setProperty("numStripeCopy", String.valueOf(numStripeCopiesField.getText()));
        configProperties.setProperty("numWholeCopy", String.valueOf(numWholeField.getText()));
        configProperties.setProperty("minSpace", String.valueOf(minSpaceField.getText()));
        configProperties.setProperty("masterIP", "127.0.0.1");
        configProperties.setProperty("preferredInterface", String.valueOf(ipJListField.getSelectedValue()).substring(0, String.valueOf(ipJListField.getSelectedValue()).indexOf(" (")));
        configProperties.setProperty("portNumber", String.valueOf(serverPortField.getText()));
        configProperties.setProperty("repository", String.valueOf(repoPathField.getText()));
        configProperties.setProperty("serverThreads", String.valueOf(serverThreadsField.getText()));
        configProperties.setProperty("serverTimeout", String.valueOf(serverTimeoutField.getText()));
        return configProperties;
    }
}
