package ir.ac.kntu;

import java.util.*;

public abstract class Person {
    private final static Scanner scan = new Scanner(System.in);

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

    public  void wallet(Person person) {

        System.out.println("My Wallet\n");
        if (person instanceof Customer customer) {
            System.out.println("Balance : " + customer.getWalletBalance());
            System.out.println("Do you want to add money to your balance? (y/n or BACK)");

            String choice = scan.nextLine().trim();
            if (choice.equalsIgnoreCase("BACK")) {
                return;
            } else if (choice.equalsIgnoreCase("y")) {
                addBalance(customer);
            } else if (choice.equalsIgnoreCase("n")) {
                wallet(person);
            } else {
                System.out.println("Please enter a valid choice.");
                wallet(person);
            }

        } else if (person instanceof Seller seller) {
            System.out.println("Balance : " + seller.getWalletBalance());
            System.out.println("Back to menu? (y/n)");

            String choice = scan.nextLine().trim();
            if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("BACK")) {
                return;
            } else {
                System.out.println("Please enter a valid choice.");
                wallet(person);
            }
        }
    }

    public static void addBalance(Customer customer) {
        System.out.print("Enter amount to add / cancel: ");
        String balance = scan.nextLine().trim();
        if (balance.equalsIgnoreCase("cancel")) {
            return;
        }

        if (!balance.matches("\\d+(\\.\\d+)?")) {
            System.out.println("Please enter a valid amount.");
            addBalance(customer);
            return;
        }

        double amount = Double.parseDouble(balance);
        customer.setWalletBalance(customer.getWalletBalance() + amount);
        System.out.println("Balance updated successfully!");
    }

}
