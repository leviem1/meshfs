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
 * Created by JFormDesigner on Mon Jan 16 18:44:07 MST 2017
 */



/**
 * @author User #1
 */
public class NewDirectoryWindow extends JFrame {
    private String currentJsonPath;
    private String fileName;
    private String serverAddress;
    private int port;
    private JSONObject jsonObj;
    private JFrame caller;
    private static JFrame newDirectoryWindow;
    public NewDirectoryWindow(String serverAddress, int port, JSONObject jsonObj, JFrame sender) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.jsonObj = jsonObj;
        this.caller = sender;
        initComponents();
        frameListeners();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void initComponents() {
        JSONObject jsonObj = JSONManipulator.getJSONObject(".catalog.json");
        DefaultMutableTreeNode tree = new DefaultMutableTreeNode("root");
        tree = (readFolder("root",jsonObj,tree));
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        dirNameLbl = new JLabel();
        dirNameTextField = new JTextField();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
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

                //---- label2 ----
                label2.setText("Directory Parent Folder");

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
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addComponent(dirNameLbl)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(dirNameTextField))
                                .addGroup(contentPanelLayout.createSequentialGroup()
                                    .addGroup(contentPanelLayout.createParallelGroup()
                                        .addComponent(label2)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 362, GroupLayout.PREFERRED_SIZE))
                                    .addGap(0, 0, Short.MAX_VALUE)))
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(dirNameLbl)
                                .addComponent(dirNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(label2)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(18, Short.MAX_VALUE))
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
                buttonBar.getRootPane().setDefaultButton(okButton);
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
                String newFolderPath = (tree1.getSelectionPath().toString().substring(1, tree1.getSelectionPath().toString().length()-1).replaceAll("[ ]*, ", "/")+"/");
                String directoryName = dirNameTextField.getText();
                System.out.println(newFolderPath);
                try {
                    FileClient.addFolder(serverAddress, port, newFolderPath, directoryName);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
                caller.dispose();

                ClientBrowser.run(serverAddress, port, newDirectoryWindow);

            }
        });
    }

    private DefaultMutableTreeNode readFolder(String folderLocation, JSONObject jsonObj, DefaultMutableTreeNode branch){
        Map<String,String> folderContents = JSONManipulator.getMapOfFolderContents(jsonObj, folderLocation);
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

    public static void run(String serverAddress, int port, JSONObject jsonObj, JFrame sender){
        JFrame newDirectoryWindow = new NewDirectoryWindow(serverAddress, port, jsonObj, sender);
        CenterWindow.centerOnScreen(newDirectoryWindow);
        newDirectoryWindow.setVisible(true);

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel dirNameLbl;
    private JTextField dirNameTextField;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
