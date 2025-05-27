package ir.ac.kntu;

import java.time.Instant;

public class SellerRequest extends Request {
    final private String sellerAgencyCode;

    public SellerRequest(String sellerAgencyCode, String description, Instant requestTime) {
        super(requestTime, description);
        this.sellerAgencyCode = sellerAgencyCode;
    }

    public String getSellerAgencyCode() {
        return sellerAgencyCode;
    }

}
