import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 * The GroupManager is a window responsible letting
 * administrators modify the group membership of new
 * users
 *
 * @author Mark Hedrick
 * @version 1.0.0
 */

class GroupManager extends JFrame {

    private static JFrame groupManager;
    private final DefaultListModel model;
    private static JComboBox groupBox;

    private GroupManager(JComboBox groupBox) {
        GroupManager.groupBox = groupBox;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }


        model = new DefaultListModel();

        if (groupBox.getItemCount() > 0) {
            for (int x = 0; x < groupBox.getModel().getSize(); x++) {
                model.add(model.getSize(), groupBox.getItemAt(x));

            }
        }

        initComponents();
        frameListeners();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        groupTitle = new JLabel();
        contentPanel = new JPanel();
        groupNameLbl = new JLabel();
        groupNameField = new JTextField();
        locationLbl = new JLabel();
        submitBtn = new JButton();
        scrollPane1 = new JScrollPane();
        groupList = new JList(model);
        removeGroup = new JButton();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("Group Manager - MeshFS");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //---- groupTitle ----
            groupTitle.setText("Group Manager");
            groupTitle.setFont(new Font("Arial", groupTitle.getFont().getStyle(), groupTitle.getFont().getSize() + 9));
            groupTitle.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPane.add(groupTitle, BorderLayout.NORTH);

            //======== contentPanel ========
            {

                //---- groupNameLbl ----
                groupNameLbl.setText("Name:");
                groupNameLbl.setFont(new Font("Arial", groupNameLbl.getFont().getStyle(), groupNameLbl.getFont().getSize() + 1));

                //---- groupNameField ----
                groupNameField.setFont(new Font("Arial", groupNameField.getFont().getStyle(), groupNameField.getFont().getSize() + 1));

                //---- locationLbl ----
                locationLbl.setText("Available Groups");
                locationLbl.setFont(new Font("Arial", locationLbl.getFont().getStyle() | Font.BOLD, locationLbl.getFont().getSize() + 1));

                //---- submitBtn ----
                submitBtn.setText("Add Group");
                submitBtn.setFont(new Font("Arial", submitBtn.getFont().getStyle(), submitBtn.getFont().getSize() + 1));

                //======== scrollPane1 ========
                {

                    //---- groupList ----
                    groupList.setFont(new Font("Arial", groupList.getFont().getStyle(), groupList.getFont().getSize() + 1));
                    scrollPane1.setViewportView(groupList);
                }

                //---- removeGroup ----
                removeGroup.setText("Remove Group");
                removeGroup.setFont(new Font("Arial", removeGroup.getFont().getStyle(), removeGroup.getFont().getSize() + 1));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(contentPanelLayout.createParallelGroup()
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                        .addComponent(groupNameLbl)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(groupNameField, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, Short.MAX_VALUE)
                                                        .addComponent(submitBtn))
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                        .addGroup(contentPanelLayout.createParallelGroup()
                                                                .addComponent(locationLbl)
                                                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 351, GroupLayout.PREFERRED_SIZE))
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(removeGroup)))
                                        .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(groupNameLbl)
                                                .addComponent(submitBtn)
                                                .addComponent(groupNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(locationLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(removeGroup)
                                        .addContainerGap(5, Short.MAX_VALUE))
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
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(425, 385);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners() {
        submitBtn.addActionListener(
                actionEvent -> {
                    for (int i = 0; i < model.getSize(); i++) {
                        if (model.get(i).toString().equals(groupNameField.getText().toLowerCase())) {
                            JOptionPane.showMessageDialog(groupManager, "Group already exists!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                            groupNameField.setText("");
                            groupNameField.requestFocus();
                            return;

                        }
                    }
                    model.add(groupList.getModel().getSize(), groupNameField.getText().toLowerCase());
                    groupNameField.setText("");
                    groupNameField.requestFocus();

                });
        removeGroup.addActionListener(
                actionEvent -> model.remove(groupList.getSelectedIndex()));
        okButton.addActionListener(
                actionEvent -> {
                    groupBox.removeAllItems();
                    for (int i = 0; i < model.getSize(); i++) {
                        String item = model.getElementAt(i).toString();
                        if (groupBox.getModel().getSize() > 0) {
                            for (int x = 0; x < groupBox.getModel().getSize(); x++) {
                                if (!groupBox.getModel().getElementAt(x).toString().equals(item)) {
                                    groupBox.addItem(item);
                                }
                            }
                        } else {
                            groupBox.addItem(item);
                        }
                    }
                    dispose();
                });
    }

    /**
     * The run method is responsible for spawning the window when called.
     *
     * @param sender   The parent window that called this window
     * @param groupBox The dropdown box containing existing groups
     */

    public static void run(JFrame sender, JComboBox groupBox) {
        groupManager = new GroupManager(groupBox);
        if (!(sender == null)) {
            CenterWindow.centerOnWindow(sender, groupManager);
        } else {
            CenterWindow.centerOnScreen(groupManager);
        }
        groupManager.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JLabel groupTitle;
    private JPanel contentPanel;
    private JLabel groupNameLbl;
    private JTextField groupNameField;
    private JLabel locationLbl;
    private JButton submitBtn;
    private JScrollPane scrollPane1;
    private JList groupList;
    private JButton removeGroup;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
