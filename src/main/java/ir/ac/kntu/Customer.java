package ir.ac.kntu;

import java.io.Serializable;
import java.util.*;

public class Customer extends Person implements Serializable {
    private Cart cart;
    private Wallet wallet;
    private List<Order> orderList;
    private List<Address> addressList;
    private List<String> ratedProductsList;
    private List<Notification> notifications;
    private List<DiscountCode> discountCodeList;
    private VendiloPlusAccount vendiloPlusAccount;

    public Customer(String name, String surname, String phoneNumber, String email, String username,
                    String password) {
        super(name, surname, phoneNumber, email, username, password);
        //TODO Cunstructor
        vendiloPlusAccount = new VendiloPlusAccount();
        orderList = new ArrayList<>();
        addressList = new ArrayList<>();
        ratedProductsList = new ArrayList<>();
        discountCodeList = new ArrayList<>();
        notifications = new ArrayList<>();
        cart = new Cart();
        wallet = new Wallet();
        this.setRole("Customer");
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
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }

        Customer customer = (Customer) object;

        return this.getName().equals(customer.getName()) &&
                this.getSurname().equals(customer.getSurname() )&&
                this.getPhoneNumber().equals(customer.getPhoneNumber()) &&
                this.getEmail().equals(customer.getEmail()) &&
                this.getUsername().equals(customer.getUsername()) &&
                this.getPassword().equals(customer.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getPhoneNumber(), getEmail(),
                            getUsername(), getPassword());
    }
}
