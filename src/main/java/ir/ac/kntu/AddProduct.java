package ir.ac.kntu;

import java.io.Serializable;
import java.util.*;

public class AddProduct implements Serializable {
    private static final String RESET = "\u001B[0m";
    private static final String TITLE = "\u001B[38;5;45m";
    private static final String MENU = "\u001B[38;5;39m";
    private static final String OPTION = "\u001B[38;5;159m";
    private static final String PROMPT = "\u001B[38;5;228m";
    private static final String SUCCESS = "\u001B[38;5;46m";
    private static final String ERROR = "\u001B[38;5;203m";
    private static final String HIGHLIGHT = "\u001B[38;5;231m";
    private static final String BOLD = "\u001B[1m";
    private final static Scanner scan = new Scanner(System.in);

    public static AddProduct getInstance() {
        return new AddProduct();
    }

    public void addNewProduct(Seller seller) {
        String productType = selectProductType();
        if (productType == null) {
            return;
        }

        Product newProduct = buildProduct(seller, productType);

        if (newProduct != null) {
            DataBase.addProduct(newProduct);
            System.out.println(SUCCESS + "Product added successfully!" + RESET);
        }
    }

    public String selectProductType() {
        while (true) {
            printMenu("Select product type",
                    new String[]{"1. DigitalProduct", "2. Book", "3. Cancel"});

            String choice = scan.nextLine().trim();

            switch (choice) {
                case "1": {
                    String digitalType = selectDigitalProductType();
                    if (digitalType != null) {
                        return digitalType;
                    }
                    break;
                }
                case "2": {
                    return "Book";
                }
                case "3": {
                    return null;
                }
                default: {
                    showError("Invalid choice, enter 1-3");
                    break;
                }
            }
        }
    }

    private String selectDigitalProductType() {
        while (true) {
            printMenu("Select a DigitalProduct",
                    new String[]{"1. Laptop", "2. Phone", "3. Back"});

            String digitalChoice = scan.nextLine().trim();
            switch (digitalChoice) {
                case "1": {
                    return "LopTop";
                }
                case "2": {
                    return "Phone";
                }
                case "3": {
                    return null;
                }
                default: {
                    showError("Invalid choice, enter 1-3");
                    break;
                }
            }
        }
    }

    private Product buildProduct(Seller seller, String productType) {
        ProductBuilder builder = ProductBuilderFactory.createBuilder(productType);
        if (builder == null) {
            return null;
        }

        while (true) {
            builder.displayCurrentState();
            int choice = getMenuChoice(builder.getFieldCount() + 2);

            if (choice == builder.getFieldCount() + 1) {
                try {
                    return builder.build(seller);
                } catch (IllegalStateException e) {
                    showError(e.getMessage());
                    pause(2000);
                }
            } else if (choice == builder.getFieldCount() + 2) {
                return null;
            } else {
                processFieldInput(builder, choice - 1);
            }
        }
    }

    private int getMenuChoice(int maxOption) {
        while (true) {
            System.out.print(PROMPT + "Enter your choice: " + RESET + HIGHLIGHT);
            try {
                int choice = Integer.parseInt(scan.nextLine().trim());
                if (choice >= 1 && choice <= maxOption) {
                    System.out.print(RESET);
                    return choice;
                }
                showError("Please enter a number between 1 and " + maxOption);
            } catch (NumberFormatException e) {
                showError("Please enter a valid number!");
            }
        }
    }

    private void processFieldInput(ProductBuilder builder, int fieldIndex) {
        String fieldName = builder.getFieldName(fieldIndex);
        System.out.print(PROMPT + fieldName + ": " + RESET + HIGHLIGHT);
        String value = scan.nextLine().trim();
        try {
            builder.setField(fieldIndex, value);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
            pause(1500);
        }
        System.out.print(RESET);
    }

    private void printMenu(String title, String[] options) {
        System.out.println(TITLE + "\n=== " + title + " ===" + RESET);
        for (String option : options) {
            System.out.println(OPTION + option + RESET);
        }
    }

    private void showError(String msg) {
        System.out.println(ERROR + "Error: " + msg + RESET);
    }

    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

interface ProductBuilder {
    void displayCurrentState();

    int getFieldCount();

    String getFieldName(int index);

    void setField(int index, String value) throws IllegalArgumentException;

