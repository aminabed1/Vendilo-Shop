package ir.ac.kntu;

import java.util.*;
import ir.ac.kntu.*;

public class LoginPage {

    public static void loginPage() {
        Person customer = new Customer("amin", "abdollahi", "09144593348", "adsfd@gmail.com", "aminabed1", "12345678aA@");
        Person seller = new Seller("amin", "aaaaaa", "09144593347", "adsv@gmail.com", "123456789abcd", "12345678aA@", "aa", "TEHRAN");
        DataBase.addPerson(customer);
        DataBase.addPerson(seller);

        showWelcomeScreen();
        readLoginInfo();
    }

    public static void showWelcomeScreen() {
        System.out.println("=========================================");
        System.out.println("|                                       |");
        System.out.println("|      WELCOME TO OUR APPLICATION       |");
        System.out.println("|                                       |");
        System.out.println("=========================================");
        System.out.println();
    }

    public static void readLoginInfo() {
        Scanner scan = new Scanner(System.in);

        while (true) {
            displayLoginMenu();

            String role = scan.next().trim();

            if (role.equals("0")) {
//                 TODO: Implement account creation
                CreateAccountPage.createAccount();
                continue;
            }

            if (!isValidRole(role)) {
                System.out.println("\n\u001B[31mERROR: Please enter a valid option (0-3)\u001B[0m\n");
                continue;
            }

            String roleName = getRoleName(role);
            System.out.println("\n\u001B[32mSelected Role: " + roleName + "\u001B[0m");

            System.out.print("\nEnter Username/Phone/Email: ");
            String authenticationField = scan.next().trim();

            System.out.print("Enter Password: ");
            String passwordField = scan.next().trim();

            //TODO complete here
            if (receiveLoginInfo(authenticationField, passwordField, roleName)) {
                Person person = findPerson(authenticationField, passwordField, roleName);
                showLoginSuccess(person);
                break;
            } else {
                System.out.println("\n\u001B[31mInvalid credentials. Please try again.\u001B[0m\n");
            }
        }
    }

    private static void displayLoginMenu() {
        System.out.println("+---------------------------------------+");
        System.out.println("|              LOGIN PAGE               |");
        System.out.println("+---------------------------------------+");
        System.out.println("| Select your role:                     |");
        System.out.println("| 1. Customer                          |");
        System.out.println("| 2. Seller                            |");
        System.out.println("| 3. Support                           |");
        System.out.println("|                                       |");
        System.out.println("| Don't have an account? Create one (0) |");
        System.out.println("+---------------------------------------+");
        System.out.print("Your choice: ");
    }

    private static boolean isValidRole(String input) {
        return input.length() == 1 && (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("0"));
    }

    private static String getRoleName(String roleCode) {
        switch (roleCode) {
            case "1": return "Customer";
            case "2": return "Seller";
            case "3": return "Support";
            default: return "";
        }
    }

    private static void showLoginSuccess(Person person) {
        System.out.println("\n\u001B[32m=========================================\u001B[0m");
        System.out.println("\u001B[32m|                                       |\u001B[0m");
        System.out.printf("\u001B[32m|   Login Successful! Welcome %-10s |\n", person.getName());
        System.out.println("\u001B[32m|                                       |\u001B[0m");
        System.out.println("\u001B[32m=========================================\u001B[0m\n");
    }

    public static boolean receiveLoginInfo(String authenticationText, String password, String role) {
        return DataBase.getPersonList().stream()
                .anyMatch(c -> (c.getUsername().equals(authenticationText)
                        || c.getEmail().equals(authenticationText)
                        || c.getPhoneNumber().equals(authenticationText))
                        && c.getRole().equals(role)
                        && c.getPassword().equals(password));
    }

    public static Person findPerson(String authenticationText, String password, String selectedRole) {
        List<Person> personList = DataBase.getPersonList();

        for (Person person : personList) {
            if (authenticate(person, authenticationText, password)) {
                return person;
            }
        }
        return null;
    }

    private static boolean authenticate(Person person, String authenticationText, String password) {
        return (person.getUsername().equals(authenticationText)
                || person.getPhoneNumber().equals(authenticationText)
                || person.getEmail().equals(authenticationText))
                && person.getPassword().equals(password);
    }
}
