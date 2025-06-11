package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;

public class Order implements Serializable {
    private final Instant orderDate;
    private String customerEmail;
    private Address deliveryAddress;

    public Order(Instant orderDate, String customerEmail, Address deliveryAddress) {
        this.orderDate = orderDate;
        this.customerEmail = customerEmail;
        this.deliveryAddress = deliveryAddress;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}

