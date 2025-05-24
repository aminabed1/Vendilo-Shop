package ir.ac.kntu;

import java.util.*;

public class MainPage {
    private static final Scanner scan = new Scanner(System.in);
    private static boolean backButtonPressed = false;

    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String BOLD = "\u001B[1m";

    public void mainPage(Person person) {
        backButtonPressed = false;
        while (!backButtonPressed) {
            clearScreen();
            showUserWelcome(person);

            switch (person.getRole()) {
                case "Customer":
                    customerMenu((Customer)person);
                    break;
                case "Seller":
                    sellerMenu((Seller)person);
                    break;
                case "Support":
                    supportMenu((Support)person);
                    break;
                default:
                    System.out.println(ERROR + "Invalid role detected!" + RESET);
                    backButtonPressed = true;
            }
        }
    }

    public void customerMenu(Customer customer) {
        displayMenuHeader("CUSTOMER DASHBOARD");
        displayCustomerDashboard(customer);
        customerHandleChoice(customer);
    }

    //TODO complete here
    public void sellerMenu(Seller seller) {
        displayMenuHeader("SELLER DASHBOARD");
        System.out.println(MENU + "+---------------------------------------+");
        System.out.println("| 1. Manage Products                   |");
        System.out.println("| 2. View Sales                       |");
        System.out.println("| 3. Account Information              |");
        System.out.println("| 4. Logout                           |");
        System.out.println("+---------------------------------------+");
        System.out.print(PROMPT + "Your choice: " + RESET + HIGHLIGHT);
        sellerHandleChoice(seller);
    }

    //TODO complete here
    public void supportMenu(Support support) {
        displayMenuHeader("SUPPORT DASHBOARD");
        System.out.println(MENU + "+---------------------------------------+");
        System.out.println("| 1. View Customer Tickets           |");
        System.out.println("| 2. Resolve Issues                  |");
        System.out.println("| 3. Account Information             |");
        System.out.println("| 4. Logout                          |");
        System.out.println("+---------------------------------------+");
        System.out.print(PROMPT + "Your choice: " + RESET + HIGHLIGHT);
        supportHandleChoice(support);
    }

    public void displayMenuHeader(String title) {
        System.out.println(TITLE + "=========================================");
        System.out.println("|                                       |");
        System.out.printf("|%s%-39s%s|\n", BOLD + HIGHLIGHT,
                "   " + title, RESET + TITLE);
        System.out.println("|                                       |");
        System.out.println("=========================================" + RESET);
        System.out.println();
    }

    public void showUserWelcome(Person person) {
        System.out.printf(PROMPT + "Welcome, %s! (%s)\n\n" + RESET,
                person.getName(), person.getRole());
    }

    public void displayCustomerDashboard(Customer customer) {
        System.out.println(MENU + "╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               CUSTOMER DASHBOARD - OPTIONS                ║");
        System.out.println("╠════════════════════════╦══════════════════╦═══════════════╣");
        System.out.println("║        " + PROMPT + "ACCOUNT" + MENU + "         ║    " + OPTION + "CATEGORIES" + MENU + "    ║    " + TITLE + "SEARCH" + MENU + "     ║");
        System.out.println("╠════════════════════════╬══════════════════╬═══════════════╣");
        System.out.println("║ " + OPTION + "1. Account Information" + MENU + " ║ " + OPTION + "6. Browse" + MENU + "        ║ " + OPTION + "7. Search" + MENU + "     ║");
        System.out.println("║ " + OPTION + "2. Cart" + MENU + "                ║ " + OPTION + "  Categories" + MENU + "     ║ " + OPTION + "  Products" + MENU + "    ║");
        System.out.println("║ " + OPTION + "3. Orders" + MENU + "              ║                  ║               ║");
        System.out.println("║ " + OPTION + "4. My Wallet" + MENU + "           ║                  ║               ║");
        System.out.println("║ " + OPTION + "5. Logout" + MENU + "              ║                  ║               ║");
        System.out.println("╚════════════════════════╩══════════════════╩═══════════════╝" + RESET);
        System.out.println();
    }

    public void customerHandleChoice(Customer customer) {
        System.out.print(PROMPT + "Enter Your Choice (1-7) or type 'BACK': " + RESET + HIGHLIGHT);

        while (true) {
            String choice = scan.nextLine().trim();

            if (choice.equalsIgnoreCase("BACK")) {
                backButtonPressed = true;
                break;
            }

            if (!choice.matches("[1-7]")) {
                System.out.println(ERROR + "Please enter a valid choice (1-7) or BACK" + RESET);
                continue;
            }

            switch (choice) {
                case "1":
                    PersonAccount personInfo = new PersonAccount();
                    personInfo.infoView(customer);
                    break;
                case "2":
                    customer.displayCart();
                    break;
                case "3":
                    DisplayOrder.getInstance().display(customer);
                    break;
                case "4":
                    customer.getWallet().walletOptionHandler(customer);
                    break;
                case "5":
                    backButtonPressed = true;
                    break;
                case "6":
                    browseCategories(customer);
                    break;
                //TODO complete here
                case "7":
                    break;
            }
            break;
        }
    }

    //TODO complete seller menu
    public void sellerHandleChoice(Seller seller) {
        while (true) {
            String choice = scan.nextLine().trim();

            if (choice.equalsIgnoreCase("BACK")) {
                backButtonPressed = true;
                break;
            }

            if (!choice.matches("[1-4]")) {
                System.out.println(ERROR + "Please enter a valid choice (1-4) or BACK" + RESET);
                continue;
            }

            switch (choice) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    //TODO complete here
//                    PersonInfo.infoView(seller);
                    break;
                case "4":
                    backButtonPressed = true;
                    break;
            }
            break;
        }
    }

    //TODO complete support menu
    public void supportHandleChoice(Support support) {
        while (true) {
            String choice = scan.nextLine().trim();

            if (choice.equalsIgnoreCase("BACK")) {
                backButtonPressed = true;
                break;
            }

            if (!choice.matches("[1-4]")) {
                System.out.println(ERROR + "Please enter a valid choice (1-4) or BACK" + RESET);
                continue;
            }

            switch (choice) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    //TODO complete here
//                    PersonInfo.infoView(support);
                    break;
                case "4":
                    backButtonPressed = true;
                    break;
            }
            break;
        }
    }
    //TODO complete here
    public void browseCategories(Customer customer) {
        clearScreen();
        System.out.println(TITLE + "╔══════════════ CATEGORIES ══════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║ " + OPTION + "1. Digital Products" + TITLE + "                    ║");
        System.out.println("║ " + OPTION + "2. Books" + TITLE + "                               ║");
        System.out.println("║ " + OPTION + "3. Back to Main Menu" + TITLE + "                   ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝" + RESET);
        System.out.println();

        while (true) {
            System.out.print(PROMPT + "Choose a category (1-3): " + RESET + HIGHLIGHT);
            String choice = scan.nextLine().trim();
            System.out.print(RESET);

            switch (choice) {
                case "1":
                    displayDigitalProducts(customer);
                    return;
                case "2":
                    displayBooks(customer);
                    return;

                case "3":
                    return;
                default:
                    System.out.println(ERROR + "Invalid choice! Please try again." + RESET);
            }
        }
    }
    //TODO
    public void displayDigitalProducts(Customer customer) {
        System.out.println("\n" + OPTION + "Displaying Digital Products..." + RESET + "\n");
    }
    //TODO
    public void displayBooks(Customer customer) {
        System.out.println("\n" + OPTION + "Displaying Books..." + RESET + "\n");
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}