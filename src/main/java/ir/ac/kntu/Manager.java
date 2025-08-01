package ir.ac.kntu;

public class Manager extends SpecialUsers {

    public Manager(String name, String surname, String username, String password) {
        super(name, surname, username, password);
        this.setRole(Role.Manager);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
