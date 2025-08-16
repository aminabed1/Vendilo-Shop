package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NotificationManage {
    private final static Scanner scan = new Scanner(System.in);
    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static boolean continueState = false;
    private static int unseenNotifications;

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
            List<Notification> newNotifications = getNotificationsList(customer);
            if (unseenNotifications == 0) {
                SystemMessage.printMessage("No New Notification", MessageTitle.Info);
            } else {
                String formatted = String.format(" %d New Notification%s\n", newNotifications.size(), (newNotifications.size() == 1 ? "" : "s"));
                SystemMessage.printMessage(formatted, MessageTitle.Info);
            }
            System.out.println("1. Display Notification List");
            System.out.println("0. Back");
            String choice = scan.nextLine();
            switch (choice) {
                case "1" -> {
                    notificationTabChoiceHandle(newNotifications);
                }
                case "0" -> {
                    continueState = false;
                    return;
                }
                default -> {
                    SystemMessage.printMessage("Enter a valid choice", MessageTitle.Error);
                }
            }
        }
    }

    public List<Notification> getNotificationsList(Customer customer) {
        unseenNotifications = 0;
        List<Notification> tempList = new ArrayList<>();
        List<Notification> notificationList = customer.getNotifications();
        notificationList.addAll(DataBase.getInstance().getNotificationList());
        for (Notification notification : notificationList) {
            if (notificationList.isEmpty()) {
                return tempList;
            }
            if (notification.getChargedProduct() != null && notification.getChargedProduct().getStock() > 0 && notification.getUnVisible()) {
                notification.setIsVisible(true);
            }
            if (notification.getUnVisible()) {
                continue;
            }
            tempList.add(notification);
            if (!notification.isNotificationSeen()) {
                unseenNotifications++;
            }
        }
        return tempList;
    }

    public void notificationTabChoiceHandle(List<Notification> notifications) {
        displayNotificationList(notifications);
        if (!handleNotificationSelection(notifications)) {
            return;
        }
    }

    private boolean handleNotificationSelection(List<Notification> notifications) {
        System.out.println("Select A Notification (By Index) Or Back (Enter 0)");
        String choice = scan.nextLine();
        if (!choice.matches("\\d+")) {
            System.out.println("Invalid Entered Value, Returning...");
            Pause.pause(2000);
            return false;
        }
        int choiceValue = Integer.parseInt(choice);
        if (choiceValue == 0) {
            return false;
        }
        if (choiceValue < 0 || choiceValue > notifications.size()) {
            System.out.println("Invalid Index, Returning...");
            Pause.pause(2000);
            return false;
        }
        System.out.println(notifications.get(choiceValue - 1));
        changeNotificationStatus(notifications.get(choiceValue - 1));
        System.out.println("Press Any Key To Continue...");
        scan.nextLine();
        return true;
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
