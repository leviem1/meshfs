import org.apache.commons.cli.*;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Properties;

/**
 * The CliParser class allows for the reading of command line arguments. Only calling on the class
 * is necessary.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */
class CliParser {

    private final Options opt = new Options();
    private final HashMap<String, String> accounts;
    private final HashMap<String, String> accountsEnc;


    CliParser(String[] args, Properties properties) {
        accounts = new HashMap<>();
        accountsEnc = new HashMap<>();

        opt.addOption("h", "help", false, "Display application's help message.");
        opt.addOption("m", "masterIP", true, "IP of master (if self, use 127.0.0.1 or own IP).");
        opt.addOption(
                "r", "regenConfig", false, "Regenerate application's default configuration file.");
        opt.addOption("nogui", false, "Run MeshFS without graphical user interface (server mode only)");
        opt.addOption("reconfig", false, "Reconfigure MeshFS graphically");
        try {
            CommandLine cmd = (new DefaultParser()).parse(opt, args);

            if (cmd.hasOption("h")) {
                help();
            }

            if (cmd.hasOption("m")) {
                properties.setProperty("masterIP", cmd.getOptionValue("m"));
            }

            if (cmd.hasOption("r")) {
                ConfigParser.loadDefaultProperties();
            }

            if (cmd.hasOption("nogui")) {
                MeshFS.nogui = true;
                if(!(MeshFS.configure)){
                    if(interactiveAuth()){
                        writeAuth();
                    }
                }
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

    private boolean interactiveAuth(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Do you wish to enable the guest user? (yes/no)");
            String guest = br.readLine();
            if(guest.equals("yes")){
                accounts.put("guest", "guest");
            }
            System.out.println("Do you wish to begin adding users? (yes/no)");
            String addUserResponse = br.readLine();
            if(addUserResponse.equals("yes")){
                while(true){
                    System.out.println("Username: ");
                    String user = br.readLine();
                    System.out.println("Password: " );
                    String pw = br.readLine();
                    accounts.put(user, pw);
                    System.out.println("Do you wish to add another user? (yes/no)");
                    String continueAdding = br.readLine();
                    if(!continueAdding.equals("yes")){
                        break;
                    }
                    for (HashMap.Entry<String, String> userAccount : accounts.entrySet()) {
                        generateEncryptedAuth(userAccount.getKey(), userAccount.getValue());
                    }
                }
                System.out.println("Users were added successfully!");
                return true;
            }else{
                System.out.println("The server cannot be started without any active user accounts!");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAuth(){
        try {
            FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + ".auth");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(accountsEnc);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateEncryptedAuth(String username, String password){
        for (int x = 0; x < username.length() - 1; x = x + 2) {
            try {
                password += username.charAt(x);
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
        messageDigest.update(password.getBytes(), 0, password.length());
        accountsEnc.put(username, new BigInteger(1, messageDigest.digest()).toString(256));
    }
}
