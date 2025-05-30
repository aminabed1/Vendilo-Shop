package ir.ac.kntu;

import java.time.Instant;
import java.util.*;

public class CustomerMainPage {
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
    private static final String BOLD = "\u001B[1m";

    public static CustomerMainPage getInstance() {
        return new CustomerMainPage();
    }

    public void mainPage(Person person) {
        backButtonPressed = false;
        while (!backButtonPressed) {
            clearScreen();
            System.out.printf(PROMPT + "Welcome, %s! (Customer)\n\n" + RESET, person.getName());
            customerMenu((Customer)person);
        }
    }

    public void customerMenu(Customer customer) {
        displayMenuHeader("CUSTOMER DASHBOARD");
        displayCustomerDashboard(customer);
        customerHandleChoice(customer);
    }

    public void displayMenuHeader(String title) {
        System.out.println(TITLE + "=========================================");
        System.out.println("|                                       |");
        System.out.printf("|%s%-39s%s|\n", BOLD + HIGHLIGHT,
                "   " + title, RESET + TITLE);
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);
        System.out.println();
    }

    public void displayCustomerDashboard(Customer customer) {
        System.out.println(MENU + "╔══════════════════════╦════════════════════╦════════════════════╦════════════════════╗");
        System.out.println("║      ACCOUNT         ║     CATEGORIES     ║       SEARCH       ║      SUPPORT       ║");
        System.out.println("╠══════════════════════╬════════════════════╬════════════════════╬════════════════════╣");
        System.out.println("║ 1. Account Info      ║ 6. Browse          ║ 7. Search          ║ 8. Support         ║");
        System.out.println("║ 2. Cart              ║    Categories      ║                    ║                    ║");
        System.out.println("║ 3. Orders            ║                    ║                    ║                    ║");
        System.out.println("║ 4. My Wallet         ║                    ║                    ║                    ║");
        System.out.println("║ 5. Logout            ║                    ║                    ║                    ║");
        System.out.println("╚══════════════════════╩════════════════════╩════════════════════╩════════════════════╝" + RESET);
        System.out.println();
    }

    public void customerHandleChoice(Customer customer) {
        System.out.print(PROMPT + "Enter Your Choice (1-8) or type 'BACK': " + RESET + HIGHLIGHT);

        while (true) {
            String choice = scan.nextLine().trim();

            if (choice.equalsIgnoreCase("BACK")) {
                backButtonPressed = true;
                break;
            }

            if (!choice.matches("[1-8]")) {
                System.out.println(ERROR + "Please enter a valid choice (1-8) or BACK" + RESET);
                continue;
            }

            switch (choice) {
                case "1":
                    PersonAccount personInfo = new PersonAccount();
                    personInfo.infoView(customer);
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
                    browseCategories(customer);
                    break;
                case "7":
                    Search.getInstance().handleSearch();
                    break;
                case "8":
                    displaySupportOptions(customer);
                    break;
            }
            break;
        }
    }

    public void displaySupportOptions(Customer customer) {
        clearScreen();
        System.out.println(TITLE + "╔═══════════ SUPPORT OPTIONS ════════════╗");
        System.out.println(        "║                                        ║");
        System.out.println("║ " + OPTION + "1. Requests       " + TITLE + "                     ║");
        System.out.println("║ " + OPTION + "2. Add a new request" + TITLE + "                   ║");
        System.out.println("║ " + OPTION + "3. Back to Main Menu" + TITLE + "                   ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝" + RESET);
        System.out.println();

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
           System.out.println("1. Product quality report");
           System.out.println("2. Inconsistency in the sent order");
           System.out.println("3. Receive order problems");
           System.out.println("4. Back ");
           System.out.println("Select an option ");
           String choice = scan.nextLine().trim();
           if (!choice.matches("[1-4]}")) {
               System.out.println(ERROR + "Please enter a valid choice (1-4) or BACK" + RESET);
               continue;
           }

           if (choice.equals("4")) {
               return;
           }

           String title = switch (choice) {
               case "1" -> "Product quality report";
               case "2" -> "Inconsistency in the sent order";
               case "3" -> "Receive order problems";
               default -> null;
           };

           System.out.println("Enter product serial number");

           String serialNumber = scan.nextLine().trim();

           if (!findProductBySerialNumber(serialNumber)) {
               System.out.println(ERROR + "Invalid serial number!" + RESET);
               continue;
           }

           Request request = new CustomerRequest(title, "unchecked", Instant.now(),
                   "empty", serialNumber, customer.getPhoneNumber());
           DataBase.addRequest(request);
           System.out.println(ERROR + "Customer Request successfully added!" + RESET);
       }
    }

    public boolean findProductBySerialNumber(String serialNumber) {
        List<Product> productList = DataBase.getProductList();
        for (Product product : productList) {
            if (product.getSerialNumber().equals(serialNumber)) {
                return true;
            }
        }
        return false;
    }

    public void displayRequest(Customer customer) {
        clearScreen();
        List<Request> allRequests = DataBase.getRequestList();
        List<CustomerRequest> customerRequests = new ArrayList<>();

        for (Request request : allRequests) {
            if (request instanceof CustomerRequest cr) {
                if (cr.getCustomerPhone().equals(customer.getPhoneNumber())) {
                    customerRequests.add(cr);
                }
            }
        }

        if (customerRequests.isEmpty()) {
            System.out.println(ERROR + "You don't have any requests yet." + RESET);
            System.out.println(PROMPT + "\nPress Enter to return..." + RESET);
            scan.nextLine();
            return;
        }

        System.out.println(TITLE + "════════════ YOUR REQUESTS ════════════" + RESET);
        for (int i = 0; i < customerRequests.size(); i++) {
            CustomerRequest request = customerRequests.get(i);
            String statusColor = request.getStatus().equals("unchecked") ? ERROR : SUCCESS;
            System.out.printf(OPTION + "%2d. " + RESET + "%-20s " + statusColor + "%-10s" + RESET +
                            " %-15s %s\n",
                    i + 1,
                    request.getRequestTitle(),
                    request.getStatus(),
                    request.getTimestamp().toString(),
                    request.getSerialNumber());
        }

        System.out.println("\n" + PROMPT + "Enter request number to view details or 'BACK' to return: " + RESET);
        while (true) {
            String input = scan.nextLine().trim();
            if (input.equalsIgnoreCase("BACK")) {
                return;
            }

            try {
                int selectedIndex = Integer.parseInt(input) - 1;
                if (selectedIndex >= 0 && selectedIndex < customerRequests.size()) {
                    displaySingleRequestDetails(customerRequests.get(selectedIndex));
                    break;
                } else {
                    System.out.println(ERROR + "Invalid number! Please enter a number between 1 and " +
                            customerRequests.size() + " or 'BACK'" + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(ERROR + "Please enter a valid number or 'BACK'" + RESET);
            }
        }
    }

    private void displaySingleRequestDetails(CustomerRequest request) {
        clearScreen();
        System.out.println(TITLE + "════════════ REQUEST DETAILS ════════════" + RESET);
        System.out.println();

        System.out.println(OPTION + "Title: " + RESET + request.getRequestTitle());
        System.out.println(OPTION + "Status: " + RESET +
                (request.getStatus().equals("unchecked") ? ERROR : SUCCESS) +
                request.getStatus() + RESET);
        System.out.println(OPTION + "Date: " + RESET + request.getTimestamp().toString());
        System.out.println(OPTION + "Serial Number: " + RESET + request.getSerialNumber());
        System.out.println(OPTION + "Your Phone: " + RESET + request.getCustomerPhone());
        System.out.println(OPTION + "Description: " + RESET + "\n" + request.getDescription());

        System.out.println("\n" + PROMPT + "Press Enter to return..." + RESET);
        scan.nextLine();
    }

    public void browseCategories(Customer customer) {
        clearScreen();
        System.out.println(TITLE + "╔══════════════ CATEGORIES ══════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║ " + OPTION + "1. Digital Products" + TITLE + "                    ║");
        System.out.println("║ " + OPTION + "2. Books" + TITLE + "                               ║");
        System.out.println("║ " + OPTION + "3. Back to Main Menu" + TITLE + "                   ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝" + RESET);
        System.out.println();

        while (true) {
            System.out.print(PROMPT + "Choose a category (1-3): " + RESET + HIGHLIGHT);
            String choice = scan.nextLine().trim();
            System.out.print(RESET);

            switch (choice) {
                case "1":
                    displayDigitalProducts(customer);
                    return;
                case "2":
                    displayBooks(customer);
                    return;
                case "3":
                    return;
                default:
                    System.out.println(ERROR + "Invalid choice! Please try again." + RESET);
            }
        }
    }

    public void displayDigitalProducts(Customer customer) {
        System.out.println("\n" + OPTION + "Displaying Digital Products..." + RESET + "\n");
    }

    public void displayBooks(Customer customer) {
        System.out.println("\n" + OPTION + "Displaying Books..." + RESET + "\n");
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}