package ir.ac.kntu;

import java.util.List;

public class LoginPage {

    public static boolean receiveLoginInfo(String authenticationText, String password, String role) {
        return DataBase.getPersonList().stream()
                .anyMatch(c -> (c.getUsername().equals(authenticationText)
                        || c.getEmail().equals(authenticationText)
                        || c.getPhoneNumber().equals(authenticationText))
                        && c.getPassword().equals(password));
    }
}

