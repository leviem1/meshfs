import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
/*
 * Created by JFormDesigner on Sat Jan 14 16:33:36 MST 2017
 */



/**
 * @author Mark Hedrick
 */
public class MoveFileWindow extends JFrame {
    private String currentJsonPath;
    private String serverAddress;
    private int port;
    private String userAccount;
    private String fileName;

    private MoveFileWindow(String fileName, String currentJsonPath, String serverAddress, int port, String userAccount) {
        this.currentJsonPath = currentJsonPath;
        this.serverAddress = serverAddress;
        this.port = port;
        this.userAccount = userAccount;
        this.fileName = fileName;
        initComponents();
        frameListeners();
        if(fileName.length() > 35){
            fileName = fileName.substring(0, 30) + "...";
        }
        this.setTitle("Move - " + fileName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        if(Reporting.getSystemOS().contains("Windows")){
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
    }

    private void initComponents() {
        JSONObject jsonObj = JSONManipulator.getJSONObject(".catalog.json");
        DefaultMutableTreeNode tree = new DefaultMutableTreeNode(userAccount);
        tree = (readFolder(userAccount, jsonObj, tree));
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree(tree);
        buttonBar = new JPanel();
        okButton = new JButton();

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
                    tree1.setFont(new Font("Arial", tree1.getFont().getStyle(), tree1.getFont().getSize() + 1));
                    scrollPane1.setViewportView(tree1);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
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
                okButton.setFont(new Font("Arial", okButton.getFont().getStyle(), okButton.getFont().getSize() + 1));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
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
        tree1.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
            buttonBar.getRootPane().setDefaultButton(okButton);
            if(node.toString().equals("(no files)")){
                tree1.setSelectionPath(null);
                okButton.setEnabled(false);
                return;
            }else{
                try{
                    if(node.getChildCount() == 0){

                        if(!(node.toString().equals(userAccount))){
                            tree1.setSelectionPath(null);
                        }
                    }
                    else{
                    }
                }catch(NullPointerException npe){

                }
            }

        });
        okButton.addActionListener(e -> {
            String newJsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/")+"/";
            try {
                if(currentJsonPath.equals(newJsonPath.substring(0, newJsonPath.lastIndexOf("/")))){
                    JOptionPane.showMessageDialog(null, "Cannot move directory to this location!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                    tree1.setSelectionPath(null);
                    okButton.setEnabled(false);
                    return;
                }
                FileClient.moveFile(serverAddress, port, currentJsonPath, newJsonPath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            dispose();
        });
    }

    private DefaultMutableTreeNode readFolder(String folderLocation, JSONObject jsonObj, DefaultMutableTreeNode branch){
        Map<String,String> folderContents = JSONManipulator.getMapOfFolderContents(jsonObj, folderLocation, userAccount);
        folderContents.remove(fileName);
        if (!(folderContents.values().contains("directory"))){
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
                    if (!(leaf.toString().equals(fileName))){
                        branch.add(leaf);
                    }
                }
            }
        }
        return branch;
    }

    public static void run(String fileName, String filePath, String serverAddress, int port, JFrame sender, String userAccount) {
        JFrame moveFileWindow = new MoveFileWindow(fileName, filePath, serverAddress, port, userAccount);
        CenterWindow.centerOnWindow(sender, moveFileWindow);
        moveFileWindow.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
