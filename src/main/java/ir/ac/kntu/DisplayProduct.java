package ir.ac.kntu;

import java.io.Serializable;
import java.util.*;

public class DisplayProduct implements Serializable {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private final static Scanner scanner = new Scanner(System.in);

    public static DisplayProduct getInstance() {
        return new DisplayProduct();
    }

    public void display(Product product, Customer customer) {
        System.out.println(product);
        showActionMenu(product, customer);
    }

    private void showActionMenu(Product product, Customer customer) {
        printMenu(product);
        handleChoice(product, customer, getUserChoice());
        Pause.pause(1500);
    }

    private void printMenu(Product product) {
        String isAvailable = (product.getStock() == 0)
                ? "║ " + ANSI_BLUE + "3. Let Me Know If It Is Available" + ANSI_PURPLE + "  ║"
                : "║                                    ║";

        System.out.println("\n" + ANSI_PURPLE + "╔════════════════════════════════════╗");
        System.out.println("║          ACTIONS MENU              ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ " + ANSI_BLUE + "1. Add to Cart" + ANSI_PURPLE + "                     ║");
        System.out.println("║ " + ANSI_BLUE + "2. Back to List" + ANSI_PURPLE + "                    ║");
        System.out.println(isAvailable);
        System.out.println("╚════════════════════════════════════╝" + ANSI_RESET);
    }

    private String getUserChoice() {
        System.out.print(ANSI_GREEN + "Enter your choice: " + ANSI_RESET);
        return scanner.nextLine().trim();
    }

    private void handleChoice(Product product, Customer customer, String choice) {
        switch (choice) {
            case "1":
                addToCart(product, customer);
                break;
            case "2":
                break;
            case "3":
                notifyIfUnavailable(product, customer);
                break;
            default:
                System.out.println(ANSI_RED + "Invalid choice!" + ANSI_RESET);
        }
    }

    private void addToCart(Product product, Customer customer) {
        if (product.getStock() > 0) {
            customer.getCart().addProductToMap(product);
            System.out.println(ANSI_GREEN + "\nProduct added to cart successfully!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "\nProduct is out of stock!" + ANSI_RESET);
        }
    }

    private void notifyIfUnavailable(Product product, Customer customer) {
        if (product.getStock() == 0) {
            createProductNotification(product, customer);
        }
    }


    public void createProductNotification(Product product, Customer customer) {
        Notification notification = new Notification(product);
        customer.addNotification(notification);
        System.out.println("You Will Know If Product Is Available" + ANSI_RESET);
        Pause.pause(2000);
    }
}
