package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;

public class Transaction implements Serializable {
    private final Instant timestamp;
    private double amount;

    public Transaction(Instant timestamp, double amount) {
        this.timestamp = timestamp;
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
