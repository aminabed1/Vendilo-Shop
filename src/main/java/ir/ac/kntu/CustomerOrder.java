package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class CustomerOrder extends Order implements Serializable {
    private final List<String> sellersAgencyCode;
    private final double totalPrice;
    private final Map<Product, Integer> productMap;
    private String customerEmail;

    public CustomerOrder(Instant orderDate, List<String> sellersAgencyCode, String customerEmail,
                         Address deliveryAddress, double totalPrice, Map<Product, Integer> productMap) {
        super(orderDate, customerEmail, deliveryAddress);
        this.sellersAgencyCode = sellersAgencyCode;
        this.customerEmail = customerEmail;
        this.totalPrice = totalPrice;
        this.productMap = productMap;
    }

    public List<String> getSellersAgencyCode() {
        return sellersAgencyCode;
    }

    @Override
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Map<Product, Integer> getProductMap() {
        return productMap;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
