package ir.ac.kntu;

import ir.ac.kntu.util.Calendar;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Wallet implements Serializable {

    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String WARNING = "\u001B[38;5;208m";
    private static final String BOLD = "\u001B[1m";
    private static final Scanner scan = new Scanner(System.in);

    private double balance;
    private List<Transaction> transactionList;

    public Wallet() {
        transactionList = new ArrayList<>();
    }

    public double getWalletBalance() {
        return balance;
    }

    public void setWalletBalance(double balance, Order order) {
        double oldBalance = this.balance;
        double amount = balance - oldBalance;
        if (amount != 0) {
            Transaction transaction = new TransactionWithOrder(Calendar.now(), amount, order);
            transactionList.add(transaction);
        }
        this.balance = balance;
    }

    public void setWalletBalance(double balance, String description) {
        double oldBalance = this.balance;
        double amount = balance - oldBalance;
        if (amount != 0) {
            Transaction transaction = new TransactionWithoutOrder(Calendar.now(), amount, description);
            transactionList.add(transaction);
        }
        this.balance = balance;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void walletOptionHandler(Person person) {
        while (true) {
            printWalletMenu(person);
            String choice = scan.nextLine().trim();
            switch (choice) {
                case "1" -> displayWallet(person);
                case "2" -> {
                    if (person instanceof Customer customer) {
                        displayTransactions(customer);
                    } else if (person instanceof Seller seller) {
                        displaySellerTransactions(seller);
                    }
                }
                case "3" -> {
                    if (person instanceof Customer customer) {
                        addBalance(customer);
                    } else if (person instanceof Seller seller) {
                        withdrawBalance(seller);
                    }
                }
                case "0" -> {
                    return;
                }
                default -> showError("Invalid option. Please try again!");
            }
        }
    }

    private void printWalletMenu(Person person) {
        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║" + BOLD + HIGHLIGHT + "           WALLET MENU               " + RESET + TITLE + "║");
        System.out.println("╠═════════════════════════════════════╣");
        System.out.println("║" + OPTION + " 1. Display Wallet                   " + TITLE + "║");
        System.out.println("║" + OPTION + " 2. Show Transactions                " + TITLE + "║");
        if (person instanceof Customer) {
            System.out.println("║" + OPTION + " 3. Add Balance                      " + TITLE + "║");
        } else if (person instanceof Seller) {
            System.out.println("║" + OPTION + " 3. Withdraw Money                   " + TITLE + "║");
        }
        System.out.println("║" + OPTION + " 0. Back to Main Menu                " + TITLE + "║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
        System.out.print(PROMPT + "Your choice: " + RESET);
    }

    public void displaySellerTransactions(Seller seller) {
        printTransactionHead();
        List<Transaction> transactions = this.getTransactionList();

        if (transactions.isEmpty()) {
            System.out.println(WARNING + "No sales transactions found." + RESET);
            Pause.pause(1500);
            return;
        }

        double totalIncome = transactions.stream().mapToDouble(Transaction::getAmount).sum();
        printTotalIncome(totalIncome);
        printTransactionTableHeader();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault());

        int counter = 1;
        for (Transaction transaction : transactions) {
            String date = formatter.format(transaction.getTimestamp());
            String amount = String.format("%.2f $", transaction.getAmount());
            String description = extractDescription(transaction);

            System.out.println(OPTION + String.format("%-5d %-20s %-12s %-30s",
                    counter++, date, SUCCESS + amount + RESET, description) + RESET);
        }

        System.out.print(PROMPT + "\nPress ENTER to return to menu" + RESET);
        scan.nextLine();
    }

    private void printTransactionHead() {
        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║" + BOLD + HIGHLIGHT + "          SALES TRANSACTIONS         " + RESET + TITLE + "║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
    }

    private void printTotalIncome(double totalIncome) {
        System.out.println(OPTION + "  Total Income: " + SUCCESS + String.format("%.2f $", totalIncome) + RESET);
        System.out.println(TITLE + "═══════════════════════════════════════" + RESET);
    }

    private void printTransactionTableHeader() {
        System.out.println(MENU + String.format("%-5s %-20s %-12s %-30s", "No.", "Date", "Amount", "Description") + RESET);
        System.out.println(MENU + "------------------------------------------------------------" + RESET);
    }

    private String extractDescription(Transaction transaction) {
        if (transaction instanceof TransactionWithOrder withOrder) {
            Order order = withOrder.getOrder();
            if (order instanceof SellerOrder sellerOrder) {
                return sellerOrder.getTransactionDescription();
            }
            return "Wallet Transaction";
        }
        return "Unknown Transaction";
    }

    public void withdrawBalance(Seller seller) {
        printWithdrawHeader();
        System.out.println(OPTION + "  Current Balance: " + HIGHLIGHT + String.format("%.2f $", this.getWalletBalance()) + RESET);
        double amount = promptAmount("Enter amount to withdraw (or type CANCEL): ");
        if (amount <= 0) {
            return;
        }

        if (amount > this.getWalletBalance()) {
            showError("Insufficient balance!");
            Pause.pause(1500);
            withdrawBalance(seller);
            return;
        }

        setWalletBalance(this.getWalletBalance() - amount, "Withdraw Balance From Wallet");
        printWithdrawSuccess();
    }

    private void printWithdrawHeader() {
        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║" + BOLD + HIGHLIGHT + "           WITHDRAW MONEY            " + RESET + TITLE + "║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
    }

    private double promptAmount(String promptMessage) {
        while (true) {
            System.out.print(OPTION + "  " + promptMessage + RESET + HIGHLIGHT);
            String input = scan.nextLine().trim();
            System.out.print(RESET);

            if ("cancel".equalsIgnoreCase(input)) {
                return 0;
            }

            if (!input.matches("\\d+(\\.\\d+)?")) {
                showError("Invalid amount! Example: 50 or 12.5");
                continue;
            }

            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                showError("Amount must be greater than 0.");
                continue;
            }
            return amount;
        }
    }

    private void printWithdrawSuccess() {
        System.out.println(SUCCESS + "\n═══════════════════════════════════════");
        System.out.println("  🎉 " + BOLD + "WITHDRAWAL SUCCESSFUL!            " + RESET + SUCCESS);
        System.out.printf("  New Balance: %.2f $%23s %n", this.getWalletBalance(), "");
        System.out.println("═══════════════════════════════════════" + RESET);
        Pause.pause(2000);
    }

    public void displayWallet(Person person) {
        printWalletHeader();

        if (person instanceof Customer customer) {
            displayBalance(this.getWalletBalance());
            if (promptYesNoBack("Would you like to add money?")) {
                addBalance(customer);
            }
        } else if (person instanceof Seller) {
            displayBalance(this.getWalletBalance());
            promptBackOnly();
        }
    }

    private void printWalletHeader() {
        System.out.println();
        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║" + BOLD + HIGHLIGHT + "             MY WALLET               " + RESET + TITLE + "║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
    }

    private void displayBalance(double balance) {
        System.out.println(OPTION + "  Balance: " + HIGHLIGHT + String.format("%.2f $", balance) + RESET);
        System.out.println(TITLE + "═══════════════════════════════════════" + RESET);
    }

    private boolean promptYesNoBack(String message) {
        while (true) {
            System.out.println(OPTION + "  " + message);
            System.out.println("  [Y] Yes     [N] No     [BACK] Return");
            System.out.print(PROMPT + "\n  Your choice: " + RESET + HIGHLIGHT);
            String choice = scan.nextLine().trim().toUpperCase();
            System.out.print(RESET);
            switch (choice) {
                case "Y":
                    return true;
                case "N":
                case "BACK":
                    return false;
                default:
                    showError("⚠ Please enter a valid choice (Y/N/BACK)");
            }
        }
    }

    private void promptBackOnly() {
        while (true) {
            System.out.println(OPTION + "  [B] Back");
            System.out.print(PROMPT + "\n  Your choice: " + RESET + HIGHLIGHT);
            String choice = scan.nextLine().trim().toUpperCase();
            System.out.print(RESET);
            if ("B".equals(choice)) {
                return;
            }
            showError("⚠ Please enter a valid choice");
        }
    }

    public void addBalance(Customer customer) {
        printAddBalanceHeader();
        System.out.println(OPTION + "  Current Balance: " + HIGHLIGHT + String.format("%.2f $", this.getWalletBalance()) + RESET);
        String input = promptAmountOrCancel();
        if (input == null) {
            return;
        }
        double amount = Double.parseDouble(input);
        setWalletBalance(this.getWalletBalance() + amount, "Change Wallet Balance");
        printSuccessBalanceUpdated();
    }

    private void printAddBalanceHeader() {
        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║" + BOLD + HIGHLIGHT + "           ADD TO BALANCE            " + RESET + TITLE + "║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
    }

    private String promptAmountOrCancel() {
        while (true) {
            System.out.print(OPTION + "  Enter amount (or type CANCEL): " + RESET + HIGHLIGHT);
            String input = scan.nextLine().trim();
            System.out.print(RESET);
            if ("cancel".equalsIgnoreCase(input)) {
                return null;
            }
            if (!input.matches("\\d+(\\.\\d+)?")) {
                showError("Invalid amount! Example: 50 or 12.5");
                continue;
            }
            double amount = Double.parseDouble(input);
            if (amount <= 0) {
                showError("Amount must be greater than 0.");
                continue;
            }
            return input;
        }
    }

    private void printSuccessBalanceUpdated() {
        System.out.println(SUCCESS + "\n═══════════════════════════════════════");
        System.out.println("  🎉 " + BOLD + "BALANCE UPDATED SUCCESSFULLY!    " + RESET + SUCCESS);
        System.out.printf("  New Balance: %.2f $%23s %n", this.getWalletBalance(), "");
        System.out.println("═══════════════════════════════════════" + RESET);
        Pause.pause(2000);
    }


    public void displayTransactions(Customer customer) {
        Instant filterStart = null, filterEnd = null;

        while (true) {
            List<Transaction> allTransactions = this.getTransactionList();
            List<Transaction> list = getFilteredTransactionList(allTransactions, filterStart, filterEnd);

            printTransactionHeader();
            if (list.isEmpty()) {
                printNoTransactions();
                return;
            }
            displayTransactionMenu(list);
            printTransactionOptions();
            String choice = readUserChoice();
            if (handleTransactionChoice(choice, list, customer)) {
                return;
            } else if ("FILTER".equalsIgnoreCase(choice)) {
                filterStart = getValidDate("Enter start date (yyyy-MM-dd): ");
                filterEnd = getValidDate("Enter end date (yyyy-MM-dd): ");
            } else if ("CLEAR".equalsIgnoreCase(choice)) {
                filterStart = null;
                filterEnd = null;
                System.out.println(SUCCESS + "Filter cleared." + RESET);
                Pause.pause(1000);
            }
        }
    }

    private void printTransactionHeader() {
        System.out.println(TITLE + "╔═════════════════════════════════════╗");
        System.out.println("║" + BOLD + HIGHLIGHT + "          TRANSACTION HISTORY        " + RESET + TITLE + "║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
    }

    private void printNoTransactions() {
        System.out.println(ERROR + "No transactions found." + RESET);
        Pause.pause(1500);
    }

    private void printTransactionOptions() {
        System.out.println(TITLE + "\n╔═════════════════════════════════════╗");
        System.out.println("║" + OPTION + " Select a transaction by index       " + TITLE + "║");
        System.out.println("║" + OPTION + " Type 'FILTER' to filter by date     " + TITLE + "║");
        System.out.println("║" + OPTION + " Type 'CLEAR' to clear filters       " + TITLE + "║");
        System.out.println("║" + OPTION + " Type 'BACK' to return               " + TITLE + "║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
    }

    private String readUserChoice() {
        System.out.print(PROMPT + "Your choice: " + RESET);
        return scan.nextLine().trim();
    }

    private boolean handleTransactionChoice(String choice, List<Transaction> list, Customer customer) {
        switch (choice.toUpperCase()) {
            case "BACK":
                return true;
            default:
                if (!choice.matches("\\d+")) {
                    showError("⚠ Enter a valid index number!");
                    return false;
                }
                int index = Integer.parseInt(choice);
                if (index <= 0 || index > list.size()) {
                    showError("⚠ Selected index out of range!");
                    return false;
                }
                handleTransactionSelection(list.get(index - 1), customer);
                return false;
        }
    }

    private Instant getValidDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(PROMPT + prompt + RESET);
            String input = scan.nextLine().trim();

            try {
                LocalDate date = LocalDate.parse(input, formatter);
                return date.atStartOfDay(ZoneId.systemDefault()).toInstant();
            } catch (DateTimeParseException e) {
                showError("⚠ Invalid date format! Use yyyy-MM-dd (e.g., 2025-06-03)");
            }
        }
    }

    private void displayTransactionMenu(List<Transaction> list) {
        int counter = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

        for (Transaction transaction : list) {
            String formattedTime = formatter.format(transaction.getTimestamp());
            double amount = transaction.getAmount();
            String color = amount > 0 ? SUCCESS : ERROR;
            String description = null;
            if (transaction instanceof TransactionWithOrder) {
                description = "Pay Order Price";
            } else if (transaction instanceof TransactionWithoutOrder withoutOrder) {
                description = withoutOrder.getDescription();
            }
            System.out.println(BOLD + "\n[" + counter++ + "]" + RESET);
            System.out.println(OPTION + "• Time: " + HIGHLIGHT + formattedTime + RESET);
            System.out.println(OPTION + "• Amount: " + color + String.format("%.2f $", amount) + RESET);
            System.out.println(OPTION + "• Type: " + HIGHLIGHT + description + RESET);
        }
    }

    private List<Transaction> getFilteredTransactionList(List<Transaction> all, Instant start, Instant end) {
        if (start == null || end == null) {
            return all;
        }
        return all.stream().filter(t -> !t.getTimestamp().isBefore(start) && !t.getTimestamp().isAfter(end)).toList();
    }

    private void handleTransactionSelection(Transaction transaction, Customer customer) {
        if (transaction instanceof TransactionWithoutOrder withoutOrder) {
            System.out.println(ERROR + "\n No additional data for this transaction." + RESET);
            System.out.println(OPTION + "• Type: " + HIGHLIGHT + withoutOrder.getDescription() + RESET);
            System.out.println(PROMPT + "\nPress anything to continue..." + RESET);
            scan.nextLine();
            return;
        }
        System.out.println(OPTION + "\n• Linked Order: " + HIGHLIGHT + "(Shown Below)" + RESET);
        DisplayOrder.getInstance().display(customer);
        while (true) {
            printTransactionMenu();
            String choice = scan.nextLine().trim();
            if (!choice.matches("[0-2]")) {
                showError("⚠ Please enter 0, 1, or 2.");
                continue;
            }
            switch (choice) {
                case "1" -> DisplayOrder.getInstance().display(customer);
                case "0" -> {
                    return;
                }
                default -> {}
            }
            break;
        }
    }

    private void printTransactionMenu() {
        System.out.println(TITLE + "\n╔═════════════════════════════════════╗");
        System.out.println("║" + BOLD + HIGHLIGHT + "       TRANSACTION OPTIONS           " + RESET + TITLE + "║");
        System.out.println("╠═════════════════════════════════════╣");
        System.out.println("║" + OPTION + " 1. Display Order Details            " + TITLE + "║");
        System.out.println("║" + OPTION + " 2. Back to Transaction List         " + TITLE + "║");
        System.out.println("║" + OPTION + " 0. Return to Wallet Menu            " + TITLE + "║");
        System.out.println("╚═════════════════════════════════════╝" + RESET);
        System.out.print(PROMPT + "Your choice: " + RESET);
    }

    private void showError(String message) {
        System.out.println(ERROR + "\n " + message + RESET);
        Pause.pause(1500);
    }
}