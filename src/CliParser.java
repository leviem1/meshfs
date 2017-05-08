import org.apache.commons.cli.*;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * The CliParser class allows for the reading of command line arguments. Only calling on the class
 * is necessary.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */
class CliParser {

    final Options opt = new Options();

    CliParser(String[] args) {
        opt.addOption("h", "help", false, "Display application's help message.");
        opt.addOption("m", "masterIP", true, "IP of master (if self, use 127.0.0.1 or own IP).");
        opt.addOption(
                "r", "regenconfig", false, "Regenerate application's default configuration file.");
        opt.addOption("nogui", false, "Run MeshFS without graphical user interface (server mode only)");
        opt.addOption("reconfig", false, "Reconfigure MeshFS graphically");
        opt.addOption("adduser", true, "Add user interactively");
        opt.addOption("u", "uuid", true, "Set UUID value for server to server communication");

        try {
            CommandLine cmd = (new DefaultParser()).parse(opt, args);

            if (cmd.hasOption("h")) {
                help();
            }

            if (cmd.hasOption("r")) {
                ConfigParser.write(ConfigParser.loadDefaultProperties());
                MeshFS.properties = ConfigParser.loadDefaultProperties();
            }

            if (cmd.hasOption("m")) {
                MeshFS.properties.setProperty("masterIP", cmd.getOptionValue("m"));
            }

            if (cmd.hasOption("nogui")) {
                MeshFS.nogui = true;
            }

            if (cmd.hasOption("reconfig")) {
                MeshFS.configure = true;
            }

            if (cmd.hasOption("u")) {
                MeshFS.properties.setProperty("uuid", cmd.getOptionValue("u"));
            }

            if (cmd.hasOption("adduser") && MeshFS.isMaster) {
                try {
                    addUser(cmd.getOptionValue("adduser"));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
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


    void addUser() {

        Scanner input = new Scanner(System.in);

        HashMap accounts = new HashMap<String, String>();
        while(true) {
            System.out.print("Username: ");
            String username = input.nextLine();
            System.out.print("Password: ");
            char[] password = System.console().readPassword();
            System.out.print("Retype Password: ");
            char[] password2 = System.console().readPassword();

            if(!Arrays.equals(password, password2)){
                System.out.println("Passwords do not match!");
                break;

            }

            String pass = new String(password);

            for (int x = 0; x < username.length() - 1; x += 2) {
                try {
                    pass += username.charAt(x);
                } catch (IndexOutOfBoundsException ignored) {
                }
            }

            MessageDigest messageDigest = null;
            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            assert messageDigest != null;
            messageDigest.update(pass.getBytes(), 0, pass.length());
            accounts.put(username, generateEncryptedAuth(username, pass));
            writeAuth(accounts);

            System.out.println("User Added!");
            System.out.println("Add another user? [y/N]");
            String response = input.nextLine();

            if(response.isEmpty() || response.toLowerCase().equals("n")){
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addUser(String username) throws IOException, ClassNotFoundException {
        char[] password = System.console().readPassword();
        System.out.print("Retype Password: ");
        char[] password2 = System.console().readPassword();

        if(!Arrays.equals(password, password2)){
            System.out.println("Passwords do not match!");
            return;
        }

        String pass = new String(password);

        FileInputStream fis = new FileInputStream(MeshFS.properties.getProperty("repository") + ".auth");
        ObjectInputStream ois = new ObjectInputStream(fis);

        HashMap<String, String> accounts;
        accounts = (HashMap) ois.readObject();

        for (int x = 0; x < username.length() - 1; x += 2) {
            try {
                pass += username.charAt(x);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert messageDigest != null;
        messageDigest.update(pass.getBytes(), 0, pass.length());
        accounts.put(username, generateEncryptedAuth(username, pass));
        writeAuth(accounts);

    }

    private void writeAuth(HashMap<String, String> accountsEnc) {
        try {
            FileOutputStream fos =
                    new FileOutputStream(MeshFS.properties.getProperty("repository") + ".auth");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(accountsEnc);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateEncryptedAuth(String username, String password) {
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
