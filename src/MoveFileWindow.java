import com.sun.deploy.util.SessionState;
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
    private String fileName;
    private String serverAddress;
    private int port;
    private JSONObject jsonObj;
    private JFrame caller;
    private static JFrame moveFileWindow;

    public MoveFileWindow(String fileName, String currentJsonPath, String serverAddress, int port, JSONObject jsonObj, JFrame caller) {
        this.fileName = fileName;
        this.currentJsonPath = currentJsonPath;
        this.serverAddress = serverAddress;
        this.port = port;
        this.jsonObj = jsonObj;
        this.caller = caller;
        initComponents();
        frameListeners();
        this.setTitle(fileName + " - Move");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        JSONObject jsonObj = JSONReader.getJSONObject(".catalog.json");
        DefaultMutableTreeNode tree = new DefaultMutableTreeNode("root");
        tree = (readFolder("root",jsonObj,tree));
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
        tree1.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) throws NullPointerException {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                try{
                    if(node.getChildCount() == 0){
                        if(!(node.toString().equals("root"))){
                            tree1.setSelectionPath(null);
                        }
                    }
                    else{
                    }
                }catch(NullPointerException npe){

                }
            }
        });
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newJsonPath = tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/")+"/";
                try {
                    JSONWriter.writeJSONObject(".catalog.json", JSONReader.moveFile(jsonObj, currentJsonPath, newJsonPath));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    FileClient.sendFile(serverAddress, port, ".catalog.json");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
                ClientBrowser.run(serverAddress, port, moveFileWindow);
            }
        });

    }

    private DefaultMutableTreeNode readFolder(String folderLocation, JSONObject jsonObj, DefaultMutableTreeNode branch){
        Map<String,String> folderContents = JSONReader.getMapOfFolderContents(jsonObj, folderLocation);
        if (!(folderContents.values().contains("directory"))){
            DefaultMutableTreeNode leaf = new DefaultMutableTreeNode("(no folders)");
            branch.add(leaf);
        }
        else {
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

    public static void run(String fileName, String filePath, String serverAddress, int port, JSONObject jsonObj, JFrame caller) {
        JFrame moveFileWindow = new MoveFileWindow(fileName, filePath, serverAddress, port, jsonObj, caller);
        CenterWindow.centerOnScreen(moveFileWindow);
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
