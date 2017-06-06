import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by markhedrick on 5/11/17.
 */
class UserAccount implements Serializable {
    private String username;
    private String password;
    private String accountType;
    private String UUID;
    private ArrayList<String> groups;

    UserAccount(String username, String password, String accountType, ArrayList<String> groups) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.groups = groups;
        UUID = java.util.UUID.randomUUID().toString();
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getAccountType() {
        return accountType;
    }

    String getUUID() {return UUID;}

    ArrayList<String> getGroups() {
        return groups;
    }

    void setAccountType(String newAccountType) {
        accountType = newAccountType;
    }

    void addGroup(String groupName) {
        if (!groups.contains(groupName)) {
            groups.add(groupName);
        }
    }

    ArrayList<String> removeGroup(String groupName) {
        if (groups.contains(groupName)) {
            groups.remove(groupName);
        }
        return groups;
    }


}
