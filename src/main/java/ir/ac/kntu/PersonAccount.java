package ir.ac.kntu;

import java.io.Serializable;
import java.util.*;

public class PersonAccount implements Serializable {
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
        System.out.printf("  %-15s: %s\n", "Email", ((OrdinaryUsers)currentPerson).getEmail());
        System.out.printf("  %-15s: %s\n", "Phone", ((OrdinaryUsers)currentPerson).getPhoneNumber());
        System.out.printf("  %-15s: %s\n", "Password", "********");
    }

    public void displayRoleSpecificInfo() {
        System.out.println("\n\u001B[33mROLE INFORMATION:\u001B[0m");

        if (currentPerson instanceof Customer) {
            System.out.println("  Type: Customer");
        } else if (currentPerson instanceof Seller seller) {
            System.out.printf("  %-15s: %s\n", "Shop Name", seller.getShopName());
            System.out.printf("  %-15s: %s\n", "Agency Code", seller.getAgencyCode());
            System.out.printf("  %-15s: %s\n", "Province", seller.getProvince());
        }
    }

    public void displayAddressesIfCustomer() {
        if (currentPerson instanceof Customer customer) {
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
            displayOptionsMenu();
            String choice = scan.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1":
                    PersonalInfoOption();
                    break;
                case "2":
                    if (currentPerson instanceof Customer) {
                        addNewAddress((Customer) currentPerson);
                    }
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
        System.out.println("1. Personal Information");
        System.out.println("2. Addresses");
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
    }

    public void PersonalInfoOption() {
        while (true) {
            displayAccountInfo();
            System.out.println("Do you want to edit your personal information? (Y/N -> BACK)");
            String choice = scan.nextLine().trim().toLowerCase();
            if (choice.equals("y")) {
                PersonInfoEdit.getInstance().editUserInfo(currentPerson);
            } else if (choice.equals("n") || choice.equals("back")) {
                return;
            } else {
                System.out.println("\n\u001B[31mInvalid option. Please try again.");
            }
        }
    }

    public String getInput(String prompt, boolean visible) {
        System.out.print(prompt);
        return visible ? scan.nextLine().trim() : new String(System.console().readPassword());
    }
}
