import org.apache.commons.cli.*;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The CliParser class allows for the reading of command line arguments. Only calling on the class
 * is necessary.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */
class CliParser {

    private final Options opt = new Options();

    CliParser(String[] args) {
        opt.addOption("h", "help", false, "Display application's help message.");
        opt.addOption("m", "masterIP", true, "IP of master (if self, use 127.0.0.1 or own IP).");
        opt.addOption(
                "r", "regenconfig", false, "Regenerate application's default configuration file.");
        opt.addOption("nogui", false, "Run MeshFS without graphical user interface (server mode only)");
        opt.addOption("reconfig", false, "Reconfigure MeshFS graphically");
        opt.addOption("adduser", true, "Add user interactively");

        try {
            CommandLine cmd = (new DefaultParser()).parse(opt, args);

            if (cmd.hasOption("h")) {
                help();
            }

            if (cmd.hasOption("m")) {
                MeshFS.properties.setProperty("masterIP", cmd.getOptionValue("m"));
            }

            if (cmd.hasOption("adduser") && MeshFS.isMaster) {
                addUser(cmd.getOptionValue("add-user"));
            }


            if (cmd.hasOption("r")) {
                ConfigParser.write(ConfigParser.loadDefaultProperties());
                MeshFS.properties = ConfigParser.loadDefaultProperties();
            }

            if (cmd.hasOption("nogui")) {
                MeshFS.nogui = true;
            }

            if (cmd.hasOption("reconfig")) {
                MeshFS.configure = true;
            }

        } catch (ParseException e) {
            help();
        }
    }

    private void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MeshFS [options]", opt);
        System.exit(0);
    }

    private void addUser(String username) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        HashMap<String, String> accountsEnc = new HashMap<>();
        char[] password;

        try {
            System.out.print("Admin Username: ");
            String adminUsername = br.readLine();
            char[] adminPassword = System.console().readPassword("New Password:");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //TODO: add comparative code for existing users w/admin perms

        while (true) {
            password = System.console().readPassword("New Password:");
            char[] password2 = System.console().readPassword("Retype New Password:");
            if (Arrays.equals(password, password2)) {
                break;
            } else {
                System.err.println("Passwords do not match!");
            }
        }

        accountsEnc.put(username, generateEncryptedAuth(username, new String(password)));
        writeAuth(accountsEnc);

        System.exit(0);
    }

    private void writeAuth(HashMap<String, String> accountsEnc){
        try {
            FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + ".auth");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(accountsEnc);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateEncryptedAuth(String username, String password){
        MessageDigest messageDigest = null;


        for (int x = 0; x < username.length() - 1; x += 2) {
            try {
                password += username.charAt(x);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        assert messageDigest != null;
        messageDigest.update(password.getBytes(), 0, password.length());
        return new BigInteger(1, messageDigest.digest()).toString(256);
    }
}
