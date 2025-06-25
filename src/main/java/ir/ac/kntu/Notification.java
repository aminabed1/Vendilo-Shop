package ir.ac.kntu;

public class Notification {
    private boolean notificationSeen = false;
    private String description = null;
    private Request request = null;
    private Product chargedProduct = null;
    private DiscountCode discountCode = null;
    private NotificationTopic topic = null;
    private boolean isVisible = true;

    public Notification(String description) {//General Message
        this.description = description;
        topic = NotificationTopic.GENERAL_MESSAGE;
    }

    public Notification(Request request) {//Request Notif
        this.request = request;
        topic = NotificationTopic.REQUEST_CHECK_RESULT;
    }

    public Notification(Product chargedProduct) {//Product notif
        this.chargedProduct = chargedProduct;
        topic = NotificationTopic.PRODUCT_QUANTITY_CHARGE;
        isVisible = false;
    }   

    public Notification(DiscountCode discountCode) {//Discount Code notif
        this.discountCode = discountCode;
        topic = NotificationTopic.NEW_DISCOUNT_CODE;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountCode getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(DiscountCode discountCode) {
        this.discountCode = discountCode;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Product getChargedProduct() {
        return chargedProduct;
    }

    public void setChargedProductSerialNumber(Product chargedProduct) {
        this.chargedProduct = chargedProduct;
    }

    public boolean isNotificationSeen() {
        return notificationSeen;
    }

    public void setNotificationSeen(boolean notificationSeen) {
        this.notificationSeen = notificationSeen;
    }

    public NotificationTopic getTopic() {
        return topic;
    }

    public void setTopic(NotificationTopic topic) {
        this.topic = topic;
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public String toString() {
        String printFormat = topic.name() + "\n";
        if (description != null) {
            printFormat += description;
        } else if (request != null) {
            printFormat += request.toString();
        } else if (chargedProduct != null) {
            printFormat += chargedProduct.toString();
        } else {
            printFormat += discountCode.toString();
        }
        return printFormat;
    }
}
