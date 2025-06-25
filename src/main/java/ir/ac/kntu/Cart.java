package ir.ac.kntu;

import java.io.Serializable;
import java.util.*;

public class Cart implements Serializable {

    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String BOLD = "\u001B[1m";

    private HashMap<Product, Integer> productMap;
    private DiscountCode discountCode;
    private double totalPrice;

    public Cart() {
        discountCode = null;
        productMap = new HashMap<>();
//        DiscountCode dc = new PercentDiscount("ABCD", true, 10, 10);
//        this.setDiscountCode(dc);
    }

    public HashMap<Product, Integer> getProductMap() {
        return productMap;
    }

    public void addProductToMap(Product product) {
        productMap.merge(product, 1, Integer::sum);
    }

    public DiscountCode getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    public double getTotalPrice() {
        if (discountCode != null) {
            applyDiscountCodeToPrice();
            return totalPrice;
        } else {
            return calculateTotalPrice();
        }
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void displayCart(Customer person) {
        if (productMap.isEmpty()) {
            displayEmptyCart();
            return;
        }
        Scanner scan = new Scanner(System.in);
        while (true) {
            displayCartContents(person);
            String choice = getCustomerChoice(scan);
            if (handleCartNavigation(choice, person)) {
                return;
            }
            handleProductSelection(choice, scan, person);
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
        printCartHeader();
        printProductList();
        printCartFooter(totalPrice);
    }

    private double calculateTotalPrice() {
        return productMap.entrySet().stream()
                .mapToDouble(e -> Double.parseDouble(e.getKey().getPrice()) * e.getValue())
                .sum();
    }

    private void printCartHeader() {
        System.out.println(MENU + "╔══════════════════════════════════════════════╗");
        System.out.println(       "║" + BOLD + "                 YOUR CART                    " + RESET + TITLE + "║");
        System.out.println(MENU + "╚══════════════════════════════════════════════╝");
    }

    private void printProductList() {
        List<Product> productList = new ArrayList<>(productMap.keySet());
        for (int i = 0; i < productList.size(); i++) {
            printProductItem(i, productList.get(i));
        }
    }

    private void printProductItem(int i, Product product) {
        System.out.println();
        System.out.println(MENU   + "╔══════════════════════════════════════════════╗");
        System.out.println(i + 1);
        System.out.printf(OPTION  + "  %-30s " + HIGHLIGHT + "%10.2f $\n", product.getFullName(), Double.parseDouble(product.getPrice()));
        System.out.println(OPTION + "  Category: " + HIGHLIGHT + product.getCategory());
        System.out.println(OPTION + "  Quantity: " + HIGHLIGHT + productMap.get(product));
        System.out.println(OPTION + "  Seller: " + HIGHLIGHT + getShopName(product.getSellerAgencyCode()));
        System.out.println(MENU   + "╚══════════════════════════════════════════════╝" + RESET);
    }

    private void printCartFooter(double totalPrice) {
        System.out.println();
        System.out.println(MENU   + "════════════════════════════════════════════════");
        System.out.println(MENU   + "  TOTAL PRICE: " + HIGHLIGHT + totalPrice);
        System.out.println(MENU   + "════════════════════════════════════════════════");
        System.out.println(OPTION + "  Select product by number or :");
        System.out.println(OPTION + "  Type DONE to checkout");
        System.out.println(OPTION + "  Type BACK to return");
        System.out.println(MENU   + "════════════════════════════════════════════════" + RESET);
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
        System.out.print("Enter action: ");
        System.out.println(MENU   + "════════════════════════════════════════════════════════");
        System.out.println(MENU   + "  Product selected: " + HIGHLIGHT + product.getFullName());
        System.out.println(MENU   + "════════════════════════════════════════════════════════");
        System.out.println(OPTION + "  1. Display product details");
        System.out.println(OPTION + "  2. Remove product from cart");
        System.out.println(OPTION + "  3. Deselect product");
        System.out.println(MENU   + "════════════════════════════════════════════════════════" + RESET);
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

    public String getShopName(String agencyCode) {
        for (Person person : DataBase.getPersonList()) {
            if (person instanceof Seller && agencyCode.equals(((Seller) person).getAgencyCode())) {
                return ((Seller) person).getShopName();
            }
        }
        return null;
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

    public void discountCodeHandle(DiscountCode discountCode, String operation) {
        if (operation.equals("Remove Discount Code")) {
            removeDiscountCode();
        } else {
            if (!isValidDiscountCode(discountCode)) {
                return;
            }
            if (!isUsableDiscountCode(discountCode)) {
                return;
            }
        }
        this.setDiscountCode(discountCode);
        System.out.println("Discount Code Applied Successfully.");
//        applyDiscountCodeToPrice(discountCode);
    }

    public boolean isValidDiscountCode(DiscountCode discountCode) {
        if (!discountCode.getIsCodeActive()) {
            showError("Discount Code is inactive.");
            return false;
        }

        if (discountCode.getUsableTimes() <= 0) {
            showError("Discount Code is out of usage.");
        }

        return true;
    }

    public boolean isUsableDiscountCode(DiscountCode discountCode) {
        if (discountCode instanceof PercentDiscount) {
            return true;
        }

        ValueDiscount codeDiscount = (ValueDiscount) discountCode;

        if ((codeDiscount.getValue() * 10) > totalPrice) {
            return true;
        } else {
            showError("Total price is less than required price to allow discounting.");
            return false;
        }
    }

    public void applyDiscountCodeToPrice() {
        totalPrice = calcTotalPrice();
        if (this.discountCode == null) {
            return;
        } else {
            if (discountCode instanceof PercentDiscount) {
                totalPrice *= (100 - ((PercentDiscount) discountCode).getPercent()) / 100;
            } else {
                totalPrice -= ((ValueDiscount) discountCode).getValue();
            }
            discountCode.setUsableTimes(discountCode.getUsableTimes() - 1);
        }
    }

    public void removeDiscountCode() {
        discountCode.setUsableTimes(discountCode.getUsableTimes() + 1);
        this.discountCode = null;
        System.out.println("Discount Code Has Been Removed Successfully.");
        pause(2000);
    }

    public double calcTotalPrice() {
        double totalPrice = 0.0;
        int productQuantity = 0;
        for (Product product : this.getProductMap().keySet()) {
            productQuantity = this.getProductMap().get(product);
            totalPrice += Double.parseDouble(product.getPrice()) * productQuantity;
        }
        return totalPrice;
    }
}