package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;

public class CustomerRequest extends Request implements Serializable {
    final private String requestTitle;
    final private String serialNumber;
    final private String customerPhone;
    private String status;

    public CustomerRequest(String requestTitle, String status, Instant requestTime, String description, String serialNumber, String customerPhone) {
        super(requestTime, description);
        this.requestTitle = requestTitle;
        this.status = status;
        this.serialNumber = serialNumber;
        this.customerPhone = customerPhone;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }
}
