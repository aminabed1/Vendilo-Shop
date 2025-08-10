package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public class SellerOrder extends Order implements Serializable {
    private final String sellerAgencyCode;
    private final double sellerBenefit;
    private final String description;
    private final Map<Product, Integer> sellerProductMap = new HashMap<>();

    public SellerOrder(String sellerAgencyCode, Instant orderDate, double sellerBenefit, Address deliveryAddress, String customerEmail, String description, Map<Product, Integer> sellerProductMap) {
        super(orderDate, customerEmail, deliveryAddress);
        this.sellerAgencyCode = sellerAgencyCode;
        this.sellerBenefit = sellerBenefit;
        this.description = description;
        this.sellerProductMap.putAll(sellerProductMap);
    }
    public String getSellerAgencyCode() {
        return sellerAgencyCode;
    }

    public double getSellerBenefit() {
        return sellerBenefit;
    }

    public String getTransactionDescription() {
        return description;
    }

    public Map<Product, Integer> getSellerProductMap() {
        return sellerProductMap;
    }
}
