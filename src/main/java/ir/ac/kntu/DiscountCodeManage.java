package ir.ac.kntu;

import java.util.List;
import java.util.Scanner;

public class DiscountCodeManage {

    private static final Scanner scanner = new Scanner(System.in);
    private static Customer mainCustomer = null;

    public static DiscountCodeManage getInstance() {
        return new DiscountCodeManage();
    }

    public void setMainCustomer(Customer customer) {
        mainCustomer = customer;
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
                case "0" -> {
                    return;
                }
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

    public void objectReceiver(Object obj) {
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
                case "0" -> {
                    return;
                }
                default -> SystemMessage.printMessage("Enter Valid Number", MessageTitle.Error);
            }
        }
    }

    private void generateValueCode(String purpose) {
        while (true) {
            System.out.println("\n--- Create Value Discount Code ---");

            Double value = readPositiveDouble("Enter Value Of Discount Code (or 0 to Back): ");
            if (value == null || value == 0) {
                return;
            }
            Integer usableTimes = readPositiveInt("Enter Usable Times: ");
            if (usableTimes == null) {
                continue;
            }
            String code = generateRandomDiscountCode();
            DiscountCode discountCode = new ValueDiscount(code, purpose, true, usableTimes, value);
            registerDiscountCode(discountCode, purpose);
        }
    }

    private Double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if ("0".equals(input)) {
                return 0.0;
            }
            if (!input.matches("\\d+(\\.\\d+)?")) {
                SystemMessage.printMessage("Please Enter A Valid Value.", MessageTitle.Error);
                continue;
            }
            double val = Double.parseDouble(input);
            if (val <= 0) {
                SystemMessage.printMessage("Value Must Be Greater Than 0.", MessageTitle.Error);
                continue;
            }
            return val;
        }
    }

    private Integer readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.matches("\\d+")) {
                SystemMessage.printMessage("Please Enter A Valid Number.", MessageTitle.Error);
                continue;
            }
            int val = Integer.parseInt(input);
            if (val <= 0) {
                SystemMessage.printMessage("Usable Times Must Be Greater Than 0.", MessageTitle.Error);
                continue;
            }
            return val;
        }
    }

    public void generatePercentCode(String purpose) {
        while (true) {
            System.out.println("\n--- Create Percent Discount Code ---");
            double percent = readValidPercent();
            if (percent == 0) {
                return;
            }
            int usableTimes = readValidPositiveInt("Enter Usable Times: ");
            String code = generateRandomDiscountCode();
            DiscountCode discountCode = new PercentDiscount(code, purpose, true, usableTimes, percent);
            registerDiscountCode(discountCode, purpose);
            break;
        }
    }

    private double readValidPercent() {
        while (true) {
            String input = readInput("Enter Percent Of Discount Code (or 0 to Back): ");
            if ("0".equals(input)) {
                return 0;
            }
            if (input.matches("\\d+")) {
                double percent = Double.parseDouble(input);
                if (percent > 0 && percent <= 100) {
                    return percent;
                }
            }
            SystemMessage.printMessage("Percent Must Be Between 1 And 100.", MessageTitle.Error);
        }
    }

    private int readValidPositiveInt(String message) {
        while (true) {
            String input = readInput(message);
            if (input.matches("\\d+")) {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
            }
            SystemMessage.printMessage("Please Enter A Valid Number Greater Than 0.", MessageTitle.Error);
        }
    }

    private String readInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }


    private void registerDiscountCode(DiscountCode discountCode, String purpose) {
        DataBase.getInstance().addDiscountCode(discountCode);
        Notification notification = new Notification(discountCode);

        if (!"General".equals(purpose)) {
            mainCustomer.addToDiscountCodeList(discountCode);
            mainCustomer.addNotification(notification);
        } else {
            DataBase.getInstance().addNotification(notification);
        }

        SystemMessage.printMessage("Successfully Generated Discount Code", MessageTitle.Success);
        mainCustomer = null;
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