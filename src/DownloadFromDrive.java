import com.google.api.services.drive.Drive;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
/*
 * Created by JFormDesigner on Fri Jun 02 08:36:58 MDT 2017
 */



/**
 * @author Mark Hedrick
 */
class DownloadFromDrive extends JFrame {

    private static JFrame downloadFromDrive;
    private final String serverAddress;
    private final int port;
    private JSONObject masterJSON;
    private JSONObject root;
    private String username;
    private JFrame sender;



    public DownloadFromDrive(String serverAddress, int port, String username, JFrame sender) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.username = username;
        this.sender = sender;

        masterJSON = new JSONObject();
        root = new JSONObject();

        initComponents();
        try {
            root.put("type", "directory");
            root.put("ID", "root");
            root.put("generated", "false");
            masterJSON.put("root", root);

            masterJSON = DriveAPI.googleJsonBuilder(MeshFS.userUUID, masterJSON, "root");

            System.out.println(masterJSON);
            tree1.setModel(new DefaultTreeModel(JSONUtils.JTreeBuilder(masterJSON, true)));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        frameListeners();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        titleLbl = new JLabel();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree();
        buttonBar = new JPanel();
        downloadBtn = new JButton();
        okButton = new JButton();

        //======== this ========
        setTitle("Google Drive - MeshFS");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- titleLbl ----
                titleLbl.setText("Google Drive - My Files");
                titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 9));
                titleLbl.setHorizontalAlignment(SwingConstants.CENTER);

                //======== scrollPane1 ========
                {
                    scrollPane1.setViewportView(tree1);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addComponent(titleLbl, GroupLayout.PREFERRED_SIZE, 631, GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane1)
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addComponent(titleLbl)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .addContainerGap())
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0};

                //---- downloadBtn ----
                downloadBtn.setText("Save to Mesh...");
                downloadBtn.setFont(new Font("Arial", downloadBtn.getFont().getStyle(), downloadBtn.getFont().getSize() + 1));
                buttonBar.add(downloadBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("OK");
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners(){
        TreeWillExpandListener treeWillExpandListener = new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();

                if(node.getChildCount() == 1 && node.getChildAt(0).toString().equals("(loading...)")) {
                    java.util.List<Object> treeList = Arrays.asList(event.getPath().getPath());
                    StringBuilder treePath = new StringBuilder();
                    for (Object item : treeList) {
                        treePath.append(item.toString()).append("/");
                    }
                    treePath = new StringBuilder(treePath.substring(0, treePath.length() - 1));
                    try {
                        masterJSON = DriveAPI.googleJsonBuilder(MeshFS.userUUID, masterJSON, treePath.toString());
                    } catch (IOException | GeneralSecurityException e1) {
                        e1.printStackTrace();
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < tree1.getRowCount(); i++){
                        if (tree1.isExpanded(i)){
                            sb.append(i).append(",");
                        }
                    }
                    tree1.setSelectionPath(new TreePath(node.getPath()));
                    sb.append(tree1.getLeadSelectionRow());
                    tree1.setModel(new DefaultTreeModel(JSONUtils.JTreeBuilder(masterJSON, true)));
                    String[] indexes = sb.toString().split(",");
                        for ( String st : indexes ){
                            int row = Integer.parseInt(st);
                            tree1.expandRow(row);

                        }
                    }

                tree1.setSelectionPath(new TreePath(node.getPath()));
                tree1.scrollPathToVisible(new TreePath(node.getPath()));
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {

            }
        };
        tree1.addTreeWillExpandListener(treeWillExpandListener);

        downloadBtn.addActionListener(
                e -> {
                    java.util.List<Object> treeList = Arrays.asList(tree1.getSelectionPath().getPath());
                    StringBuilder jsonPath = new StringBuilder();
                    for (Object item : treeList) {
                        jsonPath.append(item.toString()).append("/");
                    }
                    jsonPath = new StringBuilder(jsonPath.substring(0, jsonPath.length() - 1));

                    try {
                        File driveFile = DriveAPI.downloadFile(JSONUtils.getGoogleFileID(masterJSON, jsonPath.toString()), MeshFS.userUUID);
                        FileClient.sendFile(serverAddress, port, driveFile.getAbsolutePath(), username);
                        JOptionPane.showMessageDialog(
                                downloadFromDrive,
                                "File downloaded and distributed successfully!",
                                "MeshFS - Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        driveFile.delete();
                        dispose();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (MalformedRequestException e1) {
                        e1.printStackTrace();
                    } catch (GeneralSecurityException e1) {
                        e1.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(
                            downloadFromDrive,
                            "File downloaded and distribution failed!!",
                            "MeshFS - Failure",
                            JOptionPane.ERROR_MESSAGE);
                    dispose();

                });


    }

    public static void run(String serverAddress, int port, JFrame sender, String username) {
        downloadFromDrive = new DownloadFromDrive(serverAddress, port, username, sender);
        CenterWindow.centerOnWindow(sender, downloadFromDrive);
        downloadFromDrive.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel titleLbl;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JPanel buttonBar;
    private JButton downloadBtn;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
