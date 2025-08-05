package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;

public class Order implements Serializable {
    private final Instant orderDate;
    private final String customerEmail;
    private final Address deliveryAddress;

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

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

}

