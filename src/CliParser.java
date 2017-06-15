import org.apache.commons.cli.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.util.*;

/**
 * The CliParser class allows for the reading of command line arguments. Only calling on the class
 * is necessary.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

class CliParser {

    private final Options opt = new Options();
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
        opt.addOption("changeType", true, "Update user account type.");
        opt.addOption("listUsers", false, "Display all user accounts.");
        opt.addOption("listGroups", true, "Display all user groups.");
        opt.addOption("getUserType", true, "Get type of user.");
        opt.addOption("resetAll", false, "Reset admin password, and all user accounts to default state.");
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
                    addUser(cmd.getOptionValue("addUser"), false);
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

            if (cmd.hasOption("getUserType")) {
                getAccountType(cmd.getOptionValue("getUserType"));
            }

            if (cmd.hasOption("changeType")) {
                changeAccountType(cmd.getOptionValue("changeType"));
            }

            if (cmd.hasOption("resetAll")) {
                resetAuth();
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

    /**
     * This method is used to add an admin or guest user on the master.
     *
     * @param username name of user to be created (admin/guest)
     */
    @SuppressWarnings("unchecked")

    void addUser(String username) {
        if (username.equals("admin")) {
            System.out.println("Adding administrator account...");
            try {
                addUser("admin", true);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Do you wish to enable the guest account? [Y/n]");
            String response = new Scanner(System.in).nextLine();
            if (response.isEmpty() || response.toLowerCase().equals("y")) {
                addUser("guest");
            }

            System.out.println("Do you wish to start the server? [Y/n]");
            response = new Scanner(System.in).nextLine();
            if (!response.isEmpty()) {
                if (!response.toLowerCase().equals("y")) {
                    System.out.println("Exiting!");
                    System.exit(0);
                }
            }
        } else if (username.equals("guest")) {
            ArrayList<UserAccount> accounts = new ArrayList<>();
            if (new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
                try {
                    accounts = (ArrayList<UserAccount>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
                } catch (IOException | ClassNotFoundException ignored) {
                }
            }
            accounts.add(new UserAccount(username, Crypt.generateEncryptedPass("guest", "guest"), "user", new ArrayList<>(Collections.singletonList(username))));
            Crypt.writeAuthFile(accounts);
        }
    }

    /**
     * This method adds a user to a group interactively
     *
     * @param username user to add the group to
     */
    @SuppressWarnings("unchecked")
    private void addGroup(String username) {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("Group: ");
            String groupName = new Scanner(System.in).nextLine();

            try {
                ArrayList<UserAccount> accounts;
                if (new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
                    accounts = (ArrayList<UserAccount>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
                } else {
                    accounts = new ArrayList<>();
                }

                if (username.equals("guest")) {
                    System.out.println("The user guest cannot be modified.");
                    System.exit(1);
                }
                boolean exists = false;
                for (UserAccount account : accounts) {
                    if (account.getUsername().equals(username)) {
                        account.addGroup(groupName);
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    System.out.println("Username " + username + " does not exist!");
                    System.out.println("Exiting!");
                    System.exit(1);
                }
                Crypt.writeAuthFile(accounts);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("User group modified!!");
            System.out.println("Add another group to " + username + "? [y/N]");
            String response = input.nextLine();

            if (response.isEmpty() || response.toLowerCase().equals("n")) {
                System.out.println("Exiting!");
                System.exit(0);
            }
        }
    }

    @SuppressWarnings("unchecked")

    private void addUser(String username, boolean initial) throws IOException, ClassNotFoundException {
        ArrayList<UserAccount> accounts;
        if (new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
            accounts = (ArrayList<UserAccount>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
        } else {
            accounts = new ArrayList<>();
        }
        if (username.equals("guest")) {
            System.out.println("The user guest is reserved for MeshFS.");
            System.exit(1);
        }
        if (!initial && username.equals("admin")) {
            System.out.println("The default administrator account is reserved for the system admin");
            System.exit(1);
        }
        if (!initial) {
            for (UserAccount account : accounts) {
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
                addUser(username, false);
                return;
            }

            username = username.toLowerCase();
            String pass = new String(password);
            accounts.add(new UserAccount(username, Crypt.generateEncryptedPass(username, pass), "user", new ArrayList<>(Collections.singletonList(username))));
            Crypt.writeAuthFile(accounts);

            System.out.println("User Added!");

            System.out.println("Do you wish to start the server? [y/N]");
            String response = new Scanner(System.in).nextLine();

            if (response.isEmpty() || response.toLowerCase().equals("n")) {
                System.out.println("Exiting!");
                System.exit(0);
            }

            MeshFS.nogui = true;
        } else {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            String pw = RandomStringUtils.random(8, characters);
            accounts.add(new UserAccount("admin", Crypt.generateEncryptedPass(username, pw), "admin", new ArrayList<>(Collections.singletonList(username))));
            Crypt.writeAuthFile(accounts);
            System.out.println("Admin account generated!");
            System.out.println("Username: admin (non case-sensitive)");
            System.out.println("Password: " + pw);
            System.out.print("Press any key to continue...");
            new Scanner(System.in).nextLine();
        }


    }

    @SuppressWarnings("unchecked")

    private void removeUser(String username) throws IOException, ClassNotFoundException {
        if (username.equals("admin")) {
            System.out.println("The user 'admin' cannot be deleted!");
            return;
        }
        ArrayList<UserAccount> accounts = null;
        if (new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
            accounts = (ArrayList<UserAccount>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
        } else {
            System.err.println("No authentication file exists!");
            System.exit(1);
        }

        if (username.equals("guest")) {
            System.out.println("The user guest is reserved for MeshFS.");
            System.exit(1);
        }

        for (UserAccount account : accounts) {
            if (account.getUsername().equals(username.toLowerCase())) {
                accounts.remove(account);
                break;
            }
        }

        Crypt.writeAuthFile(accounts);
    }

    private void updateAccount(String username) throws IOException, ClassNotFoundException {
        if (username.equals("admin")) {
            System.out.println("The password for 'admin' cannot be changed!");
            return;
        }
        username = username.toLowerCase();
        removeUser(username);
        try {
            addUser(username, false);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @SuppressWarnings("unchecked")

    private void removeGroup(String username) {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Hint: typing 'all' below will result in removing all non-primary user groups");
            System.out.print("Group: ");
            String groupName = new Scanner(System.in).nextLine();
            if (groupName.equals("admin")) {
                System.out.println("The group name 'admin' is reserved!");
                break;
            }
            try {
                ArrayList<UserAccount> accounts;
                if (new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
                    accounts = (ArrayList<UserAccount>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
                } else {
                    accounts = new ArrayList<>();
                }

                if (username.equals("guest")) {
                    System.out.println("The user guest cannot be modified.");
                    System.out.println("Exiting!");
                    System.exit(1);
                }
                ArrayList<String> toRemove = new ArrayList<>();
                for (UserAccount account : accounts) {
                    if (account.getUsername().equals(username)) {
                        if (groupName.equals("all")) {
                            for (String group : account.getGroups()) {
                                if (!group.equals(username)) {
                                    toRemove.add(group);
                                }
                            }
                            System.out.println("Going to remove: " + toRemove.toString());
                            System.out.println("Proceed? [y/N]");
                            String response = input.nextLine();

                            if (response.isEmpty() || response.toLowerCase().equals("n")) {
                                System.out.println("Exiting!");
                                System.exit(0);
                            }
                            for (String group : toRemove) {
                                account.removeGroup(group);
                            }
                            Crypt.writeAuthFile(accounts);
                        } else {
                            account.removeGroup(groupName);
                            System.out.println("Going to remove" + groupName);
                            System.out.println("Proceed? [y/N]");
                            String response = input.nextLine();

                            if (response.isEmpty() || response.toLowerCase().equals("n")) {
                                System.out.println("Exiting!");
                                System.exit(0);
                            }
                            Crypt.writeAuthFile(accounts);
                        }
                        System.out.println("Exiting!");
                        System.exit(0);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("User group modified!!");
            System.out.println("Remove another group from " + username + "? [y/N]");
            String response = input.nextLine();

            if (response.isEmpty() || response.toLowerCase().equals("n")) {
                System.out.println("Exiting!");
                System.exit(0);
            }
        }
    }

    @SuppressWarnings("unchecked")

    private void listUsers() {
        try {
            ArrayList<UserAccount> accounts;
            if (new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
                accounts = (ArrayList<UserAccount>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
            } else {
                return;
            }


            for (UserAccount account : accounts) {
                System.out.println(account.getUsername() + " (" + account.getAccountType() + ")");
            }
            System.out.println("Exiting!");
            System.exit(0);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")

    private void listGroups(String username) {
        try {
            ArrayList<UserAccount> accounts;
            if (new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
                accounts = (ArrayList<UserAccount>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
            } else {
                return;
            }

            for (UserAccount account : accounts) {
                if (account.getUsername().equals(username)) {
                    System.out.println(account.getGroups().toString());

                }
            }
            System.out.println("Exiting!");
            System.exit(0);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")

    private void getAccountType(String username) {
        try {
            ArrayList<UserAccount> accounts;
            if (new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
                accounts = (ArrayList<UserAccount>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
            } else {
                return;
            }

            for (UserAccount account : accounts) {
                if (account.getUsername().equals(username)) {
                    System.out.println(account.getAccountType());
                }
            }
            System.out.println("Exiting!");
            System.exit(0);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")

    private void changeAccountType(String username) {
        try {
            ArrayList<UserAccount> accounts;
            Scanner input = new Scanner(System.in);
            if (new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
                accounts = (ArrayList<UserAccount>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
            } else {
                return;
            }

            System.out.println("Enter new account type (admin, user): ");
            String response = input.nextLine().trim();
            if (response.isEmpty() || !response.toLowerCase().equals("admin") && !response.toLowerCase().equals("user")) {
                System.out.println("Please enter \"admin\" or \"user\"");
                System.exit(0);
            } else {
                for (UserAccount account : accounts) {
                    if (account.getUsername().equals(username)) {
                        account.setAccountType(response);
                    }
                }
            }
            Crypt.writeAuthFile(accounts);
            System.out.println("Account '" + username + "' was modified to be a(n) " + response + ".");
            System.out.println("Exiting!");
            System.exit(0);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})

    private void resetAuth() {
        System.out.println("Do you still wish to reset all accounts? NOTE: This will remove all users and groups.");
        System.out.println("Proceed? [y/N]");
        String response = new Scanner(System.in).nextLine();

        if (response.isEmpty() || response.toLowerCase().equals("n")) {
            System.out.println("Exiting!");
            System.exit(0);
        }

        ArrayList<UserAccount> accounts = new ArrayList<>();
        if (new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
            try {
                accounts = (ArrayList<UserAccount>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            return;
        }
        accounts.clear();
        new File(MeshFS.properties.getProperty("repo") + ".catalog.json").delete();
        new File(MeshFS.properties.getProperty("repo") + ".manifest.json").delete();
        Crypt.writeAuthFile(accounts);
        addUser("admin");

        System.out.println("Do you wish to enable the guest account? [Y/n]");

        response = new Scanner(System.in).nextLine();
        if (response.isEmpty() || response.toLowerCase().equals("y")) {
            addUser("guest");
        }
    }
}