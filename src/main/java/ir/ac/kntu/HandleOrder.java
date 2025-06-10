package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public class HandleOrder implements Serializable {
    private static double totalPrice = 0;
    private final static Scanner scan = new Scanner(System.in);
    private static final double basePrice = 15;
    private static double finalPrice;
    private static double postPrice;
    private static List<String> sellersAgencyCode;
    private static Address selectedAddress;
    private HashMap<Product, Integer> productMap;

    public static final String WARNING = "\u001B[33m";
    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String BOLD = "\u001B[1m";

    public static HandleOrder getInstance() {
        return new HandleOrder();
    }

    public void handleOrder(Person person) {
        clearScreen();
        finalPrice = 0;
        postPrice = 0;
        productMap = new HashMap<>();
        selectedAddress = null;

        if (person instanceof Customer) {
            customerOrder(person);
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void customerOrder(Person person) {
        Cart cart = ((Customer) person).getCart();
        if (!checkProductsStock(cart)) {
            return;
        }

        selectedAddress = addressSelection((Customer) person);
        if (selectedAddress == null) {
            return;
        }

        double postingPrice = calcPostingPrice(selectedAddress.getProvince(), cart);
        postPrice = postingPrice;


        finalPrice = getFinalPrice(cart, postingPrice);
        displayCartSummary(cart);
        displayFinalizeOption(person);
    }

    public boolean checkProductsStock(Cart cart) {
        for (Product product : cart.getProductMap().keySet()) {
            if (product.getStock() < cart.getProductMap().get(product)) {
                displayOutOfStock(product);
                pause(2000);
                return false;
            }
        }
        return true;
    }

    public double calcPostingPrice(String province, Cart cart) {
        productMap = cart.getProductMap();
        for (Product product : productMap.keySet()) {
            String sellerProvince = getSellerProvince(product.getSellerAgencyCode());
            if (!province.equals(sellerProvince)) {
                return calculatePostingCost(cart, false);
            }
        }
        return calculatePostingCost(cart, true);
    }

    private double calculatePostingCost(Cart cart, boolean sameProvince) {
        return sameProvince ? (double) 5 : 15;
    }

    public String getSellerProvince(String agencyCode) {
        for (Person person : DataBase.getPersonList()) {
            if (person instanceof Seller && agencyCode.equals(((Seller) person).getAgencyCode())) {
                return ((Seller) person).getProvince();
            }
        }
        return null;
    }

    public String getShopName(String agencyCode) {
        for (Person person : DataBase.getPersonList()) {
            if (person instanceof Seller && agencyCode.equals(((Seller) person).getAgencyCode())) {
                return ((Seller) person).getShopName();
            }
        }
        return null;
    }

    public double getFinalPrice(Cart cart, double postingPrice) {
        double total = 0.0;
        for (Product product : cart.getProductMap().keySet()) {
            total += Double.parseDouble(product.getPrice());
        }
        return total + postingPrice;
    }


    public void displayCartSummary(Cart cart) {
        clearScreen();
        double totalPrice = 0;

        displayOrderSummaryHeader();

        for (Product product : cart.getProductMap().keySet()) {
            displayProductSummary(product);
            totalPrice += Double.parseDouble(product.getPrice()) * cart.getProductMap().get(product);
        }

        displayPrices(totalPrice, postPrice);
    }

    public void displayFinalizeOption(Person person) {
        while (true) {
            displayFinalizePrompt();
            String choice = scan.nextLine().trim();

            if (choice.equalsIgnoreCase("Y")) {
                if (checkEnoughBalance(person)) {
                    finalizeOrder(person);
                }
                break;
            } else if (choice.equalsIgnoreCase("N")) {
                return;
            } else {
                showError("Please enter Y or N");
            }
        }
    }

    public boolean checkEnoughBalance(Person person) {
        double balance = ((Customer) person).getWallet().getWalletBalance();
        if (balance < finalPrice) {
            displayInsufficientBalance(balance);
            pause(2000);
            return false;
        }
        return true;
    }

    public void setSellersCodeList() {
        sellersAgencyCode = new ArrayList<>();
        for (Product product : productMap.keySet()) {
            sellersAgencyCode.add(product.getSellerAgencyCode());
        }
    }

    public void finalizeOrder(Person person) {
        setSellersCodeList();
        Customer customer = (Customer) person;
        Order newOrder = new Order(productMap, Instant.now(), sellersAgencyCode, customer.getEmail(), selectedAddress, finalPrice, postPrice);
        DataBase.addOrder(newOrder);
        customer.addOrder(newOrder);
        double newBalance = customer.getWallet().getWalletBalance() - finalPrice;
        customer.getWallet().setWalletBalance(newBalance, newOrder);
        customer.setCart(new Cart());

        displayOrderCompleted(newBalance);
        chargeSellersWallet(person, productMap);
        reduceProductsStock();
        createSellerOrder(selectedAddress, person.getEmail());
        pause(3000);
    }

    public void reduceProductsStock() {
        for (Product product : productMap.keySet()) {
            product.setStock(product.getStock() - productMap.get(product));
        }
    }

    public void chargeSellersWallet(Person person, HashMap<Product, Integer> productMap) {
        for (Product product : productMap.keySet()) {
            String agencyCode = product.getSellerAgencyCode();
            Seller seller = findSellerByAgencyCode(agencyCode);
            if (seller != null) {
                double productPrice = Double.parseDouble(product.getPrice());
                double sellerShare = productPrice * 0.9;
                double currentBalance = seller.getWallet().getWalletBalance();

                Order sellerOrder = new Order(
                        Instant.now(),
                        sellerShare,
                        "Sale of product: " + product.getFullName()
                );

                seller.getWallet().setWalletBalance(currentBalance + sellerShare, sellerOrder);

            }
        }
    }

    public void createSellerOrder(Address deliveryAddress, String customerEmail) {
        for (Product product : productMap.keySet()) {
            Order newOrder = new Order(product, Instant.now(), Double.parseDouble(product.getPrice()), deliveryAddress, customerEmail);//seller order
            Seller seller = findSellerByAgencyCode(product.getSellerAgencyCode());
            seller.addOrder(newOrder);
        }
    }

    private void displayOutOfStock(Product product) {
        System.out.println(ERROR + "╔═════════════════════════════════════╗");
        System.out.println("║                                     ║");
        System.out.println("║" + BOLD + "        PRODUCT OUT OF STOCK         " + RESET + ERROR + "║");
        System.out.println("║                                     ║");
        System.out.println("╠═════════════════════════════════════╣");
        System.out.printf("  Product: %s \n", product.getFullName());
        System.out.println("╚═════════════════════════════════════╝" + RESET);
    }

    public Address addressSelection(Customer customer) {
        List<Address> addressList = customer.getAddressList();
        PersonAccount personInfo = new PersonAccount();

        while (true) {
            clearScreen();
            displayAddressSelectionHeader();

            if (addressList.isEmpty()) {
                System.out.println(WARNING + "You have no saved addresses.\n" + RESET);
            } else {
                System.out.println(SUCCESS + "Your saved addresses:" + RESET);
                personInfo.displayAddresses(addressList);
            }

            System.out.println();
            System.out.println(TITLE + "╔═════════════════════════════════════╗");
            System.out.println("║" + OPTION + "  Would you like to add an address?  " + TITLE + "║");
            System.out.println("║" + OPTION + "  [Y] Yes       [N] No               " + TITLE + "║");
            System.out.println("╚═════════════════════════════════════╝" + RESET);
            System.out.print(PROMPT + "Your choice: " + RESET + HIGHLIGHT);

            String choice = scan.nextLine().trim();
            if (choice.equalsIgnoreCase("Y")) {
                personInfo.addNewAddress(customer);
                addressList = customer.getAddressList();
            } else if (choice.equalsIgnoreCase("N")) {
                if (addressList.isEmpty()) {
                    return null;
                }

                System.out.print(PROMPT + "\nEnter address number (1-" + addressList.size() + "): " + RESET + HIGHLIGHT);
                try {
                    int selectedIndex = Integer.parseInt(scan.nextLine().trim());
                    if (selectedIndex < 1 || selectedIndex > addressList.size()) {
                        showError("Please enter a valid number");
                        continue;
                    }
                    return addressList.get(selectedIndex - 1);
                } catch (NumberFormatException e) {
                    showError("Please enter a valid number");
                }
            } else {
                showError("Please enter Y or N");
            }
        }
    }

    private void displayAddressSelectionHeader() {
        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║                                     ║");
        System.out.println("║" + BOLD + "       SELECT DELIVERY ADDRESS       " + RESET + TITLE + "║");
        System.out.println("║                                     ║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
    }

    private void displayOrderSummaryHeader() {
        System.out.println(TITLE + "╔══════════════════════════════════════════════════════╗");
        System.out.println("║" + BOLD + "                 ORDER SUMMARY                        " + RESET + TITLE + "║");
        System.out.println("╠══════════════════════════════════════════════════════╣" + RESET);
    }

    private void displayProductSummary(Product product) {
        System.out.println(MENU + "╔══════════════════════════════════════════════════════╗");
        System.out.printf(OPTION + "  %-30s " + HIGHLIGHT + "%10.2f $\n", product.getFullName(), Double.parseDouble(product.getPrice()));
        System.out.println(OPTION + "  Category: " + HIGHLIGHT + product.getCategory());
        System.out.println(OPTION + "  Seller: " + HIGHLIGHT + getShopName(product.getSellerAgencyCode()));
        System.out.println(MENU + "╚══════════════════════════════════════════════════════╝" + RESET);
    }

    private void displayPrices(double total, double postingPrice) {
        System.out.println(TITLE + "════════════════════════════════════════════════════════");
        System.out.printf(" " + BOLD + "  POSTING PRICE: " + HIGHLIGHT + "%.2f $" + TITLE + "\n", postingPrice);
        System.out.printf(" " + BOLD + "  PRODUCTS PRICE: " + HIGHLIGHT + "%.2f $" + TITLE + "\n", total);
        System.out.printf(" " + BOLD + "  TOTAL PRICE: " + HIGHLIGHT + "%.2f $" + TITLE + "\n", total + postingPrice);
        System.out.println("════════════════════════════════════════════════════════" + RESET);
    }

    private void displayFinalizePrompt() {
        System.out.println("\n" + MENU + "╔═════════════════════════════════════╗");
        System.out.println("║" + OPTION + "  Finalize your order?               " + MENU + "║");
        System.out.println("║" + OPTION + "  [Y] Yes       [N] No               " + MENU + "║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
        System.out.print(PROMPT + "Your choice: " + RESET + HIGHLIGHT);
    }

    private void displayInsufficientBalance(double current) {
        System.out.println(ERROR + "╔═════════════════════════════════════╗");
        System.out.println("║                                     ║");
        System.out.println("║" + BOLD + "      INSUFFICIENT BALANCE!          " + RESET + ERROR + "║");
        System.out.println("║                                     ║");
        System.out.println("╚═════════════════════════════════════╝");
        System.out.printf("  Current: %.2f $\n", current);
        System.out.printf("  Needed: %.2f $ \n", totalPrice);
        System.out.println("═══════════════════════════════════════" + RESET);
    }

    private void displayOrderCompleted(double newBalance) {
        System.out.println(SUCCESS + "╔═════════════════════════════════════╗");
        System.out.println("║                                     ║");
        System.out.println("║" + BOLD + "      ORDER COMPLETED SUCCESSFULLY!  " + RESET + SUCCESS + "║");
        System.out.println("║                                     ║");
        System.out.println("╚═════════════════════════════════════╝");
        System.out.printf("  Total Paid: %.2f $ \n", finalPrice);
        System.out.printf("  New Balance: %.2f $ \n", newBalance);
        System.out.println("═══════════════════════════════════════" + RESET);
    }

    private void showError(String message) {
        System.out.println(ERROR + "\n " + message + RESET);
        pause(1000);
    }

    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Seller findSellerByAgencyCode(String agencyCode) {
        for (Person p : DataBase.getPersonList()) {
            if (p instanceof Seller seller) {
                if (seller.getAgencyCode().equals(agencyCode)) {
                    return seller;
                }
            }
        }
        return null;
    }
}