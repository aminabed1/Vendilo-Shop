package ir.ac.kntu;

import java.time.Instant;
import java.util.List;
import java.util.Scanner;

public class DisplayOrder {
    private final Scanner scan = new Scanner(System.in);

    public static DisplayOrder getInstance() {
        return new DisplayOrder();
    }

    public void display(Person person) {
        List<Order> orderList = ((Customer) person).getOrder();

        if (orderList.isEmpty()) {
            System.out.println("\u001B[33m⚠️  No orders found.\u001B[0m");
            return;
        }

        while (true) {
            System.out.println("\n\u001B[36m📦 Your Orders:\u001B[0m");
            for (int i = 0; i < orderList.size(); i++) {
                System.out.print("   \u001B[32m" + (i + 1) + ".\u001B[0m ");
                displayOrder(orderList.get(i));
            }

            System.out.println("\u001B[34m🔍 Select an order to view details (press 0 to go back):\u001B[0m");
            int choice = getValidatedChoice(orderList.size());

            if (choice == 0) return;

            Order selectedOrder = orderList.get(choice - 1);
            displayOrderDetails(selectedOrder);
        }
    }

    public void displayOrder(Order order) {
        System.out.println("--------------------------------------------------");

        System.out.println("🗓️  \u001B[36mOrder Date:\u001B[0m " + formatInstant(order.getOrderDate()));

        List<String> agencyCodes = order.getSellersAgencyCode();
        if (agencyCodes != null && !agencyCodes.isEmpty()) {
            System.out.println("🏢 \u001B[36mSeller Agency Code(s):\u001B[0m " + String.join(", ", agencyCodes));
        } else {
            System.out.println("🏢 \u001B[33mNo agency code found.\u001B[0m");
        }

        System.out.printf("💰 \u001B[36mTotal Price:\u001B[0m \u001B[32m%.2f\u001B[0m\n", order.getTotalPrice());

        System.out.println("🛍️  \u001B[36mNumber of Products:\u001B[0m " + order.getProductList().size());

        Address addr = order.getDeliveryAddress();
        if (addr != null) {
            System.out.println("📍 \u001B[36mDelivery Address:\u001B[0m");
            System.out.println("    🏙️  City: " + addr.getCity());
            System.out.println("    🌆 Province: " + addr.getProvince());
            System.out.println("    🛣️  Street: " + addr.getStreet());
            System.out.println("    🏢 Plate Number: " + addr.getPlateNumber());
            System.out.println("    🔢 Postal Code: " + addr.getPostalCode());
            System.out.println("    ✏️  Details: " + addr.getDetails());
        } else {
            System.out.println("📍 \u001B[33mNo address found for this order.\u001B[0m");
        }

        System.out.println("--------------------------------------------------\n");
    }


    public void displayOrderDetails(Order order) {
        List<Product> productList = order.getProductList();

        while (true) {
            System.out.println("\n\u001B[36m🛍️  Products in this order:\u001B[0m");
            for (int j = 0; j < productList.size(); j++) {
                Product p = productList.get(j);
                System.out.println("   \u001B[32m" + (j + 1) + ".\u001B[0m " +
                        "🛒 " + p.getFullName() + " | 🏷️ " + p.getCategory() + " | 💰 " + p.getPrice());
            }

            System.out.println("\u001B[34m🔍 Select a product to view more details (press 0 to go back):\u001B[0m");
            int choice = getValidatedChoice(productList.size());

            if (choice == 0) return;

            ProductDisplay.getInstance().display(productList.get(choice - 1));
        }
    }

    private String formatInstant(Instant instant) {
        return java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(java.time.ZoneId.systemDefault())
                .format(instant);
    }


    private int getValidatedChoice(int max) {
        int choice;
        while (true) {
            String input = scan.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
                if (choice < 0 || choice > max) {
                    System.out.println("\u001B[31m Invalid index. Please enter a number from 0 to " + max + ".\u001B[0m");
                } else {
                    return choice;
                }
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31m Invalid input. Please enter a valid number.\u001B[0m");
            }
        }
    }
}