    Product build(Seller seller) throws IllegalStateException;
}

class ProductBuilderFactory {
    public static ProductBuilder createBuilder(String productType) {
        switch (productType) {
            case "Book":
                return new BookBuilder();
            case "LopTop":
                return new LaptopBuilder();
            case "Phone":
                return new PhoneBuilder();
            default:
                return null;
        }
    }
}

abstract class BaseProductBuilder implements ProductBuilder {
    static final String RESET = "\u001B[0m";
    static final String MENU = "\u001B[38;5;39m";
    static final String OPTION = "\u001B[38;5;159m";
    static final String HIGHLIGHT = "\u001B[38;5;231m";
    static final String BOLD = "\u001B[1m";

    private final List<String> fields = new ArrayList<>();
    private final List<String> fieldNames = new ArrayList<>();
    private final List<String> fieldValues = new ArrayList<>();

    // Getters and Setters
    protected List<String> getFields() {
        return new ArrayList<>(fields);
    }

    protected List<String> getFieldNames() {
        return new ArrayList<>(fieldNames);
    }

    protected List<String> getFieldValues() {
        return new ArrayList<>(fieldValues);
    }

    protected void setFieldValue(int index, String value) {
        if (index >= 0 && index < fieldValues.size()) {
            fieldValues.set(index, value);
        }
    }

    protected String getFieldValue(int index) {
        if (index >= 0 && index < fieldValues.size()) {
            return fieldValues.get(index);
        }
        return "";
    }

    protected void addFieldName(String name) {
        fieldNames.add(name);
        fieldValues.add("");
    }

    protected void initializeFieldValues(int size) {
        fieldValues.clear();
        fieldValues.addAll(Collections.nCopies(size, ""));
    }

    @Override
    public void displayCurrentState() {
        final int BOX_WIDTH = 60;
        final int LABEL_WIDTH = 25;
        final int VALUE_WIDTH = 30;

        System.out.println(MENU + "╔" + "═".repeat(BOX_WIDTH - 2) + "╗");
        System.out.println("║ " + BOLD + centerText(getProductTitle()) + RESET + MENU + " ║");
        System.out.println("╠" + "═".repeat(BOX_WIDTH - 2) + "╣");

        for (int i = 0; i < fieldNames.size(); i++) {
            String label = fieldNames.get(i);
            String value = fieldValues.get(i).isEmpty() ? "Empty" : fieldValues.get(i);

            if (value.length() > VALUE_WIDTH) {
                value = value.substring(0, VALUE_WIDTH - 3) + "...";
            }

            System.out.printf("║ " + OPTION + "%-" + LABEL_WIDTH + "s: " +
                    HIGHLIGHT + "%-" + VALUE_WIDTH + "s" +
                    MENU + "║\n", label, value);
        }

        System.out.println("╠" + "═".repeat(BOX_WIDTH - 2) + "╣");
        System.out.printf("║ " + OPTION + "%d. Confirm Information" +
                        MENU + "%" + (BOX_WIDTH - 24 - String.valueOf(fieldNames.size() + 1).length()) + "s║\n",
                fieldNames.size() + 1, "");
        System.out.printf("║ " + OPTION + "%d. Back" +
                        MENU + "%" + (BOX_WIDTH - 9 - String.valueOf(fieldNames.size() + 2).length()) + "s║\n",
                fieldNames.size() + 2, "");
        System.out.println("╚" + "═".repeat(BOX_WIDTH - 2) + "╝" + RESET);
    }

    protected String centerText(String text) {
        if (text.length() >= 56) {
            return text.substring(0, 56);
        }
        int leftPadding = (56 - text.length()) / 2;
        int rightPadding = 56 - text.length() - leftPadding;
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }

    @Override
    public int getFieldCount() {
        return fieldNames.size();
    }

    @Override
    public String getFieldName(int index) {
        if (index >= 0 && index < fieldNames.size()) {
            return fieldNames.get(index);
        }
        return "";
    }

