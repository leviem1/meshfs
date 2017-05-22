import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Mark Hedrick
 */
class ClientBrowserFileProperties extends JFrame {
    private final DefaultListModel model;
    private final DefaultListModel permModel;
    private static JFrame clientBrowserFileProperties;
    private String userAccount;
    private String serverAddress;
    private int port;
    private ArrayList<String> groupData;
    private ArrayList<String> adminData;
    private ArrayList<String> blacklistData;
    private String itemPath;


    //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel fileNameLbl;
    private JLabel fileNameValue;
    private JLabel fileSizeValue;
    private JLabel creationDateLbl;
    private JLabel creationDateValue;
    private JLabel locationLbl;
    private JScrollPane scrollPane1;
    private JList locationList;
    private JLabel permissionsLbl;
    private JScrollPane scrollPane2;
    private JList permissionList;
    private JPanel buttonBar;
    private JButton shareBtn;
    private JButton okButton;
    private JLabel titleLbl;
    //GEN-END:variables

    private ClientBrowserFileProperties(JSONObject itemContents, String userAccount, String serverAddress, int port, String itemPath) {
        this.userAccount = userAccount;
        this.serverAddress = serverAddress;
        this.port = port;
        this.itemPath = itemPath;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
        model = new DefaultListModel();
        permModel = new DefaultListModel();

        String fileName = itemPath.substring(itemPath.lastIndexOf("/") + 1);

        String fileSize = null;
        String creationDate = null;

        if (itemContents.get("type").equals("directory")) {


            fileSize = JSONUtils.humanReadableByteCount(JSONUtils.totalUsedStorageOfFolder(itemPath));
            creationDate = "2017";
        }else{
            fileName = (String) itemContents.get("fileName");
            fileSize = JSONUtils.humanReadableByteCount((long)itemContents.get("fileSize"));
            creationDate = (String) itemContents.get("creationDate");
        }


        System.out.println(fileName);



        JSONArray groups = (JSONArray) itemContents.get("groups");
        JSONArray admins = (JSONArray) itemContents.get("admins");
        JSONArray blacklist = (JSONArray) itemContents.get("blacklist");

        groupData = new ArrayList<>();
        if (groups != null) {
            for (Object group : groups) {
                groupData.add((String) group);
            }
        }
        adminData = new ArrayList<>();
        if (admins != null) {
            for (Object admin : admins) {
                adminData.add((String) admin);
            }
        }

        blacklistData = new ArrayList<>();
        if (blacklist != null) {
            for (Object aBlacklist : blacklist) {
                blacklistData.add((String) aBlacklist);
            }
        }

        permModel.add(permModel.getSize(), "Groups: " + groupData.toString());
        permModel.add(permModel.getSize(), "Admins: " + adminData.toString());
        assert blacklist != null;
        permModel.add(permModel.getSize(), "Blacklisted Users: " + blacklist.toString());

        this.setTitle("Properties - " + fileName);

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

    public static void run(JFrame sender, JSONObject itemContents, String userAccount, String serverAddress, int port, String itemPath) {
        clientBrowserFileProperties = new ClientBrowserFileProperties(itemContents, userAccount, serverAddress, port, itemPath);
        CenterWindow.centerOnWindow(sender, clientBrowserFileProperties);
        clientBrowserFileProperties.setVisible(true);
    }

    private void initComponents(String fileName, String fileSize, String creationDate) {

        //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        fileNameLbl = new JLabel();
        fileNameValue = new JLabel(fileName);
        fileSizeValue = new JLabel(fileSize);
        creationDateLbl = new JLabel();
        creationDateValue = new JLabel(creationDate);
        locationLbl = new JLabel();
        scrollPane1 = new JScrollPane();
        locationList = new JList(model);
        permissionsLbl = new JLabel();
        scrollPane2 = new JScrollPane();
        permissionList = new JList(permModel);
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
                fileNameLbl.setText("Item Name:");
                fileNameLbl.setFont(new Font("Arial", Font.PLAIN, fileNameLbl.getFont().getSize() + 1));

                //---- fileNameValue ----
                fileNameValue.setText("name");
                fileNameValue.setFont(new Font("Arial", Font.PLAIN, fileNameValue.getFont().getSize() + 1));

                //---- fileSizeValue ----
                fileSizeValue.setText("(size)");
                fileSizeValue.setFont(new Font("Arial", Font.PLAIN, fileSizeValue.getFont().getSize() + 1));

                //---- creationDateLbl ----
                creationDateLbl.setText("Creation Date:");
                creationDateLbl.setFont(new Font("Arial", Font.PLAIN, creationDateLbl.getFont().getSize() + 1));

                //---- creationDateValue ----
                creationDateValue.setText("date");
                creationDateValue.setFont(new Font("Arial", Font.PLAIN, creationDateValue.getFont().getSize() + 1));

                //---- locationLbl ----
                locationLbl.setText("Distribution Details");
                locationLbl.setFont(new Font("Arial", locationLbl.getFont().getStyle() | Font.BOLD, locationLbl.getFont().getSize() + 1));

                //======== scrollPane1 ========
                {

                    //---- locationList ----
                    locationList.setFont(new Font("Arial", Font.PLAIN, locationList.getFont().getSize() + 1));
                    locationList.setEnabled(false);
                    scrollPane1.setViewportView(locationList);
                }

                //---- permissionsLbl ----
                permissionsLbl.setText("Permissions");
                permissionsLbl.setFont(new Font("Arial", permissionsLbl.getFont().getStyle() | Font.BOLD, permissionsLbl.getFont().getSize() + 1));

                //======== scrollPane2 ========
                {

                    //---- permissionList ----
                    permissionList.setEnabled(false);
                    scrollPane2.setViewportView(permissionList);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(contentPanelLayout.createParallelGroup()
                                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                                                .addComponent(scrollPane2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                        .addGroup(contentPanelLayout.createParallelGroup()
                                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                                        .addComponent(fileNameLbl)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(fileNameValue)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(fileSizeValue, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(contentPanelLayout.createSequentialGroup()
                                                                        .addComponent(creationDateLbl)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(creationDateValue))
                                                                .addComponent(locationLbl)
                                                                .addComponent(permissionsLbl, GroupLayout.PREFERRED_SIZE, 122, GroupLayout.PREFERRED_SIZE))
                                                        .addGap(0, 184, Short.MAX_VALUE)))
                                        .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(fileNameLbl)
                                                .addComponent(fileNameValue)
                                                .addComponent(fileSizeValue))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(creationDateLbl)
                                                .addComponent(creationDateValue))
                                        .addGap(18, 18, 18)
                                        .addComponent(locationLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(permissionsLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                        .addGap(33, 33, 33))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 0, 0, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0, 0.0};

                //---- shareBtn ----
                shareBtn.setText("Edit Permissions...");
                shareBtn.setFont(new Font("Arial", shareBtn.getFont().getStyle(), shareBtn.getFont().getSize() + 1));
                buttonBar.add(shareBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setFont(new Font("Arial", Font.PLAIN, okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
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
        contentPane.add(dialogPane, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
        //GEN-END:initComponents
    }

    private void frameListeners() {
        okButton.addActionListener(e -> dispose());
        shareBtn.addActionListener(e -> SharingSettings.run(clientBrowserFileProperties, userAccount, serverAddress, port, groupData, adminData, itemPath));

    }
}
