package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManagerPage {

    private static final Scanner scan = new Scanner(System.in);

    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED_BOLD = "\u001B[31;1m";

    public static ManagerPage getInstance() {
        return new ManagerPage();
    }

    public void mainPage(Person person) {
        while (true) {
            printMenuOptions();
            String choice = getUserChoice("Enter an option: ", "[0-5]");
            if (choice == null) continue;

            switch (choice) {
                case "0" -> {
                    return;
                }
                case "1" -> manageUsers();
                case "2" -> System.out.println("TODO: Display Sellers Activities");
                case "3" -> System.out.println("TODO: Display Customers Activities");
                case "4" -> System.out.println("TODO: Generate General Discount Code");
                case "5" -> System.out.println("TODO: Publish General Message");
            }
        }
    }

    private void printMenuOptions() {
        System.out.println("\n--- Manager Menu ---");
        System.out.println("1. Manage Users");
        System.out.println("2. Display Sellers Activities");
        System.out.println("3. Display Customers Activities");
        System.out.println("4. Generate General Discount Code");
        System.out.println("5. Publish General Message");
        System.out.println("0. Back");
    }

    private String getUserChoice(String prompt, String regexPattern) {
        System.out.print(prompt);
        String choice = scan.nextLine().trim();
        if (!choice.matches(regexPattern)) {
            System.out.println(RED_BOLD + "Invalid choice, try again..." + RESET);
            return null;
        }
        return choice;
    }

    private void manageUsers() {
        while (true) {
            List<Person> persons = filterUsers(DataBase.getPersonList());
            if (persons == null || persons.isEmpty()) {
                System.out.println(RED_BOLD + "No users match the filters." + RESET);
                return;
            }

            printPersonsList(persons);

            String choice = getUserChoice("Select a Person By Index Or Press 0 To Back: ", "\\d+");
            if (choice == null) continue;
            if (choice.equals("0")) return;

            int index = Integer.parseInt(choice);
            if (index < 1 || index > persons.size()) {
                System.out.println(RED_BOLD + "Invalid index, try again..." + RESET);
                continue;
            }

            Person person = persons.get(index - 1);
            displayAndManagePerson(person);
        }
    }

    private void printPersonsList(List<Person> persons) {
        System.out.println();
        int counter = 1;
        for (Person person : persons) {
            String personData = String.format(
                    "%s%d.%s %s👤 %-20s%s | %s🏷️ %-20s%s | %s🎭 %-20s%s",
                    CYAN, counter++, RESET,
                    CYAN, person.getName(), RESET,
                    YELLOW, person.getSurname(), RESET,
                    CYAN, person.getRole(), RESET
            );
            System.out.println(personData);
        }
    }

    private List<Person> filterUsers(List<Person> persons) {
        String username = "";
        String role = "";

        while (true) {
            System.out.println("\nFilter by :");
            System.out.println("1. Username");
            System.out.println("2. Role");
            System.out.println("3. Load Users");
            System.out.println("4. Exit");

            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter Username: ");
                    username = scan.nextLine().trim();
                }
                case "2" -> {
                    System.out.println("Select A Role: ");
                    System.out.println("1. Customer");
                    System.out.println("2. Seller");
                    System.out.println("3. Support");
                    System.out.println("4. Manager");
                    String roleChoice = scan.nextLine().trim();
                    role = switch (roleChoice) {
                        case "1" -> "Customer";
                        case "2" -> "Seller";
                        case "3" -> "Support";
                        case "4" -> "Manager";
                        default -> "";
                    };
                }
                case "3" -> {
                    List<Person> filtered = applyFilter(persons, username, role);
                    return filtered.isEmpty() ? null : filtered;
                }
                case "4" -> {
                    return null;
                }
                default -> System.out.println(RED_BOLD + "Invalid choice, try again..." + RESET);
            }
        }
    }

    private List<Person> applyFilter(List<Person> persons, String username, String role) {
        List<Person> filteredPersons = new ArrayList<>();

        for (Person person : persons) {
            boolean matchesUsername = true;
            boolean matchesRole = true;

            if (!username.isEmpty()) {
                if (person instanceof SpecialUsers su) {
                    matchesUsername = su.getUsername().equalsIgnoreCase(username);
                } else {
                    matchesUsername = false;
                }
            }

            if (!role.isEmpty()) {
                matchesRole = person.getRole().name().equalsIgnoreCase(role);
            }

            if (matchesUsername && matchesRole) {
                filteredPersons.add(person);
            }
        }
        return filteredPersons;
    }

    private void displayAndManagePerson(Person person) {
        System.out.println("\n" + person);
        // TODO: افزودن امکانات بیشتر مدیریت این شخص
    }
}
