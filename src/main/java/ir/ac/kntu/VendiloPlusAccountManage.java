package ir.ac.kntu;

import ir.ac.kntu.util.Calendar;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class VendiloPlusAccountManage {
    private final static Scanner scan = new Scanner(System.in);
    private static boolean managerLogin = false;

    public static VendiloPlusAccountManage getInstance() {
        return new VendiloPlusAccountManage();
    }

    public void vendiloPlusTab(Customer customer, boolean managerLogin) {
        if (managerLogin) {
            VendiloPlusAccountManage.managerLogin = true;
        }
        while (true) {
            System.out.println("1. Account Details\n2. Buy Account\n3. Back\nYour choice: ");
            String choice = scan.nextLine();
            switch (choice) {
                case "1":
                    checkRemainingDays(customer);
                    System.out.println(customer.getVendiloPlus());
                    System.out.println("Press any key to back");
                    scan.nextLine();
                    break;
                case "2":
                    getPlusAccount(customer);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Enter 1, 2 or 3");
            }
        }
    }

    public void checkRemainingDays(Customer customer) {
        Instant endDate = customer.getVendiloPlus().getEndDate();
        long remainingDays = ChronoUnit.DAYS.between(Calendar.now(), endDate);
        if (remainingDays <= 0) {
            customer.getVendiloPlus().setIsActive(false);
        }
    }

    public void getPlusAccount(Customer customer) {
        while (true) {
            String choice = getSubscriptionChoice();
            if ("4".equals(choice)) {
                return;
            }
            double subscriptionCost;
            Instant startDate = Instant.now();
            Instant endDate = calculateEndDate(choice);
            subscriptionCost = getSubscriptionCost(choice);
            if (endDate == null || subscriptionCost == -1) {
                SystemMessage.printMessage("Enter A Numeric Value", MessageTitle.Error);
                Pause.pause(2000);
                continue;
            }
            if (managerLogin) {
                activatePremiumAccount(customer, startDate, endDate);
                return;
            }
            double balance = customer.getWallet().getWalletBalance();
            if (subscriptionCost > balance) {
                System.out.println("Insufficient Balance, Returning...");
                Pause.pause(2000);
                return;
            }
            customer.getWallet().setWalletBalance(balance - subscriptionCost, "Buy Premium Account");
            activatePremiumAccount(customer, startDate, endDate);
        }
    }

    private String getSubscriptionChoice() {
        System.out.println("Choose One Subscription :");
        System.out.println("1.  1-Month");
        System.out.println("2.  3-Month");
        System.out.println("3. 12-Month");
        System.out.println("4. Back");
        return scan.nextLine();
    }

    private Instant calculateEndDate(String choice) {
        return switch (choice) {
            case "1" -> ZonedDateTime.now().plusMonths(1).toInstant();
            case "2" -> ZonedDateTime.now().plusMonths(3).toInstant();
            case "3" -> ZonedDateTime.now().plusMonths(12).toInstant();
            default -> null;
        };
    }

    private double getSubscriptionCost(String choice) {
        return switch (choice) {
            case "1" -> 10;
            case "2" -> 25;
            case "3" -> 100;
            default -> -1;
        };
    }

    private void activatePremiumAccount(Customer customer, Instant start, Instant end) {
        customer.getVendiloPlus().setPremiumAccountDateActive(start, end);
        SystemMessage.printMessage("Vendilo Plus Account Is Active Now. You Can See It's Details.", MessageTitle.Success);
        Pause.pause(2000);
    }
}
