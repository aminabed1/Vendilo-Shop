package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

public class CustomerPage implements Serializable {
    private static final Scanner scan = new Scanner(System.in);
    private static boolean backButtonPressed = false;
    private static boolean continueState = false;
    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final CustomerPage customerMainPage = new CustomerPage();

    public static CustomerPage getInstance() {
        return customerMainPage;
    }

    public void mainPage(Person person) {
        backButtonPressed = false;
        while (!backButtonPressed) {
            System.out.printf(PROMPT + "Welcome, %s! (Customer)\n\n" + RESET, person.getName());
            customerMenu((Customer)person);
        }
    }

    public void customerMenu(Customer customer) {
        customer.getVendiloPlusAccount().checkPremiumAccountActive();
        displayMenuHeader();
        displayCustomerDashboard();
        customerHandleChoice(customer);
    }
    //TODO pup up notification addition
    public void displayMenuHeader() {
        System.out.println(TITLE + "=========================================");
        System.out.println("|                                       |");
        System.out.println("|          CUSTOMER DASHBOARD           |");
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);
        System.out.println();
    }

    public void displayCustomerDashboard() {
        System.out.println(MENU + "╔══════════════════════╦════════════════════╦════════════════════╦════════════════════╗");
        System.out.println("║      ACCOUNT         ║     CATEGORIES     ║       SEARCH       ║      SUPPORT       ║");
        System.out.println("╠══════════════════════╬════════════════════╬════════════════════╬════════════════════╣");
        System.out.println("║  1. Account Info     ║  6. Browse         ║  7. Search         ║  8. Support        ║");
        System.out.println("║  2. Cart             ║    Categories      ║                    ║  9. Discount Codes ║");
        System.out.println("║  3. Orders           ║                    ║                    ║ 10. Notifications  ║");
        System.out.println("║  4. My Wallet        ║                    ║                    ║ 11. Vendilo Plus   ║");
        System.out.println("║  5. Logout           ║                    ║                    ║                    ║");
        System.out.println("╚══════════════════════╩════════════════════╩════════════════════╩════════════════════╝" + RESET);
        System.out.println();
    }

    public void customerHandleChoice(Customer customer) {
        System.out.print(PROMPT + "Enter Your Choice (1-10) or type 'BACK': " + RESET + HIGHLIGHT);

        while (true) {
            String choice = scan.nextLine().trim();

            if (choice.equalsIgnoreCase("BACK")) {
                backButtonPressed = true;
                break;
            }

            if (!choice.matches("[1-9]|(10)|(11)")) {
                System.out.println(ERROR + "Please enter a valid choice (1-10) or BACK" + RESET);
                continue;
            }

            handleCustomerChoice(choice, customer);
            break;
        }
    }

    private void handleCustomerChoice(String choice, Customer customer) {
        switch (choice) {
            case "1":
                new PersonAccount().infoView(customer);
                break;
            case "2":
                customer.displayCart();
                break;
            case "3":
                DisplayOrder.getInstance().display(customer);
                break;
            case "4":
                customer.getWallet().walletOptionHandler(customer);
                break;
            case "5":
                backButtonPressed = true;
                break;
            case "6":
                displayProductsList(customer);
                break;
            case "7":
                Search.getInstance().handleSearch(customer);
                break;
            case "8":
                displaySupportOptions(customer);
                break;
            case "9":
                handleDiscountPanel(customer);
                break;
            case "10":
                notificationTab(customer);
                break;
            case "11":
                VendiloPlusTab(customer);
                break;
            default:
        }
    }

    public void VendiloPlusTab(Customer customer) {
        while (true) { 
            System.out.println("1. Premium Account Details");
            System.out.println("2. Buy Premium");
            System.out.println("3. Back");
            System.out.println("Your Choice : ");
            String choice = scan.nextLine();
            try {
                Integer.parseInt(choice);
            } catch (NumberFormatException exeption) {
                System.out.println("Please Enter Numeric Value");
                continue;
            }
            switch(choice) {
                case "1":
                    System.out.println(customer.getVendiloPlusAccount());
                    System.out.println("Press Any Key To Back");
                    scan.next();
                    break;
                case "2":
                    getPlusAccount(customer);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid Choice. Try Again");
                    Pause.pause(1500);          
            }
        } 
    }

    public void getPlusAccount(Customer customer) {
        while (true) {
            System.out.println("Choose One Subscription :");
            System.out.println("1.  1-Month");
            System.out.println("2.  3-Month");
            System.out.println("3. 12-Month");
            System.out.println("4. Back");
            String choice = scan.nextLine();

            try {
                Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("Enter A Numeric Value");
                continue;
            }

            double balance = customer.getWallet().getWalletBalance();
            double subscriptionCost = 0;
            Instant startDateTemp = Instant.now();
            Instant endDateTemp = null;

            switch (choice) {
                case "1":
                    endDateTemp = ZonedDateTime.now().plusMonths(1).toInstant();
                    subscriptionCost = 10;
                    break;
                case "2":
                    endDateTemp = ZonedDateTime.now().plusMonths(3).toInstant();
                    subscriptionCost = 25;
                    break;
                case "3":
                    endDateTemp = ZonedDateTime.now().plusMonths(12).toInstant();
                    subscriptionCost = 100;
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Enter A Valid Choice");
                    Pause.pause(2000);
            }

            if (subscriptionCost > balance) {
                System.out.println("Insufficient Balance, Returning...");
                Pause.pause(2000);
                return;
            }

            double newBalance = customer.getWallet().getWalletBalance() - subscriptionCost;
            customer.getWallet().setWalletBalance(newBalance, "Buy Premium Account");
            customer.getVendiloPlusAccount().setPremiumAccountDateActive(startDateTemp, endDateTemp);
            System.out.println("Premium Account Updated Successfully. Now You Can See It's Details.");
            Pause.pause(2000);
        }
    }



    public void notificationTab(Customer customer) {
        continueState = true;
        while (continueState) { 
            notificationTabHeader();
            notifTabMenu(customer);
        }
    }

    public void notificationTabHeader() {
        System.out.println(TITLE + "=========================================");
        System.out.println("|                                       |");
        System.out.println("|             NOTIFICATIONS             |");
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);    
        System.out.println();
    }

    public void notifTabMenu(Customer customer) {
        int unseenNotifs = 0;
        List<Notification> notificationListTemp = new ArrayList<>();
        for (Notification notification : customer.getNotifications()) {
            //TODO
            if (notification.getChargedProduct().getStock() > 0 && !notification.getIsVisible()) {
                notification.setIsVisible(true);
            }
            if (!notification.getIsVisible()) {
                continue;
            }
            notificationListTemp.add(notification);
            if (!notification.isNotificationSeen()) {
                unseenNotifs++;
            }
        }
        if (unseenNotifs == 0) {
            System.out.println("You have not any unseen nofication");
        } else {
            System.out.printf(" %d new notification%s", unseenNotifs, (unseenNotifs == 1 ? "" : "s"));
        }
        System.out.println("1. Display Notification List");
        System.out.println("0. Back");

        notificationTabChoiceHandle(notificationListTemp);
    }

    public void notificationTabChoiceHandle(List<Notification> notifications) {
        System.out.println("Enter Your Choice :");
        String choice = scan.nextLine();
        if (!choice.matches("\\d+")) {
            System.out.println("Please Enter Numeric Choice.");
            return;
        } 
        int choiceValue = Integer.parseInt(choice);
        if (choiceValue != 0 && choiceValue != 1) {
            System.out.println("Please Enter Valid Choice, (0\1).");
        } 

        if (choiceValue == 0) {
            continueState = false;
            return;
        } 

        if (notifications.isEmpty()) {
            System.out.println("You Don't Have Any Notification Yet");
            return;
        }

        displayNotifacationList(notifications);
        System.out.println("Select A Notification (By Index) Or Back (Enter 0)");
        choice = scan.nextLine();
        if (!choice.matches("\\d")) {
            System.out.println("Invalid Entered Value, Returning...");
            Pause.pause(2000);
            return;
        }

        choiceValue = Integer.parseInt(choice);
        if (choiceValue == 0) {
            return;
        }

        if (choiceValue <= 0 && choiceValue > notifications.size()) {
            System.out.println("Invalid Index, Returning...");
            Pause.pause(2000);
            return;
        }

        System.out.println(notifications.get(choiceValue - 1));
        changeNotifStatus(notifications.get(choiceValue - 1));
        System.out.println("Press Any Key To Continue...");
        scan.next();
    }

    public void changeNotifStatus(Notification notification) {
        notification.setNotificationSeen(true);
    }

    public void displayNotifacationList(List<Notification> notifications) {
        int counter = 1;
        for (Notification nofication : notifications) {
            System.out.println(counter + ". Title : " + nofication.getTopic().name() + "Status : " + (nofication.isNotificationSeen() ? "seen" : "unseen"));
        }   
    }

    public void handleDiscountPanel(Customer customer) {
        List<DiscountCode> discountCodeList = customer.getDiscountCodeList();
        if (discountCodeList.isEmpty()) {
            System.out.println(ERROR + "No discount codes found\n");
            return;
        }
        int counter = 1;
        for (DiscountCode discountCode : discountCodeList) {
            String codeCategory = discountCode.getClass().getSimpleName();
            System.out.println(counter + ". " + "Code : " + discountCode.getCode() + " Category : " +codeCategory);
        }
        handleDiscountMenuSelection(discountCodeList);
    }

    public void handleDiscountMenuSelection(List<DiscountCode> discountCodeList) {
        System.out.println();
        System.out.println("Select discount code by index");
        Scanner scan = new Scanner(System.in);
        String choice = scan.nextLine().trim();
        if (!choice.matches("\\d+")) {
            System.out.println(ERROR + "Please enter a valid index");
            return;
        }
        int selectedIndex = Integer.parseInt(choice);
        if (selectedIndex <= 0 || selectedIndex > discountCodeList.size()) {
            System.out.println(ERROR + "Please enter a valid index");
            return;
        }
        System.out.println(discountCodeList.get(selectedIndex - 1));
        System.out.println("press enter to back");
        scan.nextLine();
    }

    public void displaySupportOptions(Customer customer) {
        printSupportMenu();
        handleSupportChoice(customer);
    }

    private void printSupportMenu() {
        System.out.println(TITLE + "╔═══════════ SUPPORT OPTIONS ════════════╗");
        System.out.println(        "║                                        ║");
        System.out.println("║ " + OPTION + "1. Requests       " + TITLE + "                     ║");
        System.out.println("║ " + OPTION + "2. Add a new request" + TITLE + "                   ║");
        System.out.println("║ " + OPTION + "3. Back to Main Menu" + TITLE + "                   ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private void handleSupportChoice(Customer customer) {
        while (true) {
            System.out.print(PROMPT + "Choose an option (1-3): " + RESET + HIGHLIGHT);
            String choice = scan.nextLine().trim();
            System.out.print(RESET);

            switch (choice) {
                case "1":
                    displayRequest(customer);
                    return;
                case "2":
                    addNewRequest(customer);
                    return;
                case "3":
                    return;
                default:
                    System.out.println(ERROR + "Invalid choice! Please try again." + RESET);
            }
        }
    }

    public void addNewRequest(Customer customer) {
        while (true) {
            displayRequestTypes();
            String choice = scan.nextLine().trim();
            if (choice.equals("4")) {
                return;
            } else if (!choice.matches("[1-4]")) {
                System.out.println(ERROR + "Please enter a valid choice (1-4)" + RESET);
                continue;
            }
            String title = getRequestTitle(choice);
            if (title == null) {
                continue;
            }
            processRequestCreation(customer, title);
        }
    }

    private void displayRequestTypes() {
        System.out.println("1. Product quality report");
        System.out.println("2. Inconsistency in the sent order");
        System.out.println("3. Receive order problems");
        System.out.println("4. Back");
        System.out.println("Select an option:");
    }

    private String getRequestTitle(String choice) {
        return switch (choice) {
            case "1" -> "Product quality report";
            case "2" -> "Inconsistency in the sent order";
            case "3" -> "Receive order problems";
            default -> null;
        };
    }

    private void processRequestCreation(Customer customer, String title) {
        System.out.println("Enter product serial number:");
        String serialNumber = scan.nextLine().trim();

        if (!findProductBySerialNumber(serialNumber)) {
            System.out.println(ERROR + "Invalid serial number!" + RESET);
            return;
        }

        Request request = new CustomerRequest(title, "unchecked", Instant.now(),
                "empty", serialNumber, customer.getPhoneNumber());
        DataBase.addRequest(request);
        System.out.println(SUCCESS + "Customer Request successfully added!" + RESET);
    }

    public boolean findProductBySerialNumber(String serialNumber) {
        return DataBase.getProductList().stream()
                .anyMatch(p -> p.getSerialNumber().equals(serialNumber));
    }

    public void displayRequest(Customer customer) {
        List<CustomerRequest> customerRequests = getCustomerRequests(customer);

        if (customerRequests.isEmpty()) {
            showNoRequestsMessage();
            return;
        }

        displayRequestsList(customerRequests);
        handleRequestSelection(customerRequests);
    }

    private List<CustomerRequest> getCustomerRequests(Customer customer) {
        List<CustomerRequest> customerRequests = new ArrayList<>();
        for (Request request : DataBase.getRequestList()) {
            if (request instanceof CustomerRequest cr &&
                    cr.getCustomerPhone().equals(customer.getPhoneNumber())) {
                customerRequests.add(cr);
            }
        }
        return customerRequests;
    }

    private void showNoRequestsMessage() {
        System.out.println(ERROR + "You don't have any requests yet." + RESET);
        System.out.println(PROMPT + "\nPress Enter to return..." + RESET);
        scan.nextLine();
    }

    private void displayRequestsList(List<CustomerRequest> requests) {
        System.out.println(TITLE + "════════════ YOUR REQUESTS ════════════" + RESET);
        for (int i = 0; i < requests.size(); i++) {
            CustomerRequest request = requests.get(i);
            String statusColor = request.getStatus().equals("unchecked") ? ERROR : SUCCESS;
            System.out.printf(OPTION + "%2d. " + RESET + "%-20s " + statusColor + "%-10s" + RESET +
                            " %-15s %s\n",
                    i + 1,
                    request.getRequestTitle(),
                    request.getStatus(),
                    request.getTimestamp().toString(),
                    request.getSerialNumber());
        }
    }

    private void handleRequestSelection(List<CustomerRequest> requests) {
        System.out.println("\n" + PROMPT + "Enter request number to view details or 'BACK' to return: " + RESET);
        while (true) {
            String input = scan.nextLine().trim();
            if (input.equalsIgnoreCase("BACK")) {
                return;
            }

            if (!input.matches("\\d+")) {
                System.out.println(ERROR + "Please enter a valid number or 'BACK'" + RESET);
                continue;
            }

            int selectedIndex = Integer.parseInt(input) - 1;
            if (selectedIndex >= 0 && selectedIndex < requests.size()) {
                System.out.println(requests.get(selectedIndex));
                System.out.println("\n" + PROMPT + "Press Enter to return..." + RESET);
                scan.nextLine();
                break;
            } else {
                System.out.println(ERROR + "Invalid number! Please enter a number between 1 and " +
                        requests.size() + " or 'BACK'" + RESET);
            }
        }
    }

    public void displayProductsList(Person person) {
        while (true) {
            String selectedCategory = selectProductCategory();
            if (selectedCategory == null) {
                return;
            }

            List<Product> products = filterProductsByCategory(selectedCategory);
            if (products.isEmpty()) {
                System.out.println("No products found in this category.");
                continue;
            }

            browseProducts(products, (Customer)person);
        }
    }

    private String selectProductCategory() {
        System.out.println("Select product category to display:");
        System.out.println("1. Books");
        System.out.println("2. Phones");
        System.out.println("3. Laptops");
        System.out.println("0. Back to previous menu");
        System.out.print("Enter choice (0-3): ");

        String categoryChoice = scan.nextLine().trim();

        return switch (categoryChoice) {
            case "1" -> "Book";
            case "2" -> "Phone";
            case "3" -> "Laptop";
            case "0" -> null;
            default -> {
                System.out.println("Invalid choice! Please try again.");
                yield "";
            }
        };
    }

    private List<Product> filterProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        for (Product p : DataBase.getProductList()) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                products.add(p);
            }
        }
        return products;
    }

    private void browseProducts(List<Product> products, Customer customer) {
        while (true) {
            printProductsList(products);

            System.out.print("Choose product index to see details or 0 to go back: ");
            String input = scan.nextLine().trim();

            if ("0".equals(input)) {
                break;
            }
            handleProductSelection(input, products, customer);
        }
    }

    private void printProductsList(List<Product> products) {
        System.out.println("╔══════════════ PRODUCTS LIST ═══════════════╗");
        System.out.println("║ 0. Back to category selection              ║");

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.printf("║ %d. %s | Price: %s | Type: %s%n",
                    i + 1,
                    p.getFullName(),
                    p.getPrice(),
                    p.getCategory()
            );
        }
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();
    }

    private void handleProductSelection(String input, List<Product> products, Customer customer) {
        if (input == null || !input.matches("\\d+")) {
            System.out.println("Please enter a valid number!");
            return;
        }

        int index = Integer.parseInt(input);
        if (index >= 1 && index <= products.size()) {
            Product selectedProduct = products.get(index - 1);
            DisplayProduct.getInstance().display(selectedProduct, customer);
        } else {
            System.out.println("Invalid index! Try again.");
        }
    }
}