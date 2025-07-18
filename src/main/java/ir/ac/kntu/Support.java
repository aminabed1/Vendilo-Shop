package ir.ac.kntu;

public class Support extends SpecialUsers {

    public Support(String name, String surname, String username, String password) {
        super(name, surname, username, password);
        this.setRole("Support");
    }
}
