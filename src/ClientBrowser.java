import org.json.simple.JSONObject;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
/*
 * Created by JFormDesigner on Sun Nov 06 18:04:04 MST 2016
 */



/**
 * @author Mark Hedrick
 */
public class ClientBrowser extends JFrame {

    private boolean isLoaded = false;
    private String serverAddress;
    private int port;
    private DefaultMutableTreeNode tree;
    private JSONObject jsonObj;
    private static JFrame clientBrowser;
    private String userAccount;
    private Timer catalogTimer;

    private ClientBrowser(String serverAddress, int port, String userAccount) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.userAccount = userAccount;
        initComponents();
        browserBtns(false);
        frameListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        if(Reporting.getSystemOS().contains("Windows")){
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
        TimerTask catalogCheck = new TimerTask() {
            @Override
            public void run() {
                if(checkCatalog()){
                    refreshWindow();
                    this.cancel();
                    //dispose();

                }
            }
        };
        catalogTimer = new java.util.Timer();
        catalogTimer.scheduleAtFixedRate(catalogCheck, 3000, 3000);
    }

    private void initComponents() {

        try {
            FileClient.receiveFile(serverAddress, port, ".catalog.json", ".catalog.json");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        jsonObj = JSONManipulator.getJSONObject(".catalog.json");
        tree = new DefaultMutableTreeNode(userAccount);
        tree = (readFolder(userAccount, jsonObj, tree));
        if (isLoaded) {
            dialogPane.repaint();
            dialogPane.revalidate();
        }
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree(tree);
        panel1 = new JPanel();
        uploadBtn = new JButton();
        downloadBtn = new JButton();
        newDirBtn = new JButton();
        downloadAsBtn = new JButton();
        moveBtn = new JButton();
        duplicateBtn = new JButton();
        renameBtn = new JButton();
        propertiesBtn = new JButton();
        removeBtn = new JButton();
        buttonBar = new JPanel();
        refreshBtn = new JButton();
        logoutBtn = new JButton();
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

                    //---- downloadBtn ----
                    downloadBtn.setText("Save");
                    downloadBtn.setFont(new Font("Arial", downloadBtn.getFont().getStyle(), downloadBtn.getFont().getSize() + 1));

                    //---- newDirBtn ----
                    newDirBtn.setText("New Dir...");
                    newDirBtn.setFont(new Font("Arial", newDirBtn.getFont().getStyle(), newDirBtn.getFont().getSize() + 1));

                    //---- downloadAsBtn ----
                    downloadAsBtn.setText("Save As...");
                    downloadAsBtn.setFont(new Font("Arial", downloadAsBtn.getFont().getStyle(), downloadAsBtn.getFont().getSize() + 1));

                    //---- moveBtn ----
                    moveBtn.setText("Move...");
                    moveBtn.setFont(new Font("Arial", moveBtn.getFont().getStyle(), moveBtn.getFont().getSize() + 1));

                    //---- duplicateBtn ----
                    duplicateBtn.setText("Duplicate");
                    duplicateBtn.setFont(new Font("Arial", duplicateBtn.getFont().getStyle(), duplicateBtn.getFont().getSize() + 1));

                    //---- renameBtn ----
                    renameBtn.setText("Rename...");
                    renameBtn.setFont(new Font("Arial", renameBtn.getFont().getStyle(), renameBtn.getFont().getSize() + 1));

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
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(propertiesBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(uploadBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(downloadBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(removeBtn, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(newDirBtn, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(moveBtn, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(downloadAsBtn, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                            .addComponent(renameBtn, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                            .addComponent(duplicateBtn, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
                    );
                    panel1Layout.setVerticalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(uploadBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(newDirBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(downloadBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(downloadAsBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(moveBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(duplicateBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(renameBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(propertiesBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeBtn)
                                .addContainerGap(15, Short.MAX_VALUE))
                    );
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 408, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
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
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0.0};

                //---- refreshBtn ----
                refreshBtn.setText("Refresh");
                refreshBtn.setFont(new Font("Arial", refreshBtn.getFont().getStyle(), refreshBtn.getFont().getSize() + 1));
                buttonBar.add(refreshBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- logoutBtn ----
                logoutBtn.setText("Logout");
                logoutBtn.setFont(new Font("Arial", logoutBtn.getFont().getStyle(), logoutBtn.getFont().getSize() + 1));
                buttonBar.add(logoutBtn, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
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
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
        isLoaded = true;
    }

    private void frameListeners(){
        uploadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                fileChooser.setDialogTitle("Choose File to Upload");
                fileChooser.setAcceptAllFileFilterUsed(true);
                int rVal = fileChooser.showOpenDialog(null);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    String pathToFile = fileChooser.getSelectedFile().getPath();
                    Thread upload = new Thread() {
                        public void run() {
                            try {
                                FileClient.sendFile(serverAddress, port, fileChooser.getSelectedFile().getPath(), userAccount);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    };
                    upload.start();
                }
            }
        });
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshWindow();
            }
        });
        tree1.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) throws NullPointerException {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                JSONObject contents = JSONManipulator.getItemContents(jsonObj, tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/"));
                Object type = contents.get("type");
                try{
                    if(node.toString().equals("(no files)")){
                        browserBtns(false);
                        tree1.setSelectionPath(null);
                    } else if (node.toString().equals(userAccount)) {
                        browserBtns(false);

                    } else if (type.toString().equals("tempFile")){
                        browserBtns(false);
                        tree1.setSelectionPath(null);
                    } else {
                        if (node.getChildCount() != 0) {
                            if (!(node.toString().equals(userAccount))) {
                                browserBtns(false);
                                removeBtn.setEnabled(true);
                                renameBtn.setEnabled(true);
                                duplicateBtn.setEnabled(true);
                                moveBtn.setEnabled(true);
                            }
                        } else {
                            browserBtns(true);
                            removeBtn.setEnabled(true);
                            renameBtn.setEnabled(true);
                            duplicateBtn.setEnabled(true);
                            moveBtn.setEnabled(true);
                        }
                    }
                }catch(NullPointerException npe){
                }
            }
        });
        downloadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(checkCatalog()){
                    refreshWindow();
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                String jsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/");
                JSONObject fileProperties = JSONManipulator.getItemContents(jsonObj, jsonPath);
                File localFile  = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + node.toString());
                if((localFile.exists())){
                    JOptionPane.showMessageDialog(null, "File already exists!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Thread download = new Thread() {
                    public void run() {
                        downloadFile(node.toString(), System.getProperty("user.home") + File.separator + "Downloads" + File.separator + node.toString());
                        JOptionPane.showMessageDialog(null, "Download Complete", "MeshFS - Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                };
                download.start();
            }
        });
        downloadAsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(checkCatalog()){
                    refreshWindow();
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showSaveDialog(null);
                fileChooser.setDialogTitle("Choose Save Location");
                String jsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/");
                JSONObject fileProperties = JSONManipulator.getItemContents(jsonObj, jsonPath);
                int fileSizeActual = Integer.parseInt(fileProperties.get("fileSizeActual").toString());
                File localFile  = new File(fileChooser.getSelectedFile().toString());
                if((localFile.exists())){
                    JOptionPane.showMessageDialog(null, "File already exists!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Thread download = new Thread() {
                    public void run() {
                        downloadFile(node.toString(), fileChooser.getSelectedFile().toString());
                        JOptionPane.showMessageDialog(null, "Download Complete", "MeshFS - Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                };

                download.start();
            }
        });
        propertiesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                String jsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/");
                JSONObject fileProperties = JSONManipulator.getItemContents(jsonObj, jsonPath);
                Object fileSize = fileProperties.get("fileSize");
                Object creationDate = fileProperties.get("creationDate");
                Object owner = fileProperties.get("owner");

                ClientBrowserFileProperties.run(node.toString(), fileSize.toString(), creationDate.toString(), owner.toString(), clientBrowser, fileProperties);
            }
        });
        removeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String jsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/");
                try {
                    FileClient.deleteFile(serverAddress, port, jsonPath);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                refreshWindow();
            }
        });
        duplicateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String jsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/");
                try {
                    FileClient.duplicateFile(serverAddress, port, jsonPath);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                refreshWindow();
            }

        });
        moveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MoveFileWindow.run(tree1.getLastSelectedPathComponent().toString(), tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/"), serverAddress, port, jsonObj, clientBrowser, userAccount);
            }

        });
        quitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                catalogTimer.purge();
                catalogTimer.cancel();
                System.exit(0);
            }
        });
        newDirBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                NewDirectoryWindow.run(serverAddress, port, jsonObj, clientBrowser, userAccount);
            }
        });
        renameBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                String jsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/");
                RenameFileWindow.run(serverAddress, port, clientBrowser, jsonPath, node.toString(), userAccount);
            }
        });
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClientModeConfiguration.run(clientBrowser, serverAddress);
                dispose();
                catalogTimer.purge();
                catalogTimer.cancel();
            }
        });
    }

    private DefaultMutableTreeNode readFolder(String folderLocation, JSONObject jsonObj, DefaultMutableTreeNode branch){
        Map<String,String> folderContents = JSONManipulator.getMapOfFolderContents(jsonObj, folderLocation, userAccount);
        if (folderContents.keySet().isEmpty()){
            DefaultMutableTreeNode leaf = new DefaultMutableTreeNode("(no files)");
            branch.add(leaf);
        }
        else {
            for (String name : folderContents.keySet()) {
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(name);
                leaf.setAllowsChildren(folderContents.get(name).equals("directory"));
                if (leaf.getAllowsChildren()) {
                    String folderLocation2 = folderLocation + "/" + name;
                    readFolder(folderLocation2, jsonObj, leaf);
                }
                branch.add(leaf);
            }
        }
        return branch;
    }

    private void downloadFile(String node, String path){
         try {
            if(!(JSONManipulator.pullFile(tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/"), path, node, serverAddress, port))){
                 JOptionPane.showMessageDialog(null, "Download Failed! Please try again later...", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void browserBtns(boolean state){
        downloadBtn.setEnabled(state);
        downloadAsBtn.setEnabled(state);
        propertiesBtn.setEnabled(state);
        renameBtn.setEnabled(state);
        removeBtn.setEnabled(state);
        duplicateBtn.setEnabled(state);
        moveBtn.setEnabled(state);
    }

    private void refreshWindow(){
        try {
            FileClient.receiveFile(serverAddress, port, ".catalog.json", ".catalog.json");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ClientBrowser.run(serverAddress, port, this, userAccount);
        dispose();
    }

    public static void run(String serverAddress, int port, JFrame sender, String userAccount) {
        clientBrowser = new ClientBrowser(serverAddress, port, userAccount);
        CenterWindow.centerOnScreen(clientBrowser);
        clientBrowser.setVisible(true);
    }

    private boolean checkCatalog(){
        try {
            File tempCatalog = File.createTempFile(".catalog", ".json");
            tempCatalog.deleteOnExit();
            File localCatalogFile = new File(".catalog.json");
            FileClient.receiveFile(serverAddress, port, ".catalog.json", tempCatalog.getAbsolutePath());
            JSONObject latestCatalog = JSONManipulator.getJSONObject(tempCatalog.getAbsolutePath());
            JSONObject localCatalog = JSONManipulator.getJSONObject(localCatalogFile.getAbsolutePath());
            if(localCatalog.equals(latestCatalog)){
                tempCatalog.delete();
                return false;
            }else{
                FileClient.receiveFile(serverAddress, port, ".catalog.json", ".catalog.json");
                dispose();
                tempCatalog.delete();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JPanel panel1;
    private JButton uploadBtn;
    private JButton downloadBtn;
    private JButton newDirBtn;
    private JButton downloadAsBtn;
    private JButton moveBtn;
    private JButton duplicateBtn;
    private JButton renameBtn;
    private JButton propertiesBtn;
    private JButton removeBtn;
    private JPanel buttonBar;
    private JButton refreshBtn;
    private JButton logoutBtn;
    private JButton quitBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
