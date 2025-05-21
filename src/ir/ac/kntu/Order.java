package ir.ac.kntu;

import java.time.Instant;
import java.util.List;

public class Order {
    private List<Product> productList;
    private Instant orderDate;
    private List<String> sellersAgencyCode;
    private Address deliveryAddress;

    public Order(List<Product> productList, Instant orderDate,List<String> sellersAgencyCode, Address deliveryAddress) {
        this.productList = productList;
        this.orderDate = orderDate;
        this.sellersAgencyCode = sellersAgencyCode;
        this.deliveryAddress = deliveryAddress;
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


}

