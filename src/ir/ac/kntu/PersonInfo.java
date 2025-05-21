package ir.ac.kntu;

import java.util.*;

public class PersonInfo {
    private static Person currentPerson;
    private static final Scanner scan = new Scanner(System.in);

    public void infoView(Person person) {
        currentPerson = person;
        handleUserOptions();
    }

    public void displayAccountInfo() {
        displayAccountInfoHeader();
        displayPersonalInfo();
        displayRoleSpecificInfo();
        displayAddressesIfCustomer();
    }

    public void displayAccountInfoHeader() {
        System.out.println("\n\u001B[36m=============================================\u001B[0m");
        System.out.printf("\u001B[36m| %-42s |\u001B[0m\n", "ACCOUNT INFORMATION");
        System.out.printf("\u001B[36m| Role: %-36s |\u001B[0m\n", currentPerson.getRole());
        System.out.println("\u001B[36m=============================================\u001B[0m\n");
    }

    public void displayPersonalInfo() {
        System.out.println("\u001B[33mPERSONAL INFORMATION:\u001B[0m");
        System.out.printf("  %-15s: %s\n", "Name", currentPerson.getName());
        System.out.printf("  %-15s: %s\n", "Surname", currentPerson.getSurname());
        System.out.printf("  %-15s: %s\n", "Email", currentPerson.getEmail());
        System.out.printf("  %-15s: %s\n", "Phone", currentPerson.getPhoneNumber());
        System.out.printf("  %-15s: %s\n", "Username", currentPerson.getUsername());
        System.out.printf("  %-15s: %s\n", "Password", "********"); // Masked password
    }

    public void displayRoleSpecificInfo() {
        System.out.println("\n\u001B[33mROLE INFORMATION:\u001B[0m");

        if (currentPerson instanceof Customer) {
            System.out.println("  Type: Customer");
        } else if (currentPerson instanceof Seller) {
            Seller seller = (Seller) currentPerson;
            System.out.printf("  %-15s: %s\n", "Shop Name", seller.getShopName());
            System.out.printf("  %-15s: %s\n", "Agency Code", seller.getAgencyCode());
            System.out.printf("  %-15s: %s\n", "Province", seller.getProvince());
        }
    }

    public void displayAddressesIfCustomer() {
        if (currentPerson instanceof Customer) {
            Customer customer = (Customer) currentPerson;
            if (!customer.getAddressList().isEmpty()) {
                System.out.println("\n\u001B[33mSAVED ADDRESSES:\u001B[0m");
                displayAddresses(customer.getAddressList());
            }
        }
    }

    public void displayAddresses(List<Address> addresses) {
        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);
            System.out.printf("  [%d] %s, %s, %s, %s (Postal: %s) %s\n",
                    i + 1,
                    address.getPlateNumber(),
                    address.getStreet(),
                    address.getCity(),
                    address.getProvince(),
                    address.getPostalCode(),
                    address.getDetails());
        }
    }

    public void handleUserOptions() {
        while (true) {
            displayAccountInfo();
            displayOptionsMenu();
            String choice = scan.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1":
                    if (currentPerson instanceof Customer) {
                        addNewAddress((Customer) currentPerson);
                    }
                    break;
                case "2":
                    editPersonalInfo();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("\n\u001B[31mInvalid option. Please try again.\u001B[0m");
            }
        }
    }

    public void displayOptionsMenu() {
        System.out.println("\n\u001B[35mOPTIONS:\u001B[0m");
        if (currentPerson instanceof Customer) {
            System.out.println("1. Add Address");
        }
        System.out.println("2. Edit Personal Info");
        System.out.println("3. Back to Main Menu");
        System.out.print("Your choice: ");
    }

    public void addNewAddress(Customer customer) {
        System.out.println("\n\u001B[33mENTER NEW ADDRESS:\u001B[0m");

        String plateNumber = getInput("Plate Number: ", true);
        String postalCode = getInput("Postal Code: ", true);
        String street = getInput("Street: ", true);
        String city = getInput("City: ", true);
        String province = getInput("Province: ", true);
        String details = getInput("Details: ", true);

        Address newAddress = new Address(province, street, city, postalCode, plateNumber, details);
        customer.getAddressList().add(newAddress);

        System.out.println("\n\u001B[32mAddress added successfully!\u001B[0m");
//        displayAddresses(customer.getAddressList());
    }

    public void editPersonalInfo() {
        List<String> errors = new ArrayList<>();

        do {
            System.out.println("\n\u001B[36mEDIT PERSONAL INFORMATION:\u001B[0m");

            String name = getInput("Name: ", true);
            String surname = getInput("Surname: ", true);
            String email = getInput("Email: ", true);
            String phone = getInput("Phone: ", true);
            String username = getInput("Username: ", true);
            String password = getInput("Password: ", false);

            if (InfoValidator.isPersonInfoValid(name, surname, phone, email, username, password, errors)) {
                if (!isAccountInfoDuplicate(email, phone, username)) {
                    updatePersonInfo(name, surname, email, phone, username, password);
                    System.out.println("\n\u001B[32mInformation updated successfully!\u001B[0m");
                    return;
                }
                errors.add("Username, email or phone already exists");
            }

            if (!displayErrors(errors)) {
                break;
            }
            errors.clear();
        } while (true);
    }

    public boolean isAccountInfoDuplicate(String email, String phone, String username) {
        return DataBase.getPersonList().stream()
                .filter(p -> !p.equals(currentPerson))
                .anyMatch(p -> p.getEmail().equalsIgnoreCase(email)
                        || p.getPhoneNumber().equals(phone)
                        || p.getUsername().equals(username));
    }

    public void updatePersonInfo(String name, String surname, String email,
                                         String phone, String username, String password) {
        currentPerson.setName(name);
        currentPerson.setSurname(surname);
        currentPerson.setEmail(email);
        currentPerson.setPhoneNumber(phone);
        currentPerson.setUsername(username);
        currentPerson.setPassword(password);
    }

    public String getInput(String prompt, boolean visible) {
        System.out.print(prompt);
        return visible ? scan.nextLine().trim() : new String(System.console().readPassword());
    }

    public boolean displayErrors(List<String> errors) {
        System.out.println("\n\u001B[31mERRORS:\u001B[0m");
        errors.forEach(error -> System.out.println(" - " + error));
        System.out.println("Please correct the errors and try again.");
        System.out.println("Do you want to enter information again ? (y/n)");
        return (scan.nextLine().trim().equals("y"));
    }
}