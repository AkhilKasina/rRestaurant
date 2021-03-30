package eRestaurant.src.main.java.com.softwarestudiogroup1.model;

public class Patron {

    private final String username;
    private final String password;


    public Patron(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return username;
    }
}
