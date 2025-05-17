package ir.ac.kntu;

import java.util.*;

public class CreateAccountPage {
    private static final Scanner scan = new Scanner(System.in);

    public static void createAccount() {
        displayCreateAccountHeader();

        while (true) {
            String role = selectRole();
            if (role == null) {
                continue;
            }

            Person newUser = collectUserInfo(role);
            if (newUser != null) {
                showSuccessMessage(newUser);
                DataBase.addPerson(newUser);
                LoginPage.loginPage();
                break;
            }
        }
    }

    public static void displayCreateAccountHeader() {
        System.out.println("\n\u001B[36m=========================================\u001B[0m");
        System.out.println("\u001B[36m|        ACCOUNT REGISTRATION           |\u001B[0m");
        System.out.println("\u001B[36m=========================================\u001B[0m\n");
    }

    public static String selectRole() {
        System.out.println("+---------------------------------------+");
        System.out.println("| Select account type:                  |");
        System.out.println("| 1. Customer                           |");
        System.out.println("| 2. Seller                             |");
        System.out.println("+---------------------------------------+");
        System.out.print("Your choice (1-2): ");

        String input = scan.next().trim();
        if (!input.equals("1") && !input.equals("2")) {
            System.out.println("\n\u001B[31mERROR: Please enter 1 or 2\u001B[0m\n");
            return null;
        }
        return input.equals("1") ? "Customer" : "Seller";
    }

    public static Person collectUserInfo(String role) {
        System.out.println("\n\u001B[33mPlease enter your information:\u001B[0m");

        String name = getInput("Name: ", true);
        String surname = getInput("Surname: ", true);
        String phone = getInput("Phone Number: ", true);
        String email = getInput("Email: ", true);
        String username = getInput("Username: ", true);
        String password = getInput("Password: ", false);

        if (isExistingAccount(email, phone, username)) {
            System.out.println("\n\u001B[31mERROR: Email or phone number already in use\u001B[0m");
            return null;
        }

        List<String> errors = new ArrayList<>();
        if (!InfoValidator.isPersonInfoValid(name, surname, phone, email, username, password, errors)) {
            displayErrors(errors);
            return null;
        }

        return createUser(role, name, surname, phone, email, username, password);
    }

    public static String getInput(String prompt, boolean visible) {
        System.out.print(prompt);
        return visible ? scan.next().trim() : new String(System.console().readPassword());
    }

    public static boolean isExistingAccount(String email, String phone, String username) {
        return DataBase.getPersonList().stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email)
                        || c.getPhoneNumber().equals(phone)
                        || c.getUsername().equals(username));
    }

    public static void displayErrors(List<String> errors) {
        System.out.println("\n\u001B[31mAccount creation failed due to the following errors:\u001B[0m");
        errors.forEach(error -> System.out.println(" - " + error));
        System.out.println();
    }

    public static Person createUser(String role, String name, String surname, String phone,
                                     String email, String username, String password) {
        if (role.equals("Customer")) {
            return new Customer(name, surname, phone, email, username, password);
        } else {
            System.out.print("Enter shop name: ");
            String shopName = scan.next().trim();
            System.out.print("Enter province: ");
            String province = scan.next().trim();
            return new Seller(name, surname, phone, email, username, password, shopName, province);
        }
    }

    public static void showSuccessMessage(Person user) {
        System.out.println("\n\u001B[32m=========================================\u001B[0m");
        System.out.println("\u001B[32m|      ACCOUNT CREATED SUCCESSFULLY     |\u001B[0m");
        System.out.printf("\u001B[32m| Role: %-30s |\n", user.getRole());
        System.out.println("\u001B[32m=========================================\u001B[0m\n");
    }
}