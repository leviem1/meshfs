import java.util.ArrayList;

/**
 * Created by markhedrick on 5/11/17.
 */
class UserAccounts implements java.io.Serializable{
    private String username;
    private String password;
    private String accountType;
    private ArrayList<String> groups;

    UserAccounts(String username, String password, String accountType){
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    String getUsername(){
        return username;
    }

    String getPassword(){
        return password;
    }

    String getAccountType(){
        return accountType;
    }

}
