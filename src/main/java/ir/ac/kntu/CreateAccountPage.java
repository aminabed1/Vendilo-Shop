package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public class CreateAccountPage implements Serializable {
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

    public static CreateAccountPage getInstance() {
        return new CreateAccountPage();
    }

    public void createAccount(String userAccess) {
        displayCreateAccountHeader();

        while (true) {
            String role = selectRole(userAccess);
            if (role == null) {
                continue;
            }
            if (role.equals("BACK")) {
                break;
            }

            Person newUser = collectUserInfo(role);
            if (newUser != null) {
                completeAccountCreation(newUser);
                break;
            }
        }
    }

    public void displayCreateAccountHeader() {
        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║                                     ║");
        System.out.println("║" + BOLD + HIGHLIGHT + "        ACCOUNT REGISTRATION        " + RESET + TITLE + " ║");
        System.out.println("║                                     ║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public String selectRole(String userAccess) {
        System.out.println(MENU + "╔═════════════════════════════════════╗");
        System.out.println("║" + BOLD + "        SELECT ACCOUNT TYPE        " + RESET + MENU + "  ║");
        System.out.println("╠═════════════════════════════════════╣");
        if (userAccess.equals("Ordinary")) {
            System.out.println("║ " + OPTION + "1. Customer" + MENU + "                         ║");
            System.out.println("║ " + OPTION + "2. Seller" + MENU + "                           ║");
        } else {
            System.out.println("║ " + OPTION + "1. Manager " + MENU + "                         ║");
            System.out.println("║ " + OPTION + "2. Support" + MENU + "                          ║");
        }
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
        if (userAccess.equals("Ordinary")) {
            return input.equals("1") ? "Customer" : "Seller";
        } else {
            return input.equals("1") ? "Manager" : "Support";
        }
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

            if (choice.equals("6") && (role.equals("Manager") || role.equals("Support"))) {
                return null;
            }
            if (choice.equals("8") && (role.equals("Customer") || role.equals("Seller"))) {
                return null;
            }

            if ((role.equals("Manager") || role.equals("Support"))) {
                if (choice.equals("5")) {
                    confirmationGiven = true;
                }
            } else {
                if (choice.equals("7")) {
                    confirmationGiven = true;
                }
            }

            if (confirmationGiven) {
                if (!userInfo.isAnyFieldEmpty(role)) {
                    break;
                } else {
                    showError("Please fill all required fields");
                }
            } else {
                processUserInput(choice, userInfo, role);
            }
        }

        return validateAndCreateUser(role, userInfo);
    }

    private static class UserInfo {
        private String name = "";
        private String surname = "";
        private String phone = "";
        private String email = "";
        private String username = "";
        private String password = "";
        private String shopName = "";
        private String province = "";
        private String sellerID = "";

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getShopName() {
            return shopName;
        }

        public String getProvince() {
            return province;
        }

        public String getSellerID() {
            return sellerID;
        }

        public void setName(String name) {
            if (name != null) {
                this.name = name.trim();
            }
        }

        public void setSurname(String surname) {
            if (surname != null) {
                this.surname = surname.trim();
            }
        }

        public void setPhone(String phone) {
            if (phone != null) {
                this.phone = phone.trim();
            }
        }

        public void setEmail(String email) {
            if (email != null) {
                this.email = email.trim();
            }
        }

        public void setUsername(String username) {
            if (username != null) {
                this.username = username.trim();
            }
        }

        public void setPassword(String password) {
            if (password != null) {
                this.password = password;
            }
        }

        public void setShopName(String shopName) {
            if (shopName != null) {
                this.shopName = shopName.trim();
            }
        }

        public void setProvince(String province) {
            if (province != null) {
                this.province = province.trim();
            }
        }

        public void setSellerID(String sellerID) {
            if (sellerID != null) {
                this.sellerID = sellerID.trim();
            }
        }

        public boolean isAnyFieldEmpty(String role) {
            if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty()) {
                return true;
            }
            if (role.equals("Customer")) {
                return phone.isEmpty() || email.isEmpty();
            }
            if (role.equals("Seller")) {
                return phone.isEmpty() || email.isEmpty() || shopName.isEmpty() || province.isEmpty() || sellerID.isEmpty();
            }
            return false;
        }

        public Map<String, String> toMap(String role) {
            Map<String, String> infoMap = new LinkedHashMap<>();
            infoMap.put("Name", name);
            infoMap.put("Surname", surname);
            if (!role.equals("Manager") && !role.equals("Support")) {
                infoMap.put("Phone", phone);
                infoMap.put("Email", email);
            }
            infoMap.put("Username", username);
            infoMap.put("Password", password.isEmpty() ? "" : "******");
            if (role.equals("Seller")) {
                infoMap.put("Shop Name", shopName);
                infoMap.put("Province", province);
                infoMap.put("Seller ID", sellerID);
            }
            return infoMap;
        }
    }

    private void printUserInfoMenu(UserInfo info, String role) {
        String accessLevel;
        if (role.equals("Manager") || role.equals("Support")) {
            accessLevel = "Special";
        } else {
            accessLevel = "Ordinary";
        }
        int maxLabelLength = 15;
        int maxValueLength = 30;

        System.out.println(MENU + "╔══════════════════════════════════════════════════════╗");
        System.out.println("║" + BOLD + "                ENTER YOUR INFORMATION            " + RESET + MENU + "    ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");

        Map<String, String> infoMap = info.toMap(role);

        printInfoLine("1. Name", infoMap.get("Name"), maxLabelLength, maxValueLength);
        printInfoLine("2. Surname", infoMap.get("Surname"), maxLabelLength, maxValueLength);

        if (!role.equals("Manager") && !role.equals("Support")) {
            printInfoLine("3. Phone", infoMap.get("Phone"), maxLabelLength, maxValueLength);
            printInfoLine("4. Email", infoMap.get("Email"), maxLabelLength, maxValueLength);
        }

        printInfoLine(accessLevel.equals("Ordinary") ? "5" : "3" + ". Username", infoMap.get("Username"), maxLabelLength, maxValueLength);
        printInfoLine(accessLevel.equals("Ordinary") ? "5" : "4" + ". Password", infoMap.get("Password"), maxLabelLength, maxValueLength);

        if (role.equals("Seller")) {
            printInfoLine("9. Shop Name", infoMap.get("Shop Name"), maxLabelLength, maxValueLength);
            printInfoLine("10. Province", infoMap.get("Province"), maxLabelLength, maxValueLength);
            printInfoLine("11. SellerID", infoMap.get("Seller ID"), maxLabelLength, maxValueLength);
        }

        System.out.println("╠══════════════════════════════════════════════════════╣");
        if (role.equals("Manager") || role.equals("Support")) {
            System.out.println("║ " + OPTION + "5. Confirm Information" + MENU + "                               ║");
            System.out.println("║ " + OPTION + "6. Back" + MENU + "                                              ║");
        } else {
            System.out.println("║ " + OPTION + "7. Confirm Information" + MENU + "                               ║");
            System.out.println("║ " + OPTION + "8. Back" + MENU + "                                              ║");
        }
        System.out.println("╚══════════════════════════════════════════════════════╝" + RESET);
    }

    private void printInfoLine(String label, String value, int labelWidth, int valueWidth) {
        String displayValue = value == null || value.isEmpty() ? "Empty" : value;
        System.out.printf("║ " + OPTION + "%-" + labelWidth + "s: " + HIGHLIGHT + "%-" + valueWidth + "s" + MENU + "      ║\n",
                label, displayValue.length() > valueWidth ? displayValue.substring(0, valueWidth-3) + "..." : displayValue);
    }

    private void processUserInput(String choice, UserInfo info, String role) {
        switch (choice) {
            case "1":
                System.out.print(PROMPT + "Enter name: " + RESET + HIGHLIGHT);
                info.setName(scan.nextLine());
                break;
            case "2":
                System.out.print(PROMPT + "Enter surname: " + RESET + HIGHLIGHT);
                info.setSurname(scan.nextLine());
                break;
            case "3":
                if (!role.equals("Manager") && !role.equals("Support")) {
                    System.out.print(PROMPT + "Enter phone: " + RESET + HIGHLIGHT);
                    info.setPhone(scan.nextLine());
                } else {
                    System.out.print(PROMPT + "Enter Username: " + RESET + HIGHLIGHT);
                    info.setUsername(scan.nextLine());
                }
                break;
            case "4":
                if (!role.equals("Manager") && !role.equals("Support")) {
                    System.out.print(PROMPT + "Enter email: " + RESET + HIGHLIGHT);
                    info.setEmail(scan.nextLine());
                } else {
                    System.out.print(PROMPT + "Enter Password: " + RESET + HIGHLIGHT);
                    info.setPassword(scan.nextLine());
                }
                break;
            case "5":
                System.out.print(PROMPT + "Enter username: " + RESET + HIGHLIGHT);
                info.setUsername(scan.nextLine());
                break;
            case "6":
                System.out.print(PROMPT + "Enter password: " + RESET);
                info.setPassword(new String(System.console().readPassword()));
                break;
            case "9":
                if (role.equals("Seller")) {
                    System.out.print(PROMPT + "Enter shop name: " + RESET + HIGHLIGHT);
                    info.setShopName(scan.nextLine());
                }
                break;
            case "10":
                if (role.equals("Seller")) {
                    System.out.print(PROMPT + "Enter province: " + RESET + HIGHLIGHT);
                    info.setProvince(scan.nextLine());
                }
                break;
            case "11":
                if (role.equals("Seller")) {
                    System.out.print(PROMPT + "Enter sellerID: " + RESET);
                    info.setSellerID(scan.nextLine());
                }
                break;
            default:
                showError("Please enter a valid choice!");
        }
        System.out.print(RESET);
    }

    private Person validateAndCreateUser(String role, UserInfo info) {
        if (role.equals("Manager") || role.equals("Support")) {
            if (isExistingPerson(info.getUsername())) {
                showError("Username already in use!");
                return null;
            }
        } else {
            if (isExistingPerson(info.getPhone(), info.getEmail())) {
                showError("Phone Number or Email already in use!");
                return null;
            }
        }

        List<String> errors = new ArrayList<>();
        InfoValidator validator = new InfoValidator();

        if (!(role.equals("Manager") || role.equals("Support"))) {
            if (!validator.isPersonInfoValidP1(info.getName(), info.getSurname(), info.getPhone(), errors)
                    || !validator.isPersonInfoValidP2(info.getEmail(), info.getUsername(), info.getPassword(), errors)) {
                displayErrors(errors);
                return null;
            }
        }

        return switch (role) {
            case "Customer" -> new Customer(info.getName(), info.getSurname(), info.getPhone(), info.getEmail(), info.getPassword());
            case "Seller" -> new Seller(info.getName(), info.getSurname(), info.getPhone(), info.getEmail(), info.getPassword(), info.getShopName(), info.getSellerID(), info.getProvince(), generateAgencyCode());
            case "Manager" -> new Manager(info.getName(), info.getSurname(), info.getUsername(), info.getPassword(), getNewPriorityCode());
            case "Support" -> new Support(info.getName(), info.getSurname(), info.getUsername(), info.getPassword());
            default -> null;
        };
    }

    private void completeAccountCreation(Person newUser) {
        showSuccessMessage(newUser);
        DataBase.getInstance().addPerson(newUser);

        if (newUser instanceof Seller) {
            String description = "New seller sign up";
            System.out.println("Your agency code is : " + ((Seller) newUser).getAgencyCode());
            DataBase.getInstance().addRequest(new SellerRequest(((Seller) newUser).getAgencyCode(), description, Instant.now()));
            System.out.println(SUCCESS + "Your seller request has been submitted for Support" + RESET);
            Pause.pause(5000);
        }
    }

    public boolean isExistingPerson(String username) {
        for (Person p : DataBase.getInstance().getPersonList()) {
            if (p instanceof OrdinaryUsers) {
                continue;
            }
            SpecialUsers su = (SpecialUsers) p;
            if (su.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean isExistingPerson(String phoneNumber,String email) {
        for (Person p : DataBase.getInstance().getPersonList()) {
            if (p instanceof SpecialUsers) {
                continue;
            }
            OrdinaryUsers ou = (OrdinaryUsers) p;
            if (ou.getPhoneNumber().equals(phoneNumber) && ou.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public void displayErrors(List<String> errors) {
        System.out.println(ERROR + "\nAccount creation failed due to the following errors:" + RESET);
        errors.forEach(error -> System.out.println(ERROR + " - " + error + RESET));
        System.out.println();
    }

    public void showError(String message) {
        System.out.println(ERROR + "\n⚠ " + message + RESET);
        Pause.pause(1000);
    }

    public void showSuccessMessage(Person user) {
        System.out.println(SUCCESS + "\n╔═══════════════════════════════════════╗");
        System.out.println("║                                       ║");
        System.out.println("║" + BOLD + "      ACCOUNT CREATED SUCCESSFULLY     " + RESET + SUCCESS + "║");
        System.out.println("  Role:  " +  user.getRole());
        System.out.println("║                                       ║");
        System.out.println("╚═══════════════════════════════════════╝" + RESET);
        Pause.pause(2000);
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

    public int getNewPriorityCode() {
        int priorityNumber = DataBase.getInstance().getPriorityNumber();
        DataBase.getInstance().setPriorityNumber(priorityNumber + 1);
        return priorityNumber + 1;
    }
}