import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
/*
 * Created by JFormDesigner on Thu Dec 01 19:07:39 MST 2016
 */



/**
 * @author Mark Hedrick
 */
public class ClientBrowserFileProperties extends JFrame {
    ClientBrowserFileProperties(String fileName, String fileSize, String creationDate) {
        initComponents(fileName, fileSize, creationDate);
        frameListeners();
        fileNameValue.setText(fileName);
        fileNameValue.setToolTipText(fileName);
        fileSizeValue.setText(fileSize);
        creationDateValue.setText(creationDate);
        this.setTitle(fileName + " - Properties");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }


    private void initComponents(String fileName, String fileSize, String creationDate) {

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        fileNameLbl = new JLabel();
        fileSizeLbl = new JLabel();
        creationDateLbl = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        scrollPane1 = new JScrollPane();
        list1 = new JList();
        fileNameValue = new JLabel(fileName);
        fileSizeValue = new JLabel(fileSize);
        creationDateValue = new JLabel(creationDate);
        label10 = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        titleLbl = new JLabel();

        //======== this ========
        setTitle("filename - Properties");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- fileNameLbl ----
                fileNameLbl.setText("File Name:");

                //---- fileSizeLbl ----
                fileSizeLbl.setText("File Size:");

                //---- creationDateLbl ----
                creationDateLbl.setText("Creation Date:");

                //---- label5 ----
                label5.setText("Added By:");

                //---- label6 ----
                label6.setText("Access Permissions:");

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(list1);
                }

                //---- fileNameValue ----
                fileNameValue.setText("null");

                //---- fileSizeValue ----
                fileSizeValue.setText("null");

                //---- creationDateValue ----
                creationDateValue.setText("null");

                //---- label10 ----
                label10.setText("guest");

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGroup(contentPanelLayout.createParallelGroup()
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(fileNameLbl)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(fileNameValue))
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(fileSizeLbl)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(fileSizeValue))
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(label5)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(label10))
                                        .addComponent(label6)
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(creationDateLbl)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(creationDateValue)))
                                    .addGap(0, 136, Short.MAX_VALUE)))
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(fileNameLbl)
                                .addComponent(fileNameValue))
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(fileSizeLbl)
                                .addComponent(fileSizeValue))
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(creationDateLbl)
                                .addComponent(creationDateValue))
                            .addGap(18, 18, 18)
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label5)
                                .addComponent(label10))
                            .addGap(18, 18, 18)
                            .addComponent(label6)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- titleLbl ----
            titleLbl.setText("File Properties");
            titleLbl.setFont(new Font("Helvetica Neue", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
            titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPane.add(titleLbl, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners(){
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public static void run(String fileName, String fileSize, String creationDate, JFrame sender) {
        JFrame clientBrowserFileProperties = new ClientBrowserFileProperties(fileName, fileSize, creationDate);
        CenterWindow.centerOnScreen(clientBrowserFileProperties);
        clientBrowserFileProperties.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel fileNameLbl;
    private JLabel fileSizeLbl;
    private JLabel creationDateLbl;
    private JLabel label5;
    private JLabel label6;
    private JScrollPane scrollPane1;
    private JList list1;
    private JLabel fileNameValue;
    private JLabel fileSizeValue;
    private JLabel creationDateValue;
    private JLabel label10;
    private JPanel buttonBar;
    private JButton okButton;
    private JLabel titleLbl;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
