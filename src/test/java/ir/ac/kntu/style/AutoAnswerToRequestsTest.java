package ir.ac.kntu.style;

import ir.ac.kntu.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AutoAnswerToRequestsTest {

    private final AutoAnswerToRequests autoAnswer = AutoAnswerToRequests.getInstance();

    @Test
    void testRequestExpiredTrue() {
        CustomerRequest request = new CustomerRequest(RequestTitle.Products_quality, Instant.now().minusSeconds(3 * 24 * 3600), null, "SN1234", "0912000000");
        assertTrue(autoAnswer.requestExpired(request));
    }

    @Test
    void testRequestExpiredFalse() {
        CustomerRequest request = new CustomerRequest(RequestTitle.Sellers_Authentication_Requests, Instant.now().minusSeconds(1 * 24 * 3600), null, "SN5678", "0912000001");
        assertFalse(autoAnswer.requestExpired(request));
    }

    @Test
    void testAutoAnswerUpdatesExpiredRequest() {
        Customer amin = new Customer("Amin", "Ahmadi", "0912000000", "amin@test.com", "1234");
        CustomerRequest request = new CustomerRequest(RequestTitle.Inconsistency_In_The_Sent_Order, Instant.now().minusSeconds(3 * 24 * 3600), null, "SN1111", amin.getPhoneNumber());
        request.setIsChecked(false);
        DataBase.getInstance().getRequestList().add(request);
        autoAnswer.autoAnswer(amin);
        assertTrue(request.getIsChecked());
        assertEquals("Our Colleagues Will Contact You As Soon As Possible. Thank You For Your Patient.", request.getDescription());
    }

    @Test
    void testAutoAnswerIgnoresNonExpiredRequest() {
        Customer amin = new Customer("Amin", "Ahmadi", "0912000000", "amin@test.com", "1234");
        CustomerRequest request = new CustomerRequest(RequestTitle.Sellers_Authentication_Requests, Instant.now().minusSeconds( 24 * 3600), null, "SN2222", amin.getPhoneNumber());
        request.setIsChecked(false);
        autoAnswer.autoAnswer(amin);
        assertFalse(request.getIsChecked());
        assertNull(request.getDescription());
    }
}
