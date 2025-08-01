package ir.ac.kntu;

import java.util.*;

public class Customer extends OrdinaryUsers {
    private Cart cart;
    private final Wallet wallet;
    private final List<Order> orderList;
    private final List<Address> addressList;
    private final List<String> ratedProductsList;
    private final List<Notification> notifications;
    private final List<DiscountCode> discountCodeList;
    private VendiloPlusAccount vendiloPlusAccount;

    public Customer(String name, String surname, String phoneNumber, String email, String password) {
        super(name, surname, password, phoneNumber, email);
        vendiloPlusAccount = new VendiloPlusAccount();
        orderList = new ArrayList<>();
        addressList = new ArrayList<>();
        ratedProductsList = new ArrayList<>();
        discountCodeList = new ArrayList<>();
        notifications = new ArrayList<>();
        cart = new Cart();
        wallet = new Wallet();
        this.setRole(Role.Customer);
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Order> getOrder() {
        return orderList;
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void addProduct(Product product) {
        Cart newCart = this.getCart();
        newCart.addProductToMap(product);
        this.setCart(newCart);
    }

    public void displayCart() {
        Cart newCart = this.getCart();
        newCart.displayCart(this);
    }

    public List<DiscountCode> getDiscountCodeList() {
        return discountCodeList;
    }

    public void addToDiscountCodeList(DiscountCode discountCode) {
        discountCodeList.add(discountCode);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public VendiloPlusAccount getVendiloPlusAccount() {
        return vendiloPlusAccount;
    }

    public void setVendiloPlusAccount(VendiloPlusAccount vendiloPlusAccount) {
        this.vendiloPlusAccount = vendiloPlusAccount;
    }

    @Override
    public String toString() {
        return String.format(
                "%s\n" +
                        "%sAddress         :%s %d Address%s\n" +
                        "%sWallet Balance  :%s %.2f\n" +
                        "%sVendilo Plus    :%s %s",
                super.toString(),
                YELLOW, RESET, addressList.size(), (addressList.size() == 1 || addressList.isEmpty() ? "" : "es"),
                YELLOW, RESET, wallet.getWalletBalance(),
                YELLOW, RESET, vendiloPlusAccount.getIsActive() ? "Yes" : "No"
        );
    }

}
