package ir.ac.kntu;

import java.io.Serializable;
import java.util.Scanner;

public class LoginPage implements Serializable {
    private static final Scanner scan = new Scanner(System.in);

    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
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

            if ("5".equals(choice)) {
                break;
            }
            if ("0".equals(choice)) {
                new CreateAccountPage().createAccount("Ordinary");
                continue;
            }
            if (!choice.matches("[0-4]")) {
                SystemMessage.printMessage("Please enter a valid option (0-5)", MessageTitle.Error);
                continue;
            }

            Role roleName = getRoleName(choice);
            String authField = getAuthField(roleName);
            String password = getPassword();

            Person person = authenticateUser(authField, password, roleName);
            if (person == null) {
                SystemMessage.printMessage("Invalid credentials. Please try again.", MessageTitle.Error);
                continue;
            }
            if (!person.getIsActive()) {
                SystemMessage.printMessage("Your Account has been closed.", MessageTitle.Error);
                continue;
            }
            showLoginSuccess();
            openRolePage(person);
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

    private String getAuthField(Role roleName) {
        String promptMsg = switch (roleName) {
            case Customer -> "Enter Phone or Email: ";
            case Seller -> "Enter Agency code: ";
            default -> "Enter Your Username: ";
        };
        System.out.print(PROMPT + promptMsg + RESET + HIGHLIGHT);
        return scan.nextLine().trim();
    }

    private String getPassword() {
        System.out.print(RESET + PROMPT + "Enter Password: " + RESET + HIGHLIGHT);
        return scan.nextLine().trim();
    }

    private Person authenticateUser(String authText, String password, Role role) {
        for (Person person : DataBase.getInstance().getPersonList()) {
            if (person.getRole() != role) {
                continue;
            }
            switch (role) {
                case Customer -> {
                    Customer customer = (Customer) person;
                    if ((customer.getEmail().equals(authText) || customer.getPhoneNumber().equals(authText)) && person.getPassword().equals(password)) {
                        return person;
                    }
                }
                case Seller -> {
                    Seller seller = (Seller) person;
                    if (seller.getAgencyCode().equals(authText) && seller.getPassword().equals(password)) {
                        return person;
                    }
                }
                case Support -> {
                    Support support = (Support) person;
                    if (support.getUsername().equals(authText) && person.getPassword().equals(password)) {
                        return person;
                    }
                }
                case Manager -> {
                    Manager manager = (Manager) person;
                    if (manager.getUsername().equals(authText) && manager.getPassword().equals(password)){
                        return person;
                    }
                }
                default -> {}
            }
        }
        return null;
    }

    private void showLoginSuccess() {
        System.out.println("\n" + SUCCESS + "=========================================");
        System.out.println("|                                       |");
        System.out.println("|   " + BOLD + "LOGIN SUCCESSFUL!" + RESET + SUCCESS + "                   |");
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);
        Pause.pause(1500);
    }

    private void openRolePage(Person person) {
        if (person instanceof Customer) {
            CustomerPage.getInstance().mainPage(person);
        } else if (person instanceof Seller) {
            SellerPage.getInstance().mainPage(person);
        } else if (person instanceof Support) {
            SupportPage.getInstance().mainPage(person);
        } else if (person instanceof Manager) {
            ManagerPage.getInstance().mainPage(person);
        }
    }
}
