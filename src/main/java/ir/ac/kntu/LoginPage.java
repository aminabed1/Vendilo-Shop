package ir.ac.kntu;

import java.io.Serializable;
import java.util.*;

public class LoginPage implements Serializable {
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

            if (choice.equalsIgnoreCase("4")) {
                break;
            }
            if (choice.equals("0")) {
                CreateAccountPage createAccountPage = new CreateAccountPage();
                createAccountPage.createAccount();
                continue;
            }
            if (!choice.matches("[0-3]")) {
                showError("Please enter a valid option (0-4)");
                continue;
            }

            String roleName = getRoleName(choice);
            System.out.println("\n" + PROMPT + "Selected Role: " + OPTION + roleName + RESET + "\n");

            if (roleName.equals("Customer")) {
                System.out.print(PROMPT + "Enter Phone or Email: " + RESET + HIGHLIGHT);
            } else if (roleName.equals("Seller")) {
                System.out.print(PROMPT + "Enter Agency code: " + RESET + HIGHLIGHT);
            } else {
                System.out.println("Enter username: " + RESET + HIGHLIGHT);
            }

            String authenticationField = scan.nextLine().trim();
            System.out.print(RESET);
            System.out.print(PROMPT + "Enter Password: " + RESET + HIGHLIGHT);
            String passwordField = scan.nextLine().trim();
            System.out.print(RESET);
            Person person = authenticateUser(authenticationField, passwordField, roleName);
            if (person != null) {
                showLoginSuccess(person);
                if (person instanceof Customer) {
                    CustomerMainPage.getInstance().mainPage(person);
                } else if (person instanceof Seller) {
                    SellerMainPage.getInstance().mainPage(person);
                } else {
                    SupportMainPage.getInstance().mainPage(person);
                }
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
        System.out.println("| " + OPTION + "4. exit   " + MENU + "                            |");
        System.out.println("|                                       |");
        System.out.println("| " + OPTION + "0. Create New Account" + MENU + "                 |");
        System.out.println("+---------------------------------------+");
        System.out.print(PROMPT + "Your choice: " + RESET + HIGHLIGHT);
    }

    private String getRoleName(String roleCode) {
        return switch (roleCode) {
            case "1" -> "Customer";
            case "2" -> "Seller";
            case "3" -> "Support";
            default -> "";
        };
    }

    private void showLoginSuccess(Person person) {
        System.out.println("\n" + SUCCESS + "=========================================");
        System.out.println("|                                       |");
        System.out.println("|   " + BOLD + "LOGIN SUCCESSFUL!" + RESET + SUCCESS + "                   |");
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);
        Pause.pause(1500);
    }

    private void showError(String message) {
        System.out.println("\n" + ERROR + "⚠ " + message + RESET + "\n");
        Pause.pause(1000);
    }

    private Person authenticateUser(String authText, String password, String role) {
        for (Person p : DataBase.getPersonList()) {
            if (!p.getRole().equals(role)) {
                continue;
            }

            if (role.equals("Customer")) {
                boolean isMatch = (p.getEmail().equals(authText) || p.getPhoneNumber().equals(authText))
                        && p.getPassword().equals(password);
                if (isMatch) {
                    return p;
                }
            } else if (role.equals("Seller")) {
                if (p instanceof Seller seller) {
                    boolean isMatch = seller.getAgencyCode().equals(authText)
                            && seller.getPassword().equals(password);
                    if (isMatch) {
                        return seller;
                    }
                }
            } else {
                boolean isMatch = p.getUsername().equals(authText)
                        && p.getPassword().equals(password);
                if (isMatch) {
                    return p;
                }
            }
        }

        return null;
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}