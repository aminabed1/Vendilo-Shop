package ir.ac.kntu;

public class CreateAccountPage {
    public static boolean receiveAccountInfo(String email, String phoneNumber, String role) {
        //TODO check here
        return DataBase.getPersonList().stream()
                .anyMatch(c -> c.getEmail().equals(email) || c.getPhoneNumber().equals(phoneNumber));
    }
}
