import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

/**
 * @author Mark Hedrick
 */
class GreetingsWindow extends JFrame {

    private static JFrame greetingsWindow;
    private final boolean runType;

    //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel titleLbl;
    private JScrollPane scrollPane1;
    private JTextPane greetingsPane;
    private JPanel buttonBar;
    private JButton quitButton;
    private JButton configBtn;
    //GEN-END:variables

    private GreetingsWindow(boolean runType) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        toFront();

        if (Reporting.getSystemOS().contains("Windows")) {
            setIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        this.runType = runType;

        initComponents();
        frameListeners();

        if (!runType) {
            configBtn.setText("Continue...");
        }
        greetingsPane.setSelectionStart(0);
        dialogPane.getRootPane().setDefaultButton(configBtn);
        configBtn.requestFocus();
        StyledDocument document = greetingsPane.getStyledDocument();
        SimpleAttributeSet centerAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAttribute, StyleConstants.ALIGN_CENTER);
        document.setParagraphAttributes(0, document.getLength(), centerAttribute, false);
    }

    public static void run(boolean runType, JFrame sender) {
        greetingsWindow = new GreetingsWindow(runType);
        if (!(sender == null)) {
            CenterWindow.centerOnWindow(sender, greetingsWindow);
        } else {
            CenterWindow.centerOnScreen(greetingsWindow);
        }
        greetingsWindow.setVisible(true);
    }

    private void initComponents() {
        //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        titleLbl = new JLabel();
        scrollPane1 = new JScrollPane();
        greetingsPane = new JTextPane();
        buttonBar = new JPanel();
        quitButton = new JButton();
        configBtn = new JButton();

        //======== this ========
        setTitle("MeshFS - Greetings");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {

                //---- titleLbl ----
                titleLbl.setText("Welcome to MeshFS");
                titleLbl.setFont(
                        new Font("Arial", titleLbl.getFont().getStyle(), titleLbl.getFont().getSize() + 9));
                titleLbl.setHorizontalAlignment(SwingConstants.CENTER);

                //======== scrollPane1 ========
                {

                    //---- greetingsPane ----
                    greetingsPane.setText(
                            "Greetings from the MeshFS Team!  We would like to introduce to you our latest application: Mesh File System (MeshFS).  In an enterprise environment, it can be very expensive for a company to purchase, setup and maintain a file storage server. Not only is this a problem, but managing such such a server is a challenge that most growing businesses have faced at one time or another.  Although it may seem like a daunting task to store your organization's files, our product, MeshFS, simplifies this process in such a way that you can do so using a simple user interface that connects existing technology infrastructure. MeshFS is an application that transfers digital files across an entire computer network. By using MeshFS, you can create a highly efficient file storage cluster with minimal effort or cost. This dynamic file system is also cross platform compatible, meaning that nearly any computer that runs Java will be sufficient. All file manipulation is handled with MeshFS and runs as fast as the present hardware allows! Our program, MeshFS, eliminates the need for your organization to purchase and maintain an expensive file server.  -Thank You from the MeshFS Team.");
                    greetingsPane.setEditable(false);
                    greetingsPane.setFont(
                            new Font(
                                    "Arial",
                                    greetingsPane.getFont().getStyle(),
                                    greetingsPane.getFont().getSize() + 2));
                    greetingsPane.setBorder(new EmptyBorder(5, 5, 5, 5));
                    scrollPane1.setViewportView(greetingsPane);
                }

                GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
                contentPanel.setLayout(contentPanelLayout);
                contentPanelLayout.setHorizontalGroup(
                        contentPanelLayout
                                .createParallelGroup()
                                .addGroup(
                                        contentPanelLayout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(
                                                        contentPanelLayout
                                                                .createParallelGroup()
                                                                .addComponent(
                                                                        titleLbl, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                                                                .addComponent(
                                                                        scrollPane1, GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
                                                .addContainerGap()));
                contentPanelLayout.setVerticalGroup(
                        contentPanelLayout
                                .createParallelGroup()
                                .addGroup(
                                        contentPanelLayout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(titleLbl)
                                                .addGap(12, 12, 12)
                                                .addComponent(
                                                        scrollPane1, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)));
            }
            dialogPane.add(contentPanel, BorderLayout.NORTH);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());

                //---- quitButton ----
                quitButton.setText("Quit");
                quitButton.setFont(
                        new Font("Arial", quitButton.getFont().getStyle(), quitButton.getFont().getSize() + 1));
                buttonBar.add(
                        quitButton,
                        new GridBagConstraints(
                                0,
                                0,
                                1,
                                1,
                                0.0,
                                0.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 5),
                                0,
                                0));

                //---- configBtn ----
                configBtn.setText("Configuration...");
                configBtn.setFont(
                        new Font("Arial", configBtn.getFont().getStyle(), configBtn.getFont().getSize() + 1));
                buttonBar.add(
                        configBtn,
                        new GridBagConstraints(
                                1,
                                0,
                                1,
                                1,
                                0.0,
                                0.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0),
                                0,
                                0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        //GEN-END:initComponents
    }

    private void frameListeners() {
        configBtn.addActionListener(
                e -> {
                    if (!runType) {
                        ClientModeConfiguration.run(greetingsWindow, "", false);
                        dispose();
                    } else {
                        onConfigure();
                    }
                });
        quitButton.addActionListener(
                e -> {
                    dispose();
                    System.exit(0);
                });
    }

    private void onConfigure() {
        InitialConfiguration.run(greetingsWindow);
        dispose();
    }

}
