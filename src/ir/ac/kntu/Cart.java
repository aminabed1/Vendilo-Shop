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
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String BOLD = "\u001B[1m";

    public Cart() {
        productMap = new HashMap<>();
    }
    public HashMap<Product, Integer> getProductMap() {
        return productMap;
    }

    public void addProductToMap(Product product) {
        int quantity = 0;
        if (productMap.containsKey(product)) {
            quantity = productMap.get(product);
        }
        productMap.put(product, quantity + 1);
    }

//    public void addProduct(Product product) {
//        productList.add(product);
//    }
//
//    public void removeProduct(Product product) {
//        productList.remove(product);
//    }
//
//    public List<Product> getProductList() {
//        return productList;
//    }

    public void displayCart(Customer person) {
        Cart cart = person.getCart();
        HashMap<Product, Integer> productMap = cart.getProductMap();

        if (productMap.isEmpty()) {
            System.out.println(TITLE + "\n╔═════════════════════════════════════╗");
            System.out.println("║                                     ║");
            System.out.println("║" + OPTION + "       YOUR CART IS EMPTY            " + TITLE + "║");
            System.out.println("║                                     ║");
            System.out.println("╚═════════════════════════════════════╝" + RESET);
            pause(1500);
            return;
        }

        Scanner input = new Scanner(System.in);

        while (true) {
            clearScreen();
            double totalPrice = 0;

            int maxNameLength = 0;
            for (Product p : productMap.keySet()) {
                int length = p.getFullName().length();
                if (length > maxNameLength) {
                    maxNameLength = length;
                }
            }
            if (maxNameLength == 0) {
                maxNameLength = 20;
            }

            int maxPriceLength = 0;
            for (Product p : productMap.keySet()) {
                String formatted = String.format("%.2f", Double.parseDouble(p.getPrice()));
                int length = formatted.length();
                if (length > maxPriceLength) {
                    maxPriceLength = length;
                }
            }
            if (maxPriceLength == 0) {
                maxPriceLength = 6;
            }

            int boxWidth = Math.max(50, maxNameLength + maxPriceLength + 15);

            System.out.println(TITLE + "╔" + "═".repeat(boxWidth) + "╗");
            System.out.println("║" + BOLD + HIGHLIGHT + centerText("YOUR CART", boxWidth) + RESET + TITLE + "║");
            System.out.println("╠" + "═".repeat(boxWidth) + "╣" + RESET);

            List<Product> productList = new ArrayList<>(productMap.keySet());

            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                int quantity = productMap.get(product);
                String formattedPrice = String.format("%.2f $", Double.parseDouble(product.getPrice()));
                double totalProductPrice = Double.parseDouble(product.getPrice()) * quantity;

                System.out.println(OPTION + "  " + (i + 1) + ". " + MENU + "╔" + "═".repeat(boxWidth) + "╗");
                System.out.println(OPTION + "     " + BOLD + "     Name: " + RESET + HIGHLIGHT +
                        String.format("%-" + maxNameLength + "s", product.getFullName()));
                System.out.println(OPTION + "     " + BOLD + "     Price: " + RESET + HIGHLIGHT +
                        String.format("%" + maxPriceLength + "s ", formattedPrice));
                System.out.println(OPTION + "     " + BOLD + "     Quantity: " + RESET + HIGHLIGHT + quantity);
                System.out.println(OPTION + "     " + BOLD + "     Category: " + RESET + HIGHLIGHT +
                        product.getCategory());
                System.out.println(MENU + "     ╚" + "═".repeat(boxWidth) + "╝\n" + RESET);

                totalPrice += totalProductPrice;
            }


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
            System.out.print(PROMPT + "\n  Your choice: " + RESET + HIGHLIGHT);

            String choice = input.nextLine().trim();
            System.out.print(RESET);

            if (choice.equalsIgnoreCase("BACK")) {
                return;
            }

            if (choice.equalsIgnoreCase("DONE")) {
                HandleOrder.getInstance().handleOrder(person);
                pause(2000);
                return;
            }

            if (!choice.matches("\\d+")) {
                showError("Please enter a valid number");
                continue;
            }

            int index = Integer.parseInt(choice) - 1;

            if (index < 0 || index >= productList.size()) {
                showError("Invalid product number");
                continue;
            }

            Product selectedProduct = productList.get(index);

            System.out.println("\nProduct selected: " + selectedProduct.getFullName());
            System.out.println("1. Display product details");
            System.out.println("2. Remove product from cart");
            System.out.println("3. Deselect product");
            System.out.print("Enter action: ");
            String action = input.nextLine().trim();

            switch (action) {
                case "1":
                    DisplayProduct.getInstance().display(selectedProduct, null);
                    break;
                case "2":
                    cart.getProductMap().remove(selectedProduct);
                    System.out.println("Product removed from cart.");
                    pause(1500);
                    break;
                case "3":
                    selectedProduct = null;
                    break;
                default:
                    System.out.println("Invalid action");
            }
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
