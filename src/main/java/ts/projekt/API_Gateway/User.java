package ts.projekt.API_Gateway;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private int id;
    private String email;

    public User(String username, int id, String email) {
        this.username = username;
        this.id = id;
        this.email = email;
    }

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
