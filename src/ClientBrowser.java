import org.json.simple.JSONObject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
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

    public ClientBrowser(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
        initComponents();
        browserBtns(false);
        frameListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        jsonObj = JSONManipulator.getJSONObject(".catalog.json");
        tree = new DefaultMutableTreeNode("root");
        tree = (readFolder("root",jsonObj,tree));
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
        downloadBtn = new JButton();
        propertiesBtn = new JButton();
        renameBtn = new JButton();
        removeBtn = new JButton();
        duplicateBtn = new JButton();
        moveBtn = new JButton();
        uploadBtn = new JButton();
        downloadAsBtn = new JButton();
        buttonBar = new JPanel();
        refreshBtn = new JButton();
        progressBar = new JProgressBar();
        sizeLbl = new JLabel();
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
                    scrollPane1.setViewportView(tree1);
                }

                //======== panel1 ========
                {

                    //---- downloadBtn ----
                    downloadBtn.setText("Save");

                    //---- propertiesBtn ----
                    propertiesBtn.setText("Properties");

                    //---- renameBtn ----
                    renameBtn.setText("Rename...");

                    //---- removeBtn ----
                    removeBtn.setText("Remove");

                    //---- duplicateBtn ----
                    duplicateBtn.setText("Duplicate");

                    //---- moveBtn ----
                    moveBtn.setText("Move...");

                    //---- uploadBtn ----
                    uploadBtn.setText("Upload...");

                    //---- downloadAsBtn ----
                    downloadAsBtn.setText("Save As...");

                    GroupLayout panel1Layout = new GroupLayout(panel1);
                    panel1.setLayout(panel1Layout);
                    panel1Layout.setHorizontalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(propertiesBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(uploadBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(moveBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(renameBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(removeBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(duplicateBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(downloadBtn, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(downloadAsBtn, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                    panel1Layout.setVerticalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(downloadBtn)
                                .addGap(8, 8, 8)
                                .addComponent(downloadAsBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(propertiesBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(renameBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(duplicateBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(moveBtn)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(uploadBtn)
                                .addContainerGap(14, Short.MAX_VALUE))
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
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE)))
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
                buttonBar.add(refreshBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
                buttonBar.add(progressBar, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
                buttonBar.add(sizeLbl, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- quitBtn ----
                quitBtn.setText("Quit");
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
                    File file = new File(pathToFile);
                    int size = Math.toIntExact(file.length());
                    String fileSize = "";

                    System.out.println(size);

                    if((int)(Math.log10(size)+1) >= 2 && (int)(Math.log10(size)+1) < 5){
                        fileSize = size + " B";
                    }
                    else if((int)(Math.log10(size)+1) >= 5 && (int)(Math.log10(size)+1) <= 7){
                        fileSize = size/1000 + " KB";
                    }
                    else if((int)(Math.log10(size)+1) > 7 && (int)(Math.log10(size)+1) <= 9){
                        fileSize = size/1000000 + " MB";
                    }
                    else if((int)(Math.log10(size)+1) > 9 && (int)(Math.log10(size)+1) <= 11){
                        fileSize = size/1000000000 + " GB";
                    }

                    System.out.println(fileSize);
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
                    String creationDate = df.format(new Date());

                    JSONObject fileObj = new JSONObject();
                    fileObj.put("type", "file");
                    fileObj.put("fileSize", fileSize);
                    fileObj.put("fileSizeActual", size);
                    fileObj.put("creationDate", creationDate);
                    try {
                        JSONWriter.writeJSONObject(".catalog.json", JSONManipulator.putItemInFolder(jsonObj, "root", fileChooser.getSelectedFile().getName(), fileObj));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        FileClient.sendFile(serverAddress, port, fileChooser.getSelectedFile().getPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        FileClient.sendFile(serverAddress, port, ".catalog.json");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    refreshWindow();
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
                try{
                    if(node.getChildCount() != 0){
                        if(!(node.toString().equals("root"))){
                            browserBtns(false);
                            tree1.setSelectionPath(null);
                        }
                    }
                    else{
                        browserBtns(true);
                    }
                }catch(NullPointerException npe){

                }
            }
        });
        downloadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                String jsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/");
                JSONObject fileProperties = JSONManipulator.getItemContents(jsonObj, jsonPath);
                int fileSizeActual = Integer.parseInt(fileProperties.get("fileSizeActual").toString());
                File localFile  = new File(System.getProperty("user.home") + "/Downloads/" + node.toString());
                if((localFile.exists())){
                    JOptionPane.showMessageDialog(null, "File already exists!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                progressBar.setMinimum(0);
                progressBar.setMaximum(fileSizeActual);
                Thread download = new Thread() {
                    public void run() {
                        downloadFile(node.toString(), System.getProperty("user.home") + "/Downloads/" + node.toString());
                        sizeLbl.setText("done");
                        JOptionPane.showMessageDialog(null, "Download Complete", "MeshFS - Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                };
                download.start();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        File outputFile = new File(System.getProperty("user.home") + "/Downloads/" + node.toString());
                        progressBar.setValue(Math.toIntExact(outputFile.length()));
                        sizeLbl.setText(outputFile.length() + "/" + fileSizeActual);
                        if(progressBar.getValue() == progressBar.getMaximum()){
                            cancel();
                        }
                    }
                };
                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(timerTask, 0, 10);
            }
        });
        downloadAsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showSaveDialog(null);
                fileChooser.setDialogTitle("Choose Save Location");
                String jsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/");
                JSONObject fileProperties = JSONManipulator.getItemContents(jsonObj, jsonPath);
                int fileSizeActual = Integer.parseInt(fileProperties.get("fileSizeActual").toString());
                File localFile  = new File(fileChooser.getSelectedFile().toString());
                System.out.println("Writing file to: " + fileChooser.getSelectedFile().toString());
                if((localFile.exists())){
                    JOptionPane.showMessageDialog(null, "File already exists!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                progressBar.setMinimum(0);
                progressBar.setMaximum(fileSizeActual);
                Thread download = new Thread() {
                    public void run() {
                        downloadFile(node.toString(), fileChooser.getSelectedFile().toString());
                        sizeLbl.setText("done");
                        JOptionPane.showMessageDialog(null, "Download Complete", "MeshFS - Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                };

                download.start();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        File outputFile = new File(fileChooser.getSelectedFile().toString());
                        progressBar.setValue(Math.toIntExact(outputFile.length()));
                        if(progressBar.getValue() == progressBar.getMaximum()){
                            cancel();
                        }
                    }
                };
                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(timerTask, 0, 10);
            }
        });
        propertiesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                String jsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/");
                JSONObject fileProperties = JSONManipulator.getItemContents(jsonObj, jsonPath);
                Object fileSize = fileProperties.get("fileSize");
                Object creationDate = fileProperties.get("creationDate");

                ClientBrowserFileProperties.run(node.toString(), fileSize.toString(), creationDate.toString(), clientBrowser);
            }
        });
        removeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String jsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/");
                try {
                    JSONWriter.writeJSONObject(".catalog.json", JSONManipulator.removeItem(jsonObj, jsonPath));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    FileClient.sendFile(serverAddress, port, ".catalog.json");
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
                    JSONWriter.writeJSONObject(".catalog.json", JSONManipulator.copyFile(jsonObj, jsonPath, jsonPath.substring(0, jsonPath.lastIndexOf("/")), true));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    FileClient.sendFile(serverAddress, port, ".catalog.json");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                refreshWindow();
            }

        });
        moveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MoveFileWindow.run(tree1.getLastSelectedPathComponent().toString(), tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/"), serverAddress, port, jsonObj, clientBrowser);
            }

        });
        quitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private DefaultMutableTreeNode readFolder(String folderLocation, JSONObject jsonObj, DefaultMutableTreeNode branch){
        Map<String,String> folderContents = JSONManipulator.getMapOfFolderContents(jsonObj, folderLocation);
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

    public void downloadFile(String node){
        try{
            FileClient.receiveFile(serverAddress, port, node.toString());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void downloadFile(String node, String path){
        try{
            FileClient.receiveFile(serverAddress, port, node.toString(), path);
        }catch(IOException ioe){
            ioe.printStackTrace();
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
        ClientBrowser.run(serverAddress, port, clientBrowser);
        dispose();
    }

    public static void run(String serverAddress, int port, JFrame sender) {
        JFrame clientBrowser = new ClientBrowser(serverAddress, port);
        CenterWindow.centerOnScreen(clientBrowser);
        clientBrowser.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JPanel panel1;
    private JButton downloadBtn;
    private JButton propertiesBtn;
    private JButton renameBtn;
    private JButton removeBtn;
    private JButton duplicateBtn;
    private JButton moveBtn;
    private JButton uploadBtn;
    private JButton downloadAsBtn;
    private JPanel buttonBar;
    private JButton refreshBtn;
    private JProgressBar progressBar;
    private JLabel sizeLbl;
    private JButton quitBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
