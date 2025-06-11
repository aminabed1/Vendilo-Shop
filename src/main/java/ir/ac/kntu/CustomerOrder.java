package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

public class CustomerOrder extends Order implements Serializable {
    private List<String> sellersAgencyCode;
    private String customerEmail;
    private double postingPrice;
    private double totalPrice;
    private HashMap<Product, Integer> productMap = new HashMap<>();

    public CustomerOrder(Instant orderDate, List<String> sellersAgencyCode, String customerEmail,
                         Address deliveryAddress, double postingPrice, double totalPrice, HashMap<Product, Integer> productMap) {
        super(orderDate, customerEmail, deliveryAddress);
        this.sellersAgencyCode = sellersAgencyCode;
        this.customerEmail = customerEmail;
        this.postingPrice = postingPrice;
        this.totalPrice = totalPrice;
        this.productMap = productMap;
    }

    public List<String> getSellersAgencyCode() {
        return sellersAgencyCode;
    }

    public void setSellersAgencyCode(List<String> sellersAgencyCode) {
        this.sellersAgencyCode = sellersAgencyCode;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public double getPostingPrice() {
        return postingPrice;
    }

    public void setPostingPrice(double postingPrice) {
        this.postingPrice = postingPrice;
    }

    public HashMap<Product, Integer> getProductMap() {
        return productMap;
    }

    public void setProductMap(HashMap<Product, Integer> productMap) {
        this.productMap = productMap;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
