package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EditPersonInfo {
    private final static Scanner scan = new Scanner(System.in);

    private String name;
    private String surname;
    private String password;
    private String username;

    public static EditPersonInfo getInstance() {
        return new EditPersonInfo();
    }

    public void handleEdit(Person person) {
        this.name = person.getName();
        this.surname = person.getSurname();
        this.password = person.getPassword();
        this.username = (person instanceof SpecialUsers) ? ((SpecialUsers) person).getUsername() : null;

        while (true) {
            printMenu(person);
            String choice = getUserChoice();
            if (processChoice(choice, person)) {
                return;
            }
            if ("1".equals(choice)) {
                this.name = getInput("Enter Name: ");
            }
            if ("2".equals(choice)) {
                this.surname = getInput("Enter Surname: ");
            }
            if ("3".equals(choice)) {
                this.password = getInput("Enter Password: ");
            }
            if ("4".equals(choice) && person instanceof SpecialUsers) {
                this.username = getInput("Enter Username: ");
            }
        }
    }

    private void printMenu(Person person) {
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
    }

    private String getUserChoice() {
        System.out.print("\nMake Your Choice: ");
        return scan.nextLine();
    }

    private boolean processChoice(String choice, Person person) {
        switch (choice) {
            case "4":
                if (!(person instanceof SpecialUsers)) {
                    return confirmOrdinaryChanges(person);
                }
                break;
            case "5":
                if (person instanceof SpecialUsers) {
                    return confirmSpecialChanges(person);
                }
                return true;
            case "6":
                if (person instanceof SpecialUsers) {
                    return true;
                }
                break;
            default:
                if (!List.of("1", "2", "3", "4", "5", "6").contains(choice)) {
                    System.out.println("Invalid choice, try again...");
                }
        }
        return false;
    }

    private boolean confirmOrdinaryChanges(Person person) {
        if (validateOrdinaryInfoChanges(password)) {
            applyChanges(person, name, surname, password);
            System.out.println("Information Changed Successfully.");
        } else {
            System.out.println("Edit Failed.");
        }
        return true;
    }

    private boolean confirmSpecialChanges(Person person) {
        if (validateOrdinaryInfoChanges(password) && validateSpecialInfoChanges(username)) {
            applyChanges(person, name, surname, password);
            applyChanges(person, username);
            System.out.println("Information Changed Successfully.");
        } else {
            System.out.println("Edit Failed.");
        }
        return true;
    }

    private String getInput(String prompt) {
        System.out.print(prompt);
        return scan.nextLine();
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
