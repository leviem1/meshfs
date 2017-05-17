import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Mark Hedrick
 */
class ClientBrowser extends JFrame {

    private static JFrame clientBrowser;
    private final String serverAddress;
    private final int port;
    private final String userAccount;
    private final Timer catalogTimer;
    private final File catalogFile;
    private boolean isLoaded = false;
    private int failureCount;
    private boolean previousRunType;

    //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JPanel panel1;
    private JButton uploadBtn;
    private JButton newDirBtn;
    private JButton downloadAsBtn;
    private JButton duplicateBtn;
    private JButton moveBtn;
    private JButton renameBtn;
    private JButton shareBtn;
    private JButton propertiesBtn;
    private JButton removeBtn;
    private JPanel buttonBar;
    private JButton logoutBtn;
    private JButton optionsBtn;
    private JLabel statusLbl;
    private JButton quitBtn;
    private String uuid;
    //GEN-END:variables

    private ClientBrowser(
            String serverAddress, int port, String userAccount, File catalogFile, boolean previousRunType, String uuid) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        this.serverAddress = serverAddress;
        this.port = port;
        this.userAccount = userAccount;
        this.catalogFile = catalogFile;
        this.previousRunType = previousRunType;
        this.uuid = uuid;

        catalogTimer = new java.util.Timer();

        initComponents();
        frameListeners();

        clientBrowserButtonModifier(false);

        tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        TimerTask catalogCheck =
                new TimerTask() {
                    @Override
                    public void run() {
                        catalogCheck();
                    }
                };
        catalogTimer.scheduleAtFixedRate(catalogCheck, 0, 500);
        uploadBtn.requestFocus();
    }

    public static void run(
            String serverAddress,
            int port,
            JFrame sender,
            String userAccount,
            File catalogFile,
            boolean previousRunType,
            String uuid) {

        clientBrowser = new ClientBrowser(serverAddress, port, userAccount, catalogFile, previousRunType, uuid);
        CenterWindow.centerOnWindow(sender, clientBrowser);
        clientBrowser.setVisible(true);
        catalogFile.deleteOnExit();
    }

    private void initComponents() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode("root/" + userAccount);
        DefaultMutableTreeNode sharedNode = new DefaultMutableTreeNode("root/Shared");
        /*(DefaultMutableTreeNode userRoot =
                readFolder(
                        userAccount, JSONUtils.getJSONObject(catalogFile.getAbsolutePath()), userNode));*/
        /*(DefaultMutableTreeNode sharedRoot =
                (readFolder(
                        userAccount, JSONUtils.getJSONObject(catalogFile.getAbsolutePath()), sharedNode));
        root.add(userRoot);
        root.add(sharedRoot);*/

        if (isLoaded) {
            dialogPane.repaint();
            dialogPane.revalidate();
        }
        //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree(new DefaultTreeModel(root));
        panel1 = new JPanel();
        uploadBtn = new JButton();
        newDirBtn = new JButton();
        downloadAsBtn = new JButton();
        duplicateBtn = new JButton();
        moveBtn = new JButton();
        renameBtn = new JButton();
        shareBtn = new JButton();
        propertiesBtn = new JButton();
        removeBtn = new JButton();
        buttonBar = new JPanel();
        logoutBtn = new JButton();
        optionsBtn = new JButton();
        statusLbl = new JLabel();
        quitBtn = new JButton();

        //======== this ========
        setTitle("MeshFS - Client Browser");
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
                    tree1.setFont(new Font("Arial", tree1.getFont().getStyle(), tree1.getFont().getSize() + 1));
                    scrollPane1.setViewportView(tree1);
                }

                //======== panel1 ========
                {

                    //---- uploadBtn ----
                    uploadBtn.setText("Upload...");
                    uploadBtn.setFont(new Font("Arial", uploadBtn.getFont().getStyle(), uploadBtn.getFont().getSize() + 1));

                    //---- newDirBtn ----
                    newDirBtn.setText("New Folder");
                    newDirBtn.setFont(new Font("Arial", newDirBtn.getFont().getStyle(), newDirBtn.getFont().getSize() + 1));

                    //---- downloadAsBtn ----
                    downloadAsBtn.setText("Save As...");
                    downloadAsBtn.setFont(new Font("Arial", downloadAsBtn.getFont().getStyle(), downloadAsBtn.getFont().getSize() + 1));

                    //---- duplicateBtn ----
                    duplicateBtn.setText("Duplicate");
                    duplicateBtn.setFont(new Font("Arial", duplicateBtn.getFont().getStyle(), duplicateBtn.getFont().getSize() + 1));

                    //---- moveBtn ----
                    moveBtn.setText("Move...");
                    moveBtn.setFont(new Font("Arial", moveBtn.getFont().getStyle(), moveBtn.getFont().getSize() + 1));

                    //---- renameBtn ----
                    renameBtn.setText("Rename...");
                    renameBtn.setFont(new Font("Arial", renameBtn.getFont().getStyle(), renameBtn.getFont().getSize() + 1));

                    //---- shareBtn ----
                    shareBtn.setText("Share");
                    shareBtn.setFont(new Font("Arial", shareBtn.getFont().getStyle(), shareBtn.getFont().getSize() + 1));

                    //---- propertiesBtn ----
                    propertiesBtn.setText("Properties");
                    propertiesBtn.setFont(new Font("Arial", propertiesBtn.getFont().getStyle(), propertiesBtn.getFont().getSize() + 1));

                    //---- removeBtn ----
                    removeBtn.setText("Remove");
                    removeBtn.setFont(new Font("Arial", removeBtn.getFont().getStyle(), removeBtn.getFont().getSize() + 1));

                    GroupLayout panel1Layout = new GroupLayout(panel1);
                    panel1.setLayout(panel1Layout);
                    panel1Layout.setHorizontalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(newDirBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(downloadAsBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(moveBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(renameBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(shareBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(propertiesBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(removeBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(uploadBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(duplicateBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                    panel1Layout.setVerticalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(uploadBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(newDirBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(downloadAsBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(duplicateBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(moveBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(renameBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(shareBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(propertiesBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(15, Short.MAX_VALUE))
                    );
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 408, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                                .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap())
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 374, 0};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 0.0};

                //---- logoutBtn ----
                logoutBtn.setText("Logout");
                logoutBtn.setFont(new Font("Arial", logoutBtn.getFont().getStyle(), logoutBtn.getFont().getSize() + 1));
                buttonBar.add(logoutBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- optionsBtn ----
                optionsBtn.setText("Options...");
                optionsBtn.setFont(new Font("Arial", optionsBtn.getFont().getStyle(), optionsBtn.getFont().getSize() + 1));
                buttonBar.add(optionsBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- statusLbl ----
                statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
                buttonBar.add(statusLbl, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- quitBtn ----
                quitBtn.setText("Quit");
                quitBtn.setFont(new Font("Arial", quitBtn.getFont().getStyle(), quitBtn.getFont().getSize() + 1));
                buttonBar.add(quitBtn, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(550, 465);
        setLocationRelativeTo(getOwner());
        //GEN-END:initComponents
        isLoaded = true;
    }

    private void frameListeners() {
        uploadBtn.addActionListener(
                e -> {
                    final JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                    fileChooser.setDialogTitle("Choose File to Upload");
                    fileChooser.setAcceptAllFileFilterUsed(true);
                    int rVal = fileChooser.showOpenDialog(null);
                    if (rVal == JFileChooser.APPROVE_OPTION) {
                        Map<String, String> folderMap = null;
                                /*JSONUtils.buildUserCatalog(userAccount,
                                        JSONUtils.getJSONObject(catalogFile.getAbsolutePath()),
                                        userAccount,
                                        userAccount);*/
                        for (Map.Entry<String, String> item : folderMap.entrySet()) {
                            if (item.getKey().equals(fileChooser.getSelectedFile().getName())) {
                                JOptionPane.showMessageDialog(
                                        clientBrowser,
                                        "File already exists on server!",
                                        "MeshFS - Error",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        Thread upload =
                                new Thread(
                                        () -> {
                                            try {
                                                FileClient.sendFile(
                                                        serverAddress,
                                                        port,
                                                        fileChooser.getSelectedFile().getPath(),
                                                        userAccount);
                                            } catch (IOException ignored) {
                                            } catch (MalformedRequestException e1) {
                                                e1.printStackTrace();
                                            }
                                        });
                        upload.start();
                    }
                });
        tree1.addTreeSelectionListener(
                e -> {
                    DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                    if (node == null) {
                        return;
                    }
                    JSONObject contents =
                            JSONUtils.getItemContents(
                                    JSONUtils.getJSONObject(catalogFile.getAbsolutePath()),
                                    tree1
                                            .getSelectionPath()
                                            .toString()
                                            .substring(1, tree1.getSelectionPath().toString().length() - 1)
                                            .replaceAll("[ ]*, ", "/"));
                    Object type = contents.get("type");
                    try {
                        if (node.toString().equals("(no files)")) {
                            clientBrowserButtonModifier(false);
                            tree1.setSelectionPath(null);
                        } else if (node.toString().equals(userAccount)) {
                            clientBrowserButtonModifier(false);
                        } else if (type.toString().equals("tempFile")) {
                            clientBrowserButtonModifier(false);
                            tree1.setSelectionPath(null);
                        } else {
                            if (node.getChildCount() != 0) {
                                if (!(node.toString().equals(userAccount))) {
                                    clientBrowserButtonModifier(false);
                                    removeBtn.setEnabled(true);
                                    renameBtn.setEnabled(true);
                                    duplicateBtn.setEnabled(true);
                                    moveBtn.setEnabled(true);
                                }
                            } else {
                                clientBrowserButtonModifier(true);
                                removeBtn.setEnabled(true);
                                renameBtn.setEnabled(true);
                                duplicateBtn.setEnabled(true);
                                moveBtn.setEnabled(true);
                            }
                        }
                    } catch (NullPointerException ignored) {
                    }
                });
        downloadAsBtn.addActionListener(
                e -> {
                    DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(new File(node.toString()));
                    int rVal = fileChooser.showSaveDialog(null);
                    if (rVal == JFileChooser.APPROVE_OPTION) {
                        fileChooser.setDialogTitle("Choose Save Location");
                        File localFile = new File(fileChooser.getSelectedFile().toString());
                        if ((localFile.exists())) {
                            JOptionPane.showMessageDialog(
                                    null, "File already exists!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Thread download =
                                new Thread(
                                        () -> {
                                            statusLbl.setText("Downloading...");
                                            downloadFile(fileChooser.getSelectedFile().toString());
                                        });

                        download.start();
                    }
                });
        propertiesBtn.addActionListener(
                e -> {
                    DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                    String jsonPath =
                            tree1
                                    .getSelectionPath()
                                    .toString()
                                    .substring(1, tree1.getSelectionPath().toString().length() - 1)
                                    .replaceAll("[ ]*, ", "/");
                    JSONObject jsonObject = JSONUtils.getJSONObject(catalogFile.getAbsolutePath());
                    JSONObject fileProperties = JSONUtils.getItemContents(jsonObject, jsonPath);
                    Object owner = fileProperties.get("owner");
                    JSONObject fileInfo = JSONUtils.getItemContents(jsonObject, "fileInfo/" + fileProperties.get("fileName").toString());
                    Object fileSize = fileInfo.get("fileSize");
                    Object creationDate = fileInfo.get("creationDate");


                    ClientBrowserFileProperties.run(
                            node.toString(),
                            fileSize.toString(),
                            creationDate.toString(),
                            owner.toString(),
                            clientBrowser,
                            fileProperties);
                });
        removeBtn.addActionListener(
                e -> {
                    String jsonPath =
                            tree1
                                    .getSelectionPath()
                                    .toString()
                                    .substring(1, tree1.getSelectionPath().toString().length() - 1)
                                    .replaceAll("[ ]*, ", "/");
                    try {
                        FileClient.deleteFile(serverAddress, port, jsonPath);
                        catalogCheck();
                    } catch (IOException ignored) {
                    } catch (MalformedRequestException e1) {
                        e1.printStackTrace();
                    }
                });
        duplicateBtn.addActionListener(
                e -> {
                    String jsonPath =
                            tree1
                                    .getSelectionPath()
                                    .toString()
                                    .substring(1, tree1.getSelectionPath().toString().length() - 1)
                                    .replaceAll("[ ]*, ", "/");
                    try {
                        FileClient.duplicateFile(serverAddress, port, jsonPath);
                        catalogCheck();
                    } catch (IOException ignored) {
                    } catch (MalformedRequestException e1) {
                        e1.printStackTrace();
                    }
                });
        moveBtn.addActionListener(
                e ->
                        MoveFileWindow.run(
                                tree1.getLastSelectedPathComponent().toString(),
                                tree1
                                        .getSelectionPath()
                                        .toString()
                                        .substring(1, tree1.getSelectionPath().toString().length() - 1)
                                        .replaceAll("[ ]*, ", "/"),
                                serverAddress,
                                port,
                                clientBrowser,
                                userAccount,
                                catalogFile));
        quitBtn.addActionListener(
                e -> {
                    dispose();
                    catalogTimer.purge();
                    catalogTimer.cancel();
                    System.exit(0);
                });
        newDirBtn.addActionListener(
                e ->
                        NewDirectoryWindow.run(
                                serverAddress, port, clientBrowser, userAccount, catalogFile));
        renameBtn.addActionListener(
                e -> {
                    DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                    String jsonPath =
                            tree1
                                    .getSelectionPath()
                                    .toString()
                                    .substring(1, tree1.getSelectionPath().toString().length() - 1)
                                    .replaceAll("[ ]*, ", "/");
                    RenameFileWindow.run(
                            serverAddress,
                            port,
                            clientBrowser,
                            jsonPath,
                            node.toString(),
                            userAccount,
                            catalogFile);
                });
        logoutBtn.addActionListener(
                e -> {
                    ClientModeConfiguration.run(clientBrowser, serverAddress, previousRunType);
                    dispose();
                    catalogTimer.purge();
                    catalogTimer.cancel();
                });
        optionsBtn.addActionListener(
                e -> UserAccountOptions.run(clientBrowser, userAccount, serverAddress, port, previousRunType));
    }


    private void downloadFile(String path) {
        try {
            JSONUtils.pullFile(
                    tree1
                            .getSelectionPath()
                            .toString()
                            .substring(1, tree1.getSelectionPath().toString().length() - 1)
                            .replaceAll("[ ]*, ", "/"),
                    path,
                    path.substring(path.lastIndexOf(File.separator)),
                    serverAddress,
                    port,
                    catalogFile);
            JOptionPane.showMessageDialog(
                        null, "Download Complete", "MeshFS - Success", JOptionPane.INFORMATION_MESSAGE);
                statusLbl.setText("Download Completed!");
                Thread.sleep(1000);
                statusLbl.setText("");
        } catch (IOException ignored) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (PullRequestException e) {
            e.printStackTrace();
        } catch (MalformedRequestException e) {
            e.printStackTrace();
        } catch (FileTransferException e) {
            e.printStackTrace();
        }
    }

    private void clientBrowserButtonModifier(boolean state) {
        downloadAsBtn.setEnabled(state);
        propertiesBtn.setEnabled(state);
        renameBtn.setEnabled(state);
        removeBtn.setEnabled(state);
        duplicateBtn.setEnabled(state);
        moveBtn.setEnabled(state);
        shareBtn.setEnabled(state);
    }

    private void catalogCheck() {
        SwingUtilities.invokeLater(
                () -> {
                    if (failureCount >= 5) {
                        catalogTimer.cancel();
                        catalogTimer.purge();
                        JOptionPane.showMessageDialog(
                                null, "Server Offline!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                        ClientModeConfiguration.run(clientBrowser, serverAddress, previousRunType);
                        dispose();
                    } else {
                        try {
                            File tempCatalog = File.createTempFile(".catalog", ".json");
                            tempCatalog.deleteOnExit();
                            try(FileWriter fileWriter = new FileWriter(tempCatalog.getAbsolutePath())){
                                fileWriter.write(FileClient.getUserFiles(serverAddress, port, userAccount, uuid).toString());
                            }
                            JSONObject latestCatalog =
                                    JSONUtils.getJSONObject(tempCatalog.getAbsolutePath());
                            JSONObject localCatalog =
                                    JSONUtils.getJSONObject(catalogFile.getAbsolutePath());
                            if (localCatalog.equals(latestCatalog)) {
                                tempCatalog.delete();
                            } else {
                                FileClient.receiveFile(
                                        serverAddress, port, ".catalog.json", catalogFile.getAbsolutePath());
                                clientBrowserButtonModifier(false);
                                tree1.removeAll();
                                //tree1.setModel(
                                        //new DefaultTreeModel(JSONUtils.JTreeBuilder(JSONUtils.()));
                                tempCatalog.delete();
                            }
                        } catch (IOException ignored) {
                        } catch (MalformedRequestException e) {
                            e.printStackTrace();
                        } catch (FileTransferException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
