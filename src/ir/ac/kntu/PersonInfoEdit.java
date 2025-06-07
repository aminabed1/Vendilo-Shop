package ir.ac.kntu;

import java.io.Serializable;
import java.util.*;

public class PersonInfoEdit implements Serializable {
    private static final Scanner scan = new Scanner(System.in);
    private static Person currentPerson;

    private static String nameTemp, surNameTemp, phoneTemp, emailTemp,
            usernameTemp, passwordTemp, shopNameTemp;

    private static final String RESET = "\u001B[0m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String BOLD = "\u001B[1m";

    public static PersonInfoEdit getInstance() {
        return new PersonInfoEdit();
    }

    public void editUserInfo(Person user) {
        currentPerson = user;
        loadTempData();

        while (true) {
            printUserInfoMenu();

            System.out.print(PROMPT + "Select an option: " + RESET + HIGHLIGHT);
            String choice = scan.nextLine().trim();
            System.out.print(RESET);

            switch (choice) {
                case "1" -> nameTemp = promptInput("Enter new name: ");
                case "2" -> surNameTemp = promptInput("Enter new surname: ");
                case "3" -> phoneTemp = promptInput("Enter new phone: ");
                case "4" -> emailTemp = promptInput("Enter new email: ");
                case "5" -> usernameTemp = promptInput("Enter new username: ");
                case "6" -> passwordTemp = promptInput("Enter new password: ");
                case "7" -> {
                    if (currentPerson instanceof Seller) {
                        shopNameTemp = promptInput("Enter new shop name: ");
                    } else {
                        System.out.println(ERROR + "Invalid choice." + RESET);
                    }
                }
                case "#" -> {
                    if (tryApplyChanges()) {
                        System.out.println(SUCCESS + "Information updated successfully!" + RESET);
                        return;
                    }
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println(ERROR + "Invalid choice." + RESET);
            }
        }
    }

    private void loadTempData() {
        nameTemp = currentPerson.getName();
        surNameTemp = currentPerson.getSurname();
        phoneTemp = currentPerson.getPhoneNumber();
        emailTemp = currentPerson.getEmail();
        usernameTemp = currentPerson.getUsername();
        passwordTemp = currentPerson.getPassword();
        shopNameTemp = (currentPerson instanceof Seller seller) ? seller.getShopName() : null;
    }

    private String promptInput(String message) {
        System.out.print(PROMPT + message + RESET + HIGHLIGHT);
        String input = scan.nextLine().trim();
        System.out.print(RESET);
        return input;
    }

    private boolean tryApplyChanges() {
        List<String> errorList = new ArrayList<>();

        String oldUsername = currentPerson.getUsername();
        String oldEmail = currentPerson.getEmail();
        String oldPhone = currentPerson.getPhoneNumber();
        currentPerson.setUsername("");
        currentPerson.setEmail("");
        currentPerson.setPhoneNumber("");
        InfoValidator validator = new InfoValidator();
        boolean isValid = validator.isPersonInfoValid(
                nameTemp, surNameTemp, phoneTemp, emailTemp, usernameTemp, passwordTemp, errorList);

        currentPerson.setUsername(oldUsername);
        currentPerson.setEmail(oldEmail);
        currentPerson.setPhoneNumber(oldPhone);

        if (!isValid) {
            displayErrors(errorList);
            return false;
        }

        updateInfo();
        return true;
    }

    private void updateInfo() {
        currentPerson.setName(nameTemp);
        currentPerson.setSurname(surNameTemp);
        currentPerson.setPhoneNumber(phoneTemp);
        currentPerson.setEmail(emailTemp);
        currentPerson.setUsername(usernameTemp);
        currentPerson.setPassword(passwordTemp);
        if (shopNameTemp != null && currentPerson instanceof Seller seller) {
            seller.setShopName(shopNameTemp);
        }
    }

    private void printUserInfoMenu() {
        System.out.println(MENU + "╔══════════════════════════════════════════════════════╗");
        System.out.println("║" + BOLD + "                CURRENT USER INFORMATION           " + RESET + MENU + "   ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        printInfoLine("1. Name", nameTemp);
        printInfoLine("2. Surname", surNameTemp);
        printInfoLine("3. Phone", phoneTemp);
        printInfoLine("4. Email", emailTemp);
        printInfoLine("5. Username", usernameTemp);
        printInfoLine("6. Password", passwordTemp);

        if (currentPerson instanceof Seller) {
            printInfoLine("7. Shop Name", shopNameTemp);
        }

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║ " + OPTION + "#. Confirm Changes" + MENU + "                                   ║");
        System.out.println("║ " + OPTION + "0. Cancel Edit    " + MENU + "                                   ║");
        System.out.println("╚══════════════════════════════════════════════════════╝" + RESET);
    }

    private void printInfoLine(String label, String value) {
        String display = (value == null || value.isEmpty()) ? "Empty" : value;
        if (display.length() > 30) {
            display = display.substring(0, 27) + "...";
        }
        System.out.printf(" " + OPTION + "%-15s: " + HIGHLIGHT + "%-30s" + MENU + "\n", label, display);
    }

    private void displayErrors(List<String> errors) {
        System.out.println(ERROR + "\nInformation edit failed due to the following errors:" + RESET);
        errors.forEach(error -> System.out.println(ERROR + " - " + error + RESET));
        System.out.println();
    }
}
