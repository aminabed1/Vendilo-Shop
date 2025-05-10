package ir.ac.kntu;

import java.util.List;

public class LoginPage {

    public static boolean receiveLoginInfo(String username, String password, String role) {
        switch (role) {
            case "Customer":
                return DataBase.getCustomerList().stream()
                        .anyMatch(c -> c.getUsername().equals(username) && c.getPassword().equals(password));

            case "Seller":
                return DataBase.getSellerList().stream()
                        .anyMatch(s -> s.getUsername().equals(username) && s.getPassword().equals(password));

            case "Support":
                return DataBase.getSupportList().stream()
                        .anyMatch(s -> s.getUsername().equals(username) && s.getPassword().equals(password));

            default:
                return false;
        }
    }
}

