package ir.ac.kntu;

import java.util.List;

public class LoginPage {

    public static boolean receiveLoginInfo(String authenticationText, String password, String role) {
        switch (role) {
            case "Customer":
                return DataBase.getCustomerList().stream()
                        .anyMatch(c -> (c.getUsername().equals(authenticationText)
                                || c.getEmail().equals(authenticationText)
                                || c.getPhoneNumber().equals(authenticationText))
                                && c.getPassword().equals(password));

            case "Seller":
                return DataBase.getSellerList().stream()
                        .anyMatch(s -> (s.getUsername().equals(authenticationText)
                                || s.getEmail().equals(authenticationText)
                                || s.getPhoneNumber().equals(authenticationText))
                                && s.getPassword().equals(password));

//            case "Support":
//                return DataBase.getSupportList().stream()
//                        .anyMatch(s -> (s.getUsername().equals(authenticationText)
//                                || s.getEmail().equals(authenticationText)
//                                || s.getPhoneNumber().equals(authenticationText))
//                                && s.getPassword().equals(password));

            default:
                return false;
        }
    }
}

