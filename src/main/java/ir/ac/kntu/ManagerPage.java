package ir.ac.kntu;

import ir.ac.kntu.util.Calendar;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManagerPage {
    private static final Scanner scan = new Scanner(System.in);
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED_BOLD = "\u001B[31;1m";
    private static final String GREEN = "\u001B[32m";

    public static ManagerPage getInstance() {
        return new ManagerPage();
    }

    public void mainPage(Person person) {
        while (true) {
            printMenuOptions();
            String choice = getUserChoice("Enter an option: ", "[0-5]");
            if (choice == null) {
                continue;
            }
            switch (choice) {
                case "0" -> {
                    return;
                }
                case "1" -> manageUsers(person);
                case "2" -> sellersOrCustomersActivities(Role.Seller);
                case "3" -> sellersOrCustomersActivities(Role.Customer);
                case "4" -> DiscountCodeManage.getInstance().generateDiscountCode();
                case "5" -> generalMessage();
                default -> {}
            }
        }
    }

    private void printMenuOptions() {
        System.out.println("\n--- Manager Menu ---");
        System.out.println("1. Manage Users");
        System.out.println("2. Display Sellers Activities");
        System.out.println("3. Display Customers Activities");
        System.out.println("4. Generate General Discount Code");
        System.out.println("5. Publish General Message");
        System.out.println("0. Back");
    }

    private String getUserChoice(String prompt, String regexPattern) {
        System.out.print(prompt);
        String choice = scan.nextLine().trim();
        if (!choice.matches(regexPattern)) {
            System.out.println(RED_BOLD + "Invalid choice, try again..." + RESET);
            return null;
        }
        return choice;
    }

    private void manageUsers(Person person) {
        while (true) {
            System.out.println("\n--- Manage Users ---");
            System.out.println("1. Print All Users List");
            System.out.println("2. Create New Special User");
            System.out.println("3. Back");
            System.out.print("Make Your Choice: ");
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> handleAllUsers(person);
                case "2" -> CreateAccountPage.getInstance().createAccount("Special");
                case "3" -> {
                    return;
                }
                default -> System.out.println(RED_BOLD + "Invalid choice, try again..." + RESET);
            }
        }
    }

    private void handleAllUsers(Person manager) {
        while (true) {
            List<Person> persons = filterUsers(DataBase.getInstance().getPersonList());
            if (persons == null || persons.isEmpty()) {
                System.out.println(RED_BOLD + "No users match the filters." + RESET);
                return;
            }
            printPersonsList(persons);
            String choice = getUserChoice("Select a Person By Index Or Press 0 To Back: ", "\\d+");
            if (choice == null || "0".equals(choice)) {
                return;
            }

            int index = Integer.parseInt(choice);
            if (index < 1 || index > persons.size()) {
                System.out.println(RED_BOLD + "Invalid index, try again..." + RESET);
                continue;
            }
            displayAndManagePerson(persons.get(index - 1), (Manager) manager);
        }
    }

    private void printPersonsList(List<Person> persons) {
        System.out.println();
        int counter = 1;
        for (Person person : persons) {
            System.out.printf("%s%d.%s %s👤 %-20s%s | %s🏷️ %-20s%s | %s🎭 %-20s%s\n",
                    CYAN, counter++, RESET,
                    CYAN, person.getName(), RESET,
                    YELLOW, person.getSurname(), RESET,
                    CYAN, person.getRole(), RESET);
        }
    }

    private List<Person> filterUsers(List<Person> persons) {
        String username = "";
        String role = "";
        while (true) {
            System.out.println("\nFilter by:");
            System.out.println("1. Username: " + (username.isEmpty() ? "Empty Filter" : username));
            System.out.println("2. Role: " + (role.isEmpty() ? "Empty Filter" : role));
            System.out.println("3. Load Users");
            System.out.println("4. Back");
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter Username: ");
                    username = scan.nextLine().trim();
                }
                case "2" -> {
                    System.out.println("Select A Role:");
                    System.out.println("1. Customer\n2. Seller\n3. Support\n4. Manager");
                    String roleChoice = scan.nextLine().trim();
                    role = switch (roleChoice) {
                        case "1" -> "Customer";
                        case "2" -> "Seller";
                        case "3" -> "Support";
                        case "4" -> "Manager";
                        default -> "";
                    };
                }
                case "3" -> {
                    return applyFilter(persons, username, role);
                }
                case "4" -> {
                    return null;
                }
                default -> System.out.println(RED_BOLD + "Invalid choice, try again..." + RESET);
            }
        }
    }

    private List<Person> applyFilter(List<Person> persons, String username, String role) {
        List<Person> filtered = new ArrayList<>();
        for (Person person : persons) {
            boolean userMatch = username.isEmpty() || (person instanceof SpecialUsers specialUsers && specialUsers.getUsername().equalsIgnoreCase(username));
            boolean roleMatch = role.isEmpty() || person.getRole().name().equalsIgnoreCase(role);
            if (userMatch && roleMatch) {
                filtered.add(person);
            }
        }
        return filtered;
    }

    private void changeActivationStatus(Person person) {
        while (true) {
            System.out.printf("\n\nStatus : %s", person.getIsActive() ?  GREEN + "ACTIVE" + RESET :  RED_BOLD + "INACTIVE" + RESET);
            System.out.printf("\n1. %s Account\n", person.getIsActive() ? "Inactive" : "Active");
            System.out.println("2. Back");
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    if (person.getIsActive()) {
                        person.inactivePerson();
                    } else {
                        person.activePerson();
                    }
                    SystemMessage.printMessage(String.format("Account %s successfully", person.getIsActive() ? "activated" : "inactivated"), MessageTitle.Success);
                }
                case "2" -> {
                    return;
                }
                default -> System.out.println(RED_BOLD + "Invalid choice, try again..." + RESET);
            }
        }
    }

    private void displayAndManagePerson(Person person, Manager manager) {
        while (true) {
            printPersonInfoMenu(person);
            String choice = scan.nextLine();
            if (handlePersonChoice(choice, person, manager)) {
                return;
            }
        }
    }

    private void printPersonInfoMenu(Person person) {
        System.out.println("\n" + person + "\n");
        System.out.println(RED_BOLD + "----Options----" + RESET);
        System.out.println("1. Edit Information");
        System.out.println("2. Active Or Deactivate Account");
        if (person instanceof Support) {
            System.out.println("3. Manage Support Tasks");
        }
        System.out.println("0. Back");
    }

    private boolean handlePersonChoice(String choice, Person person, Manager manager) {
        switch (choice) {
            case "1" -> EditPersonInfo.getInstance().handleEdit(person);
            case "2" -> {
                if (person instanceof Manager otherManager && manager.getPriorityCode() > otherManager.getPriorityCode()) {
                    SystemMessage.printMessage("You Can Not InActive This Manager", MessageTitle.Error);
                    Pause.pause(1500);
                } else {
                    changeActivationStatus(person);
                }
            }
            case "3" -> {
                if (person instanceof Support) {
                    manageSupportTasks(person);
                } else {
                    System.out.println(RED_BOLD + "Invalid choice, try again..." + RESET);
                }
            }
            case "0", "back", "Back" -> {
                return true;
            }
            default -> System.out.println(RED_BOLD + "Invalid choice, try again..." + RESET);
        }
        return false;
    }

    private void manageSupportTasks(Person person) {
        Support support = (Support) person;
        while (true) {
            clearConsole();
            printSupportMenu(support);
            String input = scan.nextLine().trim();
            if (handleSupportChoice(input, support)) {
                return;
            }
        }
    }

    private void printSupportMenu(Support support) {
        System.out.println("========== Support Tasks ==========");
        int index = 1;
        for (RequestTitle requestTitle : RequestTitle.values()) {
            String checkbox = support.getRequestTitles().contains(requestTitle) ? "X" : " ";
            String displayName = formatTitle(requestTitle);
            System.out.printf("%d. [%s] %s\n", index++, checkbox, displayName);
        }
        System.out.println("5. Confirm and Back");
        System.out.print("\nSelect (1-4): ");
    }

    private boolean handleSupportChoice(String input, Support support) {
        switch (input) {
            case "1", "2", "3", "4" -> {
                int choice = Integer.parseInt(input) - 1;
                RequestTitle title = RequestTitle.values()[choice];
                if (support.getRequestTitles().contains(title)) {
                    support.removeRequestTitle(title);
                } else {
                    support.addRequestTitle(title);
                }
                return false;
            }
            case "5" -> {
                SystemMessage.printMessage("Changes saved.", MessageTitle.Success);
                waitEnter();
                return true;
            }
            default -> {
                SystemMessage.printMessage("Invalid choice. Try again...", MessageTitle.Error);
                waitEnter();
                return false;
            }
        }
    }

    private String formatTitle(RequestTitle title) {
        return switch (title) {
            case Sellers_Authentication_Requests -> "Sellers Authentication Requests";
            case Products_quality -> "Products Quality";
            case Inconsistency_In_The_Sent_Order -> "Inconsistency In The Sent Order";
            case Receive_Order_Problems -> "Receive Order Problems";
        };
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void waitEnter() {
        System.out.print("Press Enter to continue...");
        scan.nextLine();
    }

    private void sellersOrCustomersActivities(Role role) {
        while (true) {
            System.out.printf("\n1. Display %s Activities In A List\n", role == Role.Customer ? "Customers" : "Sellers");
            System.out.printf("2. Search A %s\n", role == Role.Customer ? "Customer By Phone Number" : "Sellers By Agency Code");
            System.out.println("0. Back");
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    displaySellersOrCustomersList(role == Role.Seller ? Role.Seller : Role.Customer);
                }
                case "2" -> {
                    displayOneSellerOrCustomerActivities(role);
                }
                case "0" -> {
                    return;
                }
                default -> SystemMessage.printMessage("Invalid choice. Try again...", MessageTitle.Error);
            }
        }
    }

    private void displaySellersOrCustomersList(Role role) {
        List<Person> personList = DataBase.getInstance().getPersonList();
        int counter = 1;
        String title = (role == Role.Customer ? "Customer" : "Seller");
        String valueLabel = (role == Role.Customer ? "Total Buy" : "Benefit");
        System.out.printf(CYAN + "\n=== %s List ===\n" + RESET, title);
        for (Person person : personList) {
            if (role == Role.Seller && person instanceof Seller seller) {
                double totalBenefit = calculateSellerOrCustomerFee(seller);
                System.out.printf("%s%2d. %s%-20s %-20s%s | %s%s:%s %s%.2f%s%n", YELLOW, counter++, CYAN, seller.getName(), seller.getSurname(), RESET, RED_BOLD, valueLabel, RESET, GREEN, totalBenefit, RESET
                );
            } else if (role == Role.Customer && person instanceof Customer customer) {
                double totalBuy = calculateSellerOrCustomerFee(customer);
                System.out.printf("%s%2d. %s%-20s %-20s%s | %s%s:%s %s%.2f%s%n", YELLOW, counter++, CYAN, customer.getName(), customer.getSurname(), RESET, RED_BOLD, valueLabel, RESET, GREEN, totalBuy, RESET
                );
            }
        }
        System.out.println(CYAN + "====================" + RESET);
        waitEnter();
    }

    private double calculateSellerOrCustomerFee(Person person) {
        Instant oneMonthAgo = Calendar.now().minus(Duration.ofDays(30));
        double total = 0.0;
        if (person instanceof Seller seller) {
            String agencyCode = seller.getAgencyCode();
            for (Order order : seller.getOrders()) {
                if (order instanceof SellerOrder sellerOrder &&
                        sellerOrder.getSellerAgencyCode().equals(agencyCode) &&
                        sellerOrder.getOrderDate().isAfter(oneMonthAgo)) {

                    total += sellerOrder.getSellerBenefit();
                }
            }
        } else if (person instanceof Customer customer) {
            for (Order order : customer.getOrder()) {
                if (order.getOrderDate().isAfter(oneMonthAgo)) {
                    total += ((CustomerOrder) order).getTotalPrice();
                }
            }
        }
        return total;
    }

    private void displayOneSellerOrCustomerActivities(Role role) {
        String roleName = (role == Role.Customer ? "Customer" : "Seller");
        System.out.printf(YELLOW + "Enter %s : " + RESET, "Customer".equals(roleName) ? "Customer's Phone Number" : "Seller's Agency Code");
        String authField = scan.nextLine().trim();
        Person person = (Person) findSellerOrCustomer(authField, role);
        if (person == null) {
            String errorMessage = String.format("%s Not Found", "Customer".equals(roleName) ? "Customer" : "Seller");
            SystemMessage.printMessage(errorMessage, MessageTitle.Error);
        } else if (person instanceof Customer customer) {
            manageCustomer(customer);
        } else if (person instanceof Seller seller) {
            manageSeller(seller);
        }
    }

    public void manageSeller(Seller seller) {
        while (true) {
            printSellerHeader(seller);
            System.out.print(YELLOW + "Choice: " + RESET);
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> handleBonusInput(seller);
                case "0" -> {
                    return;
                }
                default -> SystemMessage.printMessage("Invalid choice.", MessageTitle.Error);
            }
        }
    }

    private void printSellerHeader(Seller seller) {
        double totalBenefit = calculateSellerOrCustomerFee(seller);
        System.out.println(CYAN + "\n=== Seller Activities ===" + RESET);
        System.out.printf("%sSeller:%s %s %s (%s)%n", YELLOW, RESET,
                seller.getName(), seller.getSurname(), seller.getAgencyCode());
        System.out.printf("%sTotal Benefit:%s %.2f%n%n", GREEN, RESET, totalBenefit);
        System.out.println("1. " + CYAN + "Give Bonus" + RESET);
        System.out.println("0. " + RED_BOLD + "Back" + RESET);
    }

    private void handleBonusInput(Seller seller) {
        System.out.print("Bonus Amount: ");
        String amt = scan.nextLine().trim();
        if (!amt.matches("\\d+")) {
            SystemMessage.printMessage("Invalid amount.", MessageTitle.Error);
            return;
        }
        double amount = Double.parseDouble(amt);
        if (amount <= 0) {
            SystemMessage.printMessage("Amount must be positive.", MessageTitle.Error);
            return;
        }
        seller.getWallet().setWalletBalance(amount, "Bonus Given");
        SystemMessage.printMessage("Bonus Added.", MessageTitle.Success);
    }

    private void manageCustomer(Customer customer) {
        while (true) {
            double totalBuy = calculateSellerOrCustomerFee(customer);
            System.out.println(CYAN + "\n=== Customer Activities ===" + RESET);
            System.out.printf("%sCustomer:%s %s %s (%s)%n", YELLOW, RESET, customer.getName(), customer.getSurname(), customer.getPhoneNumber());
            System.out.printf("%sTotal Buy:%s %.2f%n%n", GREEN, RESET, totalBuy);
            System.out.println("1. " + CYAN + "Give Discount Code To Customer" + RESET);
            System.out.println("2. " + CYAN + "Active Vendilo Plus Account" + RESET);
            System.out.println("0. " + RED_BOLD + "Back" + RESET);
            System.out.print(YELLOW + "Your choice: " + RESET);
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    DiscountCodeManage.getInstance().setMainCustomer(customer);
                    DiscountCodeManage.getInstance().objectReceiver(customer);
                }
                case "2" -> {
                    VendiloPlusAccountManage.getInstance().vendiloPlusTab(customer, true);
                }
                case "0" -> {
                    return;
                }
                default -> {
                    SystemMessage.printMessage("Invalid choice. Try again...", MessageTitle.Error);
                }
            }
        }
    }

    private Object findSellerOrCustomer(String authCode, Role role) {
        for (Person person : DataBase.getInstance().getPersonList()) {
            switch (role) {
                case Customer:
                    if (!(person instanceof Customer customer)) {
                        continue;
                    }
                    if (customer.getPhoneNumber().equals(authCode)) {
                        return customer;
                    }
                case Seller:
                    if (!(person instanceof Seller seller)) {
                        continue;
                    }
                    if (seller.getAgencyCode().equals(authCode)) {
                        return seller;
                    }
                default:
            }
        }
        return null;
    }

    public void generalMessage() {
        while (true) {
            System.out.println("1. Publish General Message");
            System.out.println("2. Back");
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter General Message: ");
                    String message = scan.nextLine().trim();
                    Notification generalMessage = new Notification(message);
                    DataBase.getInstance().addNotification(generalMessage);
                    SystemMessage.printMessage("General Message Published Successfully", MessageTitle.Success);
                }
                case "2" -> {
                    return;
                }
                default -> {
                    SystemMessage.printMessage("Invalid choice. Try again...", MessageTitle.Error);
                }
            }
        }
    }
}