import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


/**
 * The SharingSettings is a window
 * responsible for letting users share
 * files with other users and groups
 *
 * @author Mark Hedrick
 * @version 1.0.0
 */

class SharingSettings extends JFrame {

    private DefaultListModel groupsModel;
    private String serverAddress;
    private int port;
    private String itemPath;
    private JFrame sender;

    private SharingSettings(String serverAddress, int port, ArrayList groups, JFrame sender, String itemPath) {

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

        String[] serverGroups = null;
        try {
            serverGroups = FileClient.getGroups(serverAddress, port).split(",");
        } catch (MalformedRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initComponents();
        for (Object groupName : groups) {
            groupsModel.addElement(groupName);
        }
        for (String groupName : serverGroups != null ? serverGroups : new String[0]) {
            if (!groups.contains(groupName)) {
                addGroupBox.addItem(groupName.trim());

            }
        }
        frameListeners();
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
                                        .addContainerGap(19, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 85, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- titleLbl ----
            titleLbl.setText("Sharing Settings");
            titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 7));
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

    private void sendPermissions() {
        ArrayList<String> members = new ArrayList<>();
        for (int i = 0; i < groupsList.getModel().getSize(); i++) {
            members.add(groupsList.getModel().getElementAt(i).toString());
        }
        try {
            FileClient.editPermissions(serverAddress, port, itemPath, members.toString());
        } catch (MalformedRequestException | IOException e) {
            e.printStackTrace();
        }
        sender.dispose();
        dispose();
    }

    /**
     * The run method is responsible for spawning the window when
     * called.
     *
     * @param sender          The parent window that called this window
     * @param serverAddress   The address used to connect to the master
     *                        server
     * @param port            The port number used to connect to the
     *                        master server
     * @param groups          The groups that can receive a shared file
     * @param itemPath        The path to the item being shared
     */

    public static void run(JFrame sender, String serverAddress, int port, ArrayList groups, String itemPath) {
        JFrame sharingSettings = new SharingSettings(serverAddress, port, groups, sender, itemPath);
        CenterWindow.centerOnWindow(sender, sharingSettings);
        sharingSettings.setVisible(true);
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
