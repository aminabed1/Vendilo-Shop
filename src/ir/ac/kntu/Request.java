package ir.ac.kntu;

import java.time.Instant;
import java.util.List;

public class Request {
    private String sellerAgencyCode;
    private String description;
    private Instant timestamp;
    private boolean isChecked = false;

    public Request(String sellerAgencyCode, String description) {
        this.sellerAgencyCode = sellerAgencyCode;
        this.description = description;
        this.timestamp = Instant.now();
        DataBase.addRequest(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellerAgencyCode() {
        return sellerAgencyCode;
    }

    public void setSellerAgencyCode(String sellerAgencyCode) {
        this.sellerAgencyCode = sellerAgencyCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
