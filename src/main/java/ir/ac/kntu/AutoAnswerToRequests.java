package ir.ac.kntu;

import ir.ac.kntu.util.Calendar;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class AutoAnswerToRequests {
    private static AutoAnswerToRequests autoAnswer;

    public static AutoAnswerToRequests getInstance() {
        return autoAnswer;
    }

    public void autoAnswer(Person person) {
        List<Request> requests = DataBase.getInstance().getRequestList();
        for (Request request : requests) {
            if (person instanceof Customer) {
                if (!(request instanceof CustomerRequest)) {
                    continue;
                }
            } else if (person instanceof Seller) {
                if (!(request instanceof SellerRequest)) {
                    continue;
                }
            }
            if (request.getIsChecked() && !requestExpired(request)) {
                continue;
            }
            request.setDescription("Our Colleagues Will Contact You As Soon As Possible. Thank You For Your Patient.");
            request.setIsChecked(true);
        }
    }

    public boolean requestExpired(Request request) {
        Instant requestTime = request.getTimestamp();
        Instant now = Calendar.now();
        Duration duration = Duration.between(requestTime, now);
        return duration.toDays() > 2;
    }
}
