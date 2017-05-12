import org.apache.commons.cli.*;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
        opt.addOption("reconfig", false, "Reconfigure MeshFS graphically.");
        opt.addOption("addUser", true, "Add user interactively.");
        opt.addOption("addGroup", true, "Update user groups interactively.");
        opt.addOption("removeUser", true, "Remove user interactively.");
        opt.addOption("removeGroup", true, "Remove a group from a user interactively.");
        opt.addOption("changePass", true, "Update user credentials interactively.");
        opt.addOption("listUsers", false, "Display all user accounts.");
        opt.addOption("listGroups", true, "Display all user groups.");
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

            if (cmd.hasOption("u")) {
                MeshFS.properties.setProperty("uuid", cmd.getOptionValue("u"));
            }

            if (cmd.hasOption("m")) {
                MeshFS.properties.setProperty("masterIP", cmd.getOptionValue("m"));
            }

            if (cmd.hasOption("addUser")) {
                try {
                    addUser(cmd.getOptionValue("addUser"));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (cmd.hasOption("removeUser")) {
                removeUser(cmd.getOptionValue("removeUser"));
            }

            if (cmd.hasOption("changePass")) {
                updateAccount(cmd.getOptionValue("changePass"));
            }

            if (cmd.hasOption("addGroup")) {
                addGroup(cmd.getOptionValue("addGroup"));
            }

            if (cmd.hasOption("removeGroup")) {
                removeGroup(cmd.getOptionValue("removeGroup"));
            }

            if (cmd.hasOption("listUsers")) {
                listUsers();
            }

            if (cmd.hasOption("listGroups")) {
                listGroups(cmd.getOptionValue("listGroups"));
            }

            if (cmd.hasOption("nogui")) {
                MeshFS.nogui = true;
            }

            if (cmd.hasOption("reconfig")) {
                MeshFS.configure = true;
            }

        } catch (ParseException e) {
            help();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
        ArrayList<UserAccounts> accounts;
        if(new File(MeshFS.properties.getProperty("repository")+".auth").exists()){
            accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
        }else{
            accounts = new ArrayList<>();
        }
        if (username.equals("guest")) {
            System.out.println("The user guest is reserved for MeshFS.");
            System.exit(1);
        }

        for (UserAccounts account : accounts) {
            if (account.getUsername().equals(username)) {
                System.out.println("User already exists!");
                System.out.println("Quitting!");
                return;
            }
        }

        System.out.print("Enter Password for " + username + ": ");
        char[] password = System.console().readPassword();
        System.out.print("Retype Password: ");
        char[] password2 = System.console().readPassword();

        if (!Arrays.equals(password, password2)) {
            System.out.println("Passwords do not match!");
            addUser(username);
            return;
        }

        username = username.toLowerCase();
        String pass = new String(password);

        accounts.add(new UserAccounts(username, Crypt.generateEncryptedPass(username, pass), "user", new ArrayList<>(Arrays.asList(username))));
        Crypt.writeAuthFile(accounts);
    }

    void removeUser(String username) throws IOException, ClassNotFoundException {
        ArrayList<UserAccounts> accounts = null;
        if(new File(MeshFS.properties.getProperty("repository")+".auth").exists()){
            accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
        }else{
            System.err.println("No authentication file exists!");
            System.exit(1);
        }

        if (username.equals("guest")) {
            System.out.println("The user guest is reserved for MeshFS.");
            System.exit(1);
        }

        for(UserAccounts account : accounts){
            if(account.getUsername().equals(username.toLowerCase())){
                accounts.remove(account);
                break;
            }
        }

        Crypt.writeAuthFile(accounts);
    }

    void updateAccount(String username) throws IOException, ClassNotFoundException {
        username = username.toLowerCase();
        removeUser(username);
        try {
            addUser(username);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    void addGroup(String username){
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.print("Group: ");
            String groupName = new Scanner(System.in).nextLine();

            try {
                ArrayList<UserAccounts> accounts;
                if(new File(MeshFS.properties.getProperty("repository")+".auth").exists()){
                    accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
                }else{
                    accounts = new ArrayList<>();
                }

                if (username.equals("guest")) {
                    System.out.println("The user guest cannot be modified.");
                    System.exit(1);
                }
                boolean exists = false;
                for (UserAccounts account : accounts) {
                    if (account.getUsername().equals(username)) {
                        account.addGroup(groupName);
                        exists = true;
                        break;
                    }
                }
                if(!exists){
                    System.out.println("Username " + username + " does not exist!");
                    System.exit(1);
                }
                Crypt.writeAuthFile(accounts);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("User group modified!!");
            System.out.println("Add another group to " + username + "? [y/N]");
            String response = input.nextLine();

            if(response.isEmpty() || response.toLowerCase().equals("n")){
                System.exit(0);
            }
        }
    }

    void removeGroup(String username){
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.print("Group: ");
            String groupName = new Scanner(System.in).nextLine();

            try {
                ArrayList<UserAccounts> accounts;
                if(new File(MeshFS.properties.getProperty("repository")+".auth").exists()){
                    accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
                }else{
                    accounts = new ArrayList<>();
                }

                if (username.equals("guest")) {
                    System.out.println("The user guest cannot be modified.");
                    System.exit(1);
                }
                ArrayList<String> toRemove = new ArrayList<>();
                for (UserAccounts account : accounts) {
                    if (account.getUsername().equals(username)) {
                        if(groupName.equals("all")){
                            for(String group : account.getGroups()){
                                if(!group.equals(username)){
                                    toRemove.add(group);
                                }
                            }
                            System.out.println("Going to remove: " + toRemove.toString());
                            System.out.println("Proceed? [y/N]");
                            String response = input.nextLine();

                            if(response.isEmpty() || response.toLowerCase().equals("n")){
                                System.exit(0);
                            }
                            for(String group : toRemove){
                                account.removeGroup(group);
                            }
                            Crypt.writeAuthFile(accounts);
                        }else{
                            account.removeGroup(groupName);
                            System.out.println("Going to remove" + groupName);
                            System.out.println("Proceed? [y/N]");
                            String response = input.nextLine();

                            if(response.isEmpty() || response.toLowerCase().equals("n")){
                                System.exit(0);
                            }
                            Crypt.writeAuthFile(accounts);
                        }
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("User group modified!!");
            System.out.println("Remove another group from " + username + "? [y/N]");
            String response = input.nextLine();

            if(response.isEmpty() || response.toLowerCase().equals("n")){
                System.exit(0);
            }
        }
    }

    void listUsers(){
        try {
            ArrayList<UserAccounts> accounts;
            if(new File(MeshFS.properties.getProperty("repository")+".auth").exists()){
                accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
            }else{
                return;
            }


            for (UserAccounts account : accounts) {
                System.out.println(account.getUsername());
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void listGroups(String username){
        try {
            ArrayList<UserAccounts> accounts;
            if(new File(MeshFS.properties.getProperty("repository")+".auth").exists()){
                accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
            }else{
                return;
            }

            for (UserAccounts account : accounts) {
                if(account.getUsername().equals(username)){
                    System.out.println(account.getGroups().toString());

                }
            }
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}