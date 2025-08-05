package ir.ac.kntu;

public class OrdinaryUsers extends Person {
    private String phoneNumber;
    private String email;

    public OrdinaryUsers(String name, String surname, String password, String phoneNumber, String email, boolean isActive) {
        super(name, surname, password, isActive);
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format(
                 super.toString() +
                        "%sPhoneNumber :%s %s\n" +
                        "%sEmail       :%s %s\n",
                YELLOW, RESET, phoneNumber,
                YELLOW, RESET, email
        );
    }
}
