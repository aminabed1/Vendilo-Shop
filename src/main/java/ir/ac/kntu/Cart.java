package ir.ac.kntu;

import java.io.Serializable;
import java.util.*;

public class Cart implements Serializable {
    private HashMap<Product, Integer> productMap;
    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String BOLD = "\u001B[1m";

    public Cart() {
        productMap = new HashMap<>();
    }

    public HashMap<Product, Integer> getProductMap() {
        return productMap;
    }

    public void addProductToMap(Product product) {
        productMap.merge(product, 1, Integer::sum);
    }

    public void displayCart(Customer person) {
        if (productMap.isEmpty()) {
            displayEmptyCart();
            return;
        }

        Scanner input = new Scanner(System.in);
        while (true) {
            clearScreen();
            displayCartContents(person);

            String choice = getCustomerChoice(input);
            if (handleCartNavigation(choice, person)) {
                return;
            }

            handleProductSelection(choice, input, person);
        }
    }

    private void displayEmptyCart() {
        System.out.println(TITLE + "\n╔═════════════════════════════════════╗");
        System.out.println("║                                     ║");
        System.out.println("║" + OPTION + "       YOUR CART IS EMPTY            " + TITLE + "║");
        System.out.println("║                                     ║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
        pause(1500);
    }

    private void displayCartContents(Customer person) {
        double totalPrice = calculateTotalPrice();
        int[] columnWidths = calculateColumnWidths();
        int boxWidth = Math.max(50, columnWidths[0] + columnWidths[1] + 15);

        printCartHeader(boxWidth);
        printProductList(boxWidth, columnWidths);
        printCartFooter(boxWidth, totalPrice);
    }

    private int[] calculateColumnWidths() {
        int maxNameLength = productMap.keySet().stream()
                .mapToInt(p -> p.getFullName().length())
                .max().orElse(20);

        int maxPriceLength = productMap.keySet().stream()
                .mapToInt(p -> String.format("%.2f", Double.parseDouble(p.getPrice())).length())
                .max().orElse(6);

        return new int[]{maxNameLength, maxPriceLength};
    }

    private double calculateTotalPrice() {
        return productMap.entrySet().stream()
                .mapToDouble(e -> Double.parseDouble(e.getKey().getPrice()) * e.getValue())
                .sum();
    }

    private void printCartHeader(int boxWidth) {
        System.out.println(TITLE + "╔" + "═".repeat(boxWidth) + "╗");
        System.out.println("║" + BOLD + HIGHLIGHT + centerText("YOUR CART", boxWidth) + RESET + TITLE + "║");
        System.out.println("╠" + "═".repeat(boxWidth) + "╣" + RESET);
    }

    private void printProductList(int boxWidth, int[] columnWidths) {
        List<Product> productList = new ArrayList<>(productMap.keySet());
        for (int i = 0; i < productList.size(); i++) {
            printProductItem(i, productList.get(i), boxWidth, columnWidths);
        }
    }

    private void printProductItem(int index, Product product, int boxWidth, int[] columnWidths) {
        int quantity = productMap.get(product);
        String formattedPrice = String.format("%.2f $", Double.parseDouble(product.getPrice()));

        System.out.println(OPTION + "  " + (index + 1) + ". " + MENU + "╔" + "═".repeat(boxWidth) + "╗");
        System.out.println(OPTION + "     " + BOLD + "     Name: " + RESET + HIGHLIGHT +
                String.format("%-" + columnWidths[0] + "s", product.getFullName()));
        System.out.println(OPTION + "     " + BOLD + "     Price: " + RESET + HIGHLIGHT +
                String.format("%" + columnWidths[1] + "s ", formattedPrice));
        System.out.println(OPTION + "     " + BOLD + "     Quantity: " + RESET + HIGHLIGHT + quantity);
        System.out.println(OPTION + "     " + BOLD + "     Category: " + RESET + HIGHLIGHT +
                product.getCategory());
        System.out.println(MENU + "     ╚" + "═".repeat(boxWidth) + "╝\n" + RESET);
    }

    private void printCartFooter(int boxWidth, double totalPrice) {
        String totalLine = "TOTAL:  " + String.format("%.2f $      ", totalPrice);
        System.out.println(TITLE + "╔" + "═".repeat(boxWidth) + "╗");
        System.out.println("║" + PROMPT + String.format("%-" + (boxWidth - 1) + "s", totalLine) + TITLE + " ║");
        System.out.println("╠" + "═".repeat(boxWidth) + "╣");
        System.out.println("║" + OPTION + "  Select product by number  " +
                " ".repeat(boxWidth - 28) + TITLE + "║");
        System.out.println("║" + OPTION + "  Type " + BOLD + "DONE" + RESET + OPTION + " to checkout     " +
                " ".repeat(boxWidth - 28) + TITLE + "║");
        System.out.println("║" + OPTION + "  Type " + BOLD + "BACK" + RESET + OPTION + " to return     " +
                " ".repeat(boxWidth - 26) + TITLE + "║");
        System.out.println("╚" + "═".repeat(boxWidth) + "╝" + RESET);
    }

    private String getCustomerChoice(Scanner input) {
        System.out.print(PROMPT + "\n  Your choice: " + RESET + HIGHLIGHT);
        String choice = input.nextLine().trim();
        System.out.print(RESET);
        return choice;
    }

    private boolean handleCartNavigation(String choice, Customer person) {
        if (choice.equalsIgnoreCase("BACK")) {
            return true;
        }

        if (choice.equalsIgnoreCase("DONE")) {
            HandleOrder.getInstance().handleOrder(person);
            pause(2000);
            return true;
        }
        return false;
    }

    private void handleProductSelection(String choice, Scanner input, Customer person) {
        if (!choice.matches("\\d+")) {
            showError("Please enter a valid number");
            return;
        }

        int index = Integer.parseInt(choice) - 1;
        List<Product> productList = new ArrayList<>(productMap.keySet());

        if (index < 0 || index >= productList.size()) {
            showError("Invalid product number");
            return;
        }

        processSelectedProduct(input, productList.get(index));
    }

    private void processSelectedProduct(Scanner input, Product product) {
        System.out.println("\nProduct selected: " + product.getFullName());
        System.out.println("1. Display product details");
        System.out.println("2. Remove product from cart");
        System.out.println("3. Deselect product");
        System.out.print("Enter action: ");
        String action = input.nextLine().trim();

        switch (action) {
            case "1":
                DisplayProduct.getInstance().display(product, null);
                break;
            case "2":
                productMap.remove(product);
                System.out.println("Product removed from cart.");
                pause(1500);
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid action");
        }
    }

    public String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }

    public void showError(String message) {
        System.out.println(ERROR + "\n⚠ " + message + RESET);
        pause(1000);
    }

    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}