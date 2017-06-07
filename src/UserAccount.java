import java.io.Serializable;
import java.util.ArrayList;

/**
 * The UserAccount class represents the UserAccount Object. The UserAccount objects is comprised of a username
 * password, list of groups, a unique identifier, and the type of the account itself.
 *
 *
 * @author Mark Hedrick
 * @version 1.0.0
 */

class UserAccount implements Serializable {
    private String username;
    private String password;
    private String accountType;
    private String UUID;
    private ArrayList<String> groups;

    /**
     * Creates a UserAccount to be written in the authentication file
     *
     *
     * @param username      The username of the account
     * @param password      The password of the account
     * @param accountType   The type of the account type
     * @param groups        The list of groups for the particular account
     */

    UserAccount(String username, String password, String accountType, ArrayList<String> groups) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.groups = groups;
        UUID = java.util.UUID.randomUUID().toString();
    }

    /**
     * Returns the username of the current UserAccount
     *
     *
     * @return Returns the username of associated with the UserAccount
     */

    String getUsername() {
        return username;
    }

    /**
     * Returns the password of the current UserAccount
     *
     *
     * @return Returns the hash of the password of associated with the UserAccount
     */

    String getPassword() {
        return password;
    }

    /**
     * Returns the type of account of the current UserAccount
     *
     *
     * @return Returns the type of user associated with the UserAccount
     */

    String getAccountType() {
        return accountType;
    }

    /**
     * Returns the UUID of the current UserAccount
     *
     *
     * @return Returns the UUID of the current UserAccount
     */

    String getUUID() {
        return UUID;
    }

    /**
     * Returns the Groups of the current UserAccount
     *
     *
     * @return Returns the Groups of the current UserAccount
     */

    ArrayList<String> getGroups() {
        return groups;
    }

    /**
     * Returns the updated Groups of the current UserAccount after removing the groupName
     *
     *
     * @param groupName The name of the Group to Remove from the current groups
     * @return Returns the updated list of groups
     */

    ArrayList<String> removeGroup(String groupName) {
        if (groups.contains(groupName)) {
            groups.remove(groupName);
        }
        return groups;
    }

    /**
     * Sets the type of account associated with the current UserAccount
     *
     *
     * @param newAccountType The type of account to set the account type to
     */

    void setAccountType(String newAccountType) {
        accountType = newAccountType;
    }

    /**
     * Adds the groupName to the current groups of the corresponding UserAccount
     *
     *
     * @param groupName The name of the group to add to the current groups of a user
     */

    void addGroup(String groupName) {
        if (!groups.contains(groupName)) {
            groups.add(groupName);
        }
    }

}
