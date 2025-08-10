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

            if (choice.equalsIgnoreCase("5")) {
                break;
            }
            if (choice.equals("0")) {
                CreateAccountPage createAccountPage = new CreateAccountPage();
                createAccountPage.createAccount("Ordinary");
                continue;
            }
            if (!choice.matches("[0-4]")) {
                showError("Please enter a valid option (0-5)");
                continue;
            }

            Role roleName = getRoleName(choice);
            System.out.println("\n" + PROMPT + "Selected Role: " + OPTION + roleName + RESET + "\n");

            if (roleName == Role.Customer) {
                System.out.print(PROMPT + "Enter Phone or Email: " + RESET + HIGHLIGHT);
            } else if (roleName == Role.Seller) {
                System.out.print(PROMPT + "Enter Agency code: " + RESET + HIGHLIGHT);
            } else {
                System.out.print(PROMPT + "Enter Your Username: " + RESET + HIGHLIGHT);
            }

            String authenticationField = scan.nextLine().trim();
            System.out.print(RESET);
            System.out.print(PROMPT + "Enter Password: " + RESET + HIGHLIGHT);
            String passwordField = scan.nextLine().trim();
            System.out.print(RESET);
            Person person = authenticateUser(authenticationField, passwordField, roleName);
            if (person != null) {
                if (!person.getIsActive()) {
                    SystemMessage.printMessage("Your Account has been closed.", MessageTitle.Error);
                    continue;
                }
                showLoginSuccess();
                if (person instanceof Customer) {
                    CustomerPage.getInstance().mainPage(person);
                } else if (person instanceof Seller) {
                    SellerPage.getInstance().mainPage(person);

                } else if (person instanceof Support) {
                    SupportPage.getInstance().mainPage(person);
                } else if (person instanceof Manager){
                    ManagerPage.getInstance().mainPage(person);
                }
            } else {
                SystemMessage.printMessage("Invalid credentials. Please try again.", MessageTitle.Error);
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
        System.out.println("| " + OPTION + "4. Manager" + MENU + "                            |");
        System.out.println("| " + OPTION + "5. exit   " + MENU + "                            |");
        System.out.println("|                                       |");
        System.out.println("| " + OPTION + "0. Create New Account" + MENU + "                 |");
        System.out.println("+---------------------------------------+");
        System.out.print(PROMPT + "Your choice: " + RESET + HIGHLIGHT);
    }

    private Role getRoleName(String roleCode) {
        return switch (roleCode) {
            case "1" -> Role.Customer;
            case "2" -> Role.Seller;
            case "3" -> Role.Support;
            case "4" -> Role.Manager;
            default -> null;
        };
    }

    private void showLoginSuccess() {
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

    private Person authenticateUser(String authText, String password, Role role) {
        for (Person person : DataBase.getInstance().getPersonList()) {
            if (person.getRole() != role) {
                continue;
            }
            switch (role) {
                case Customer -> {
                    Customer customer = (Customer) person;
                    boolean isMatch = (customer.getEmail().equals(authText) || customer.getPhoneNumber().equals(authText))
                            && person.getPassword().equals(password);
                    if (isMatch) {
                        return person;
                    }
                }
                case Seller -> {
                    Seller seller = (Seller) person;
                    boolean isMatch = seller.getAgencyCode().equals(authText)
                            && seller.getPassword().equals(password);
                    if (isMatch) {
                        return person;
                    }
                }
                case Support -> {
                    Support support = (Support) person;

                    boolean isMatch = support.getUsername().equals(authText)
                            && person.getPassword().equals(password);
                    if (isMatch) {
                        return person;
                    }
                }
                case Manager -> {
                    Manager manager = (Manager) person;
                    boolean isMatch = manager.getUsername().equals(authText)
                            && manager.getPassword().equals(password);
                    if (isMatch) {
                        return person;
                    }
                }
                default -> {
                    return null;
                }
            }
        }
        return null;
    }
}