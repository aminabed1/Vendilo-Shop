package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public class DisplayOrder implements Serializable {
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

            if (choice == 0) {
                return;
            }

            Order selectedOrder = orderList.get(choice - 1);
            displayOrderDetails(selectedOrder, person);
        }
    }

    public void displayOrder(Order order) {
        System.out.println("--------------------------------------------------");

        System.out.println("🗓️  \u001B[36mOrder Date:\u001B[0m " + formatInstant(order.getOrderDate()));

        List<String> agencyCodes = ((CustomerOrder) order).getSellersAgencyCode();
        if (agencyCodes != null && !agencyCodes.isEmpty()) {
            System.out.println("🏢 \u001B[36mSeller Agency Code(s):\u001B[0m " + String.join(", ", agencyCodes));
        } else {
            System.out.println("🏢 \u001B[33mNo agency code found.\u001B[0m");
        }

        System.out.printf("💰 \u001B[36mTotal Price:\u001B[0m \u001B[32m%.2f\u001B[0m\n", ((CustomerOrder) order).getTotalPrice());

        System.out.println("🛍️  \u001B[36mNumber of Products:\u001B[0m " +((CustomerOrder) order).getProductMap().size());

        Address addr = order.getDeliveryAddress();
        if (addr != null) {
            System.out.println("📍 \u001B[36mDelivery Address:\u001B[0m");
            System.out.println("  🏙️ City: " + addr.getCity());
            System.out.println("  🌆 Province: " + addr.getProvince());
            System.out.println("  🛣️ Street: " + addr.getStreet());
            System.out.println("  🏢 Plate Number: " + addr.getPlateNumber());
            System.out.println("  🔢 Postal Code: " + addr.getPostalCode());
            System.out.println("  ✏️ Details: " + addr.getDetails());
        }

        System.out.println("--------------------------------------------------\n");
    }

    public void displayOrderDetails(Order order, Person person) {
        HashMap<Product, Integer> productMap = ((CustomerOrder) order).getProductMap();
        List<Product> productList = new ArrayList<>(productMap.keySet());

        while (true) {
            System.out.println("\n\u001B[36m🛍️  Products in this order:\u001B[0m");

            for (int j = 0; j < productList.size(); j++) {
                Product p = productList.get(j);
                int quantity = productMap.get(p);
                System.out.println("   \u001B[32m" + (j + 1) + ".\u001B[0m " +
                        "🛒 " + p.getFullName() + " | 🏷️ " + p.getCategory() +
                        " | 💰 " + p.getPrice() + " | 📦 Qty: " + quantity);
            }

            System.out.println("   \u001B[33m0. Go back\u001B[0m");

            if (!(person instanceof Seller)) {
                SystemMessage.printMessage("F. Send Feedback To A Product", MessageTitle.Info);
            }

            System.out.println("\u001B[34m🔍 Select a product to view more details, " +
                    (person instanceof Seller ? "'0' to go back:" : "'F' to Send FeedBack To A Product, or '0' to go back:") + "\u001B[0m");

            String input = scan.nextLine().trim();

            if (input.equalsIgnoreCase("0")) {
                return;
            } else if (input.equalsIgnoreCase("F")) {
                if (person instanceof Seller) {
                    System.out.println("\u001B[31mSellers Cannot Send Feedback TO Products.\u001B[0m");
                } else {
                    rateProduct(productList, person);
                }
            } else {
                try {
                    int choice = Integer.parseInt(input);
                    if (choice < 1 || choice > productList.size()) {
                        System.out.println("\u001B[31mInvalid product number.\u001B[0m");
                    } else {
                        DisplayProduct.getInstance().display(productList.get(choice - 1), null);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mInvalid input.\u001B[0m");
                }
            }
        }
    }

    private void rateProduct(List<Product> productList, Person person) {
        System.out.println("\u001B[34mEnter the number of the product you want to rate (0 to cancel):\u001B[0m");
        int choice = getValidatedChoice(productList.size());
        if (choice == 0) {
            return;
        }

        Product productToRate = productList.get(choice - 1);
        if (productToRate.getRatingMap().containsKey(person)) {
            SystemMessage.printMessage("You have already rated this product.", MessageTitle.Error);
            return;
        }

        System.out.println("\u001B[34mPlease enter your rating (1 to 5):\u001B[0m");
        double rating;
        while (true) {
            String ratingInput = scan.nextLine().trim();
            if (!ratingInput.matches("\\d+(\\.\\d+)?")) {
                SystemMessage.printMessage("Invalid rating. Please enter a number between 1 and 5.", MessageTitle.Error);
                continue;
            }
            rating = Double.parseDouble(ratingInput);

            if (rating < 1 || rating > 5) {
                SystemMessage.printMessage("Rating must be between 1 and 5. Try again :", MessageTitle.Error);
            } else {
                break;
            }
        }
        SystemMessage.printMessage("Please Enter Your Feedback : ", MessageTitle.Info);
        String feedbackInput = scan.nextLine().trim();
        ProductReview productReview = new ProductReview(rating, feedbackInput);
        boolean added = productToRate.addRating(person, productReview);
        if (added) {
            SystemMessage.printMessage("Thank you! Your Feedback Has Been Recorded.", MessageTitle.Success);
        } else {
            SystemMessage.printMessage("You Have already rated this product.", MessageTitle.Error);
        }
    }

    private String formatInstant(Instant instant) {
        return java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(java.time.ZoneId.systemDefault())
                .format(instant);
    }

    private int getValidatedChoice(int max) {
        while (true) {
            String input = scan.nextLine().trim();
            if (!input.matches("\\d+")) {
                SystemMessage.printMessage("Invalid input. Please enter a valid number.", MessageTitle.Error);
                continue;
            }
            int choice = Integer.parseInt(input);
            if (choice < 0 || choice > max) {
                SystemMessage.printMessage("Invalid index. Please enter a number from 0 to " + max + ".", MessageTitle.Error);
            } else {
                return choice;
            }
        }
    }
}
