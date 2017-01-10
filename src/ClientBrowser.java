import org.json.simple.JSONObject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
/*
 * Created by JFormDesigner on Sun Nov 06 18:04:04 MST 2016
 */



/**
 * @author Mark Hedrick
 */
public class ClientBrowser extends JFrame {
    public ClientBrowser() {
        initComponents();
        frameListeners();
    }

    private void initComponents() {
        String folderLocation = "root";
        JSONObject jsonObj = JSONReader.getJSONObject(MeshFS.properties.getProperty("repository") + "catalog.json");
        DefaultMutableTreeNode tree = new DefaultMutableTreeNode("root");
        tree = (readFolder(folderLocation,jsonObj,tree));



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
        buttonBar = new JPanel();
        refreshBtn = new JButton();
        okButton = new JButton();

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
                    downloadBtn.setText("Download");

                    //---- propertiesBtn ----
                    propertiesBtn.setText("Properties");

                    //---- renameBtn ----
                    renameBtn.setText("Rename...");

                    //---- removeBtn ----
                    removeBtn.setText("Remove...");

                    //---- duplicateBtn ----
                    duplicateBtn.setText("Duplicate");

                    //---- moveBtn ----
                    moveBtn.setText("Move...");

                    //---- uploadBtn ----
                    uploadBtn.setText("Upload...");

                    GroupLayout panel1Layout = new GroupLayout(panel1);
                    panel1.setLayout(panel1Layout);
                    panel1Layout.setHorizontalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addComponent(downloadBtn, GroupLayout.Alignment.TRAILING)
                                    .addComponent(propertiesBtn, GroupLayout.Alignment.TRAILING)
                                    .addComponent(renameBtn, GroupLayout.Alignment.TRAILING)
                                    .addComponent(removeBtn, GroupLayout.Alignment.TRAILING)
                                    .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(uploadBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(moveBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(duplicateBtn, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
                    );
                    panel1Layout.setVerticalGroup(
                        panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(downloadBtn)
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
                                .addContainerGap(27, Short.MAX_VALUE))
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
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                );
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0};

                //---- refreshBtn ----
                refreshBtn.setText("Refresh");
                buttonBar.add(refreshBtn, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("OK");
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
                    //
                }
            }
        });
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tree1.updateUI();
            }
        });
    }

    private DefaultMutableTreeNode readFolder(String folderLocation, JSONObject jsonObj, DefaultMutableTreeNode branch){
        Map<String,String> folderContents = JSONReader.getMapOfFolderContents(jsonObj, folderLocation);
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

    public static void run() {
        JFrame clientBrowser = new ClientBrowser();
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
    private JPanel buttonBar;
    private JButton refreshBtn;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
