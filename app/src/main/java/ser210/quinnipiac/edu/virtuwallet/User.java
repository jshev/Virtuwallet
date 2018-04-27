package ser210.quinnipiac.edu.virtuwallet;

/**
 * Created by juliannashevchenko on 4/27/18.
 */

public class User {

    private int id;
    private String username;
    private String password;

    public User(){
        this.username = "";
        this.password = "";
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        // Used by ArrayAdapter in ListView?
        return username;
    }
}
