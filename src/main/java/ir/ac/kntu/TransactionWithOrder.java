package ir.ac.kntu;

import java.time.Instant;

public class TransactionWithOrder extends Transaction {
    private Order order;

    public TransactionWithOrder(Instant timestamp, double amount, Order order) {
        super(timestamp, amount);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
