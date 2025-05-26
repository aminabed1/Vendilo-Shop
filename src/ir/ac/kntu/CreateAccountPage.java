package ir.ac.kntu;

import java.util.*;

public class CreateAccountPage {
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

    public void createAccount() {
        clearScreen();
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

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void displayCreateAccountHeader() {
        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║                                     ║");
        System.out.println("║" + BOLD + HIGHLIGHT + "        ACCOUNT REGISTRATION        " + RESET + TITLE + " ║");
        System.out.println("║                                     ║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public String selectRole() {
        System.out.println(MENU + "╔═════════════════════════════════════╗");
        System.out.println("║" + BOLD + "        SELECT ACCOUNT TYPE        " + RESET + MENU + "  ║");
        System.out.println("╠═════════════════════════════════════╣");
        System.out.println("║ " + OPTION + "1. Customer" + MENU + "                         ║");
        System.out.println("║ " + OPTION + "2. Seller" + MENU + "                           ║");
        System.out.println("╠═════════════════════════════════════╣");
        System.out.println("║ " + OPTION + "Type " + BOLD + "BACK" + RESET + OPTION + " to return" + MENU + "                 ║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
        System.out.print(PROMPT + "Your choice (1-2): " + RESET + HIGHLIGHT);

        String input = scan.nextLine().trim();
        System.out.print(RESET);

        if (input.equalsIgnoreCase("BACK")) {
            return "BACK";
        }
        if (!input.equals("1") && !input.equals("2")) {
            showError("Please enter 1, 2 or BACK");
            return null;
        }
        return input.equals("1") ? "Customer" : "Seller";
    }

    public Person collectUserInfo(String role) {
        System.out.println("\n" + PROMPT + "Please enter your information:" + RESET);

        UserInfo userInfo = new UserInfo();
        boolean confirmationGiven = false;

        while (true) {
            printUserInfoMenu(userInfo, role);

            System.out.print(PROMPT + "Enter your choice: " + RESET + HIGHLIGHT);
            String choice = scan.nextLine().trim();
            System.out.print(RESET);

            if (choice.equals("8")) {
                return null;
            }

            if (choice.equals("7")) {
                confirmationGiven = true;
            } else {
                processUserInput(choice, userInfo, role);
                continue;
            }

            if (confirmationGiven) {
                if (validateUserInfoCompletion(userInfo, role)) {
                    break;
                } else {
                    showError("Please fill all required fields");
                }
            }
        }

        return validateAndCreateUser(role, userInfo);
    }

    private class UserInfo {
        String name = "";
        String surname = "";
        String phone = "";
        String email = "";
        String username = "";
        String password = "";
        String shopName = "";
        String province = "";
        String sellerID = "";
    }

    private void printUserInfoMenu(UserInfo info, String role) {
        int maxLabelLength = 15;
        int maxValueLength = 30;

        System.out.println(MENU + "╔══════════════════════════════════════════════════════╗");
        System.out.println("║" + BOLD + "                ENTER YOUR INFORMATION            " + RESET + MENU + "    ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");

        printInfoLine("1. Name", info.name, maxLabelLength, maxValueLength);
        printInfoLine("2. Surname", info.surname, maxLabelLength, maxValueLength);
        printInfoLine("3. Phone", info.phone, maxLabelLength, maxValueLength);
        printInfoLine("4. Email", info.email, maxLabelLength, maxValueLength);
        printInfoLine("5. Username", info.username, maxLabelLength, maxValueLength);
        printInfoLine("6. Password", info.password.isEmpty() ? "Empty" : "******", maxLabelLength, maxValueLength);

        if (role.equals("Seller")) {
            printInfoLine("9. Shop Name", info.shopName, maxLabelLength, maxValueLength);
            printInfoLine("10. Province", info.province, maxLabelLength, maxValueLength);
            printInfoLine("11. SellerID", info.sellerID, maxLabelLength, maxValueLength);
        }

        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║ " + OPTION + "7. Confirm Information" + MENU + "                               ║");
        System.out.println("║ " + OPTION + "8. Back" + MENU + "                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════╝" + RESET);
    }

    private void printInfoLine(String label, String value, int labelWidth, int valueWidth) {
        String displayValue = value.isEmpty() ? "Empty" : value;
        System.out.printf("║ " + OPTION + "%-" + labelWidth + "s: " + HIGHLIGHT + "%-" + valueWidth + "s" + MENU + "      ║\n",
                label, displayValue.length() > valueWidth ? displayValue.substring(0, valueWidth-3) + "..." : displayValue);
    }

    private void processUserInput(String choice, UserInfo info, String role) {
        switch (choice) {
            case "1":
                System.out.print(PROMPT + "Enter name: " + RESET + HIGHLIGHT);
                info.name = scan.nextLine().trim();
                break;
            case "2":
                System.out.print(PROMPT + "Enter surname: " + RESET + HIGHLIGHT);
                info.surname = scan.nextLine().trim();
                break;
            case "3":
                System.out.print(PROMPT + "Enter phone: " + RESET + HIGHLIGHT);
                info.phone = scan.nextLine().trim();
                break;
            case "4":
                System.out.print(PROMPT + "Enter email: " + RESET + HIGHLIGHT);
                info.email = scan.nextLine().trim();
                break;
            case "5":
                System.out.print(PROMPT + "Enter username: " + RESET + HIGHLIGHT);
                info.username = scan.nextLine().trim();
                break;
            case "6":
                System.out.print(PROMPT + "Enter password: " + RESET);
                info.password = new String(System.console().readPassword());
                break;
            case "9":
                if (role.equals("Seller")) {
                    System.out.print(PROMPT + "Enter shop name: " + RESET + HIGHLIGHT);
                    info.shopName = scan.nextLine().trim();
                }
                break;
            case "10":
                if (role.equals("Seller")) {
                    System.out.print(PROMPT + "Enter province: " + RESET + HIGHLIGHT);
                    info.province = scan.nextLine().trim();
                }
                break;
            case "11":
                if (role.equals("Seller")) {
                    System.out.print(PROMPT + "Enter sellerID: " + RESET);
                    info.sellerID = scan.nextLine().trim();
                }
                break;
            default:
                showError("Please enter a valid choice!");
        }
        System.out.print(RESET);
    }

    private boolean validateUserInfoCompletion(UserInfo info, String role) {
        if (info.name.isEmpty() || info.surname.isEmpty() || info.phone.isEmpty() ||
                info.email.isEmpty() || info.username.isEmpty() || info.password.isEmpty()) {
            return false;
        }

        return (!role.equals("Seller") || (!info.shopName.isEmpty() && !info.province.isEmpty())) && !info.sellerID.isEmpty();
    }

    private Person validateAndCreateUser(String role, UserInfo info) {
        if (role.equals("Seller")) {
            if (isExistingSeller(info.email, info.phone, info.username, info.sellerID)) {
                showError("Email, phone, username or sellerID already in use!");
                return null;
            }
        }

        if (isExistingCustomer(info.email, info.phone, info.username)) {
            showError("Email, phone or username already in use");
            return null;
        }

        List<String> errors = new ArrayList<>();
        if (!InfoValidator.isPersonInfoValid(info.name, info.surname, info.phone,
                info.email, info.username, info.password, errors)) {
            displayErrors(errors);
            return null;
        }

        if (role.equals("Customer")) {
            return new Customer(info.name, info.surname, info.phone,
                    info.email, info.username, info.password);
        } else {
            return new Seller(info.name, info.surname, info.phone,
                    info.email, info.username, info.password,
                    info.shopName, info.sellerID, info.province, generateAgencyCode());
        }
    }

    private void completeAccountCreation(Person newUser) {
        showSuccessMessage(newUser);
        DataBase.addPerson(newUser);

        if (newUser instanceof Seller) {
            String description = "New seller sign up";
            new Request(((Seller) newUser).getAgencyCode(), description);
            System.out.println("Your agency code is : " + ((Seller) newUser).getAgencyCode());
            System.out.println(SUCCESS + "Your seller request has been submitted for Support" + RESET);
            pause(5000);
        }
    }

    public boolean isExistingSeller(String email, String phone, String username, String sellerID) {
        return DataBase.getPersonList().stream()
                .anyMatch(c -> c.getEmail().equals(email)
                        || c.getPhoneNumber().equals(phone)
                        || c.getUsername().equals(username)
                        || (c instanceof Seller && ((Seller) c).getSellerID().equals(sellerID)));
    }

    public boolean isExistingCustomer(String email, String phone, String username) {
        return DataBase.getPersonList().stream()
                .anyMatch(c -> c.getEmail().equals(email)
                        || c.getPhoneNumber().equals(phone)
                        || c.getUsername().equals(username));
    }

    public void displayErrors(List<String> errors) {
        System.out.println(ERROR + "\nAccount creation failed due to the following errors:" + RESET);
        errors.forEach(error -> System.out.println(ERROR + " - " + error + RESET));
        System.out.println();
    }

    public void showError(String message) {
        System.out.println(ERROR + "\n⚠ " + message + RESET);
        pause(1000);
    }

    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void showSuccessMessage(Person user) {
        System.out.println(SUCCESS + "\n╔═══════════════════════════════════════╗");
        System.out.println("║                                       ║");
        System.out.println("║" + BOLD + "      ACCOUNT CREATED SUCCESSFULLY     " + RESET + SUCCESS + "║");
        System.out.printf("║ Role: %-31s ║\n", user.getRole());
        if (user instanceof Seller seller) {
            System.out.printf("║ Shop: %-31s ║\n", seller.getShopName());
            System.out.printf("║ Province: %-27s ║\n", seller.getProvince());
            System.out.printf("║ sellerID: %-27s ║\n", seller.getSellerID());
        }
        System.out.println("║                                       ║");
        System.out.println("╚═══════════════════════════════════════╝" + RESET);
        pause(2000);
    }

    public String generateAgencyCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            code.append(generateRandomNumber());
            code.append(generateCharacter());
        }

        return code.toString();
    }

    public int generateRandomNumber() {
        return (int) (Math.random() * 10);
    }

    public String generateCharacter() {
        return String.valueOf((char) ((int) (Math.random() * 25) + 65));
    }
}