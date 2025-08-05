package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EditPersonInfo {
    public static Scanner scan = new Scanner(System.in);

    public static EditPersonInfo getInstance() {
        return new EditPersonInfo();
    }

    public void handleEdit(Person person) {
        String name = person.getName();
        String surname = person.getSurname();
        String password = person.getPassword();
        String username = (person instanceof SpecialUsers) ? ((SpecialUsers) person).getUsername() : null;

        while (true) {
            System.out.println("Edit:");
            System.out.println("1. Name: " + name);
            System.out.println("2. Surname: " + surname);
            System.out.println("3. Password: " + password);
            if (person instanceof SpecialUsers) {
                System.out.println("4. Username: " + username);
                System.out.println("5. Confirm Changes");
                System.out.println("6. Back");
            } else {
                System.out.println("4. Confirm Changes");
                System.out.println("5. Back");
            }

            System.out.print("\nMake Your Choice: ");
            String choice = scan.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter Name: ");
                    name = scan.nextLine();
                    break;
                case "2":
                    System.out.print("Enter Surname: ");
                    surname = scan.nextLine();
                    break;
                case "3":
                    System.out.print("Enter Password: ");
                    password = scan.nextLine();
                    break;
                case "4":
                    if (person instanceof SpecialUsers) {
                        System.out.print("Enter Username: ");
                        username = scan.nextLine();
                        break;
                    }
                    if (validateOrdinaryInfoChanges(password)) {
                        applyChanges(person, name, surname, password);
                        System.out.println("Information Changed Successfully.");
                    } else {
                        System.out.println("Edit Failed.");
                    }
                    return;
                case "5":
                    if (person instanceof SpecialUsers) {
                        if (validateOrdinaryInfoChanges(password) && validateSpecialInfoChanges(username)) {
                            applyChanges(person, name, surname, password);
                            applyChanges(person, username);
                            System.out.println("Information Changed Successfully.");
                        } else {
                            System.out.println("Edit Failed.");
                        }
                    } else {
                        return;
                    }
                    return;
                case "6":
                    if (person instanceof SpecialUsers) {
                        return;
                    }
                    break;
                default:
                    System.out.println("Invalid choice, try again...");
            }
        }
    }

    public boolean validateOrdinaryInfoChanges(String password) {
        return InfoValidator.getInstance().passwordValidation(password, new ArrayList<>());
    }

    public boolean validateSpecialInfoChanges(String username) {
        List<String> messageListList = new ArrayList<>();
        boolean isValid = InfoValidator.getInstance().usernameValidation(username, messageListList);
        if (!messageListList.isEmpty()) {
            SystemMessage.printMessage(messageListList, MessageTitle.Error);
        }
        return isValid;
    }

    public void applyChanges(Person person, String name, String surname, String password) {
        person.setName(name);
        person.setSurname(surname);
        person.setPassword(password);
    }

    public void applyChanges(Person person, String username) {
        ((SpecialUsers) person).setUsername(username);
    }
}
