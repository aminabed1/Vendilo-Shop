package ir.ac.kntu;

import java.time.Instant;

public abstract class Request {
    private String description;
    private final Instant timestamp;
    private boolean isChecked = false;

    public Request(Instant timestamp, String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getTimestamp() {
        return timestamp;
    }


    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
