package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NotificationManage {
    private final static Scanner scan = new Scanner(System.in);
    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static boolean continueState = false;

    public static NotificationManage getInstance() {
        return new NotificationManage();
    }

    public void notificationTab(Customer customer) {
        continueState = true;
        while (continueState) {
            notificationTabHeader();
            notificationTabMenu(customer);
        }
    }

    public void notificationTabMenu(Customer customer) {
        while (true) {
            List<Notification> unseenNotifications = getUnseenNotificationsList(customer);
            if (unseenNotifications.isEmpty()) {
                System.out.println("No New Notification");
            } else {
                System.out.printf(" %d New Notification%s\n", unseenNotifications.size(), (unseenNotifications.size() == 1 ? "" : "s"));

            }
            System.out.println("1. Display Notification List");
            System.out.println("0. Back");
            String choice = scan.nextLine();
            switch (choice) {
                case "1" -> {
                    notificationTabChoiceHandle(unseenNotifications);
                }
                case "0" -> {
                    return;
                }
                default -> {
                    SystemMessage.printMessage("Enter a valid choice", MessageTitle.Error);
                }
            }
        }
    }

    public List<Notification> getUnseenNotificationsList(Customer customer) {
        int unseenNotifs = 0;
        List<Notification> notificationListTemp = new ArrayList<>();
        List<Notification> notificationList = customer.getNotifications();
        notificationList.addAll(DataBase.getInstance().getNotificationList());
        for (Notification notification : notificationList) {
            if (notificationList.isEmpty()) {
                return null;
            }
            if (notification.getChargedProduct() != null && notification.getChargedProduct().getStock() > 0 && notification.getUnVisible()) {
                notification.setIsVisible(true);
            }
            if (notification.getUnVisible()) {
                continue;
            }
            notificationListTemp.add(notification);
            if (!notification.isNotificationSeen()) {
                unseenNotifs++;
            }
        }
        if (unseenNotifs == 0) {
            return null;
        }
        return notificationListTemp;
    }

    public void notificationTabChoiceHandle(List<Notification> notifications) {
        System.out.println("Enter Your Choice :");
        String choice = scan.nextLine();
        if (!choice.matches("\\d+")) {
            System.out.println("Please Enter Numeric Choice.");
            return;
        }
        int choiceValue = Integer.parseInt(choice);
        if (choiceValue != 0 && choiceValue != 1) {
            System.out.println("Please Enter Valid Choice, (0\1).");
        }

        if (choiceValue == 0) {
            continueState = false;
            return;
        }

        if (notifications.isEmpty()) {
            System.out.println("You Don't Have Any Notification Yet");
            return;
        }

        displayNotificationList(notifications);
        System.out.println("Select A Notification (By Index) Or Back (Enter 0)");
        choice = scan.nextLine();
        if (!choice.matches("\\d+")) {
            System.out.println("Invalid Entered Value, Returning...");
            Pause.pause(2000);
            return;
        }

        choiceValue = Integer.parseInt(choice);
        if (choiceValue == 0) {
            return;
        }

        if (choiceValue < 0 || choiceValue > notifications.size()) {
            System.out.println("Invalid Index, Returning...");
            Pause.pause(2000);
            return;
        }

        System.out.println(notifications.get(choiceValue - 1));
        changeNotificationStatus(notifications.get(choiceValue - 1));
        System.out.println("Press Any Key To Continue...");
        scan.next();
    }

    public void changeNotificationStatus(Notification notification) {
        notification.setNotificationSeen(true);
    }

    public void displayNotificationList(List<Notification> notifications) {
        int counter = 1;
        for (Notification notification : notifications) {
            System.out.println(counter + ". Title : " + notification.getTopic().name().replace("_", " ") + "Status : " + (notification.isNotificationSeen() ? "seen" : "unseen"));
        }
    }

    public void notificationTabHeader() {
        System.out.println(TITLE + "=========================================");
        System.out.println("|                                       |");
        System.out.println("|             NOTIFICATIONS             |");
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);
        System.out.println();
    }
}
