package ir.ac.kntu;

import java.util.*;

public class Customer extends Person{
    private List<Address> addressList;
    private List<String> ratedProductsList;
    private double wallet;
    private Cart cart;
    private List<Order> orderList;

    public Customer(String name, String surname, String phoneNumber, String email, String username,
                    String password) {
        super(name, surname, phoneNumber, email, username, password);
        orderList = new ArrayList<>();
        addressList = new ArrayList<>();
        ratedProductsList = new ArrayList<>();
        cart = new Cart();
        this.setRole("Customer");
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public List<String> getRatedProductsList() {
        return ratedProductsList;
    }

    public void setRatedProductsList(List<String> ratedProductsList) {
        this.ratedProductsList = ratedProductsList;
    }

    public double getWalletBalance() {
        return wallet;
    }

    public void setWalletBalance(double wallet) {
        this.wallet = wallet;
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

    public void addProduct(Product product) {
        Cart newCart = this.getCart();
        newCart.addProduct(product);
        this.setCart(newCart);
    }

    public void displayCart() {
        Cart newCart = this.getCart();
        newCart.displayCart(this);
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
