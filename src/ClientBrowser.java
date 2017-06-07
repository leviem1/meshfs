import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The ClientBrowser is a window responsible for letting users manipulate
 * and mange their files across MeshFS
 *
 * @author Mark Hedrick
 * @version 1.0.0
 */

class ClientBrowser extends JFrame {

    private static JFrame clientBrowser;
    private final String serverAddress;
    private final int port;
    private final String userAccount;
    private final Timer catalogTimer;
    private JSONObject catalogObj;
    private boolean isLoaded = false;
    private int failureCount;
    private boolean previousRunType;
    private boolean userType;
    private JPopupMenu rightClickMenu;
    private JMenuItem duplicateBtn;
    private JMenuItem moveBtn;
    private JMenuItem renameBtn;
    private JMenuItem removeBtn;
    private JMenuItem propertiesBtn;
    private JMenuItem sendToDriveBtn;
    private JMenuItem blacklistUserBtn;

    private ClientBrowser(
            String serverAddress, int port, String userAccount, JSONObject catalogObj, boolean previousRunType) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.userAccount = userAccount;
        this.catalogObj = catalogObj;
        this.previousRunType = previousRunType;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        initComponents();

        catalogTimer = new java.util.Timer();
        tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        TimerTask catalogCheck =
                new TimerTask() {
                    @Override
                    public void run() {
                        if (failureCount >= 5) {
                            catalogTimer.cancel();
                            catalogTimer.purge();
                            JOptionPane.showMessageDialog(clientBrowser, "Server Offline!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                            ClientModeConfiguration.run(clientBrowser, serverAddress, previousRunType);
                            dispose();
                        } else {
                            catalogCheck();
                        }
                    }
                };

        catalogTimer.scheduleAtFixedRate(catalogCheck, 0, 500);
        uploadBtn.requestFocus();

        duplicateBtn = new JMenuItem("Duplicate...");
        moveBtn = new JMenuItem("Move...");
        renameBtn = new JMenuItem("Rename...");
        removeBtn = new JMenuItem("Delete...");
        propertiesBtn = new JMenuItem("Properties");
        sendToDriveBtn = new JMenuItem("Send to My Drive");
        blacklistUserBtn = new JMenuItem("Hide this File");

        frameListeners();

    }

