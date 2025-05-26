package ir.ac.kntu;

import java.util.*;
import java.util.regex.*;

public class InfoValidator {
    private static final String ALL_NUMERIC_REGEX = "^09[0-9]{9}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9._-]{6,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String ALL_CHAR_REGEX = "^[A-Za-z\\s]{3,}$";

    static List<Person> personList ;
    static List<Product> productList ;

    public static boolean emailUniquementCheck(String email, List<String> errorList) {
        for (Person person : personList) {
            if (person.getEmail().equals(email)) {
                errorList.add("Email already in use");
                return false;
            }
        }

        return true;
    }

    public static boolean phoneNumberUniquementCheck(String phoneNumber, List<String> errorList) {
        for (Person person : personList) {
            if (person.getPhoneNumber().equals(phoneNumber)) {
                errorList.add("Phone number already in use");
                return false;
            }
        }

        return true;
    }

    public static boolean usernameUniquementCheck(String username, List<String> errorList) {
        for (Person person : personList) {
            if (person.getUsername().equals(username)) {
                errorList.add("Username already in use");
                return false;
            }
        }

        return true;
    }

    public static boolean nameValidation(String name, List<String> errorList) {
        boolean result = true;

        if (name == null || name.isEmpty()) {
            result = false;
            errorList.add(name + " as field <name> is null or empty");
        }

        if (name != null && name.length() < 3) {
            result = false;
            errorList.add(name + " as field <name> is too short");
        }

        if (!Objects.requireNonNull(name).matches(ALL_CHAR_REGEX)) {
            result = false;
            errorList.add("Name contains invalid characters");
        }

        return result;
    }

    public static boolean phoneNumberValidation(String phoneNumber, List<String> errorList) {
        boolean result = true;

        if (!phoneNumber.matches(ALL_NUMERIC_REGEX)) {
            errorList.add("Phone number contains invalid characters");
            result = false;
        }

        if (phoneNumber.length() != 11) {
            errorList.add("Phone number is not 11 digits");
            result = false;
        }

        return result;
    }

    public static boolean emailValidation(String email, List<String> errorList) {
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

    public static boolean usernameValidation(String username, List<String> errorList) {
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

    public static boolean passwordValidation(String password, List<String> errorList) {
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

    public boolean AgeValidation(String age, List<String> errorList) {
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

    public static boolean PageCountValidation(String pageCount, List<String> errorList) {

        if (pageCount.length() > 4) {
            errorList.add("Page count is too much, unreal info available");
            return false;
        }

        if (pageCount.matches(ALL_NUMERIC_REGEX)) {
            errorList.add("Page number is contains invalid characters");
            return false;
        }

        return true;
    }

    public static boolean isPersonInfoValid(String name, String surname, String phoneNumber, String email,
                                     String username, String password, List<String> errorList) {
        personList = DataBase.getPersonList();
        boolean isUnique = emailUniquementCheck(email, errorList) &&
                phoneNumberUniquementCheck(phoneNumber, errorList) &&
                usernameUniquementCheck(username, errorList);

        boolean isValid = nameValidation(name, errorList) &&
                nameValidation(surname, errorList) &&
                phoneNumberValidation(phoneNumber, errorList) &&
                emailValidation(email, errorList) &&
                usernameValidation(username, errorList) &&
                passwordValidation(password, errorList);

        return isValid && isUnique;
    }
}
