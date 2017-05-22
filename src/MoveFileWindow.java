import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Mark Hedrick
 */
class MoveFileWindow extends JFrame {
    private static JFrame moveFileWindow;
    private final String currentJsonPath;
    private final String serverAddress;
    private final int port;
    private final String userAccount;
    private final String fileName;
    private File catalogFile;
    //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JPanel buttonBar;
    private JButton okButton;
    private JLabel label1;
    //GEN-END:variables

    private MoveFileWindow(
            String fileName,
            String currentJsonPath,
            String serverAddress,
            int port,
            String userAccount,
            File catalogFile) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        this.currentJsonPath = currentJsonPath;
        this.serverAddress = serverAddress;
        this.port = port;
        this.userAccount = userAccount;
        this.fileName = fileName;
        this.catalogFile = catalogFile;

        initComponents();
        frameListeners();

        if (fileName.length() > 35) {
            fileName = fileName.substring(0, 30) + "...";
        }

        setTitle("Move - " + fileName);

        tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    public static void run(
            String fileName,
            String filePath,
            String serverAddress,
            int port,
            JFrame sender,
            String userAccount,
            File catalogFile) {
        moveFileWindow =
                new MoveFileWindow(fileName, filePath, serverAddress, port, userAccount, catalogFile);
        CenterWindow.centerOnWindow(sender, moveFileWindow);
        moveFileWindow.setVisible(true);
    }

    private void initComponents() {
        JSONObject jsonObj = JSONUtils.getJSONObject(catalogFile.getAbsolutePath());
        boolean userType = false;
        try {
            if (FileClient.getUserType(serverAddress, port, userAccount, MeshFS.properties.getProperty("uuid")).equals("admin"))
                userType = true;
        } catch (MalformedRequestException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DefaultMutableTreeNode tree = JSONUtils.JTreeBuilder(jsonObj, userType);
        //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree(tree);
        buttonBar = new JPanel();
        okButton = new JButton();
        label1 = new JLabel();

        //======== this ========
        setTitle("filename - Move");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //======== scrollPane1 ========
                {

                    //---- tree1 ----
                    tree1.setFont(
                            new Font("Arial", tree1.getFont().getStyle(), tree1.getFont().getSize() + 1));
                    scrollPane1.setViewportView(tree1);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout
                                .createParallelGroup()
                                .addGroup(
                                        contentPanelLayout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                                                .addContainerGap()));
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout
                                .createParallelGroup()
                                .addGroup(
                                        contentPanelLayout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(
                                                        scrollPane1,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        200,
                                                        GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 80};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.setFont(
                        new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                buttonBar.add(
                        okButton,
                        new GridBagConstraints(
                                1,
                                0,
                                1,
                                1,
                                0.0,
                                0.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0),
                                0,
                                0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- label1 ----
            label1.setText("Select a location to move the selected file to:");
            label1.setFont(
                    new Font("Arial", label1.getFont().getStyle(), label1.getFont().getSize() + 1));
            dialogPane.add(label1, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        //GEN-END:initComponents
    }

    private void frameListeners() {
        tree1.addTreeSelectionListener(
                e -> {
                    DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                    buttonBar.getRootPane().setDefaultButton(okButton);
                    if (node.toString().equals("(no files)")) {
                        tree1.setSelectionPath(null);
                        okButton.setEnabled(false);
                    } else {
                        try {
                            if (node.getChildCount() == 0) {

                                if (!(node.toString().equals(userAccount))) {
                                    tree1.setSelectionPath(null);
                                }
                            }
                        } catch (NullPointerException ignored) {

                        }
                    }
                });
        okButton.addActionListener(
                e -> {
                    String newJsonPath =
                            tree1
                                    .getSelectionPath()
                                    .toString()
                                    .substring(1, tree1.getSelectionPath().toString().length() - 1)
                                    .replaceAll("[ ]*, ", "/")
                                    + "/";
                    try {
                        if (currentJsonPath.equals(newJsonPath.substring(0, newJsonPath.lastIndexOf("/")))) {
                            JOptionPane.showMessageDialog(
                                    moveFileWindow,
                                    "Cannot move directory to this location!",
                                    "MeshFS - Error",
                                    JOptionPane.ERROR_MESSAGE);
                            tree1.setSelectionPath(null);
                            okButton.setEnabled(false);
                            return;
                        }
                        try {
                            FileClient.moveFile(serverAddress, port, currentJsonPath, newJsonPath);
                        } catch (MalformedRequestException e1) {
                            e1.printStackTrace();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    dispose();
                });
    }

    private DefaultMutableTreeNode readFolder(
            String folderLocation, JSONObject jsonObj, DefaultMutableTreeNode branch) {
        Map<String, String> folderContents = JSONUtils.getMapOfFolderContents(jsonObj, null);
        folderContents.remove(fileName);
        if (!(folderContents.values().contains("directory"))) {
            DefaultMutableTreeNode leaf = new DefaultMutableTreeNode("(no files)");
            branch.add(leaf);
        } else {
            for (String name : folderContents.keySet()) {
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(name);
                leaf.setAllowsChildren(folderContents.get(name).equals("directory"));
                if (leaf.getAllowsChildren()) {
                    String folderLocation2 = folderLocation + "/" + name;
                    readFolder(folderLocation2, jsonObj, leaf);
                    if (!(leaf.toString().equals(fileName))) {
                        branch.add(leaf);
                    }
                }
            }
        }
        return branch;
    }
}
