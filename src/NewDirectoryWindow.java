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
 * @author Mark Hedrick
 */
public class NewDirectoryWindow extends JFrame {
    private String serverAddress;
    private int port;
    private JSONObject jsonObj;
    private JFrame caller;
    private static JFrame newDirectoryWindow;
    private String userAccount;
    private NewDirectoryWindow(String serverAddress, int port, JSONObject jsonObj, JFrame sender, String userAccount) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.jsonObj = jsonObj;
        this.caller = sender;
        this.userAccount = userAccount;
        initComponents();
        okButton.setEnabled(false);
        frameListeners();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        if(Reporting.getSystemOS().contains("Windows")){
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
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
                dirNameLbl.setFont(new Font("Arial", dirNameLbl.getFont().getStyle(), dirNameLbl.getFont().getSize() + 1));

                //---- dirNameTextField ----
                dirNameTextField.setFont(new Font("Arial", dirNameTextField.getFont().getStyle(), dirNameTextField.getFont().getSize() + 1));

                //---- label2 ----
                label2.setText("Directory Parent Folder");
                label2.setFont(new Font("Arial", label2.getFont().getStyle(), label2.getFont().getSize() + 1));

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
        tree1.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) throws NullPointerException {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                if(node != null){
                    okButton.setEnabled(true);
                }
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
                for(int i = 0; i < tree1.getModel().getChildCount(tree1.getModel().getRoot()); i++){
                    if(directoryName.equals(tree1.getModel().getChild(tree1.getModel().getRoot(), i).toString())){
                        dirNameTextField.setText("");
                        JOptionPane.showMessageDialog(null, "Directory already exists!", "MeshFS - Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                try {
                    FileClient.addFolder(serverAddress, port, newFolderPath, directoryName, userAccount);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
                caller.dispose();
                ClientBrowser.run(serverAddress, port, newDirectoryWindow, userAccount);
            }
        });
    }

    private DefaultMutableTreeNode readFolder(String folderLocation, JSONObject jsonObj, DefaultMutableTreeNode branch){
        Map<String,String> folderContents = JSONManipulator.getMapOfFolderContents(jsonObj, folderLocation, userAccount);
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

    public static void run(String serverAddress, int port, JSONObject jsonObj, JFrame sender, String userAccount){
        JFrame newDirectoryWindow = new NewDirectoryWindow(serverAddress, port, jsonObj, sender, userAccount);
        CenterWindow.centerOnWindow(sender, newDirectoryWindow);
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
