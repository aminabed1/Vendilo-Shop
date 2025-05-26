package ir.ac.kntu;

import java.time.Instant;
import java.util.List;

public class Order {
    private List<Product> productList;
    private Instant orderDate;
    private List<String> sellersAgencyCode;
    private String customerEmail;
    private Address deliveryAddress;
    private double postingPrice = 0;
    private double totalPrice = 0;
    private double productPrice = 0;
    private Product product;



    private String transactionDescription;

    public Order(List<Product> productList, Instant orderDate,List<String> sellersAgencyCode, String customerEmail,Address deliveryAddress, double totalPrice, double postingPrice) {
        this.productList = productList;
        this.orderDate = orderDate;
        this.sellersAgencyCode = sellersAgencyCode;
        this.customerEmail = customerEmail;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
        this.postingPrice = postingPrice;

    }

    public Order (Instant orderDate, double price, String description) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.productPrice = price;
        this.transactionDescription = description;
    }

    public Order(Product product, Instant orderDate, double price,
                 Address deliveryAddress, String customerEmail) {
        this.productList = List.of(product);
        this.orderDate = orderDate;
        this.totalPrice = price;
        this.productPrice = price;
        this.deliveryAddress = deliveryAddress;
        this.customerEmail = customerEmail;
        this.sellersAgencyCode = List.of(product.getSellerAgencyCode());
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

    public void setPostingPrice(double postingPrice) {
        this.postingPrice = postingPrice;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

