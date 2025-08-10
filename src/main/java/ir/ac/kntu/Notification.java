package ir.ac.kntu;

public class Notification {
    private final NotificationTopic topic;
    private boolean notificationSeen = false;
    private String description = null;
    private Request request = null;
    private Product chargedProduct = null;
    private DiscountCode discountCode = null;
    private boolean isVisible = true;

    public Notification(String description) {//General Message
        this.description = description;
        topic = NotificationTopic.General_Message;
    }

    public Notification(Request request) {//Request Notif
        this.request = request;
        topic = NotificationTopic.Request_Check_Result;
    }

    public Notification(Product chargedProduct) {//Product notif
        this.chargedProduct = chargedProduct;
        topic = NotificationTopic.Product_Quantity_Charge;
        isVisible = false;
    }   

    public Notification(DiscountCode discountCode) {//Discount Code notif
        this.discountCode = discountCode;
        topic = NotificationTopic.New_Discount_Code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product getChargedProduct() {
        return chargedProduct;
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

    public boolean getUnVisible() {
        return !isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(topic.name() + "\n");
        if (description != null) {
            stringBuilder.append(description);
        } else if (request != null) {
            stringBuilder.append(request);
        } else if (chargedProduct != null) {
            stringBuilder.append(chargedProduct);
        } else {
            stringBuilder.append(discountCode.toString());
        }
        return stringBuilder.toString();
    }
}
