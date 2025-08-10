package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;

public class CustomerRequest extends Request implements Serializable {
    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String SUCCESS = "\u001B[38;5;46m";

    final private RequestTitle requestTitle;
    final private String serialNumber;
    final private String customerPhone;
    private String status;

    public CustomerRequest(RequestTitle requestTitle, String status, Instant requestTime, String description, String serialNumber, String customerPhone) {
        super(requestTime, description);
        this.requestTitle = requestTitle;
        this.status = status;
        this.serialNumber = serialNumber;
        this.customerPhone = customerPhone;
    }

    public RequestTitle getRequestTitle() {
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
    
    @Override
    public String toString() {
        return  TITLE + "════════════ REQUEST DETAILS ════════════" + RESET + "\n" + 
            OPTION + "Title: " + RESET + requestTitle + "\n" + 
            OPTION + "Status: " + RESET + ("unchecked".equals(getStatus()) ? ERROR : SUCCESS) + status + RESET + "\n" +
            OPTION + "Date: " + RESET + getTimestamp().toString() + "\n" +
            OPTION + "Serial Number: " + RESET + serialNumber + "\n" + 
            OPTION + "Your Phone: " + RESET + customerPhone + "\n" +
            OPTION + "Description: " + RESET + "\n" + getDescription();
    }
}