    protected void validateNumericField(String value, String fieldName, boolean isDouble) throws IllegalArgumentException {
        try {
            if (isDouble) {
                double num = Double.parseDouble(value);
                if (num <= 0) {
                    throw new IllegalArgumentException(fieldName + " must be positive");
                }
            } else {
                int num = Integer.parseInt(value);
                if (num < 0) {
                    throw new IllegalArgumentException(fieldName + " cannot be negative");
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number");
        }
    }

    abstract String getProductTitle();

    abstract void validate() throws IllegalStateException;
}

class BookBuilder extends BaseProductBuilder {
    public BookBuilder() {
        addFieldName("Name");
        addFieldName("Price");
        addFieldName("Author");
        addFieldName("Genre");
        addFieldName("Page Count");
        addFieldName("ISBN");
        addFieldName("Stock");
        addFieldName("Description");
    }

    @Override
    public void setField(int index, String value) throws IllegalArgumentException {
        if (index == 1 || index == 6) {
            validateNumericField(value, index == 1 ? "Price" : "Stock", index == 1);
        }
        setFieldValue(index, value);
    }

    @Override
    public Product build(Seller seller) throws IllegalStateException {
        validate();
        return new Book(
                getFieldValue(0), getFieldValue(1), getFieldValue(7),
                getFieldValue(2), getFieldValue(4), getFieldValue(3),
                getFieldValue(5), null, "base", Integer.parseInt(getFieldValue(6)),
                seller.getAgencyCode()
        );
    }

    @Override
    String getProductTitle() {
        return "BOOK INFORMATION";
    }

    @Override
    void validate() throws IllegalStateException {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < getFieldCount(); i++) {
            if (getFieldValue(i).isEmpty() && i != 7) {
                errors.add(getFieldName(i) + " cannot be empty");
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalStateException(String.join(", ", errors));
        }
    }
}

class LaptopBuilder extends BaseProductBuilder {
    public LaptopBuilder() {
        addFieldName("Brand");
        addFieldName("Price");
        addFieldName("Internal Storage");
        addFieldName("RAM");
        addFieldName("OS");
        addFieldName("Battery Capacity");
        addFieldName("Chipset");
        addFieldName("GPU Chipset");
        addFieldName("Bluetooth Support (yes/no)");
        addFieldName("Webcam (yes/no)");
        addFieldName("RAM Generation");
        addFieldName("Stock");
        addFieldName("Description");
    }

    @Override
    public void setField(int index, String value) throws IllegalArgumentException {
        if (index == 1 || index == 11) {
            validateNumericField(value, index == 1 ? "Price" : "Stock", index == 1);
        } else if (index == 8 || index == 9) {
            if (!value.equalsIgnoreCase("yes") && !value.equalsIgnoreCase("no")) {
                throw new IllegalArgumentException("Must be 'yes' or 'no'");
            }
            value = value.toLowerCase();
        }
        setFieldValue(index, value);
    }

    @Override
    public Product build(Seller seller) throws IllegalStateException {
        validate();
        return new LopTop(
                getFieldValue(0), getFieldValue(1), getFieldValue(2),
                getFieldValue(3), getFieldValue(4), getFieldValue(5),
                getFieldValue(6), getFieldValue(7),
                getFieldValue(8).equals("yes"),
                getFieldValue(9).equals("yes"), getFieldValue(10),
                Integer.parseInt(getFieldValue(11)), seller.getAgencyCode()
        );
    }

    @Override
    String getProductTitle() {
        return "LAPTOP INFORMATION";
    }

    @Override
    void validate() throws IllegalStateException {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < getFieldCount(); i++) {
            if (getFieldValue(i).isEmpty() && i != 12) {
                errors.add(getFieldName(i) + " cannot be empty");
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalStateException(String.join(", ", errors));
        }
    }
}

class PhoneBuilder extends BaseProductBuilder {
    public PhoneBuilder() {
        addFieldName("Brand");
        addFieldName("Price");
        addFieldName("Internal Storage");
        addFieldName("RAM");
        addFieldName("OS");
        addFieldName("Battery Capacity");
        addFieldName("Chipset");
        addFieldName("Main Camera Res");
        addFieldName("Front Camera Res");
        addFieldName("Network Info");
        addFieldName("SD Card Support (yes/no)");
        addFieldName("Stock");
        addFieldName("Description");
    }

    @Override
    public void setField(int index, String value) throws IllegalArgumentException {
        if (index == 1 || index == 11) {
            validateNumericField(value, index == 1 ? "Price" : "Stock", index == 1);
        } else if (index == 10) {
            if (!value.equalsIgnoreCase("yes") && !value.equalsIgnoreCase("no")) {
                throw new IllegalArgumentException("Must be 'yes' or 'no'");
            }
            value = value.toLowerCase();
        }
        setFieldValue(index, value);
    }

    @Override
    public Product build(Seller seller) throws IllegalStateException {
        validate();
        return new Phone(
                getFieldValue(0), getFieldValue(2), getFieldValue(3),
                getFieldValue(4), getFieldValue(5), getFieldValue(6),
                getFieldValue(7), getFieldValue(8), getFieldValue(9),
                getFieldValue(10).equals("yes"), getFieldValue(1),
                Integer.parseInt(getFieldValue(11)), seller.getAgencyCode()
        );
    }

    @Override
    String getProductTitle() {
        return "PHONE INFORMATION";
    }

    @Override
    void validate() throws IllegalStateException {
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < getFieldCount(); i++) {
            if (getFieldValue(i).isEmpty() && i != 12) {
                errors.add(getFieldName(i) + " cannot be empty");
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalStateException(String.join(", ", errors));
        }
    }
}