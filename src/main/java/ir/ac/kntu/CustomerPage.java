package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public class CustomerPage implements Serializable {
    private static final Scanner scan = new Scanner(System.in);
    private static boolean backButtonPressed = false;
    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final CustomerPage customerPage = new CustomerPage();

    public static CustomerPage getInstance() {
        return customerPage;
    }

    public void mainPage(Person person) {
        backButtonPressed = false;
        while (!backButtonPressed) {
            System.out.printf(PROMPT + "Welcome, %s! (Customer)\n\n" + RESET, person.getName());
            customerMenu((Customer)person);
        }
    }

    public void customerMenu(Customer customer) {
        pupUpNotificationTab(customer);
        customer.getVendiloPlus().checkPremiumAccountActive();
        displayMenuHeader();
        displayCustomerDashboard();
        customerHandleChoice(customer);
    }

    public void pupUpNotificationTab(Customer customer) {
        List<Notification> newNotifications = NotificationManage.getInstance().getNotificationsList(customer);
        if (newNotifications == null || newNotifications.isEmpty()) {
            return;
        }

        String formatted = String.format("          %d New Notification%s          ", newNotifications.size(), newNotifications.size() == 1 ? "" : "s");
        System.out.println(TITLE + "=========================================");
        System.out.println("|                                       |");
        System.out.println(formatted);
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);
        System.out.println();
        Pause.pause(2500);
    }

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

            if ("back".equalsIgnoreCase(choice)) {
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
            case "1" -> showAccountInfo(customer);
            case "2" -> showCart(customer);
            case "3" -> showOrders(customer);
            case "4" -> handleWallet(customer);
            case "5" -> backButtonPressed = true;
            case "6" -> showProductList(customer);
            case "7" -> handleSearch(customer);
            case "8" -> showSupportOptions(customer);
            case "9" -> handleDiscountCodes(customer);
            case "10" -> showNotifications(customer);
            case "11" -> handleVendiloPlus(customer);
            default -> {
            }
        }
    }

    private void showAccountInfo(Customer customer) {
        new PersonAccount().infoView(customer);
    }

    private void showCart(Customer customer) {
        customer.displayCart();
    }

    private void showOrders(Customer customer) {
        DisplayOrder.getInstance().display(customer);
    }

    private void handleWallet(Customer customer) {
        customer.getWallet().walletOptionHandler(customer);
    }

    private void showProductList(Customer customer) {
        displayProductsList(customer);
    }

    private void handleSearch(Customer customer) {
        Search.getInstance().handleSearch(customer);
    }

    private void showSupportOptions(Customer customer) {
        displaySupportOptions(customer);
    }

    private void handleDiscountCodes(Customer customer) {
        DiscountCodeManage.getInstance().handleDiscountCodePanel(customer);
    }

    private void showNotifications(Customer customer) {
        NotificationManage.getInstance().notificationTab(customer);
    }

    private void handleVendiloPlus(Customer customer) {
        VendiloPlusAccountManage.getInstance().vendiloPlusTab(customer, false);
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
                    AutoAnswerToRequests.getInstance().autoAnswer(customer);
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
            if ("4".equals(choice)) {
                return;
            } else if (!choice.matches("[1-4]")) {
                System.out.println(ERROR + "Please enter a valid choice (1-4)" + RESET);
                continue;
            }
            RequestTitle title = getRequestTitle(choice);
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

    private RequestTitle getRequestTitle(String choice) {
        return switch (choice) {
            case "1" -> RequestTitle.Products_quality;
            case "2" -> RequestTitle.Inconsistency_In_The_Sent_Order;
            case "3" -> RequestTitle.Receive_Order_Problems;
            default -> null;
        };
    }

    private void processRequestCreation(Customer customer, RequestTitle title) {
        System.out.println("Enter product serial number:");
        String serialNumber = scan.nextLine().trim();

        if (!findProductBySerialNumber(serialNumber)) {
            System.out.println(ERROR + "Invalid serial number!" + RESET);
            return;
        }

        Request request = new CustomerRequest(title, Instant.now(),
                "empty", serialNumber, customer.getPhoneNumber());
        DataBase.getInstance().addRequest(request);
        System.out.println(SUCCESS + "Customer Request successfully added!" + RESET);
    }

    public boolean findProductBySerialNumber(String serialNumber) {
        return DataBase.getInstance().getProductList().stream()
                .anyMatch(product -> product.getSerialNumber().equals(serialNumber));
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
        for (Request request : DataBase.getInstance().getRequestList()) {
            if (request instanceof CustomerRequest customerRequest && customerRequest.getCustomerPhone().equals(customer.getPhoneNumber())) {
                customerRequests.add(customerRequest);
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
            String statusColor = (request.getIsChecked() ? SUCCESS : ERROR);
            System.out.printf(OPTION + "%2d. " + RESET + "%-20s " + statusColor + "%-10s" + RESET +
                            " %-15s %s\n",
                    i + 1,
                    request.getRequestTitle(),
                    request.getIsChecked() ? "Checked" : "Unchecked",
                    request.getTimestamp().toString(),
                    request.getSerialNumber());
        }
    }

    private void handleRequestSelection(List<CustomerRequest> requests) {
        System.out.println("\n" + PROMPT + "Enter request number to view details or 'BACK' to return: " + RESET);
        while (true) {
            String input = scan.nextLine().trim();
            if ("back".equalsIgnoreCase(input)) {
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
        for (Product product : DataBase.getInstance().getProductList()) {
            switch (category) {
                case "Book" :
                    if (product instanceof Book) {
                        products.add(product);
                    }
                    break;
                case "Phone" :
                    if (product instanceof Phone) {
                        products.add(product);
                    }
                    break;
                case "Laptop" :
                    if (product instanceof LopTop) {
                        products.add(product);
                    }
                    break;
                default :
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
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.printf("║ %d. %s | Price: %s | Type: %s%n",
                    i + 1,
                    product.getFullName(),
                    product.getPrice(),
                    product.getCategory()
            );
        }
        System.out.println("║ 0. Back to category selection              ║");
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