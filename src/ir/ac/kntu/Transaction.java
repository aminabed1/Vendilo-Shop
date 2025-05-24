package ir.ac.kntu;

import java.time.Instant;
import java.util.*;
public class Transaction {
    private final Instant timestamp;
    private double amount;
    private Order order;

    public Transaction(Instant timestamp, double amount, Order order) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.order = order;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
