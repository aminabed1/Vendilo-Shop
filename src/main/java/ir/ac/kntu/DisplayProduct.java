package ir.ac.kntu;

import java.io.Serializable;
import java.util.*;

public class DisplayProduct implements Serializable {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    public static DisplayProduct getInstance() {
        return new DisplayProduct();
    }

    public void display(Product product, Customer customer) {
        System.out.println(product);
        showActionMenu(product, customer);
    }

    private void showActionMenu(Product product, Customer customer) {
        Scanner scanner = new Scanner(System.in);
        String isAvailable = (product.getStock() == 0) ? "║ " + ANSI_BLUE + "3. Let Me Know If It Is Available" + ANSI_PURPLE + "  ║" : "║                                    ║";
        System.out.println("\n" + ANSI_PURPLE + "╔════════════════════════════════════╗");
        System.out.println(                     "║          ACTIONS MENU              ║");
        System.out.println(                     "╠════════════════════════════════════╣");
        System.out.println("║ " + ANSI_BLUE + "1. Add to Cart" + ANSI_PURPLE + "                     ║");
        System.out.println("║ " + ANSI_BLUE + "2. Back to List" + ANSI_PURPLE + "                    ║");
        System.out.println(isAvailable);
        System.out.println("╚════════════════════════════════════╝" + ANSI_RESET);

        System.out.print(ANSI_GREEN + "Enter your choice: " + ANSI_RESET);
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                if (product.getStock() > 0) {
                    customer.getCart().addProductToMap(product);
                    System.out.println(ANSI_GREEN + "\nProduct added to cart successfully!" + ANSI_RESET);
                } else {
                    System.out.println(ANSI_RED + "\nProduct is out of stock!" + ANSI_RESET);
                }
                break;
            case "2":
                break;
            case "3":
                if (product.getStock() == 0) {
                    createProductNotification(product, customer);
                    break;
                }
            default:
                System.out.println(ANSI_RED + "Invalid choice!" + ANSI_RESET);
        }
        Pause.pause(1500);
    }

    public void displayField(String key, String value) {
        if (value == null || value.isEmpty()) {
            System.out.printf(ANSI_YELLOW + "%-20s: " + ANSI_RED + "Not Available\n" + ANSI_RESET, key);
        } else {
            System.out.printf(ANSI_GREEN + "%-20s: " + ANSI_RESET + "%s\n", key, value);
        }
    }

    public void createProductNotification(Product product, Customer customer) {
        Notification notification = new Notification(product);
        customer.addNotification(notification);
        System.out.println("You Will Know If Product Is Available" + ANSI_RESET);
        Pause.pause(2000);
    }
}
