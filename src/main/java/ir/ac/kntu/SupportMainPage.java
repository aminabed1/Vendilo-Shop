package ir.ac.kntu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SupportMainPage implements Serializable {

    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String BOLD = "\u001B[1m";
    private static final String DIVIDER = "──────────────────────────────────────────────";

    private static final Scanner scan = new Scanner(System.in);

    public static SupportMainPage getInstance() {
        return new SupportMainPage();
    }

    public void mainPage(Person person) {
        while (true) {
            clearScreen();
            displayHeader("SUPPORT MAIN PAGE");
            displayMainMenu();

            System.out.print(PROMPT + "\nEnter your choice: " + RESET);
            String choice = scan.nextLine().trim();

            switch (choice) {
                case "1" -> handleSellerAuthenticationRequests();
                case "2" -> handleCustomerRequests();
                case "3" -> handleOrderManagement();
                case "0" -> {
                    return;
                }
                default -> showInvalidOptionError(3);
            }
        }
    }

    private void displayMainMenu() {
        System.out.println(MENU + "\nMain Menu:" + RESET);
        System.out.println(OPTION + "1. Sellers Authentication Requests");
        System.out.println("2. Customer Requests");
        System.out.println("3. Orders Management");
        System.out.println("0. Exit" + RESET);
    }

    private void handleSellerAuthenticationRequests() {
        List<SellerRequest> sellersRequests = getPendingSellerRequests();

        while (true) {
            clearScreen();
            displayHeader("SELLER AUTHENTICATION REQUESTS");

            if (sellersRequests.isEmpty()) {
                showSuccessMessage("No pending seller requests found.");
                pause(1500);
                return;
            }

            displaySellerRequestsList(sellersRequests);

            int selectedIndex = getSelectedIndex(sellersRequests.size());
            if (selectedIndex == -1) {
                return;
            }

            SellerRequest selectedRequest = sellersRequests.get(selectedIndex);
            handleSellerRequestDetails(selectedRequest);
        }
    }

    private List<SellerRequest> getPendingSellerRequests() {
        List<SellerRequest> pendingRequests = new ArrayList<>();
        for (Request r : DataBase.getRequestList()) {
            if (r instanceof SellerRequest) {
                pendingRequests.add((SellerRequest) r);
            }
        }
        return pendingRequests;
    }

    private void displaySellerRequestsList(List<SellerRequest> requests) {
        System.out.println(MENU + "Pending Requests:" + RESET);
        System.out.println(DIVIDER);

        for (int i = 0; i < requests.size(); i++) {
            SellerRequest request = requests.get(i);
            Seller seller = findSellerByAgencyCode(request.getSellerAgencyCode());
            String status = (seller != null && seller.getIsValidSeller())
                    ? SUCCESS + "APPROVED" + RESET
                    : ERROR + "PENDING" + RESET;

            System.out.printf(HIGHLIGHT + "%2d" + RESET + " | %-20s | %-10s | %s\n",
                    i + 1,
                    request.getTimestamp(),
                    status,
                    truncate(request.getDescription()));
        }
        System.out.println(DIVIDER);
    }

    private void handleSellerRequestDetails(SellerRequest request) {
        Seller seller = findSellerByAgencyCode(request.getSellerAgencyCode());

        while (true) {
            clearScreen();
            displayHeader("SELLER REQUEST DETAILS");

            displaySellerInfo(seller);
            displayRequestDetails(request);

            if (!seller.getIsValidSeller()) {
                System.out.println(OPTION + "\n1. Approve seller account");
                System.out.println("2. Reject request and provide reason");
            } else {
                System.out.println(OPTION + "\n1. Disable seller account");
            }
            System.out.println("0. Back to list" + RESET);

            System.out.print(PROMPT + "\nEnter your choice: " + RESET);
            String choice = scan.nextLine().trim();

            switch (choice) {
                case "0" -> {
                    return;
                }
                case "1" -> {
                    toggleSellerStatus(seller);
                    return;
                }
                case "2" -> {
                    if (!seller.getIsValidSeller()) {
                        rejectSellerRequest(request);
                        return;
                    }
                }
                default -> showInvalidOptionError(2);
            }
        }
    }

    private void toggleSellerStatus(Seller seller) {
        seller.setIsValidSeller(!seller.getIsValidSeller());
        showSuccessMessage("Seller account " + (seller.getIsValidSeller() ? "approved" : "disabled") + " successfully!");
        pause(1500);
    }

    private void rejectSellerRequest(SellerRequest request) {
        System.out.print(PROMPT + "Enter rejection reason: " + RESET);
        String reason = scan.nextLine().trim();
        request.setDescription(reason);
        request.setIsChecked(true);
        showSuccessMessage("Request rejected with reason noted.");
        pause(1500);
    }

    private void handleCustomerRequests() {
        List<CustomerRequest> customerRequests = getPendingCustomerRequests();

        while (true) {
            clearScreen();
            displayHeader("CUSTOMER REQUESTS");

            if (customerRequests.isEmpty()) {
                showSuccessMessage("No pending customer requests found.");
                pause(1500);
                return;
            }

            displayCustomerRequestsList(customerRequests);

            int selectedIndex = getSelectedIndex(customerRequests.size());
            if (selectedIndex == -1) {
                return;
            }

            CustomerRequest selectedRequest = customerRequests.get(selectedIndex);
            handleCustomerRequestDetails(selectedRequest);

            customerRequests = getPendingCustomerRequests();
        }
    }

    private List<CustomerRequest> getPendingCustomerRequests() {
        List<CustomerRequest> pendingRequests = new ArrayList<>();
        for (Request r : DataBase.getRequestList()) {
            if (r instanceof CustomerRequest cr) {
                if ("unchecked".equals(cr.getStatus())) {
                    pendingRequests.add(cr);
                }
            }
        }
        return pendingRequests;
    }

    private void displayCustomerRequestsList(List<CustomerRequest> requests) {
        System.out.println(MENU + "Pending Customer Requests:" + RESET);
        System.out.println(DIVIDER);

        for (int i = 0; i < requests.size(); i++) {
            CustomerRequest request = requests.get(i);
            String status = request.getStatus().equals("unchecked")
                    ? ERROR + "PENDING" + RESET
                    : SUCCESS + "CHECKED" + RESET;

            System.out.printf(HIGHLIGHT + "%2d" + RESET + " | %-20s | %-10s | %-15s | %s\n",
                    i + 1,
                    request.getRequestTitle(),
                    status,
                    request.getCustomerPhone(),
                    truncate(request.getDescription()));
        }
        System.out.println(DIVIDER);
    }

    private void handleCustomerRequestDetails(CustomerRequest request) {
        while (true) {
            clearScreen();
            displayHeader("CUSTOMER REQUEST DETAILS");

            System.out.println(MENU + "Request Information:" + RESET);
            System.out.println(DIVIDER);
            System.out.printf("%-15s: %s\n", "Title", request.getRequestTitle());
            System.out.printf("%-15s: %s\n", "Status",
                    request.getStatus().equals("unchecked") ? ERROR + "PENDING" + RESET : SUCCESS + "CHECKED" + RESET);
            System.out.printf("%-15s: %s\n", "Customer Phone", request.getCustomerPhone());
            System.out.printf("%-15s: %s\n", "Serial Number", request.getSerialNumber());
            System.out.printf("%-15s: %s\n", "Date", request.getTimestamp());
            System.out.println(DIVIDER);
            System.out.println(MENU + "\nOriginal Description:" + RESET);
            System.out.println(request.getDescription());
            System.out.println(DIVIDER);

            System.out.println(OPTION + "\n1. Add response and mark as checked");
            System.out.println("0. Back to list" + RESET);

            System.out.print(PROMPT + "\nEnter your choice: " + RESET);
            String choice = scan.nextLine().trim();

            switch (choice) {
                case "0" -> {
                    return;
                }
                case "1" -> {
                    addResponseToRequest(request);
                    Notification notification = new Notification(request);
                    Customer customer = findCustomerByPhone(request.getCustomerPhone());
                    customer.addNotification(notification);
                    return;
                }
                default -> showInvalidOptionError(1);
            }
        }
    }

    public Customer findCustomerByPhone(String phone) {
        for (Person person : DataBase.getPersonList()) {
            if (person.getPhoneNumber().equals(phone)) {
                return (Customer) person;
            }
        }
        return null;
    }

    private void addResponseToRequest(CustomerRequest request) {
        System.out.print(PROMPT + "\nEnter your response to this request: " + RESET);
        String response = scan.nextLine().trim();

        String updatedDescription = request.getDescription() +
                "\n\n=== SUPPORT RESPONSE ===\n" + response;

        request.setDescription(updatedDescription);
        request.setStatus("checked");

        showSuccessMessage("Request updated successfully!");
        pause(1500);
    }

    private void handleOrderManagement() {
        clearScreen();
        displayHeader("ORDERS MANAGEMENT");
        displayOrdersList();
    }

    private void displayOrdersList() {
        List<Order> mainOrders = getMainOrders();

        while (true) {
            clearScreen();
            displayHeader("ORDERS LIST");

            if (mainOrders.isEmpty()) {
                showSuccessMessage("No orders found.");
                pause(1500);
                return;
            }

            for (int i = 0; i < mainOrders.size(); i++) {
                Order order = mainOrders.get(i);
                if (order instanceof CustomerOrder customerOrder) {
                    System.out.printf("%2d. Customer email: %-30s | Total Price: %-10.2f | Order Date: %s\n",
                            i + 1,
                            customerOrder.getCustomerEmail(),
                            customerOrder.getTotalPrice(),
                            customerOrder.getOrderDate());
                } else {
                    System.out.printf("%2d. [UNKNOWN ORDER TYPE]\n", i + 1);
                }
            }

            System.out.print(PROMPT + "\nEnter choice (0 to back): " + RESET);
            String choice = scan.nextLine().trim();

            if (!isValidChoice(choice, mainOrders.size())) {
                showErrorMessage("Please enter a valid choice (1-" + mainOrders.size() + ")");
                pause(1000);
                continue;
            }

            if (choice.equals("0")) {
                return;
            }

            Order selectedOrder = mainOrders.get(Integer.parseInt(choice) - 1);
            DisplayOrder.getInstance().displayOrderDetails(selectedOrder, null);

            System.out.println(PROMPT + "\nPress any key to continue..." + RESET);
            scan.nextLine();
        }
    }

    private List<Order> getMainOrders() {
        List<Order> mainOrders = new ArrayList<>();
        for (Order order : DataBase.getOrderList()) {
            if (order instanceof CustomerOrder) {
                mainOrders.add(order);
            }
        }
        return mainOrders;
    }

    private Seller findSellerByAgencyCode(String agencyCode) {
        for (Person p : DataBase.getPersonList()) {
            if (p instanceof Seller seller) {
                if (seller.getAgencyCode().equals(agencyCode)) {
                    return seller;
                }
            }
        }
        return null;
    }

    private int getSelectedIndex(int maxIndex) {
        while (true) {
            System.out.print(PROMPT + "\nSelect a request by index (0 to back): " + RESET);
            String choice = scan.nextLine().trim();

            if (choice.equals("0")) {
                return -1;
            }

            if (isValidChoice(choice, maxIndex)) {
                return Integer.parseInt(choice) - 1;
            }

            showErrorMessage("Please enter a valid index (1-" + maxIndex + ")");
            pause(1000);
        }
    }

    private boolean isValidChoice(String choice, int maxIndex) {
        return choice.matches("\\d+") &&
                Integer.parseInt(choice) > 0 &&
                Integer.parseInt(choice) <= maxIndex;
    }

    private void displaySellerInfo(Seller seller) {
        System.out.println(MENU + "Seller Information:" + RESET);
        System.out.println(DIVIDER);
        System.out.printf("%-15s: %s\n", "Name", seller.getName() + " " + seller.getSurname());
        System.out.printf("%-15s: %s\n", "Agency Code", seller.getAgencyCode());
        System.out.printf("%-15s: %s\n", "Province", seller.getProvince());
        System.out.printf("%-15s: %s\n", "Status",
                seller.getIsValidSeller() ? SUCCESS + "APPROVED" + RESET : ERROR + "PENDING" + RESET);
        System.out.println(DIVIDER);
    }

    private void displayRequestDetails(SellerRequest request) {
        System.out.println(MENU + "\nRequest Details:" + RESET);
        System.out.println(DIVIDER);
        System.out.printf("%-15s: %s\n", "Timestamp", request.getTimestamp());
        System.out.printf("%-15s: %s\n", "Description", request.getDescription());
        System.out.println(DIVIDER);
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void displayHeader(String title) {
        System.out.println(TITLE + BOLD + "\n" + title + RESET);
        System.out.println(DIVIDER);
    }

    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void showErrorMessage(String message) {
        System.out.println(ERROR + "[!] " + message + RESET);
    }

    private void showSuccessMessage(String message) {
        System.out.println(SUCCESS + "[✓] " + message + RESET);
    }


    private void showInvalidOptionError(int maxOption) {
        showErrorMessage("Please enter a valid option (0-" + maxOption + ")");
        pause(1000);
    }

    private String truncate(String text) {
        return text.length() <= 30 ? text : text.substring(0, 27) + "...";
    }
}