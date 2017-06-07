import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.*;

/**
 * @author Mark Hedrick
 */

class ChangeUserGroup extends JFrame {

    private static JFrame changeUserGroup;
    private String userAccount;
    private String serverAddress;
    private int port;
    private final DefaultListModel currentGroupModel;
    private final DefaultListModel newGroupModel;

    public ChangeUserGroup(String userAccount, String serverAddress, int port) {
        this.userAccount = userAccount;
        this.serverAddress = serverAddress;
        this.port = port;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        initComponents();
        frameListeners();

        currentGroupModel = new DefaultListModel();
        newGroupModel = new DefaultListModel();

        updateGroupInfo();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        changeUserGroupTitle = new JLabel();
        newGroupsLbl = new JLabel();
        currentGroupsLbl = new JLabel();
        splitPane1 = new JSplitPane();
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        currentGroupsList = new JList(currentGroupModel);
        panel2 = new JPanel();
        scrollPane2 = new JScrollPane();
        newGroupsList = new JList(newGroupModel);
        panel3 = new JPanel();
        addGroupLbl = new JLabel();
        userGroupBox = new JComboBox();
        addGroupBtn = new JButton();
        removeMembershipBtn = new JButton();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("MeshFS - Change User Group");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- changeUserGroupTitle ----
                changeUserGroupTitle.setText("Change User Group");
                changeUserGroupTitle.setFont(new Font("Arial", changeUserGroupTitle.getFont().getStyle(), changeUserGroupTitle.getFont().getSize() + 9));
                changeUserGroupTitle.setHorizontalAlignment(SwingConstants.CENTER);

                //---- newGroupsLbl ----
                newGroupsLbl.setText("New Group Membership");
                newGroupsLbl.setFont(new Font("Arial", newGroupsLbl.getFont().getStyle() | Font.BOLD, newGroupsLbl.getFont().getSize() + 1));

                //---- currentGroupsLbl ----
                currentGroupsLbl.setText("Current Group Membership");
                currentGroupsLbl.setFont(new Font("Arial", currentGroupsLbl.getFont().getStyle() | Font.BOLD, currentGroupsLbl.getFont().getSize() + 1));

                //======== splitPane1 ========
                {
                    splitPane1.setDividerLocation(256);

                    //======== panel1 ========
                    {
                        panel1.setLayout(new BorderLayout());

                        //======== scrollPane1 ========
                        {

                            //---- currentGroupsList ----
                            currentGroupsList.setEnabled(false);
                            currentGroupsList.setFont(new Font("Arial", currentGroupsList.getFont().getStyle(), currentGroupsList.getFont().getSize() + 1));
                            scrollPane1.setViewportView(currentGroupsList);
                        }
                        panel1.add(scrollPane1, BorderLayout.CENTER);
                    }
                    splitPane1.setLeftComponent(panel1);

                    //======== panel2 ========
                    {
                        panel2.setLayout(new BorderLayout());

                        //======== scrollPane2 ========
                        {

                            //---- newGroupsList ----
                            newGroupsList.setFont(new Font("Arial", newGroupsList.getFont().getStyle(), newGroupsList.getFont().getSize() + 1));
                            scrollPane2.setViewportView(newGroupsList);
                        }
                        panel2.add(scrollPane2, BorderLayout.CENTER);

                        //======== panel3 ========
                        {

                            //---- addGroupLbl ----
                            addGroupLbl.setText("Add Group:");
                            addGroupLbl.setFont(new Font("Arial", addGroupLbl.getFont().getStyle(), addGroupLbl.getFont().getSize() + 1));
                            addGroupLbl.setHorizontalTextPosition(SwingConstants.CENTER);
                            addGroupLbl.setVerticalTextPosition(SwingConstants.BOTTOM);

                            //---- userGroupBox ----
                            userGroupBox.setFont(new Font("Arial", userGroupBox.getFont().getStyle(), userGroupBox.getFont().getSize() + 1));
                            userGroupBox.setEditable(true);

                            //---- addGroupBtn ----
                            addGroupBtn.setText("+");
                            addGroupBtn.setFont(new Font("Arial", addGroupBtn.getFont().getStyle(), addGroupBtn.getFont().getSize() + 1));
                            addGroupBtn.setPreferredSize(new Dimension(38, 38));
                            addGroupBtn.setMinimumSize(new Dimension(37, 37));
                            addGroupBtn.setMaximumSize(new Dimension(37, 37));

                            //---- removeMembershipBtn ----
                            removeMembershipBtn.setText("-");
                            removeMembershipBtn.setFont(new Font("Arial", removeMembershipBtn.getFont().getStyle(), removeMembershipBtn.getFont().getSize() + 1));
                            removeMembershipBtn.setMaximumSize(new Dimension(37, 37));
                            removeMembershipBtn.setMinimumSize(new Dimension(37, 37));
                            removeMembershipBtn.setPreferredSize(new Dimension(37, 37));

                            GroupLayout panel3Layout = new GroupLayout(panel3);
                            panel3.setLayout(panel3Layout);
                            panel3Layout.setHorizontalGroup(
                                panel3Layout.createParallelGroup()
                                    .addGroup(panel3Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel3Layout.createParallelGroup()
                                            .addGroup(panel3Layout.createSequentialGroup()
                                                .addComponent(addGroupLbl, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(145, Short.MAX_VALUE))
                                            .addGroup(panel3Layout.createSequentialGroup()
                                                .addComponent(userGroupBox, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(addGroupBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(removeMembershipBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(3, 3, 3))))
                            );
                            panel3Layout.setVerticalGroup(
                                panel3Layout.createParallelGroup()
                                    .addGroup(panel3Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(addGroupLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(userGroupBox, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(addGroupBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(removeMembershipBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            );
                        }
                        panel2.add(panel3, BorderLayout.SOUTH);
                    }
                    splitPane1.setRightComponent(panel2);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(changeUserGroupTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(currentGroupsLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(newGroupsLbl, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
                                    .addGap(19, 19, 19)))
                            .addContainerGap())
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(splitPane1, GroupLayout.PREFERRED_SIZE, 512, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addComponent(changeUserGroupTitle)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(currentGroupsLbl)
                                .addComponent(newGroupsLbl))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(splitPane1, GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
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
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners() {
        removeMembershipBtn.addActionListener(
                e -> {
                    userGroupBox.addItem(newGroupsList.getSelectedValue().toString());
                    newGroupModel.removeElement(newGroupsList.getSelectedValue());
                    if (userGroupBox.getModel().getSize() > 0) {
                        addGroupBtn.setEnabled(true);
                    }
                });

        addGroupBtn.addActionListener(
                e -> {
                    newGroupModel.addElement(userGroupBox.getSelectedItem());
                    userGroupBox.removeItem(userGroupBox.getSelectedItem());
                    if (userGroupBox.getModel().getSize() < 1) {
                        addGroupBtn.setEnabled(false);
                    }
                });

        okButton.addActionListener(
                e -> {
                    ArrayList<String> newGroups = new ArrayList<>();
                    for (int i = 0; i < newGroupModel.getSize(); i++) {
                        newGroups.add(newGroupModel.getElementAt(i).toString());
                    }
                    if (!newGroups.isEmpty()) {
                        try {
                            FileClient.updateUserGroupMembership(serverAddress, port, userAccount, newGroups.toString().replace("[", "").replace("]", ""), MeshFS.properties.getProperty("uuid"));
                            dispose();
                        } catch (IOException | MalformedRequestException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(
                                    changeUserGroup,
                                    "Error updating groups",
                                    "MeshFS - Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
    }

    private void updateGroupInfo() {
        String myGroups = null;
        String allGroups = null;
        try {
            myGroups = FileClient.getUserGroups(serverAddress, port, userAccount, MeshFS.properties.getProperty("uuid"));
            allGroups = FileClient.getGroups(serverAddress, port, MeshFS.properties.getProperty("uuid"));

        } catch (MalformedRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (myGroups == null || allGroups == null) {
            return;
        }
        java.util.List<String> myGroupsList = Arrays.asList(myGroups.split(", "));
        java.util.List<String> allGroupsList = new ArrayList<>();
        allGroupsList.addAll(Arrays.asList(allGroups.split(", ")));
        Set<String> allGroupsDistinct = new HashSet<>();
        allGroupsDistinct.addAll(allGroupsList);
        allGroupsList.clear();
        allGroupsList.addAll(allGroupsDistinct);
        Collections.sort(allGroupsList);
        Collections.sort(myGroupsList);


        for (String myGroup : myGroupsList) {
            currentGroupModel.addElement(myGroup);
            newGroupModel.addElement(myGroup);
        }

        ArrayList<String> existingGroups = new ArrayList<>();
        for (int i = 0; i < currentGroupModel.getSize(); i++) {
            existingGroups.add(currentGroupModel.getElementAt(i).toString());
        }

        Collections.sort(existingGroups);

        for (String aGroup : allGroupsList) {
            if (!existingGroups.contains(aGroup)) {
                userGroupBox.addItem(aGroup);
            }
        }
    }

    public static void run(JFrame sender, String userAccount, String serverAddress, int port) {
        changeUserGroup = new ChangeUserGroup(userAccount, serverAddress, port);
        CenterWindow.centerOnWindow(sender, changeUserGroup);
        changeUserGroup.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel changeUserGroupTitle;
    private JLabel newGroupsLbl;
    private JLabel currentGroupsLbl;
    private JSplitPane splitPane1;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JList currentGroupsList;
    private JPanel panel2;
    private JScrollPane scrollPane2;
    private JList newGroupsList;
    private JPanel panel3;
    private JLabel addGroupLbl;
    private JComboBox userGroupBox;
    private JButton addGroupBtn;
    private JButton removeMembershipBtn;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
