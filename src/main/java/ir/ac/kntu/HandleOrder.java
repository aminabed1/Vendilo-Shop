package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public class HandleOrder implements Serializable {
    private final static Scanner scan = new Scanner(System.in);
    private static double totalPrice;
    private static double postPrice;
    private static double finalPrice;
    private static List<String> sellersAgencyCode;
    private static Address selectedAddress;
    private Map<Product, Integer> productMap;

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
        productMap = new HashMap<>();
        customerOrder(person);
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

        postPrice = calculatePostingPrice(selectedAddress.getProvince(), cart, ((Customer) person).getVendiloPlus().getIsActive());

        boolean codeChanged;
        while (true) {
            totalPrice = cart.getTotalPrice();
            finalPrice = calculateFinalPrice();
            displayCartSummary(cart);
            codeChanged = discountCodeHandle(cart, person);
            if (codeChanged) {
                continue;
            }
            break;
        }
        displayFinalizeOption(person);
    }

    public boolean checkProductsStock(Cart cart) {
        for (Product product : cart.getProductMap().keySet()) {
            if (product.getStock() < cart.getProductMap().get(product)) {
                displayOutOfStock(product);
                Pause.pause(2000);
                return false;
            }
        }
        return true;
    }

    public double calculatePostingPrice(String province, Cart cart, boolean isPremiumAccount) {
        productMap = cart.getProductMap();
        for (Product product : productMap.keySet()) {
            String sellerProvince = getSellerProvince(product.getSellerAgencyCode());
            if (!province.equals(sellerProvince)) {
                return calculatePostingCost(false, isPremiumAccount);
            }
        }
        return calculatePostingCost(true, isPremiumAccount);
    }

    private double calculatePostingCost(boolean sameProvince, boolean isPremiumAccount) {
        if (isPremiumAccount) {
            return sameProvince ? (double) 0 : 5;
        } else {
            return sameProvince ? (double) 5 : 15;
        }
    }

    public String getSellerProvince(String agencyCode) {
        for (Person person : DataBase.getInstance().getPersonList()) {
            if (person instanceof Seller && agencyCode.equals(((Seller) person).getAgencyCode())) {
                return ((Seller) person).getProvince();
            }
        }
        return null;
    }

    public String getShopName(String agencyCode) {
        for (Person person : DataBase.getInstance().getPersonList()) {
            if (person instanceof Seller && agencyCode.equals(((Seller) person).getAgencyCode())) {
                return ((Seller) person).getShopName();
            }
        }
        return null;
    }

    public double calculateFinalPrice() {
        return postPrice + totalPrice;
    }

    public void displayCartSummary(Cart cart) {
        displayOrderSummaryHeader();

        for (Product product : cart.getProductMap().keySet()) {
            displayProductSummary(product);
        }

        displayPrices();
    }

    public boolean discountCodeHandle(Cart cart, Person person) {
        while (true) {
            double percentage = ((Customer) person).getVendiloPlus().getIsActive() ? 0.95 : 1;
            boolean hasCode = cart.getDiscountCode() != null;
            totalPrice = cart.getTotalPrice() * percentage;

            if (!confirmAddOrRemove(hasCode)) {
                continue;
            }

            if (!wantsToAdd()) {
                return false;
            }

            DiscountCode code = null;
            if (!hasCode) {
                code = askAndValidateDiscountCode(person);
                if (code == null) {
                    continue;
                }
                cart.discountCodeHandle(code, "Add Discount Code");
            } else {
                cart.discountCodeHandle(null, "Remove Discount Code");
            }

            totalPrice = cart.getTotalPrice() * percentage;
            return true;
        }
    }

    private boolean confirmAddOrRemove(boolean hasCode) {
        System.out.println("Do you want to " + (hasCode ? "remove" : "add") + " discount code ? (y/n)");
        String choice = scan.nextLine();
        if (!("y".equalsIgnoreCase(choice) || "n".equalsIgnoreCase(choice))) {
            System.out.println("Please enter valid choice");
            return false;
        }
        return true;
    }

    private boolean wantsToAdd() {
        System.out.print("");
        String choice = scan.nextLine();
        return "y".equalsIgnoreCase(choice);
    }

    private DiscountCode askAndValidateDiscountCode(Person person) {
        System.out.println("Please enter a discount code");
        String discountCode = scan.nextLine();
        DiscountCode code = isDiscountCodeAllowedForPerson(discountCode, person);
        if (code == null) {
            System.out.println("Invalid discount code");
        }
        return code;
    }

    public DiscountCode isDiscountCodeAllowedForPerson(String discountCode, Person person) {
        List<DiscountCode> discountCodeList = ((Customer) person).getDiscountCodeList();
        for (DiscountCode code : discountCodeList) {
            if (code.getCode().equals(discountCode)) {
                return code;
            }
        }

        for (DiscountCode code : DataBase.getInstance().getDiscountCodeList()) {
            if (code.getCode().equals(discountCode)) {
                return code;
            }
        }

        return null;
    }

    public void displayFinalizeOption(Person person) {
        while (true) {
            displayFinalizePrompt();
            String choice = scan.nextLine().trim();
            if ("y".equalsIgnoreCase(choice)) {
                if (checkEnoughBalance(person)) {
                    finalizeOrder(person);
                }
                break;
            } else if ("n".equalsIgnoreCase(choice)) {
                return;
            } else {
                SystemMessage.printMessage("Please enter Y or N", MessageTitle.Error);
            }
        }
    }

    public boolean checkEnoughBalance(Person person) {
        double balance = ((Customer) person).getWallet().getWalletBalance();
        if (balance < finalPrice) {
            displayInsufficientBalance(balance);
            Pause.pause(2000);
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

    public Map<Product, Integer> fillSellerMap(String sellerAgencyCode) {
        Map<Product, Integer> sellerProductMap = new HashMap<>();
        for (Product product : productMap.keySet()) {
            if (sellerAgencyCode.equals(product.getSellerAgencyCode())) {
                sellerProductMap.put(product, productMap.get(product));
            }
        }
        return sellerProductMap;
    }

    public void finalizeOrder(Person person) {
        setSellersCodeList();
        Customer customer = (Customer) person;
        CustomerOrder newOrder = new CustomerOrder(Instant.now(), sellersAgencyCode, customer.getEmail(), selectedAddress, finalPrice, productMap);
        //TODO enhance here and prevent duplicate heap memory usage
        DataBase.getInstance().addOrder(newOrder);
        customer.addOrder(newOrder);
        //TODO till here
        double newBalance = customer.getWallet().getWalletBalance() - finalPrice;
        customer.getWallet().setWalletBalance(newBalance, newOrder);
        DiscountCode discountCode = ((Customer) person).getCart().getDiscountCode();

        if (discountCode != null) {
            discountCode.setUsableTimes(discountCode.getUsableTimes() - 1);
        }
        customer.setCart(new Cart());

        displayOrderCompleted(newBalance);
        handleSellerOrder(person, productMap);
        reduceProductsStock();
        Pause.pause(3000);
    }

    public void reduceProductsStock() {
        for (Product product : productMap.keySet()) {
            product.setStock(product.getStock() - productMap.get(product));
        }
    }

    public void handleSellerOrder(Person customer, Map<Product, Integer> productMap) {
        for (Product product : productMap.keySet()) {
            String agencyCode = product.getSellerAgencyCode();
            Seller seller = findSellerByAgencyCode(agencyCode);
            Map<Product, Integer> sellerProductMap = fillSellerMap(agencyCode);

            double productPrice = Double.parseDouble(product.getPrice()) * productMap.get(product);
            double sellerBenefit = productPrice * 0.9;
            double currentBalance = seller.getWallet().getWalletBalance();

            SellerOrder sellerOrder = new SellerOrder(agencyCode, Instant.now(), sellerBenefit, selectedAddress, ((OrdinaryUsers) customer).getEmail(), "Sale of product: ", sellerProductMap);

            seller.getWallet().setWalletBalance(currentBalance + sellerBenefit, sellerOrder);
            seller.addOrder(sellerOrder);
            DataBase.getInstance().addOrder(sellerOrder);
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
            displayAddressSelectionHeader();

            displayAddressesOrWarning(addressList, personInfo);

            String choice = promptYesNo();

            if ("y".equalsIgnoreCase(choice)) {
                personInfo.addNewAddress(customer);
                addressList = customer.getAddressList();
            } else if ("n".equalsIgnoreCase(choice)) {
                if (addressList.isEmpty()) {
                    return null;
                }
                Address selected = promptAddressSelection(addressList);
                if (selected != null) {
                    return selected;
                }
            } else {
                SystemMessage.printMessage("Please enter Y or N", MessageTitle.Error);
            }
        }
    }

    private void displayAddressesOrWarning(List<Address> addressList, PersonAccount personInfo) {
        if (addressList.isEmpty()) {
            System.out.println(WARNING + "You have no saved addresses.\n" + RESET);
        } else {
            System.out.println(SUCCESS + "Your saved addresses:" + RESET);
            personInfo.displayAddresses(addressList);
        }
    }

    private String promptYesNo() {
        System.out.println();
        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║" + OPTION + "  Would you like to add an address?  " + TITLE + "║");
        System.out.println("║" + OPTION + "  [Y] Yes       [N] No               " + TITLE + "║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
        System.out.print(PROMPT + "Your choice: " + RESET + HIGHLIGHT);
        return scan.nextLine().trim();
    }

    private Address promptAddressSelection(List<Address> addressList) {
        System.out.print(PROMPT + "\nEnter address number (1-" + addressList.size() + "): " + RESET + HIGHLIGHT);
        String input = scan.nextLine().trim();
        try {
            int selectedIndex = Integer.parseInt(input);
            if (selectedIndex < 1 || selectedIndex > addressList.size()) {
                SystemMessage.printMessage("Please enter a valid number", MessageTitle.Error);
                return null;
            }
            return addressList.get(selectedIndex - 1);
        } catch (NumberFormatException e) {
            SystemMessage.printMessage("Please enter a valid number", MessageTitle.Error);
            return null;
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
        System.out.println(MENU   + "╔══════════════════════════════════════════════════════╗");
        System.out.printf(OPTION  + "  %-30s " + HIGHLIGHT + "%10.2f $\n", product.getFullName(), Double.parseDouble(product.getPrice()));
        System.out.println(OPTION + "  Category: " + HIGHLIGHT + product.getCategory());
        System.out.println(OPTION + "  Quantity: " + HIGHLIGHT + productMap.get(product));
        System.out.println(OPTION + "  Seller: " + HIGHLIGHT + getShopName(product.getSellerAgencyCode()));
        System.out.println(MENU   + "╚══════════════════════════════════════════════════════╝" + RESET);
    }

    private void displayPrices() {
        System.out.println(TITLE + "════════════════════════════════════════════════════════");
        System.out.printf(" " + BOLD + "  POSTING PRICE: " + HIGHLIGHT + "%.2f $" + TITLE + "\n", postPrice);
        System.out.printf(" " + BOLD + "  PRODUCTS PRICE: " + HIGHLIGHT + "%.2f $" + TITLE + "\n", totalPrice);
        System.out.printf(" " + BOLD + "  FINAL PRICE: " + HIGHLIGHT + "%.2f $" + TITLE + "\n", finalPrice);
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
        System.out.printf("  Needed: %.2f $ \n", finalPrice);
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
    
    public Seller findSellerByAgencyCode(String agencyCode) {
        for (Person p : DataBase.getInstance().getPersonList()) {
            if (p instanceof Seller seller) {
                if (seller.getAgencyCode().equals(agencyCode)) {
                    return seller;
                }
            }
        }
        return null;
    }
}