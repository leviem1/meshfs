import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by markhedrick on 5/11/17.
 */
class UserAccounts implements Serializable {
    private String username;
    private String password;
    private String accountType;
    private ArrayList<String> groups;

    UserAccounts(String username, String password, String accountType, ArrayList<String> groups) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.groups = groups;

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

    void setAccountType(String newAccountType) {
        accountType = newAccountType;
    }


    ArrayList<String> getGroups() {
        return groups;
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
