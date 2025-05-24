package ir.ac.kntu;

import java.time.Instant;
import java.util.List;

public class Order {
    private List<Product> productList;
    private Instant orderDate;
    private List<String> sellersAgencyCode;
    private String customerEmail;
    private Address deliveryAddress;
    final private double postingPrice;
    final private double totalPrice;

    public Order(List<Product> productList, Instant orderDate,List<String> sellersAgencyCode, String customerEmail,Address deliveryAddress, double totalPrice, double postingPrice) {
        this.productList = productList;
        this.orderDate = orderDate;
        this.sellersAgencyCode = sellersAgencyCode;
        this.customerEmail = customerEmail;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
        this.postingPrice = postingPrice;

    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
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

    public void setShopName(List<String> sellersAgencyCode) {
        this.sellersAgencyCode = sellersAgencyCode;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getPostingPrice() {
        return postingPrice;
    }
}

