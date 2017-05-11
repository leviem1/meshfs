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
    CommandLine cmd;

    CliParser(String[] args) {
        opt.addOption("h", "help", false, "Display application's help message.");
        opt.addOption("m", "masterIP", true, "IP of master (if self, use 127.0.0.1 or own IP).");
        opt.addOption("r", "regenconfig", false, "Regenerate application's default configuration file.");
        opt.addOption("nogui", false, "Run MeshFS without graphical user interface (server mode only)");
        opt.addOption("reconfig", false, "Reconfigure MeshFS graphically");
        opt.addOption("adduser", true, "Add user interactively");
        opt.addOption("deluser", true, "Remove user interactively");
        opt.addOption("changepass", true, "Update user credentials interactively");

        opt.addOption("u", "uuid", true, "Set UUID value for server to server communication");

        try {
            cmd = (new DefaultParser()).parse(opt, args);

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

            if (cmd.hasOption("adduser")) {
                try {
                    addUser(cmd.getOptionValue("adduser"));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (cmd.hasOption("deluser")) {
                removeUser(cmd.getOptionValue("deluser"));
            }

            if (cmd.hasOption("changepass")) {
                updateAccount(cmd.getOptionValue("changepass"));
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
        while(true) {
            System.out.print("Username: ");
            String username = new Scanner(System.in).nextLine();

            try {
                addUser(username);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("User Added!");
            System.out.println("Add another user? [y/N]");
            String response = input.nextLine();

            if(response.isEmpty() || response.toLowerCase().equals("n")){
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    void addUser(String username) throws IOException, ClassNotFoundException {
        username = username.toLowerCase();
        if(username.equals("guest")){
            System.out.print("The user guest is reserved for MeshFS.");
            System.exit(1);
        }
        System.out.print("Enter Password for " + username + ": ");
        char[] password = System.console().readPassword();
        System.out.print("Retype Password: ");
        char[] password2 = System.console().readPassword();

        if(!Arrays.equals(password, password2)){
            System.out.println("Passwords do not match!");
            addUser(username);
            return;
        }

        String pass = new String(password);

        File auth = new File(MeshFS.properties.getProperty("repository") + ".auth");
        HashMap<String, String> accounts;

        if(auth.exists()) {
            FileInputStream fis = new FileInputStream(auth);
            ObjectInputStream ois = new ObjectInputStream(fis);

            accounts = (HashMap) ois.readObject();
            for (HashMap.Entry<String, String> entry : accounts.entrySet()) {
                String un = entry.getKey();
                if(username.equals(un)){
                    System.out.println("User already exists!");
                    System.out.println("Quitting!");
                    return;
                }
            }
        }else{
            accounts = new HashMap<>();
        }

        accounts.put(username, Crypt.generateEncryptedAuth(username.toLowerCase(), pass));
        writeAuth(accounts);
    }

    void removeUser(String username) {
        username = username.toLowerCase();
        File auth = new File(MeshFS.properties.getProperty("repository") + ".auth");
        HashMap<String, String> accounts;
        if (auth.exists()) {
            try {
                FileInputStream fis = new FileInputStream(auth);
                ObjectInputStream ois = new ObjectInputStream(fis);
                accounts = (HashMap) ois.readObject();
                fis.close();
                ois.close();
                if(!accounts.containsKey(username)){
                    System.out.println("User " + username + " does not exist!");
                    System.exit(1);
                }
                accounts.remove(username);
                writeAuth(accounts);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    void updateAccount(String username){
        username = username.toLowerCase();
        removeUser(username);
        try {
            addUser(username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
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

}
