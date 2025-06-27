package ir.ac.kntu;

import java.time.Instant;

public class TransactionWithoutOrder extends Transaction{
    private String description;

    public TransactionWithoutOrder(Instant timestamp, double amount, String description) {
        super(timestamp, amount);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
