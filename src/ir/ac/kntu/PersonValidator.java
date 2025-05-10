package ir.ac.kntu;

import java.util.*;
import java.util.regex.*;

public class PersonValidator {
    private static final String PHONE_REGEX = "^09[0-9]{9}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9._-]{6,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String NAME_REGEX = "^[A-Za-z\\s]{3,}$";

//    private Customer customer = null;
    List<Customer> customerList = DataBase.getCustomerList();

    public boolean emailUniquementCheck(String email, List<String> errorList) {
        for (Customer customer : customerList) {
            if (customer.getEmail().equals(email)) {
                errorList.add("Email already in use");
                return false;
            }
        }

        return true;
    }

    public boolean phoneNumberUniquementCheck(String phoneNumber, List<String> errorList) {
        for (Customer customer : customerList) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                errorList.add("Phone number already in use");
                return false;
            }
        }

        return true;
    }

    public boolean usernameUniquementCheck(String username, List<String> errorList) {
        for (Customer customer : customerList) {
            if (customer.getUsername().equals(username)) {
                errorList.add("Username already in use");
                return false;
            }
        }

        return true;
    }

    public boolean checkNameAndSurnameValidation(String name, List<String> errorList) {
        boolean result = true;

        if (name == null || name.isEmpty()) {
            result = false;
            errorList.add(name + " as field <name> is null or empty");
        }

        if (name != null && name.length() < 3) {
            result = false;
            errorList.add(name + " as field <name> is too short");
        }

        if (!Objects.requireNonNull(name).matches(NAME_REGEX)) {
            result = false;
            errorList.add("Name contains invalid characters");
        }

        return result;
    }

    public boolean phoneNumberValidation(String phoneNumber, List<String> errorList) {
        boolean result = true;

        if (!phoneNumber.matches(PHONE_REGEX)) {
            errorList.add("Phone number contains invalid characters");
            result = false;
        }

        if (phoneNumber.length() != 11) {
            errorList.add("Phone number is not 11 digits");
            result = false;
        }

        return result;
    }

    public boolean emailValidation(String email, List<String> errorList) {
        boolean result = true;

        if (email.length() > 40) {
            errorList.add("Email address is too long");
            result = false;
        }

        if (!email.matches(EMAIL_REGEX)) {
            errorList.add("Email address is not valid");
            result = false;
        }

        return result;
    }

    public boolean usernameValidation(String username, List<String> errorList) {
        boolean result = true;

        if (username.length() < 6) {
            errorList.add("Username is too short");
            result = false;
        }

        if (!username.matches(USERNAME_REGEX)) {
            errorList.add("Username contains invalid characters");
            result = false;
        }

        return result;
    }

    public boolean passwordValidation(String password, List<String> errorList) {
        boolean result = true;

        if (password.length() < 8) {
            errorList.add("Password is too short");
            result = false;
        }

        if (!password.matches(PASSWORD_REGEX)) {
            errorList.add("Password must contain at least one lowercase letter and one uppercase letter");
            result = false;
        }

        return result;
    }

    public boolean checkAgeValidation(String age, List<String> errorList) {
        Pattern agePatt = Pattern.compile("[0-9]{2,3}");
        Matcher ageMath = agePatt.matcher(age);

        int intAge = Integer.parseInt(age);

        if (intAge < 10) {
            errorList.add("You are not old enough");
            return false;
        }

        if (intAge > 120) {
            errorList.add("You are Nouh. Go and build your ship");
            return false;
        }

        if (!ageMath.find()) {
            errorList.add("Age is not valid");
            return false;
        }

        return true;
    }
}
