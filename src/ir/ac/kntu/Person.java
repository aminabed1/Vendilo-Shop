package ir.ac.kntu;

import java.util.*;

public abstract class Person {
    private final static Scanner scan = new Scanner(System.in);

    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String BOLD = "\u001B[1m";

    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private int age;
    private String gender;
    private List<String> errorList;
    private String role;


    public Person(String name, String surname, String phoneNumber, String email, String username, String password) {
        errorList = new ArrayList<>();
        if (InfoValidator.isPersonInfoValid(name, surname, phoneNumber, email, username, password, errorList)) {
            this.name = name;
            this.surname = surname;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.username = username;
            this.password = password;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public abstract boolean equals(Object object);

    public abstract int hashCode();

    public void wallet(Person person) {
        clearScreen();

        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║                                     ║");
        System.out.println("║" + BOLD + HIGHLIGHT + "               MY WALLET             " + RESET + TITLE + "║");
        System.out.println("║                                     ║");
        System.out.println("╠═════════════════════════════════════╣" + RESET);

        if (person instanceof Customer customer) {
            System.out.println(OPTION + "  Balance: " + HIGHLIGHT +
                    String.format("%.2f $", customer.getWalletBalance()) + RESET);
            System.out.println(TITLE + "╠═════════════════════════════════════╣" + RESET);
            System.out.println(OPTION + "  Add money to your balance?" + RESET);
            System.out.println(OPTION + "  [Y] Yes     [N] No     [BACK] Return" + RESET);
            System.out.print(PROMPT + "\n  Your choice: " + RESET + HIGHLIGHT);

            String choice = scan.nextLine().trim();
            System.out.print(RESET);

            if (choice.equalsIgnoreCase("BACK")) {
                return;
            } else if (choice.equalsIgnoreCase("Y")) {
                addBalance(customer);
            } else if (choice.equalsIgnoreCase("N")) {
                wallet(person);
            } else {
                showError("Please enter a valid choice (Y/N/BACK)");
                wallet(person);
            }

        } else if (person instanceof Seller seller) {
            System.out.println(OPTION + "  Balance: " + HIGHLIGHT +
                    String.format("%.2f $", seller.getWalletBalance()) + RESET);
            System.out.println(TITLE + "╠═════════════════════════════════════╣" + RESET);
            System.out.println(OPTION + "  Press any key to return to menu" + RESET);
            System.out.print(PROMPT + "  > " + RESET + HIGHLIGHT);

            scan.nextLine();
            System.out.print(RESET);
        }
    }

    public void addBalance(Customer customer) {
        clearScreen();

        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║                                     ║");
        System.out.println("║" + BOLD + HIGHLIGHT + "           ADD TO BALANCE            " + RESET + TITLE + "║");
        System.out.println("║                                     ║");
        System.out.println("╠═════════════════════════════════════╣" + RESET);
        System.out.println(OPTION + "  Current Balance: " + HIGHLIGHT +
                String.format("%.2f $", customer.getWalletBalance()) + RESET);
        System.out.print(OPTION + "  Enter amount or type " + BOLD + "CANCEL" + RESET + OPTION + ": " + RESET + HIGHLIGHT);

        String balance = scan.nextLine().trim();
        System.out.print(RESET);

        if (balance.equalsIgnoreCase("CANCEL")) {
            return;
        }

        if (!balance.matches("\\d+(\\.\\d+)?")) {
            showError("Please enter a valid amount (e.g. 50 or 12.50)");
            addBalance(customer);
            return;
        }

        double amount = Double.parseDouble(balance);
        if (amount <= 0) {
            showError("Amount must be greater than 0");
            addBalance(customer);
            return;
        }

        customer.setWalletBalance(customer.getWalletBalance() + amount);

        System.out.println(SUCCESS + "\n╔═════════════════════════════════════╗");
        System.out.println("║                                     ║");
        System.out.println("║" + BOLD + "    BALANCE UPDATED SUCCESSFULLY!    " + RESET + SUCCESS + "║");
        System.out.printf("║ New Balance: %.2f $ \n", customer.getWalletBalance());
        System.out.println("║                                     ║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
        pause(2000);
    }

    private void showError(String message) {
        System.out.println(ERROR + "\n⚠ " + message + RESET);
        pause(1000);
    }

    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
