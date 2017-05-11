import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author Mark Hedrick
 */
class ClientBrowserFileProperties extends JFrame {
    private final DefaultListModel model;

    //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel fileNameLbl;
    private JLabel fileSizeLbl;
    private JLabel creationDateLbl;
    private JLabel ownerLbl;
    private JLabel locationLbl;
    private JScrollPane scrollPane1;
    private JList locationList;
    private JLabel fileNameValue;
    private JLabel fileSizeValue;
    private JLabel creationDateValue;
    private JLabel ownerValue;
    private JPanel buttonBar;
    private JButton shareBtn;
    private JButton okButton;
    private JLabel titleLbl;
    //GEN-END:variables

    private ClientBrowserFileProperties(
            String fileName,
            String fileSize,
            String creationDate,
            String owner,
            JSONObject itemContents) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        this.setTitle("Properties - " + fileName);

        model = new DefaultListModel();

        initComponents(fileName, fileSize, creationDate);
        frameListeners();

        String fileNameLbl = fileName;
        if (fileName.length() > 25) {
            fileNameLbl = fileName.substring(0, 22) + "...";
        } else if (fileName.length() > 13) {
            fileName = fileName.substring(0, 10) + "...";
        }

        fileNameValue.setToolTipText(fileName);
        fileNameValue.setText(fileNameLbl);
        fileSizeValue.setText(fileSize);
        creationDateValue.setText(creationDate);
        ownerValue.setText(owner);

        int pos = locationList.getModel().getSize();
        int stripes = 0;
        int wholes = 0;
        for (Object key : itemContents.keySet()) {
            String keyStr = key.toString();
            Object keyValue = itemContents.get(keyStr);
            if (keyStr.contains("stripe_")) {
                if (!keyValue.toString().equals("[]")) {
                    stripes += 1;
                }
            } else if (keyStr.contains("whole")) {
                if (!keyValue.toString().equals("[]")) {
                    wholes += 1;
                }
            }
        }

        model.add(pos, "Stripes: " + stripes);
        model.add(pos, "Wholes: " + wholes);
    }

    public static void run(
            String fileName,
            String fileSize,
            String creationDate,
            String owner,
            JFrame sender,
            JSONObject itemContents) {
        JFrame clientBrowserFileProperties =
                new ClientBrowserFileProperties(fileName, fileSize, creationDate, owner, itemContents);
        CenterWindow.centerOnWindow(sender, clientBrowserFileProperties);
        clientBrowserFileProperties.setVisible(true);
    }

    private void initComponents(String fileName, String fileSize, String creationDate) {

        //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        fileNameLbl = new JLabel();
        fileSizeLbl = new JLabel();
        creationDateLbl = new JLabel();
        ownerLbl = new JLabel();
        locationLbl = new JLabel();
        scrollPane1 = new JScrollPane();
        locationList = new JList(model);
        fileNameValue = new JLabel(fileName);
        fileSizeValue = new JLabel(fileSize);
        creationDateValue = new JLabel(creationDate);
        ownerValue = new JLabel();
        buttonBar = new JPanel();
        shareBtn = new JButton();
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
                fileNameLbl.setFont(new Font("Arial", Font.PLAIN, fileNameLbl.getFont().getSize() + 1));

                //---- fileSizeLbl ----
                fileSizeLbl.setText("File Size:");
                fileSizeLbl.setFont(new Font("Arial", Font.PLAIN, fileSizeLbl.getFont().getSize() + 1));

                //---- creationDateLbl ----
                creationDateLbl.setText("Creation Date:");
                creationDateLbl.setFont(new Font("Arial", Font.PLAIN, creationDateLbl.getFont().getSize() + 1));

                //---- ownerLbl ----
                ownerLbl.setText("Added By:");
                ownerLbl.setFont(new Font("Arial", Font.PLAIN, ownerLbl.getFont().getSize() + 1));

                //---- locationLbl ----
                locationLbl.setText("File Details:");
                locationLbl.setFont(new Font("Arial", Font.PLAIN, locationLbl.getFont().getSize() + 1));

                //======== scrollPane1 ========
                {

                    //---- locationList ----
                    locationList.setFont(new Font("Arial", Font.PLAIN, locationList.getFont().getSize() + 1));
                    locationList.setEnabled(false);
                    scrollPane1.setViewportView(locationList);
                }

                //---- fileNameValue ----
                fileNameValue.setText("null");
                fileNameValue.setFont(new Font("Arial", Font.PLAIN, fileNameValue.getFont().getSize() + 1));

                //---- fileSizeValue ----
                fileSizeValue.setText("null");
                fileSizeValue.setFont(new Font("Arial", Font.PLAIN, fileSizeValue.getFont().getSize() + 1));

                //---- creationDateValue ----
                creationDateValue.setText("null");
                creationDateValue.setFont(new Font("Arial", Font.PLAIN, creationDateValue.getFont().getSize() + 1));

                //---- ownerValue ----
                ownerValue.setText("null");
                ownerValue.setFont(new Font("Arial", Font.PLAIN, ownerValue.getFont().getSize() + 1));

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
                                            .addComponent(ownerLbl)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(ownerValue))
                                        .addComponent(locationLbl)
                                        .addGroup(contentPanelLayout.createSequentialGroup()
                                            .addComponent(creationDateLbl)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(creationDateValue)))
                                    .addGap(0, 142, Short.MAX_VALUE)))
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
                                .addComponent(ownerLbl)
                                .addComponent(ownerValue))
                            .addGap(18, 18, 18)
                            .addComponent(locationLbl)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0};

                //---- shareBtn ----
                shareBtn.setText("Share...");
                shareBtn.setFont(new Font("Arial", Font.PLAIN, shareBtn.getFont().getSize() + 1));
                buttonBar.add(shareBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setFont(new Font("Arial", Font.PLAIN, okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- titleLbl ----
            titleLbl.setText("File Properties");
            titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
            titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPane.add(titleLbl, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        //GEN-END:initComponents
    }

    private void frameListeners() {
        okButton.addActionListener(e -> dispose());
    }
}
