package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class SellerMainPage implements Serializable {

    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String BOLD = "\u001B[1m";
    private static final String WARNING = "\u001B[38;5;208m";

    private final Scanner scan = new Scanner(System.in);
    private static List<Request> currentSellerRequests = new ArrayList<>();

    public static SellerMainPage getInstance() {
        return new SellerMainPage();
    }

    public List<Request> sellerRequests(String agencyCode) {
        currentSellerRequests.clear();
        List<Request> requests = DataBase.getRequestList();
        for (Request request : requests) {
            if (!(request instanceof SellerRequest)) {
                continue;
            }

            if (((SellerRequest) request).getSellerAgencyCode().equals(agencyCode)) {
                currentSellerRequests.add(request);
            }
        }
        return currentSellerRequests;
    }

    public void displayMenuHeader(String title) {
        String formattedTitle = String.format("   %-39s", title);
        System.out.println(TITLE + "=========================================");
        System.out.println("|                                       |");
        System.out.printf(" %s%s%s \n", BOLD + HIGHLIGHT, formattedTitle, RESET + TITLE);
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);
        System.out.println();
    }

    public void inactiveAccountDisplay(Seller seller) {
        while (true) {
            displayMenuHeader("ACCOUNT INACTIVE");
            System.out.println(ERROR + "Your account is not active or has been disabled." + RESET);
            System.out.println(MENU + "1. Send new request to support");
            System.out.println("2. Display requests");
            System.out.println("3. Log out" + RESET);
            System.out.print(PROMPT + "Enter an option: " + RESET);

            String option = scan.nextLine().trim();

            switch (option) {
                case "1" -> sendNewRequest(seller);
                case "2" -> displayRequest(seller);
                case "3" -> { return; }
                default -> System.out.println(ERROR + "Invalid option. Please try again." + RESET);
            }
        }
    }

    public void mainPage(Person person) {
        currentSellerRequests = sellerRequests(((Seller) person).getAgencyCode());

        Seller seller = (Seller) person;

        if (!seller.getIsValidSeller()) {
            inactiveAccountDisplay(seller);
            return;
        }

        while (true) {
            displayMenuHeader("SELLER DASHBOARD");
            System.out.println(MENU +
                    "+---------------------------------------+\n" +
                    "| 1. Products                           |\n" +
                    "| 2. My Wallet                          |\n" +
                    "| 3. Orders                             |\n" +
                    "| 4. Exit                               |\n" +
                    "+---------------------------------------+");
            System.out.print(PROMPT + "Your choice: " + RESET);

            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> myProductsMenu(seller.getAgencyCode());
                case "2" -> sellerWallet(seller);
                case "3" -> displaySellerOrders(seller);
                case "4" -> {
                    System.out.println(SUCCESS + "Exiting seller panel..." + RESET);
                    pause(1500);
                    return;
                }
                default -> System.out.println(ERROR + "Please enter a valid choice (1-4)." + RESET);
            }
        }
    }

    private void displaySellerOrders(Seller seller) {
        List<Order> sellerOrders = seller.getOrders();


        if (sellerOrders.isEmpty()) {
            System.out.println(WARNING + "You don't have any orders yet." + RESET);
            pause(1500);
            return;
        }

        while (true) {
            clearScreen();
            displayMenuHeader("MY ORDERS");

            System.out.println(MENU + String.format("%-5s %-20s %-15s %-12s",
                    "No.", "Product", "Date", "Amount") + RESET);
            System.out.println(MENU + "----------------------------------------------------" + RESET);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.systemDefault());

            for (int i = 0; i < sellerOrders.size(); i++) {
                Order order = sellerOrders.get(i);
                Map<Product, Integer> productMap = order.getProductMap();
                List<Product> productList = new ArrayList<>(productMap.keySet());

                Product lastProduct = productList.getLast();
                String productName = lastProduct.getFullName();

                String date = formatter.format(order.getOrderDate());
                String amount = String.format("%.2f $", order.getProductPrice() * 0.9);

                System.out.println(OPTION + String.format("%-5d %-20s %-15s %-12s",
                        i+1, productName, date, SUCCESS + amount + RESET) + RESET);
            }


            System.out.print(PROMPT + "\nSelect an order (or 0 to back): " + RESET);
            String input = scan.nextLine().trim();

            if (input.equals("0")) {
                return;
            }

            try {
                int selected = Integer.parseInt(input);
                if (selected < 1 || selected > sellerOrders.size()) {
                    showError("Invalid selection!");
                    continue;
                }

                displayOrderDetails(sellerOrders.get(selected - 1));
            } catch (NumberFormatException e) {
                showError("Please enter a valid number");
            }
        }
    }

    private void displayOrderDetails(Order order) {
        clearScreen();
        displayMenuHeader("ORDER DETAILS");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault());

        System.out.println(OPTION + "Date: " + HIGHLIGHT + formatter.format(order.getOrderDate()) + RESET);
        System.out.println(OPTION + "Customer Email: " + HIGHLIGHT + order.getCustomerEmail() + RESET);
        System.out.println(OPTION + "Delivery Address: " + HIGHLIGHT +
                order.getDeliveryAddress().toString() + RESET);

        System.out.println(MENU + "\n════════════════ PRODUCTS ════════════════" + RESET);
        for (Product product : order.getProductMap().keySet()) {
            System.out.println(OPTION + "- " + product.getFullName() + " (" + product.getCategory() + ")");
            System.out.println("  Price: " + HIGHLIGHT + product.getPrice() + " $" + RESET);
        }

        System.out.println(MENU + "\n════════════════ SUMMARY ════════════════" + RESET);
        System.out.println(OPTION + "Products Total: " + HIGHLIGHT + order.getProductPrice() + " $" + RESET);
        System.out.println(OPTION + "Your Share (90%): " + SUCCESS +
                String.format("%.2f $", order.getProductPrice() * 0.9) + RESET);

        System.out.print(PROMPT + "\nPress ENTER to continue..." + RESET);
        scan.nextLine();
    }

    public void myProductsMenu(String agencyCode) {
        Seller seller = findSellerByAgencyCode(agencyCode);

        while (true) {
            displayMenuHeader("PRODUCT MANAGEMENT");
            System.out.println(MENU + "1. My products");
            System.out.println("2. Add a new product");
            System.out.println("3. Back" + RESET);
            System.out.print(PROMPT + "Your choice: " + RESET);

            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> manageMyProducts(seller);
                case "2" -> addNewProduct(seller);
                case "3" -> { return; }
                default -> System.out.println(ERROR + "Please enter a valid choice (1-3)" + RESET);
            }
        }
    }

    public void manageMyProducts(Seller seller) {
        List<Product> myProducts = getSellerProducts(seller.getAgencyCode());

        if (myProducts.isEmpty()) {
            System.out.println(WARNING + "You don't have any products yet." + RESET);
            pause(2000);
            return;
        }

        while (true) {
            displayMenuHeader("MY PRODUCTS");
            displayProductList(myProducts);

            System.out.print(PROMPT + "Select a product by index or press 0 to back: " + RESET);
            String choice = scan.nextLine().trim();

            if (choice.equals("0")) {
                return;
            }

            if (!isValidChoice(choice, 1, myProducts.size())) {
                System.out.println(ERROR + "Invalid product index." + RESET);
                continue;
            }

            Product selectedProduct = myProducts.get(Integer.parseInt(choice) - 1);
            productDetailsMenu(selectedProduct);
        }
    }

    private void productDetailsMenu(Product product) {
        while (true) {
            displayMenuHeader("PRODUCT DETAILS: " + product.getFullName());
            System.out.println(MENU + "1. Display product details");
            System.out.println("2. Change product stock");
            System.out.println("3. Back" + RESET);
            System.out.print(PROMPT + "Your choice: " + RESET);

            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> DisplayProduct.getInstance().display(product, null);
                case "2" -> changeProductStock(product);
                case "3" -> { return; }
                default -> System.out.println(ERROR + "Invalid choice. Please try again." + RESET);
            }
        }
    }

    private void displayProductList(List<Product> products) {
        System.out.println(MENU + String.format("%-5s %-20s %-15s %-10s %-10s",
                "No.", "Name", "Category", "Price", "Stock") + RESET);
        System.out.println(MENU + "----------------------------------------------------------" + RESET);

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.println(OPTION + String.format("%-5d %-20s %-15s %-10s %-10d",
                    i+1,
                    p.getFullName(),
                    p.getCategory(),
                    p.getPrice(),
                    p.getStock()) + RESET);
        }
        System.out.println();
    }

    public void changeProductStock(Product product) {
        while (true) {
            System.out.print(PROMPT + "Enter new stock (or 'back' to cancel): " + RESET);
            String input = scan.nextLine().trim();

            if (input.equalsIgnoreCase("back")) {
                return;
            }

            if (!input.matches("\\d+")) {
                System.out.println(ERROR + "Please enter a positive number." + RESET);
                continue;
            }

            int newStock = Integer.parseInt(input);
            if (newStock < 0) {
                System.out.println(ERROR + "Stock cannot be negative." + RESET);
                continue;
            }

            product.setStock(newStock);
            System.out.println(SUCCESS + "Stock updated successfully to: " + newStock + RESET);
            pause(2000);
            return;
        }
    }

    public void sendNewRequest(Seller seller) {
        if (!currentSellerRequests.isEmpty() && !currentSellerRequests.getLast().getIsChecked()) {
            System.out.println(WARNING + "Your last request has not been checked yet. Please wait." + RESET);
            pause(2000);
            return;
        }

        System.out.print(PROMPT + "Enter request description: " + RESET);
        String description = scan.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println(ERROR + "Description cannot be empty." + RESET);
            return;
        }

        DataBase.addRequest(new SellerRequest(seller.getAgencyCode(), description, Instant.now()));
        System.out.println(SUCCESS + "Request submitted successfully!" + RESET);
        pause(2000);
    }

    public void displayRequest(Seller seller) {
        currentSellerRequests = new ArrayList<>();
        for (Request req : DataBase.getRequestList()) {
            if (req instanceof SellerRequest) {
                SellerRequest sReq = (SellerRequest) req;
                if (sReq.getSellerAgencyCode().equals(seller.getAgencyCode())) {
                    currentSellerRequests.add(req);
                }
            }
        }

        if (currentSellerRequests.isEmpty()) {
            System.out.println(WARNING + "You don't have any requests yet." + RESET);
            pause(2000);
            return;
        }

        while (true) {
            displayMenuHeader("MY REQUESTS");
            listRequests(currentSellerRequests);
            System.out.print(PROMPT + "Select a request by index or 0 to go back: " + RESET);

            String input = scan.nextLine().trim();
            if (input.equals("0")) {
                return;
            }

            if (!isValidChoice(input, 1, currentSellerRequests.size())) {
                System.out.println(ERROR + "Invalid request index." + RESET);
                continue;
            }

            Request request = currentSellerRequests.get(Integer.parseInt(input) - 1);
            printRequest(request);
        }
    }

    private void listRequests(List<Request> requests) {
        System.out.println(MENU + String.format("%-5s %-10s", "No.", "Status") + RESET);
        System.out.println(MENU + "----------------------" + RESET);

        for (int i = 0; i < requests.size(); i++) {
            Request req = requests.get(i);
            String status = req.getIsChecked() ? SUCCESS + "Checked" : ERROR + "Unchecked";
            System.out.println(OPTION + String.format("%-5d %s%s", i+1, status, RESET));
        }
        System.out.println();
    }

    public void printRequest(Request request) {
        displayMenuHeader("REQUEST DETAILS");
        System.out.println(MENU + "Status: " +
                (request.getIsChecked() ? SUCCESS + "Checked" : ERROR + "Unchecked") + RESET);
        System.out.println(OPTION + "\nDescription:\n" + request.getDescription() + RESET);
        System.out.print(PROMPT + "\nPress any key to continue..." + RESET);
        scan.nextLine();
    }

    public Seller findSellerByAgencyCode(String agencyCode) {
        for (Person p : DataBase.getPersonList()) {
            if (p instanceof Seller s) {
                if (s.getAgencyCode().equals(agencyCode)) {
                    return s;
                }
            }
        }
        return null;
    }

    public List<Product> getSellerProducts(String agencyCode) {
        List<Product> result = new ArrayList<>();
        for (Product p : DataBase.getProductList()) {
            if (p.getSellerAgencyCode().equals(agencyCode)) {
                result.add(p);
            }
        }
        return result;
    }

    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isValidChoice(String choice, int min, int max) {
        if (!choice.matches("\\d+")) {
            return false;
        }
        int num = Integer.parseInt(choice);
        return num >= min && num <= max;
    }

    private void addNewProduct(Seller seller) {
        AddProduct.getInstance().addNewProduct(seller);
    }

    private void sellerWallet(Seller seller) {
        seller.getWallet().walletOptionHandler(seller);

        double balance = seller.getWallet().getWalletBalance();
        double totalSales = calculateTotalSales(seller);

        System.out.println(TITLE + "\n═══════════════════════════════════════" + RESET);
        System.out.println(OPTION + "  Current Balance: " + HIGHLIGHT +
                String.format("%.2f $", balance) + RESET);
        System.out.println(OPTION + "  Total Sales: " + HIGHLIGHT +
                String.format("%.2f $", totalSales) + RESET);
        System.out.println(TITLE + "═══════════════════════════════════════" + RESET);
    }

    private double calculateTotalSales(Seller seller) {
        double total = 0.0;
        for (Transaction t : seller.getWallet().getTransactionList()) {
            if (t.getAmount() > 0) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void showError(String message) {
        System.out.println(ERROR + "\n " + message + RESET);
        pause(1500);
    }
}