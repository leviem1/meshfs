import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 * @author User #1
 */
class SharingSettings extends JFrame {
    private static JFrame sharingSettings;

    public SharingSettings(String itemName) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        this.setTitle("Sharing Settings - " + itemName);
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        fileNameLbl = new JLabel();
        scrollPane1 = new JScrollPane();
        list1 = new JList();
        fileNameLbl3 = new JLabel();
        addUserBox = new JComboBox();
        addUserBtn = new JButton();
        removeUserBtn = new JButton();
        fileNameLbl2 = new JLabel();
        scrollPane2 = new JScrollPane();
        list2 = new JList();
        fileNameLbl4 = new JLabel();
        addGroupBox = new JComboBox();
        addGroupBtn = new JButton();
        removeGroupBtn = new JButton();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        titleLbl = new JLabel();

        //======== this ========
        setTitle("filename - Sharing Settings");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- fileNameLbl ----
                fileNameLbl.setText("Users");
                fileNameLbl.setFont(new Font("Arial", Font.BOLD, fileNameLbl.getFont().getSize() + 1));

                //======== scrollPane1 ========
                {

                    //---- list1 ----
                    list1.setBorder(null);
                    list1.setFont(new Font("Arial", list1.getFont().getStyle(), list1.getFont().getSize()));
                    scrollPane1.setViewportView(list1);
                }

                //---- fileNameLbl3 ----
                fileNameLbl3.setText("Add User:");
                fileNameLbl3.setFont(new Font("Arial", fileNameLbl3.getFont().getStyle(), fileNameLbl3.getFont().getSize()));

                //---- addUserBox ----
                addUserBox.setFont(new Font("Arial", addUserBox.getFont().getStyle(), addUserBox.getFont().getSize()));
                addUserBox.setEditable(true);

                //---- addUserBtn ----
                addUserBtn.setText("+");
                addUserBtn.setFont(new Font("Arial", addUserBtn.getFont().getStyle(), addUserBtn.getFont().getSize()));

                //---- removeUserBtn ----
                removeUserBtn.setText("-");
                removeUserBtn.setEnabled(false);
                removeUserBtn.setFont(new Font("Arial", removeUserBtn.getFont().getStyle(), removeUserBtn.getFont().getSize()));

                //---- fileNameLbl2 ----
                fileNameLbl2.setText("Groups");
                fileNameLbl2.setFont(new Font("Arial", Font.BOLD, fileNameLbl2.getFont().getSize() + 1));

                //======== scrollPane2 ========
                {

                    //---- list2 ----
                    list2.setFont(new Font("Arial", list2.getFont().getStyle(), list2.getFont().getSize()));
                    scrollPane2.setViewportView(list2);
                }

                //---- fileNameLbl4 ----
                fileNameLbl4.setText("Add Group:");
                fileNameLbl4.setFont(new Font("Arial", fileNameLbl4.getFont().getStyle(), fileNameLbl4.getFont().getSize()));

                //---- addGroupBox ----
                addGroupBox.setFont(new Font("Arial", addGroupBox.getFont().getStyle(), addGroupBox.getFont().getSize()));
                addGroupBox.setEditable(true);

                //---- addGroupBtn ----
                addGroupBtn.setText("+");
                addGroupBtn.setFont(new Font("Arial", addGroupBtn.getFont().getStyle(), addGroupBtn.getFont().getSize()));

                //---- removeGroupBtn ----
                removeGroupBtn.setText("-");
                removeGroupBtn.setEnabled(false);
                removeGroupBtn.setFont(new Font("Arial", removeGroupBtn.getFont().getStyle(), removeGroupBtn.getFont().getSize()));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(fileNameLbl, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 319, GroupLayout.PREFERRED_SIZE)
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(fileNameLbl3, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(addUserBox, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(addUserBtn)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(removeUserBtn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                                .addComponent(fileNameLbl2, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                                .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 319, GroupLayout.PREFERRED_SIZE)
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(fileNameLbl4)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(addGroupBox, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(addGroupBtn)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(removeGroupBtn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap(169, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(fileNameLbl)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(fileNameLbl3)
                                .addComponent(addUserBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(addUserBtn)
                                .addComponent(removeUserBtn, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(fileNameLbl2)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(fileNameLbl4)
                                .addComponent(addGroupBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(addGroupBtn)
                                .addComponent(removeGroupBtn, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
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
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel fileNameLbl;
    private JScrollPane scrollPane1;
    private JList list1;
    private JLabel fileNameLbl3;
    private JComboBox addUserBox;
    private JButton addUserBtn;
    private JButton removeUserBtn;
    private JLabel fileNameLbl2;
    private JScrollPane scrollPane2;
    private JList list2;
    private JLabel fileNameLbl4;
    private JComboBox addGroupBox;
    private JButton addGroupBtn;
    private JButton removeGroupBtn;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel titleLbl;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
