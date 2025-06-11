package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public class SellerOrder extends Order implements Serializable {
    private final double sellerBenefit;
    private final String transactionDescription;
    private final Map<Product, Integer> sellerProductMap = new HashMap<>();

    public SellerOrder(Instant orderDate, double sellerBenefit, Address deliveryAddress, String customerEmail, String transactionDescription, Map<Product, Integer> sellerProductMap) {
        super(orderDate, customerEmail, deliveryAddress);
        this.sellerBenefit = sellerBenefit;
        this.transactionDescription = transactionDescription;
        this.sellerProductMap.putAll(sellerProductMap);
    }

    public double getSellerBenefit() {
        return sellerBenefit;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public Map<Product, Integer> getSellerProductMap() {
        return sellerProductMap;
    }
}
