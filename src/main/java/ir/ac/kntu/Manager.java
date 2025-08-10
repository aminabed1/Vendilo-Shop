package ir.ac.kntu;

public class Manager extends SpecialUsers {
    private final int priorityCode;
    public Manager(String name, String surname, String username, String password, int priorityCode) {
        super(name, surname, username, password);
        this.setRole(Role.Manager);
        this.priorityCode = priorityCode;
    }

    public int getPriorityCode() {
        return priorityCode;
    }

}
