package ir.ac.kntu;

import ir.ac.kntu.util.Calendar;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class VendiloPlusAccountManage {
    public static Scanner scan = new Scanner(System.in);
    private static boolean managerLogin = false;

    public static VendiloPlusAccountManage getInstance() {
        return new VendiloPlusAccountManage();
    }

    public void vendiloPlusTab(Customer customer, boolean managerLogin) {
        if (managerLogin) {
            VendiloPlusAccountManage.managerLogin = true;
        }
        while (true) {
            System.out.println("1. Vendilo Plus Account Details");
            System.out.println("2. Buy Vendilo Plus Account");
            System.out.println("3. Back");
            System.out.println("Your Choice : ");
            String choice = scan.nextLine();
            try {
                Integer.parseInt(choice);
            } catch (NumberFormatException exeption) {
                System.out.println("Please Enter Numeric Value");
                continue;
            }
            switch(choice) {
                case "1":
                    checkRemainingDays(customer);
                    System.out.println(customer.getVendiloPlusAccount());
                    System.out.println("Press Any Key To Back");
                    scan.nextLine();
                    break;
                case "2":
                    getPlusAccount(customer);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid Choice. Try Again");
                    Pause.pause(1500);
            }
        }
    }

    public void checkRemainingDays(Customer customer) {
        Instant endDate = customer.getVendiloPlusAccount().getEndDate();
        long remainingDays = ChronoUnit.DAYS.between(Calendar.now(), endDate);
        if (remainingDays <= 0) {
            customer.getVendiloPlusAccount().setIsActive(false);
        }
    }

    public void getPlusAccount(Customer customer) {
        while (true) {
            System.out.println("Choose One Subscription :");
            System.out.println("1.  1-Month");
            System.out.println("2.  3-Month");
            System.out.println("3. 12-Month");
            System.out.println("4. Back");
            String choice = scan.nextLine();

            double balance = customer.getWallet().getWalletBalance();
            double subscriptionCost;
            Instant startDateTemp = Instant.now();
            Instant endDateTemp;

            switch (choice) {
                case "1" -> {
                    endDateTemp = ZonedDateTime.now().plusMonths(1).toInstant();
                    subscriptionCost = 10;
                }
                case "2" -> {
                    endDateTemp = ZonedDateTime.now().plusMonths(3).toInstant();
                    subscriptionCost = 25;
                }
                case "3" -> {
                    endDateTemp = ZonedDateTime.now().plusMonths(12).toInstant();
                    subscriptionCost = 100;
                }
                case "4" -> {
                    return;
                }
                default -> {
                    SystemMessage.printMessage("Enter A Numeric Value", MessageTitle.Error);
                    Pause.pause(2000);
                    continue;
                }
            }
            if (managerLogin) {
                customer.getVendiloPlusAccount().setPremiumAccountDateActive(startDateTemp, endDateTemp);
                SystemMessage.printMessage("Vendilo Plus Account Is Active Now. You Can See It's Details.", MessageTitle.Success);
                Pause.pause(2000);
                return;
            }

            if (subscriptionCost > balance) {
                System.out.println("Insufficient Balance, Returning...");
                Pause.pause(2000);
                return;
            }

            double newBalance = customer.getWallet().getWalletBalance() - subscriptionCost;
            customer.getWallet().setWalletBalance(newBalance, "Buy Premium Account");
            customer.getVendiloPlusAccount().setPremiumAccountDateActive(startDateTemp, endDateTemp);
            SystemMessage.printMessage("Vendilo Plus Account Is Active Now. You Can See It's Details.", MessageTitle.Success);
            Pause.pause(2000);
        }
    }
}
