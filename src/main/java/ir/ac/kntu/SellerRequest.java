package ir.ac.kntu;

import java.io.Serializable;
import java.time.Instant;

public class SellerRequest extends Request implements Serializable {
    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String MENU = "\u001B[38;5;39m";

    final private String sellerAgencyCode;

    public SellerRequest(String sellerAgencyCode, String description, Instant requestTime) {
        super(requestTime, description);
        this.sellerAgencyCode = sellerAgencyCode;
    }

    public String getSellerAgencyCode() {
        return sellerAgencyCode;
    }

    @Override
    public String toString() {
        return  TITLE + "════════════ REQUEST DETAILS ════════════" + RESET + "\n" + 
            MENU + "Status: " + (getIsChecked() ? SUCCESS + "Checked" : ERROR + "Unchecked") + RESET + "\n" + 
            OPTION + "\nDescription:\n" + getDescription() + RESET;
    }

}
