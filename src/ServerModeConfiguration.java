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
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import static java.lang.String.valueOf;
import static java.lang.Math.toIntExact;
/**
 * Created by Mark Hedrick on 10/30/16.
 */
public class ServerModeConfiguration extends JFrame {

    private static JFrame serverModeConfiguration;
    private ServerModeConfiguration() {
        initComponents();
        frameListeners();
        ipJListField.setSelectedIndex(0);
        freeSpaceLbl.setText("(Free Space: " + valueOf(Reporting.getSystemStorage()/1073741824) + " GB)");
        spaceSldr.setMaximum(toIntExact(Reporting.getSystemStorage()/1073741824)-10);
        spaceSldr.setMinimum(0);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        if(Reporting.getSystemOS().contains("Windows")){
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
    }

    private void initComponents() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        serverSettingPane = new JTabbedPane();
        networkTab = new JPanel();
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
        storageTab = new JPanel();
        repositoryLbl = new JLabel();
        repoPathField = new JTextField(System.getProperty("user.dir")+ File.separator + "repo");
        minFreeSpaceLbl = new JLabel();
        minSpaceField = new JFormattedTextField(numberFormat);
        label1 = new JLabel();
        spaceSldr = new JSlider();
        freeSpaceLbl = new JLabel();
        browseBtn = new JButton();
        buttonBar = new JPanel();
        importConfigBtn = new JButton();
        backupConfigBtn = new JButton();
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

                //======== networkTab ========
                {
                    networkTab.setBackground(new Color(229, 229, 229));

                    //---- serverPortLbl ----
                    serverPortLbl.setText("Network Port:");
                    serverPortLbl.setFont(new Font("Arial", serverPortLbl.getFont().getStyle(), serverPortLbl.getFont().getSize() + 1));

                    //---- serverPortField ----
                    serverPortField.setText("5704");
                    serverPortField.setFont(new Font("Arial", serverPortField.getFont().getStyle(), serverPortField.getFont().getSize() + 1));

                    //---- serverThreadsLbl ----
                    serverThreadsLbl.setText("Network Threads:");
                    serverThreadsLbl.setFont(new Font("Arial", serverThreadsLbl.getFont().getStyle(), serverThreadsLbl.getFont().getSize() + 1));

                    //---- serverThreadsField ----
                    serverThreadsField.setText("16");
                    serverThreadsField.setFont(new Font("Arial", serverThreadsField.getFont().getStyle(), serverThreadsField.getFont().getSize() + 1));

                    //---- serverTimeoutLbl ----
                    serverTimeoutLbl.setText("Server Timeout:");
                    serverTimeoutLbl.setFont(new Font("Arial", serverTimeoutLbl.getFont().getStyle(), serverTimeoutLbl.getFont().getSize() + 1));

                    //---- serverTimeoutField ----
                    serverTimeoutField.setText("90");
                    serverTimeoutField.setFont(new Font("Arial", serverTimeoutField.getFont().getStyle(), serverTimeoutField.getFont().getSize() + 1));

                    //---- stripesLbl ----
                    stripesLbl.setText("Stripes:");
                    stripesLbl.setFont(new Font("Arial", stripesLbl.getFont().getStyle(), stripesLbl.getFont().getSize() + 1));

                    //---- numStripesField ----
                    numStripesField.setText("3");
                    numStripesField.setFont(new Font("Arial", numStripesField.getFont().getStyle(), numStripesField.getFont().getSize() + 1));

                    //---- stripedCopiesLbl ----
                    stripedCopiesLbl.setText("Striped Copies:");
                    stripedCopiesLbl.setFont(new Font("Arial", stripedCopiesLbl.getFont().getStyle(), stripedCopiesLbl.getFont().getSize() + 1));

                    //---- numStripeCopiesField ----
                    numStripeCopiesField.setText("2");
                    numStripeCopiesField.setFont(new Font("Arial", numStripeCopiesField.getFont().getStyle(), numStripeCopiesField.getFont().getSize() + 1));

                    //---- wholeCopiesLbl ----
                    wholeCopiesLbl.setText("Whole Copies:");
                    wholeCopiesLbl.setFont(new Font("Arial", wholeCopiesLbl.getFont().getStyle(), wholeCopiesLbl.getFont().getSize() + 1));

                    //---- numWholeField ----
                    numWholeField.setText("2");
                    numWholeField.setFont(new Font("Arial", numWholeField.getFont().getStyle(), numWholeField.getFont().getSize() + 1));

                    //---- serverNetworkInterfaceLbl ----
                    serverNetworkInterfaceLbl.setText("Network Interface:");
                    serverNetworkInterfaceLbl.setFont(new Font("Arial", serverNetworkInterfaceLbl.getFont().getStyle(), serverNetworkInterfaceLbl.getFont().getSize() + 1));

                    //======== scrollPane1 ========
                    {
                        scrollPane1.setViewportView(ipJListField);
                    }

                    GroupLayout networkTabLayout = new GroupLayout(networkTab);
                    networkTab.setLayout(networkTabLayout);
                    networkTabLayout.setHorizontalGroup(
                        networkTabLayout.createParallelGroup()
                            .addGroup(networkTabLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addGroup(networkTabLayout.createSequentialGroup()
                                        .addComponent(serverPortLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(serverPortField, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(stripedCopiesLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(numStripeCopiesField, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(networkTabLayout.createSequentialGroup()
                                        .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addGroup(GroupLayout.Alignment.LEADING, networkTabLayout.createSequentialGroup()
                                                .addComponent(stripesLbl)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(numStripesField, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(GroupLayout.Alignment.LEADING, networkTabLayout.createSequentialGroup()
                                                    .addComponent(serverTimeoutLbl)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(serverTimeoutField, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(GroupLayout.Alignment.LEADING, networkTabLayout.createSequentialGroup()
                                                    .addComponent(serverThreadsLbl)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(serverThreadsField, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(networkTabLayout.createParallelGroup()
                                            .addGroup(networkTabLayout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(networkTabLayout.createParallelGroup()
                                                    .addGroup(networkTabLayout.createSequentialGroup()
                                                        .addComponent(wholeCopiesLbl)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(numWholeField, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(serverNetworkInterfaceLbl)))
                                            .addGroup(GroupLayout.Alignment.TRAILING, networkTabLayout.createSequentialGroup()
                                                .addGap(94, 94, 94)
                                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(22, Short.MAX_VALUE))
                    );
                    networkTabLayout.setVerticalGroup(
                        networkTabLayout.createParallelGroup()
                            .addGroup(networkTabLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(serverPortLbl)
                                    .addComponent(serverPortField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(stripedCopiesLbl)
                                    .addComponent(numStripeCopiesField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(serverThreadsLbl)
                                    .addComponent(serverThreadsField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(wholeCopiesLbl)
                                    .addComponent(numWholeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(serverTimeoutLbl)
                                    .addComponent(serverTimeoutField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(serverNetworkInterfaceLbl))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(networkTabLayout.createParallelGroup()
                                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                    .addGroup(networkTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(numStripesField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(stripesLbl)))
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

                    //---- label1 ----
                    label1.setText("GB");
                    label1.setFont(new Font("Arial", label1.getFont().getStyle(), label1.getFont().getSize() + 1));

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
                                        .addComponent(repositoryLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(repoPathField, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(browseBtn))
                                    .addGroup(storageTabLayout.createSequentialGroup()
                                        .addComponent(minFreeSpaceLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(minSpaceField, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(label1)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(spaceSldr, GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE))
                                    .addGroup(storageTabLayout.createSequentialGroup()
                                        .addComponent(freeSpaceLbl, GroupLayout.PREFERRED_SIZE, 351, GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 99, Short.MAX_VALUE)))
                                .addContainerGap())
                    );
                    storageTabLayout.setVerticalGroup(
                        storageTabLayout.createParallelGroup()
                            .addGroup(storageTabLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(storageTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(repositoryLbl)
                                    .addComponent(repoPathField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(browseBtn))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(storageTabLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(storageTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(minFreeSpaceLbl)
                                        .addComponent(minSpaceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(label1))
                                    .addComponent(spaceSldr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(freeSpaceLbl)
                                .addContainerGap(122, Short.MAX_VALUE))
                    );
                }
                serverSettingPane.addTab("Storage", storageTab);
            }
            dialogPane.add(serverSettingPane, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 309, 0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 0.0, 0.0};

                //---- importConfigBtn ----
                importConfigBtn.setText("Import...");
                importConfigBtn.setFont(new Font("Arial", importConfigBtn.getFont().getStyle(), importConfigBtn.getFont().getSize() + 1));
                buttonBar.add(importConfigBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- backupConfigBtn ----
                backupConfigBtn.setText("Backup...");
                backupConfigBtn.setFont(new Font("Arial", backupConfigBtn.getFont().getStyle(), backupConfigBtn.getFont().getSize() + 1));
                buttonBar.add(backupConfigBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- resetConfigBtn ----
                resetConfigBtn.setText("Reset");
                resetConfigBtn.setFont(new Font("Arial", resetConfigBtn.getFont().getStyle(), resetConfigBtn.getFont().getSize() + 1));
                buttonBar.add(resetConfigBtn, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- titleLbl ----
            titleLbl.setText("Server Settings");
            titleLbl.setFont(new Font("Helvetica Neue", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
            titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPane.add(titleLbl, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(490, 365);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners(){
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
        serverPortField.getDocument().addDocumentListener(new DocumentListener() {

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
                }else {
                    backupConfigBtn.setEnabled(true);
                    okButton.setEnabled(true);
                }

            }
        });
        serverThreadsField.getDocument().addDocumentListener(new DocumentListener() {

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
                }else {
                    backupConfigBtn.setEnabled(true);
                    okButton.setEnabled(true);
                }

            }
        });
        serverTimeoutField.getDocument().addDocumentListener(new DocumentListener() {

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
                }else {
                    backupConfigBtn.setEnabled(true);
                    okButton.setEnabled(true);
                }

            }
        });
        numStripesField.getDocument().addDocumentListener(new DocumentListener() {

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
                }else {
                    backupConfigBtn.setEnabled(true);
                    okButton.setEnabled(true);
                }

            }
        });
        numStripeCopiesField.getDocument().addDocumentListener(new DocumentListener() {

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
                if (!(checkFields(numStripeCopiesField))) {
                    backupConfigBtn.setEnabled(false);
                    okButton.setEnabled(false);
                }else {
                    backupConfigBtn.setEnabled(true);
                    okButton.setEnabled(true);
                }

            }
        });
        numWholeField.getDocument().addDocumentListener(new DocumentListener() {

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
                if (!(checkFields(numWholeField))) {
                    backupConfigBtn.setEnabled(false);
                    okButton.setEnabled(false);
                }else {
                    backupConfigBtn.setEnabled(true);
                    okButton.setEnabled(true);
                }

            }
        });
        repoPathField.getDocument().addDocumentListener(new DocumentListener() {

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
                if (repoPathField.getText().isEmpty() == true){
                    backupConfigBtn.setEnabled(false);
                    okButton.setEnabled(false);
                }else{
                    backupConfigBtn.setEnabled(true);
                    okButton.setEnabled(true);
                }
            }
        });
        minSpaceField.getDocument().addDocumentListener(new DocumentListener() {

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
                }else {
                    backupConfigBtn.setEnabled(true);
                    okButton.setEnabled(true);
                }

            }
        });
        resetConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetConfig();
            }
        });
    }

    private ArrayList<String> ipJList(){
        ArrayList<String> x = new ArrayList<>();
        for(String ip : Reporting.getIpAddress()){
            x.add(ip);
        }
        return x;
    }

    private void onOk(){
        try{
            File repoDirectory = new File(String.valueOf(repoPathField.getText()));
            if (!repoDirectory.exists()) {
                repoDirectory.mkdirs();
            }
            writeConfig();
            JOptionPane.showMessageDialog(null, "Configuration was saved!", "MeshFS - Success", JOptionPane.INFORMATION_MESSAGE);

        }catch (Exception z) {
            JOptionPane.showMessageDialog(null, "There was an error applying the Configuration!", "MeshFS - Error", JOptionPane.WARNING_MESSAGE);
            z.printStackTrace();
        }
        ServerConfigConfirmation.run(this, "<html><center><b>Interface:</b> " + MeshFS.properties.getProperty("preferredInterface") + "<br><br><b>Timeout:</b> " + MeshFS.properties.getProperty("timeout") + "s<br><br><b>Port:</b> " + MeshFS.properties.getProperty("portNumber") + "<br><br><b>File Copies / Stripes / Striped Copies</b>: " + MeshFS.properties.getProperty("numWholeCopy") + "/" + MeshFS.properties.getProperty("numStripes") + "/" + MeshFS.properties.getProperty("numStripeCopy") + "<br><br><b>Repository:</b> " + MeshFS.properties.getProperty("repository") + "<br><br><b>Minimum Space:</b> " + MeshFS.properties.getProperty("minSpace") + "<br><br><b>Server Threads:</b> " + MeshFS.properties.getProperty("serverThreads") + "</center></html>");

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

    private void backupConfig(){
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

    private void resetConfig(){
        serverPortField.setText(MeshFS.properties.getProperty("portNumber"));
        numStripesField.setText(MeshFS.properties.getProperty("numStripes"));
        numStripeCopiesField.setText(MeshFS.properties.getProperty("numStripeCopy"));
        numWholeField.setText(MeshFS.properties.getProperty("numWholeCopy"));
        minSpaceField.setText(MeshFS.properties.getProperty("minSpace"));
        repoPathField.setText(MeshFS.properties.getProperty("repository"));
        serverThreadsField.setText(MeshFS.properties.getProperty("serverThreads"));
        serverTimeoutField.setText(MeshFS.properties.getProperty("timeout"));
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
        configProperties.setProperty("timeout", String.valueOf(serverTimeoutField.getText()));
        return configProperties;
    }

    public boolean checkFields(JFormattedTextField field) {
        if (field.getText().isEmpty() == true){
            return false;
        }else{
            return true;
        }
    }

    public static void run(JFrame sender) {
        JFrame serverModeConfiguration = new ServerModeConfiguration();
        CenterWindow.centerOnWindow(sender, serverModeConfiguration);
        serverModeConfiguration.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JTabbedPane serverSettingPane;
    private JPanel networkTab;
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
    private JPanel storageTab;
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
    private JButton resetConfigBtn;
    private JButton okButton;
    private JLabel titleLbl;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
