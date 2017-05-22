import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 * @author User #1
 */
class GroupManager extends JFrame {
    private static JFrame groupManager;
    private final DefaultListModel model;
    private static JComboBox groupBox;

    public GroupManager(JComboBox groupBox) {

        GroupManager.groupBox = groupBox;
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
        cancelButton = new JButton();

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
            groupTitle.setFont(new Font("Arial", groupTitle.getFont().getStyle(), groupTitle.getFont().getSize() + 5));
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
                locationLbl.setText("Current Groups");
                locationLbl.setFont(new Font("Arial", locationLbl.getFont().getStyle() | Font.BOLD, locationLbl.getFont().getSize() + 1));

                //---- submitBtn ----
                submitBtn.setText("Add Group");
                submitBtn.setFont(new Font("Arial", submitBtn.getFont().getStyle(), submitBtn.getFont().getSize() + 1));

                //======== scrollPane1 ========
                {
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
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(locationLbl)
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                        .addComponent(groupNameLbl)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(groupNameField, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(submitBtn))
                                                .addComponent(scrollPane1)
                                                .addComponent(removeGroup, GroupLayout.Alignment.TRAILING))
                                        .addContainerGap(12, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(groupNameLbl)
                                                .addComponent(groupNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(submitBtn))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(locationLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(removeGroup)
                                        .addContainerGap(17, Short.MAX_VALUE))
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
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.setFont(new Font("Arial", cancelButton.getFont().getStyle(), cancelButton.getFont().getSize() + 1));
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
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

    public static void run(JFrame sender, JComboBox groupBox) {
        groupManager = new GroupManager(groupBox);
        if (!(sender == null)) {
            CenterWindow.centerOnWindow(sender, groupManager);
        } else {
            CenterWindow.centerOnScreen(groupManager);
        }
        groupManager.setVisible(true);
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
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
