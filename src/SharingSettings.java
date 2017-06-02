import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author Mark Hedrick
 */
class SharingSettings extends JFrame {
    private static JFrame sharingSettings;
    private ArrayList groups;
    private DefaultListModel groupsModel;
    private String serverAddress;
    private int port;
    private String itemPath;
    private JFrame sender;

    public SharingSettings(String userAccount, String serverAddress, int port, ArrayList groups, ArrayList admins, JFrame sender, String itemPath) {

        this.groups = groups;
        this.serverAddress = serverAddress;
        this.port = port;
        this.itemPath = itemPath;
        this.sender = sender;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
        groupsModel = new DefaultListModel();
        initComponents();
        frameListeners();
        String[] serverGroups = null;
        try {
            serverGroups = FileClient.getGroups(serverAddress, port, MeshFS.properties.getProperty("uuid")).split(",");
        } catch (MalformedRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Object groupName : groups) {
            groupsModel.addElement(groupName);
        }
        for (String groupName : serverGroups != null ? serverGroups : new String[0]) {
            if (!groups.contains(groupName)) {
                addGroupBox.addItem(groupName.trim());

            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        fileNameLbl = new JLabel();
        scrollPane2 = new JScrollPane();
        groupsList = new JList(groupsModel);
        addGroupBox = new JComboBox();
        addGroupBtn = new JButton();
        removeGroupBtn = new JButton();
        buttonBar = new JPanel();
        okButton = new JButton();
        titleLbl = new JLabel();

        //======== this ========
        setTitle("Sharing");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- fileNameLbl ----
                fileNameLbl.setText("Users / Groups");
                fileNameLbl.setFont(new Font("Arial", Font.BOLD, fileNameLbl.getFont().getSize() + 1));

                //======== scrollPane2 ========
                {

                    //---- groupsList ----
                    groupsList.setFont(new Font("Arial", groupsList.getFont().getStyle(), groupsList.getFont().getSize()));
                    scrollPane2.setViewportView(groupsList);
                }

                //---- addGroupBox ----
                addGroupBox.setFont(new Font("Arial", addGroupBox.getFont().getStyle(), addGroupBox.getFont().getSize()));

                //---- addGroupBtn ----
                addGroupBtn.setText("+");
                addGroupBtn.setFont(new Font("Arial", addGroupBtn.getFont().getStyle(), addGroupBtn.getFont().getSize()));

                //---- removeGroupBtn ----
                removeGroupBtn.setText("-");
                removeGroupBtn.setFont(new Font("Arial", removeGroupBtn.getFont().getStyle(), removeGroupBtn.getFont().getSize()));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(fileNameLbl, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 83, Short.MAX_VALUE))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(addGroupBox, GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(addGroupBtn)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(removeGroupBtn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(fileNameLbl)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(removeGroupBtn, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addComponent(addGroupBtn)
                                .addComponent(addGroupBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(22, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- titleLbl ----
            titleLbl.setText("Sharing Settings");
            titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
            titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPane.add(titleLbl, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners() {
        addGroupBtn.addActionListener(
                e -> {
                    groupsModel.add(groupsModel.getSize(), addGroupBox.getSelectedItem().toString());
                    addGroupBox.removeItem(addGroupBox.getSelectedItem());

                });
        removeGroupBtn.addActionListener(
                e -> groupsModel.removeElement(groupsList.getSelectedValue()));
        okButton.addActionListener(
                e -> sendPermissions()
        );
    }

    public static void run(JFrame sender, String userAccount, String serverAddress, int port, ArrayList groups, ArrayList admins, String itemPath) {
        sharingSettings = new SharingSettings(userAccount, serverAddress, port, groups, admins, sender, itemPath);
        CenterWindow.centerOnWindow(sender, sharingSettings);
        sharingSettings.setVisible(true);
    }

    private void sendPermissions() {
        ArrayList<String> members = new ArrayList<>();
        for (int i = 0; i < groupsList.getModel().getSize(); i++) {
            members.add(groupsList.getModel().getElementAt(i).toString());
        }
        try {
            FileClient.editPermissions(serverAddress, port, itemPath, members.toString(), MeshFS.properties.getProperty("uuid"));
        } catch (MalformedRequestException | IOException e) {
            //TODO: Some sort of error?
            e.printStackTrace();
        }
        sender.dispose();
        dispose();
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel fileNameLbl;
    private JScrollPane scrollPane2;
    private JList groupsList;
    private JComboBox addGroupBox;
    private JButton addGroupBtn;
    private JButton removeGroupBtn;
    private JPanel buttonBar;
    private JButton okButton;
    private JLabel titleLbl;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