    private void initComponents() {
        userType = userAccount.equals("admin");
        DefaultMutableTreeNode root = JSONUtils.JTreeBuilder(catalogObj, userType);
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
        uploadBtn = new JButton();
        newDirBtn = new JButton();
        downloadAsBtn = new JButton();
        buttonBar = new JPanel();
        logoutBtn = new JButton();
        optionsBtn = new JButton();
        saveFromDriveBtn = new JButton();
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

                //---- uploadBtn ----
                uploadBtn.setText("Upload...");
                uploadBtn.setFont(new Font("Arial", uploadBtn.getFont().getStyle(), uploadBtn.getFont().getSize() + 1));

                //---- newDirBtn ----
                newDirBtn.setText("New Folder");
                newDirBtn.setFont(new Font("Arial", newDirBtn.getFont().getStyle(), newDirBtn.getFont().getSize() + 1));

                //---- downloadAsBtn ----
                downloadAsBtn.setText("Save As...");
                downloadAsBtn.setFont(new Font("Arial", downloadAsBtn.getFont().getStyle(), downloadAsBtn.getFont().getSize() + 1));

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(contentPanelLayout.createParallelGroup()
                                                .addComponent(uploadBtn, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(newDirBtn, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(downloadAsBtn, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                        .addComponent(uploadBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(newDirBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(downloadAsBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(212, Short.MAX_VALUE))
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 0, 0, 374, 0};
                ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0};

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

                //---- saveFromDriveBtn ----
                saveFromDriveBtn.setText("Save from Drive...");
                saveFromDriveBtn.setFont(new Font("Arial", saveFromDriveBtn.getFont().getStyle(), saveFromDriveBtn.getFont().getSize() + 1));
                buttonBar.add(saveFromDriveBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- statusLbl ----
                statusLbl.setHorizontalAlignment(SwingConstants.CENTER);
                buttonBar.add(statusLbl, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- quitBtn ----
                quitBtn.setText("Quit");
                quitBtn.setFont(new Font("Arial", quitBtn.getFont().getStyle(), quitBtn.getFont().getSize() + 1));
                buttonBar.add(quitBtn, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(625, 440);
        setLocationRelativeTo(getOwner());
        //GEN-END:initComponents
        isLoaded = true;
    }

    private void frameListeners() {
        MouseAdapter ma = new MouseAdapter() {
            private void myPopupEvent(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                JTree tree = (JTree) e.getSource();
                TreePath path = tree.getPathForLocation(x, y);
                java.util.List<Object> treeList = Arrays.asList(path.getPath());
                StringBuilder jsonPath = new StringBuilder();
                for (Object item : treeList) {
                    jsonPath.append(item.toString()).append("/");
                }
                JSONObject contents = JSONUtils.getItemContents(catalogObj, jsonPath.toString());
                String type = contents.get("type").toString();

                if (type.equals("file") && !path.getLastPathComponent().toString().equals(userAccount) && !path.getLastPathComponent().toString().equals("root") && !path.getLastPathComponent().toString().equals("Shared") && !path.getLastPathComponent().toString().equals("Users")) {
                    rightClickMenu = new JPopupMenu();
                    rightClickMenu.add(renameBtn);
                    rightClickMenu.add(moveBtn);
                    rightClickMenu.add(duplicateBtn);
                    rightClickMenu.add(new JPopupMenu.Separator());
                    rightClickMenu.add(removeBtn);
                    rightClickMenu.add(blacklistUserBtn);
                    rightClickMenu.add(sendToDriveBtn);
                    rightClickMenu.add(new JPopupMenu.Separator());
                    rightClickMenu.add(propertiesBtn);
                    tree.setSelectionPath(path);
                    rightClickMenu.show(tree, x, y);
                } else if (type.equals("directory") && !path.getLastPathComponent().toString().equals(userAccount) && !path.getLastPathComponent().toString().equals("root") && !path.getLastPathComponent().toString().equals("Shared") && !path.getLastPathComponent().toString().equals("Users") && !path.getLastPathComponent().toString().equals(" (uploading)") && !path.getLastPathComponent().toString().equals(" (corrupted)") && !path.getLastPathComponent().toString().equals(" (distributing)")) {
                    rightClickMenu = new JPopupMenu();
                    rightClickMenu.add(renameBtn);
                    rightClickMenu.add(moveBtn);
                    rightClickMenu.add(duplicateBtn);
                    rightClickMenu.add(new JPopupMenu.Separator());
                    rightClickMenu.add(removeBtn);
                    rightClickMenu.add(new JPopupMenu.Separator());
                    rightClickMenu.add(propertiesBtn);
                    tree.setSelectionPath(path);
                    rightClickMenu.show(tree, x, y);
                }
            }

            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) myPopupEvent(e);
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) myPopupEvent(e);
            }
        };
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
                        JSONObject userFiles = null;
                        try {
                            userFiles = FileClient.getUserFiles(serverAddress, port, userAccount);
                        } catch (MalformedRequestException e1) {
                            e1.printStackTrace();
                        } catch (IOException ignored) {
                        }
                        Map<String, String> folderMap = JSONUtils.getMapOfFolderContents(userFiles, null);

                        for (Map.Entry<String, String> item : folderMap.entrySet()) {
                            if (item.getKey().equals(fileChooser.getSelectedFile().getName())) {
                                JOptionPane.showMessageDialog(
                                        clientBrowser,
                                        "File already exists on this MeshFS server!",
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
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                    if (node == null) {
                        return;
                    }

                    if (node.toString().equals(userAccount) || node.toString().equals("Shared") || node.toString().equals("root") || node.toString().contains(" (corrupted)") || node.toString().equals(" (uploading)") || node.toString().equals(" (distributing)") || node.toString().equals(" (no files)") ) {
                        tree1.setSelectionPath(null);
                        newDirBtn.setEnabled(false);
                        downloadAsBtn.setEnabled(false);
                        return;
                    }else{
                        newDirBtn.setEnabled(true);
                        downloadAsBtn.setEnabled(true);
                    }

                    java.util.List<Object> treeList = Arrays.asList(tree1.getSelectionPath().getPath());
                    StringBuilder treePath = new StringBuilder();
                    for (Object item : treeList) {
                        treePath.append(item.toString()).append("/");
                    }
                    treePath = new StringBuilder(treePath.substring(0, treePath.length() - 1));

                    Object type = JSONUtils.getItemContents(catalogObj, treePath.toString());
                    try {
                        if (node.toString().equals("(no files)")) {
                            tree1.setSelectionPath(null);
                        } else if (type.toString().equals("tempFile")) {
                            tree1.setSelectionPath(null);
                        } else if (type.toString().equals("directory")) {
                            propertiesBtn.setEnabled(true);
                        } else {
                            if (node.getChildCount() != 0) {
                                if (!(node.toString().equals(userAccount))) {
                                    removeBtn.setEnabled(true);
                                    renameBtn.setEnabled(true);
                                    moveBtn.setEnabled(true);
                                }
                            } else {
                                removeBtn.setEnabled(true);
                                renameBtn.setEnabled(true);
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
                    java.util.List<Object> treeList = Arrays.asList(tree1.getSelectionPath().getPath());
                    StringBuilder jsonPath = new StringBuilder();
                    for (Object item : treeList) {
                        jsonPath.append(item.toString()).append("/");
                    }
                    jsonPath = new StringBuilder(jsonPath.substring(0, jsonPath.length() - 1));
                    JSONObject fileProperties = JSONUtils.getItemContents(catalogObj, jsonPath.toString());

                    ClientBrowserFileProperties.run(clientBrowser, fileProperties, userAccount, serverAddress, port, jsonPath.toString(), catalogObj);
                });
        removeBtn.addActionListener(
                e -> {
                    java.util.List<Object> treeList = Arrays.asList(tree1.getSelectionPath().getPath());
                    StringBuilder jsonPath = new StringBuilder();
                    for (Object item : treeList) {
                        jsonPath.append(item.toString()).append("/");
                    }
                    jsonPath = new StringBuilder(jsonPath.substring(0, jsonPath.length() - 1));
                    try {

                        tree1.collapsePath(tree1.getSelectionPath());


                        FileClient.deleteFile(serverAddress, port, jsonPath.toString());
                        catalogCheck();
                    } catch (IOException ignored) {
                    } catch (MalformedRequestException e1) {
                        e1.printStackTrace();
                    }
                });
        duplicateBtn.addActionListener(
                e -> {
                    java.util.List<Object> treeList = Arrays.asList(tree1.getSelectionPath().getPath());
                    StringBuilder jsonPath = new StringBuilder();
                    for (Object item : treeList) {
                        jsonPath.append(item.toString()).append("/");
                    }
                    jsonPath = new StringBuilder(jsonPath.substring(0, jsonPath.length() - 1));
                    try {
                        FileClient.duplicateFile(serverAddress, port, jsonPath.toString());
                        catalogCheck();
                    } catch (IOException ignored) {
                    } catch (MalformedRequestException e1) {
                        e1.printStackTrace();
                    }
                });
        moveBtn.addActionListener(
                e -> {
                    java.util.List<Object> treeList = Arrays.asList(tree1.getSelectionPath().getPath());
                    StringBuilder jsonPath = new StringBuilder();
                    for (Object item : treeList) {
                        jsonPath.append(item.toString()).append("/");
                    }
                    jsonPath = new StringBuilder(jsonPath.substring(0, jsonPath.length() - 1));
                    MoveFileWindow.run(
                            tree1.getLastSelectedPathComponent().toString(),
                            jsonPath.toString(),
                            serverAddress,
                            port,
                            clientBrowser,
                            userAccount,
                            catalogObj);
                });
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
                                serverAddress, port, clientBrowser, userAccount, catalogObj));
        renameBtn.addActionListener(
                e -> {
                    DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                    java.util.List<Object> treeList = Arrays.asList(tree1.getSelectionPath().getPath());
                    StringBuilder jsonPath = new StringBuilder();
                    for (Object item : treeList) {
                        jsonPath.append(item.toString()).append("/");
                    }
                    jsonPath = new StringBuilder(jsonPath.substring(0, jsonPath.length() - 1));
                    RenameFileWindow.run(
                            serverAddress,
                            port,
                            clientBrowser,
                            jsonPath.toString(),
                            node.toString(),
                            userAccount);
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

        tree1.addMouseListener(ma);
        sendToDriveBtn.addActionListener(
                e -> {
                    java.util.List<Object> treeList = Arrays.asList(tree1.getSelectionPath().getPath());
                    StringBuilder jsonPath = new StringBuilder();
                    for (Object item : treeList) {
                        jsonPath.append(item.toString()).append("/");
                    }
                    jsonPath = new StringBuilder(jsonPath.substring(0, jsonPath.length() - 1));
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                    File tempFile = null;
                    try {
                        tempFile = File.createTempFile(node.toString(), "");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        JSONUtils.pullFile(
                                catalogObj,
                                jsonPath.toString(),
                                tempFile.getAbsolutePath(),
                                tempFile.getAbsolutePath().substring(tempFile.getAbsolutePath().lastIndexOf(File.separator)),
                                true);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (MalformedRequestException e1) {
                        e1.printStackTrace();
                    } catch (PullRequestException e1) {
                        e1.printStackTrace();
                    } catch (FileTransferException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        DriveAPI.uploadFile(tempFile, node.toString(), Files.probeContentType(Paths.get(tempFile.getAbsolutePath())), MeshFS.userUUID);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (GeneralSecurityException e1) {
                        e1.printStackTrace();
                    }
                });
        blacklistUserBtn.addActionListener(
                e -> {
                    java.util.List<Object> treeList = Arrays.asList(tree1.getSelectionPath().getPath());
                    StringBuilder jsonPath = new StringBuilder();
                    for (Object item : treeList) {
                        jsonPath.append(item.toString()).append("/");
                    }
                    jsonPath = new StringBuilder(jsonPath.substring(0, jsonPath.length() - 1));

                    try {
                        FileClient.blacklistUser(serverAddress, Integer.parseInt(MeshFS.properties.getProperty("portNumber")), JSONUtils.catalogStringFixer(jsonPath.toString()), userAccount);
                    } catch (MalformedRequestException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                });
        saveFromDriveBtn.addActionListener(
                e -> DownloadFromDrive.run(serverAddress, port, clientBrowser, userAccount));
    }

    private void downloadFile(String path) {
        java.util.List<Object> treeList = Arrays.asList(tree1.getSelectionPath().getPath());
        StringBuilder jsonPath = new StringBuilder();
        for (Object item : treeList) {
            jsonPath.append(item.toString()).append("/");
        }
        jsonPath = new StringBuilder(jsonPath.substring(0, jsonPath.length() - 1));
        try {
            JSONUtils.pullFile(
                    catalogObj,
                    jsonPath.toString(),
                    path,
                    path.substring(path.lastIndexOf(File.separator)),
                    true);
            JOptionPane.showMessageDialog(
                    null, "Download Complete", "MeshFS - Success", JOptionPane.INFORMATION_MESSAGE);
            statusLbl.setText("Download Completed!");
            Thread.sleep(1000);
            statusLbl.setText("");
        } catch (IOException ignored) {
        } catch (InterruptedException | MalformedRequestException | FileTransferException e) {
            e.printStackTrace();
        } catch (PullRequestException e) {
            try {
                java.util.List<String> locationsToCorrupt = FileRestore.findFileReferencesInCatalog(catalogObj, JSONUtils.getItemContents(catalogObj, jsonPath.toString()).get("alphanumericName").toString());
                for (String location : locationsToCorrupt) {
                    FileClient.renameFile(serverAddress, port, location, location.substring(location.lastIndexOf("/")) + " (corrupted)");
                }
            } catch (IOException | MalformedRequestException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void catalogCheck() {
        SwingUtilities.invokeLater(
                () -> {
                    try {
                        if (!clientBrowser.isVisible()) return;
                        String localCatalog = catalogObj.toString();
                        String latestCatalog = null;
                        try {
                            latestCatalog = FileClient.getUserFiles(serverAddress, port, userAccount).toString();
                        } catch (MalformedRequestException e) {
                            e.printStackTrace();
                        } catch (IOException ioe) {
                            failureCount += 1;
                        }

                        if (!localCatalog.equals(latestCatalog) && latestCatalog != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < tree1.getRowCount(); i++) {
                                if (tree1.isExpanded(i)) {
                                    sb.append(i).append(",");
                                }
                            }
                            tree1.setModel(new DefaultTreeModel(JSONUtils.JTreeBuilder((JSONObject) new JSONParser().parse(latestCatalog), userType)));
                            String[] indexes = sb.toString().split(",");
                            for (String st : indexes) {
                                int row = Integer.parseInt(st);
                                tree1.expandRow(row);

                            }
                            try {
                                catalogObj = FileClient.getUserFiles(serverAddress, port, userAccount);
                            } catch (MalformedRequestException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

        );
    }

    /**
     * The run method is responsible for spawning the window when
     * called.
     *
     * @param serverAddress   The address used to connect to the master
     *                        server
     * @param port            The port number used to connect to the
     *                        master server
     * @param sender          The parent window that called this window
     * @param userAccount     The user account of the active user
     * @param catalogObj      The JSON catalog object of your user files
     * @param previousRunType This value is used to toggle how the window
     *                        is displayed
     */

    public static void run(
            String serverAddress,
            int port,
            JFrame sender,
            String userAccount,
            JSONObject catalogObj,
            boolean previousRunType) {

        clientBrowser = new ClientBrowser(serverAddress, port, userAccount, catalogObj, previousRunType);
        CenterWindow.centerOnWindow(sender, clientBrowser);
        clientBrowser.setVisible(true);
    }

    //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JButton uploadBtn;
    private JButton newDirBtn;
    private JButton downloadAsBtn;
    private JPanel buttonBar;
    private JButton logoutBtn;
    private JButton optionsBtn;
    private JButton saveFromDriveBtn;
    private JLabel statusLbl;
    private JButton quitBtn;
    //GEN-END:variables

}
