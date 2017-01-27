import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Mon Jan 09 21:01:45 MST 2017
 */


public class ServerConfigConfirmation extends JFrame {
    private HashMap accountDetails;
    private Properties properties;
    private ServerConfigConfirmation(String content, HashMap accountDetails, Properties properties) {
        this.properties = properties;
        this.accountDetails = accountDetails;
        try{
            ServerModeConfiguration.writeConfig(properties);
            JOptionPane.showMessageDialog(null, "Configuration was saved!", "MeshFS - Success", JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error when saving configuration!", "MeshFS - Success", JOptionPane.ERROR_MESSAGE);
        }
        initComponents();
        frameListeners();
        configValues.setContentType("text/html");
        configValues.setText(content);
        configValues.setCaretPosition(0);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        if(Reporting.getSystemOS().contains("Windows")){
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        configValuesPane = new JScrollPane();
        configValues = new JTextPane();
        buttonBar = new JPanel();
        okButton = new JButton();
        titleLbl = new JLabel();

        //======== this ========
        setTitle("MeshFS - Server Configuration Confirmation");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- label1 ----
                label1.setText("Confirm the following values before the server launches:");
                label1.setHorizontalAlignment(SwingConstants.CENTER);
                label1.setFont(new Font("Arial", label1.getFont().getStyle(), label1.getFont().getSize() + 1));

                //======== configValuesPane ========
                {
                    configValuesPane.setViewportView(configValues);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPanelLayout.createParallelGroup()
                                .addComponent(label1, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                                .addComponent(configValuesPane, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
                            .addContainerGap())
                );
                contentPanelLayout.setVerticalGroup(
                    contentPanelLayout.createParallelGroup()
                        .addGroup(contentPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(label1)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(configValuesPane, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
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

            //---- titleLbl ----
            titleLbl.setText("Config Parameters");
            titleLbl.setFont(new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 5));
            titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPane.add(titleLbl, BorderLayout.NORTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void frameListeners(){
        okButton.addActionListener(e -> {
            try {
                onOk();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void onOk() throws IOException {
        FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository")+".auth");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(accountDetails);
        oos.flush();

        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar;
        try {
            currentJar = new File(MeshFS.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if(!currentJar.getName().endsWith(".jar")) {
                return;
            }
            final ArrayList<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-server");
            command.add("-cp");
            command.add(MeshFS.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            command.add("-jar");
            command.add(currentJar.getPath());
            command.add("-nogui");
            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            dispose();
            System.exit(0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void run(JFrame sender, String content, HashMap accountDetails, Properties properties) {
        JFrame serverConfigConfirmation = new ServerConfigConfirmation(content, accountDetails, properties);
        CenterWindow.centerOnWindow(sender, serverConfigConfirmation);
        serverConfigConfirmation.setVisible(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JScrollPane configValuesPane;
    private JTextPane configValues;
    private JPanel buttonBar;
    private JButton okButton;
    private JLabel titleLbl;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
