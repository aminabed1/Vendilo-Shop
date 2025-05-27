package ir.ac.kntu;

import java.time.Instant;

public class CustomerRequest extends Request {
    final private String requestTitle;
    final private String status;

    public CustomerRequest(String requestTitle, String status, Instant requestTime, String description) {
        super(requestTime, description);
        this.requestTitle = requestTitle;
        this.status = status;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public String getStatus() {
        return status;
    }
}
