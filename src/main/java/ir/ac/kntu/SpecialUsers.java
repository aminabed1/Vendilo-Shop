package ir.ac.kntu;

public class SpecialUsers extends Person {
    private String username;

    public SpecialUsers(String name, String surname, String username, String password) {
        super(name, surname, password);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
