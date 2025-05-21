package ir.ac.kntu;

import java.util.*;

public class LoginPage {
    private static final Scanner scan = new Scanner(System.in);

    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String BOLD = "\u001B[1m";

    public void loginPage() {
        clearScreen();
        showWelcomeScreen();
        readLoginInfo();
    }

    private void showWelcomeScreen() {
        System.out.println(TITLE + "=========================================");
        System.out.println("|                                       |");
        System.out.println("|    " + BOLD + HIGHLIGHT + "WELCOME TO VENDILO ONLINE SHOP" + RESET + TITLE + "     |");
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);
        System.out.println();
    }

    private void readLoginInfo() {
        while (true) {
            displayLoginMenu();

            String choice = scan.nextLine().trim();

            if (choice.equalsIgnoreCase("BACK")) {
                break;
            }

            if (choice.equals("0")) {
                CreateAccountPage createAccountPage = new CreateAccountPage();
                createAccountPage.createAccount();
                continue;
            }

            if (!isValidRole(choice)) {
                showError("Please enter a valid option (0-3) or BACK");
                continue;
            }

            String roleName = getRoleName(choice);
            System.out.println("\n" + PROMPT + "Selected Role: " + OPTION + roleName + RESET + "\n");

            System.out.print(PROMPT + "Enter Username, Phone or Email: " + RESET + HIGHLIGHT);
            String authenticationField = scan.nextLine().trim();
            System.out.print(RESET);

            System.out.print(PROMPT + "Enter Password: " + RESET + HIGHLIGHT);
            String passwordField = scan.nextLine().trim();
            System.out.print(RESET);

            Person person = authenticateUser(authenticationField, passwordField, roleName);
            if (person != null) {
                showLoginSuccess(person);
                MainPage mainPage = new MainPage();
                mainPage.mainPage(person);
//                break;
            } else {
                showError("Invalid credentials. Please try again.");
            }
        }
    }

    private void displayLoginMenu() {
        System.out.println(MENU + "+---------------------------------------+");
        System.out.println("|              " + BOLD + "LOGIN PAGE" + RESET + MENU + "               |");
        System.out.println("+---------------------------------------+");
        System.out.println("| " + PROMPT + "Select your role:" + MENU + "                     |");
        System.out.println("| " + OPTION + "1. Customer" + MENU + "                           |");
        System.out.println("| " + OPTION + "2. Seller" + MENU + "                             |");
        System.out.println("| " + OPTION + "3. Support" + MENU + "                            |");
        System.out.println("|                                       |");
        System.out.println("| " + OPTION + "0. Create New Account" + MENU + "                 |");
        System.out.println("+---------------------------------------+");
        System.out.print(PROMPT + "Your choice: " + RESET + HIGHLIGHT);
    }

    private boolean isValidRole(String input) {
        return input.matches("[0-3]");
    }

    private String getRoleName(String roleCode) {
        switch (roleCode) {
            case "1": return "Customer";
            case "2": return "Seller";
            case "3": return "Support";
            default: return "";
        }
    }

    private void showLoginSuccess(Person person) {
        System.out.println("\n" + SUCCESS + "=========================================");
        System.out.println("|                                       |");
        System.out.println("|   " + BOLD + "LOGIN SUCCESSFUL!" + RESET + SUCCESS + "                   |");
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);
        pause(1500);
    }

    private void showError(String message) {
        System.out.println("\n" + ERROR + "⚠ " + message + RESET + "\n");
        pause(1000);
    }

    private Person authenticateUser(String authText, String password, String role) {
        return DataBase.getPersonList().stream()
                .filter(p -> p.getRole().equals(role) &&
                        (p.getUsername().equals(authText) ||
                                p.getEmail().equals(authText) ||
                                p.getPhoneNumber().equals(authText)) &&
                        p.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}