package ir.ac.kntu;

import java.util.*;
//TODO create account for seller is coming soon
public class CreateAccountPage {
    private static final Scanner scan = new Scanner(System.in);

    public static void createAccount() {
        displayCreateAccountHeader();

        while (true) {
            String role = selectRole();
            if (role == null) continue;
            if (role.equals("BACK")) break;

            Person newUser = collectUserInfo(role);
            if (newUser != null) {
                completeAccountCreation(newUser);
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
        System.out.print("Your choice (1-2) / BACK: ");

        String input = scan.next().trim();
        if (input.equals("BACK")) {
            return "BACK";
        }
        if (!input.equals("1") && !input.equals("2")) {
            System.out.println("\n\u001B[31mERROR: Please enter 1, 2 or BACK\u001B[0m\n");
            return null;
        }
        return input.equals("1") ? "Customer" : "Seller";
    }

    public static Person collectUserInfo(String role) {
        System.out.println("\n\u001B[33mPlease enter your information:\u001B[0m");

        UserInfo userInfo = new UserInfo();
        boolean confirmationGiven = false;

        while (true) {
            printUserInfoMenu(userInfo, role);

            String choice = scan.next().trim();
            if (choice.equals("8")) {
                return null;
            }

            if (choice.equals("7")) {
                confirmationGiven = true;
            } else {
                processUserInput(choice, userInfo);
                continue;
            }

            if (confirmationGiven) {
                if (validateUserInfoCompletion(userInfo)) {
                    break;
                } else {
                    System.out.println("\n\u001B[31mERROR: Please fill all fields...!\u001B[0m\n");
                }
            }
        }

        return validateAndCreateUser(role, userInfo);
    }

    private static class UserInfo {
        String name = "";
        String surname = "";
        String phone = "";
        String email = "";
        String username = "";
        String password = "";
    }

    private static void printUserInfoMenu(UserInfo info, String role) {
        System.out.println("1.name : " + (info.name.isEmpty() ? "Empty" : info.name));
        System.out.println("2.surname : " + (info.surname.isEmpty() ? "Empty" : info.surname));
        System.out.println("3.phone : " + (info.phone.isEmpty() ? "Empty" : info.phone));
        System.out.println("4.email : " + (info.email.isEmpty() ? "Empty" : info.email));
        System.out.println("5.username : " + (info.username.isEmpty() ? "Empty" : info.username));
        System.out.println("6.password : " + (info.password.isEmpty() ? "Empty" : info.password));
        //TODO complete here
        if (role.equals("Seller")) {}
        System.out.println("7.Confirm Information");
        System.out.println("8.Back");
        System.out.println("Enter your choice: ");
    }

    private static void processUserInput(String choice, UserInfo info) {
        System.out.println("\u001B[31mEnter data : \u001B[0m");
        switch (choice) {
            case "1": info.name = scan.next().trim(); break;
            case "2": info.surname = scan.next().trim(); break;
            case "3": info.phone = scan.next().trim(); break;
            case "4": info.email = scan.next().trim(); break;
            case "5": info.username = scan.next().trim(); break;
            case "6": info.password = scan.next().trim(); break;
            default: System.out.println("\n\u001B[31mERROR: Please enter a valid choice!\n");
        }
    }

    private static boolean validateUserInfoCompletion(UserInfo info) {
        return !(info.name.isEmpty() || info.surname.isEmpty() || info.phone.isEmpty() ||
                info.email.isEmpty() || info.username.isEmpty() || info.password.isEmpty());
    }

    private static Person validateAndCreateUser(String role, UserInfo info) {
        if (isExistingAccount(info.email, info.phone, info.username)) {
            System.out.println("\n\u001B[31mERROR: Email or phone number already in use\u001B[0m");
            return null;
        }

        List<String> errors = new ArrayList<>();
        if (!InfoValidator.isPersonInfoValid(info.name, info.surname, info.phone,
                info.email, info.username, info.password, errors)) {
            displayErrors(errors);
            return null;
        }

        return createUser(role, info.name, info.surname, info.phone,
                info.email, info.username, info.password);
    }

    private static void completeAccountCreation(Person newUser) {
        showSuccessMessage(newUser);
        DataBase.addPerson(newUser);

        if (newUser instanceof Seller) {
            new Request(newUser.getUsername());
        }
    }

    public static String getInput(String prompt, boolean visible) {
        System.out.print(prompt);
        return visible ? scan.next().trim() : new String(System.console().readPassword());
    }

    public static boolean isExistingAccount(String email, String phone, String username) {
        System.out.println(DataBase.getPersonList().size());

        return DataBase.getPersonList().stream()
                .anyMatch(c -> c.getEmail().equals(email)
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