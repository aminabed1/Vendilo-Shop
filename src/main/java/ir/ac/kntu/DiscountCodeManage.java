package ir.ac.kntu;

import java.util.List;
import java.util.Scanner;

public class DiscountCodeManage {

    private static final Scanner scanner = new Scanner(System.in);
    private static Customer mainCustomer = null;

    public static DiscountCodeManage getInstance() {
        return new DiscountCodeManage();
    }

    public void handleDiscountCodePanel(Customer customer) {
        List<DiscountCode> discountCodeList = customer.getDiscountCodeList();
        if (discountCodeList.isEmpty()) {
            SystemMessage.printMessage("No discount codes found\n", MessageTitle.Error);
            return;
        }

        System.out.println("\n--- Discount Codes ---");
        for (int i = 0; i < discountCodeList.size(); i++) {
            DiscountCode discountCode = discountCodeList.get(i);
            String codeCategory = discountCode.getClass().getSimpleName();
            System.out.printf("%2d. Code: %-10s | Category: %s%n", i + 1, discountCode.getCode(), codeCategory);
        }
        System.out.println("------------------------\n");

        handleDiscountMenuSelection(discountCodeList);
    }

    public void handleDiscountMenuSelection(List<DiscountCode> discountCodeList) {
        System.out.print("Select discount code by index: ");
        String choice = scanner.nextLine().trim();

        if (!choice.matches("\\d+")) {
            SystemMessage.printMessage("Please enter a valid index", MessageTitle.Error);
            return;
        }

        int selectedIndex = Integer.parseInt(choice);
        if (selectedIndex <= 0 || selectedIndex > discountCodeList.size()) {
            SystemMessage.printMessage("Please enter a valid index", MessageTitle.Error);
            return;
        }

        System.out.println("\nSelected Code Info:");
        System.out.println(discountCodeList.get(selectedIndex - 1));
        System.out.println("\nPress Enter to go back...");
        scanner.nextLine();
    }

    public void generateDiscountCode() {
        while (true) {
            System.out.println("\n--- Generate Discount Code ---");
            System.out.println("1. General Discount Code");
            System.out.println("2. Private Discount Code");
            System.out.println("0. Back");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> objectReceiver("General");
                case "2" -> {
                    System.out.print("Enter Customer Phone Number: ");
                    String phoneNumber = scanner.nextLine();
                    mainCustomer = getCustomerByPhoneNumber(phoneNumber);
                    if (mainCustomer == null) {
                        System.out.println("Customer Not Found");
                        continue;
                    }
                    objectReceiver(mainCustomer);
                }
                case "0" -> { return; }
                default -> SystemMessage.printMessage("Enter Valid Number", MessageTitle.Error);
            }
        }
    }

    private Customer getCustomerByPhoneNumber(String phoneNumber) {
        for (Person person : DataBase.getInstance().getPersonList()) {
            if (person instanceof Customer customer && customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }
        return null;
    }

    private void objectReceiver(Object obj) {
        if (obj instanceof Customer customer) {
            codeCategoryOption(customer.getPhoneNumber());
        } else {
            codeCategoryOption("General");
        }
    }

    private void codeCategoryOption(String purpose) {
        while (true) {
            System.out.println("\n--- Code Type Selection ---");
            System.out.println("1. Value Code");
            System.out.println("2. Percent Code");
            System.out.println("0. Back");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> generateValueCode(purpose);
                case "2" -> generatePercentCode(purpose);
                case "0" -> { return; }
                default -> SystemMessage.printMessage("Enter Valid Number", MessageTitle.Error);
            }
        }
    }

    private void generateValueCode(String purpose) {
        while (true) {
            System.out.println("\n--- Create Value Discount Code ---");
            System.out.print("Enter Value Of Discount Code (or 0 to Back): ");
            String valueInput = scanner.nextLine();

            if (valueInput.equals("0")) return;
            if (!valueInput.matches("\\d+")) {
                SystemMessage.printMessage("Please Enter A Valid Value.", MessageTitle.Error);
                continue;
            }

            double value = Double.parseDouble(valueInput);
            if (value <= 0) {
                SystemMessage.printMessage("Value Must Be Greater Than 0.", MessageTitle.Error);
                continue;
            }

            System.out.print("Enter Usable Times: ");
            String usableTimesInput = scanner.nextLine();
            if (!usableTimesInput.matches("\\d+")) {
                SystemMessage.printMessage("Please Enter A Valid Number.", MessageTitle.Error);
                continue;
            }

            int usableTimes = Integer.parseInt(usableTimesInput);
            if (usableTimes <= 0) {
                SystemMessage.printMessage("Usable Times Must Be Greater Than 0.", MessageTitle.Error);
                continue;
            }

            String code = generateRandomDiscountCode();
            DiscountCode dc = new ValueDiscount(code, purpose, true, usableTimes, value);
            registerDiscountCode(dc, purpose);
        }
    }

    public void generatePercentCode(String purpose) {
        while (true) {
            System.out.println("\n--- Create Percent Discount Code ---");
            System.out.print("Enter Percent Of Discount Code (or 0 to Back): ");
            String percentInput = scanner.nextLine();

            if (percentInput.equals("0")) return;
            if (!percentInput.matches("\\d+")) {
                SystemMessage.printMessage("Please Enter A Valid Percent.", MessageTitle.Error);
                continue;
            }

            double percent = Double.parseDouble(percentInput);
            if (percent <= 0 || percent > 100) {
                SystemMessage.printMessage("Percent Must Be Between 1 And 100.", MessageTitle.Error);
                continue;
            }

            System.out.print("Enter Usable Times: ");
            String usableTimesInput = scanner.nextLine();
            if (!usableTimesInput.matches("\\d+")) {
                SystemMessage.printMessage("Please Enter A Valid Number.", MessageTitle.Error);
                continue;
            }

            int usableTimes = Integer.parseInt(usableTimesInput);
            if (usableTimes <= 0) {
                SystemMessage.printMessage("Usable Times Must Be Greater Than 0.", MessageTitle.Error);
                continue;
            }

            String code = generateRandomDiscountCode();
            DiscountCode dc = new PercentDiscount(code, purpose,true, usableTimes, percent);
            registerDiscountCode(dc, purpose);
        }
    }

    private void registerDiscountCode(DiscountCode dc, String purpose) {
        DataBase.getInstance().addDiscountCode(dc);
        Notification notification = new Notification(dc);

        if (!purpose.equals("General")) {
            mainCustomer.addToDiscountCodeList(dc);
            mainCustomer.addNotification(notification);
        } else {
            DataBase.getInstance().addNotification(notification);
        }

        SystemMessage.printMessage("Successfully Generated Discount Code", MessageTitle.Success);
    }

    public String generateRandomDiscountCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append(generateRandomNumber());
            code.append(generateCharacter());
        }
        return code.toString();
    }

    private int generateRandomNumber() {
        return (int) (Math.random() * 10);
    }

    private String generateCharacter() {
        return String.valueOf((char) ((int) (Math.random() * 26) + 'A'));
    }
}