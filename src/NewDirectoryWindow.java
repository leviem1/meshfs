import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Mark Hedrick
 */
class NewDirectoryWindow extends JFrame {
    private static JFrame newDirectoryWindow;
    private final String serverAddress;
    private final int port;
    private final String userAccount;
    private final String uuid;
    private File catalogFile;
    //GEN-BEGIN:variables
    private JTextField dirNameTextField;
    private JTree tree1;
    private JPanel buttonBar;
    private JButton okButton;
    //GEN-END:variables

    private NewDirectoryWindow(
            String serverAddress, int port, String userAccount, File catalogFile, String uuid) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.userAccount = userAccount;
        this.catalogFile = catalogFile;
        this.uuid = uuid;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        initComponents();
        frameListeners();

        okButton.setEnabled(false);
        tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    public static void run(
            String serverAddress,
            int port,
            JFrame sender,
            String userAccount,
            File catalogFile,
            String uuid) {
        newDirectoryWindow =
                new NewDirectoryWindow(serverAddress, port, userAccount, catalogFile, uuid);
        CenterWindow.centerOnWindow(sender, newDirectoryWindow);
        newDirectoryWindow.setVisible(true);
    }

    private void initComponents() {
        JSONObject jsonObj = JSONManipulator.getJSONObject(catalogFile.getAbsolutePath());
        DefaultMutableTreeNode tree = new DefaultMutableTreeNode(userAccount);

        tree = (readFolder(userAccount, jsonObj, tree));
        //GEN-BEGIN:initComponents

        JPanel dialogPane = new JPanel();
        JPanel contentPanel = new JPanel();
        JLabel dirNameLbl = new JLabel();
        dirNameTextField = new JTextField();
        JLabel label2 = new JLabel();
        JScrollPane scrollPane1 = new JScrollPane();
        tree1 = new JTree(tree);
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("New Directory - MeshFS");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- dirNameLbl ----
                dirNameLbl.setText("Directory Name:");
                dirNameLbl.setFont(
                        new Font("Arial", dirNameLbl.getFont().getStyle(), dirNameLbl.getFont().getSize() + 1));

                //---- dirNameTextField ----
                dirNameTextField.setFont(
                        new Font(
                                "Arial",
                                dirNameTextField.getFont().getStyle(),
                                dirNameTextField.getFont().getSize() + 1));

                //---- label2 ----
                label2.setText("Parent Folder");
                label2.setFont(
                        new Font("Arial", label2.getFont().getStyle(), label2.getFont().getSize() + 1));

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
                                                .addGroup(
                                                        contentPanelLayout
                                                                .createParallelGroup()
                                                                .addGroup(
                                                                        contentPanelLayout
                                                                                .createSequentialGroup()
                                                                                .addComponent(dirNameLbl)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(dirNameTextField))
                                                                .addGroup(
                                                                        contentPanelLayout
                                                                                .createSequentialGroup()
                                                                                .addComponent(label2)
                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                .addGroup(
                                                                        GroupLayout.Alignment.TRAILING,
                                                                        contentPanelLayout
                                                                                .createSequentialGroup()
                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                .addComponent(
                                                                                        scrollPane1,
                                                                                        GroupLayout.PREFERRED_SIZE,
                                                                                        362,
                                                                                        GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap()));
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout
                                .createParallelGroup()
                                .addGroup(
                                        contentPanelLayout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(
                                                        contentPanelLayout
                                                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(dirNameLbl)
                                                                .addComponent(
                                                                        dirNameTextField,
                                                                        GroupLayout.PREFERRED_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(label2)
                                                .addPreferredGap(
                                                        LayoutStyle.ComponentPlacement.RELATED,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        Short.MAX_VALUE)
                                                .addComponent(
                                                        scrollPane1,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        196,
                                                        GroupLayout.PREFERRED_SIZE)));
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
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        //GEN-END:initComponents
    }

    private void frameListeners() {
        tree1.addTreeSelectionListener(
                e -> checkDirectoryName());
        dirNameTextField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {
                            public void changedUpdate(DocumentEvent e) {
                                changed();
                            }

                            public void removeUpdate(DocumentEvent e) {
                                changed();
                            }

                            public void insertUpdate(DocumentEvent e) {
                                changed();
                            }

                            public void changed() {
                                checkDirectoryName();
                            }
                        });
        okButton.addActionListener(
                e -> {
                    String newFolderPath =
                            (tree1
                                    .getSelectionPath()
                                    .toString()
                                    .substring(1, tree1.getSelectionPath().toString().length() - 1)
                                    .replaceAll("[ ]*, ", "/")
                                    + "/");
                    String directoryName = dirNameTextField.getText();
                    for (int i = 0; i < tree1.getModel().getChildCount(tree1.getModel().getRoot()); i++) {
                        if (directoryName.equals(
                                tree1.getModel().getChild(tree1.getModel().getRoot(), i).toString())) {
                            dirNameTextField.setText("");
                            JOptionPane.showMessageDialog(
                                    newDirectoryWindow,
                                    "Directory already exists!",
                                    "MeshFS - Error",
                                    JOptionPane.ERROR_MESSAGE);
                            dirNameTextField.requestFocus();
                            tree1.setSelectionPath(null);
                            return;
                        }
                    }
                    try {
                        FileClient.addFolder(
                                serverAddress, port, newFolderPath, directoryName, userAccount, uuid);
                    } catch (IOException ignored) {

                    }
                    dispose();
                });
    }

    private DefaultMutableTreeNode readFolder(
            String folderLocation, JSONObject jsonObj, DefaultMutableTreeNode branch) {
        Map<String, String> folderContents =
                JSONManipulator.getMapOfFolderContents(jsonObj, folderLocation, userAccount);
        if (!(folderContents.values().contains("directory"))) {
            DefaultMutableTreeNode leaf = new DefaultMutableTreeNode("(no folders)");
            branch.add(leaf);
        } else {
            for (String name : folderContents.keySet()) {
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(name);
                leaf.setAllowsChildren(folderContents.get(name).equals("directory"));
                if (leaf.getAllowsChildren()) {
                    String folderLocation2 = folderLocation + "/" + name;
                    readFolder(folderLocation2, jsonObj, leaf);
                    branch.add(leaf);
                }
            }
        }
        return branch;
    }

    private void checkDirectoryName(){
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
        if (node != null) {
            if(!(dirNameTextField.getText().isEmpty())){
                okButton.setEnabled(true);
            }else{
                okButton.setEnabled(false);
            }
        }
        buttonBar.getRootPane().setDefaultButton(okButton);
        try {
            assert node != null;
            if (node.getChildCount() == 0) {
                if (!(node.toString().equals(userAccount))) {
                    tree1.setSelectionPath(null);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }
}
