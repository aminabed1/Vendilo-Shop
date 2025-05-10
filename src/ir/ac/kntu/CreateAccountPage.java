package ir.ac.kntu;

public class CreateAccountPage {
    public static boolean receiveAccountInfo(String email, String phoneNumber, String role) {
        switch (role) {
            case "Customer":
                return DataBase.getCustomerList().stream()
                        .anyMatch(c -> c.getEmail().equals(email) && c.getPhoneNumber().equals(phoneNumber));

            case "Seller":
                return DataBase.getSellerList().stream()
                        .anyMatch(c ->  c.getEmail().equals(email) && c.getPhoneNumber().equals(phoneNumber));

            default:
                return false;
        }
    }
}
