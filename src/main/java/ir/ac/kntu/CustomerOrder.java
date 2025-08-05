package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public class CustomerOrder extends Order implements Serializable {
    private final List<String> sellersAgencyCode;
    private final double totalPrice;
    private final HashMap<Product, Integer> productMap;
    private String customerEmail;

    public CustomerOrder(Instant orderDate, List<String> sellersAgencyCode, String customerEmail,
                         Address deliveryAddress, double totalPrice, HashMap<Product, Integer> productMap) {
        super(orderDate, customerEmail, deliveryAddress);
        this.sellersAgencyCode = sellersAgencyCode;
        this.customerEmail = customerEmail;
        this.totalPrice = totalPrice;
        this.productMap = productMap;
    }

    public List<String> getSellersAgencyCode() {
        return sellersAgencyCode;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public HashMap<Product, Integer> getProductMap() {
        return productMap;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
