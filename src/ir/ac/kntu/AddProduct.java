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
        if (productType == null) return;

        Product newProduct = switch (productType) {
            case "Book" -> addBook(seller);
            case "LopTop" -> addLaptop(seller);
            case "Phone" -> addPhone(seller);
            default -> null;
        };

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
                case "1" -> {
                    String digitalType = selectDigitalProductType();
                    if (digitalType != null) return digitalType;
                }
                case "2" -> { return "Book"; }
                case "3" -> { return null; }
                default -> showError("Invalid choice, enter 1-3");
            }
        }
    }

    private String selectDigitalProductType() {
        while (true) {
            printMenu("Select a DigitalProduct",
                    new String[]{"1. Laptop", "2. Phone", "3. Back"});

            String digitalChoice = scan.nextLine().trim();
            switch (digitalChoice) {
                case "1" -> { return "LopTop"; }
                case "2" -> { return "Phone"; }
                case "3" -> { return null; }
                default -> showError("Invalid choice, enter 1-3");
            }
        }
    }

    private abstract static class ProductInfo {
        String brand = "";
        String price = "";
        String description = "";
        String stock = "";

        abstract String[] getFields();

        abstract String[] getFieldLabels();

        abstract boolean validate();

        abstract Product createProduct(Seller seller);
    }

    private static class BookInfo extends ProductInfo {
        String fullName = "";
        String author = "";
        String genre = "";
        String numberOfPage = "";
        String ISBN = "";

        @Override
        String[] getFields() {
            return new String[]{fullName, price, author, genre, numberOfPage, ISBN, stock, description};
        }

        @Override
        String[] getFieldLabels() {
            return new String[]{"1. Name", "2. Price", "3. Author", "4. Genre", "5. Page Count", "6. ISBN", "7. Stock", "8. Description"};
        }

        @Override
        boolean validate() {
            List<String> errors = new ArrayList<>();

            if (fullName.isEmpty()) errors.add("Name cannot be empty");
            if (price.isEmpty()) errors.add("Price cannot be empty");
            if (author.isEmpty()) errors.add("Author cannot be empty");
            if (genre.isEmpty()) errors.add("Genre cannot be empty");
            if (numberOfPage.isEmpty()) errors.add("Page count cannot be empty");
            if (ISBN.isEmpty()) errors.add("ISBN cannot be empty");
            if (stock.isEmpty()) errors.add("Stock cannot be empty");

            try {
                double priceValue = Double.parseDouble(price);
                if (priceValue <= 0) errors.add("Price must be positive");
            } catch (NumberFormatException e) {
                errors.add("Price must be a valid number");
            }

            try {
                int stockValue = Integer.parseInt(stock);
                if (stockValue < 0) errors.add("Stock cannot be negative");
            } catch (NumberFormatException e) {
                errors.add("Stock must be a valid integer");
            }

            if (!errors.isEmpty()) {
                for (String error : errors) {
                    System.out.println(ERROR + "Error: " + error + RESET);
                }
                return false;
            }

            return true;
        }

        @Override
        Product createProduct(Seller seller) {
            return new Book(fullName, price, description, author,
                    numberOfPage, genre, ISBN, null, "base", Integer.parseInt(stock), seller.getAgencyCode());
        }
    }

    private static class LaptopInfo extends ProductInfo {
        String internalStorage = "";
        String RAM = "";
        String OS = "";
        String batteryCapacity = "";
        String chipset = "";
        String GPUChipset = "";
        String supportBluetooth = "";
        String hasWebCam = "";
        String RAMGeneration = "";

        @Override
        String[] getFields() {
            return new String[]{brand, price, internalStorage, RAM, OS, batteryCapacity,
                    chipset, GPUChipset, supportBluetooth, hasWebCam, RAMGeneration, stock, description};
        }

        @Override
        String[] getFieldLabels() {
            return new String[]{"1. Brand", "2. Price", "3. Internal Storage", "4. RAM",
                    "5. OS", "6. Battery Capacity", "7. Chipset", "8. GPU Chipset",
                    "9. Bluetooth Support", "10. Webcam", "11. RAM Generation",
                    "12. Stock", "13. Description"};
        }

        @Override
        boolean validate() {
            List<String> errors = new ArrayList<>();

            if (brand.isEmpty()) errors.add("Brand cannot be empty");
            if (price.isEmpty()) errors.add("Price cannot be empty");
            if (internalStorage.isEmpty()) errors.add("Internal storage cannot be empty");
            if (RAM.isEmpty()) errors.add("RAM cannot be empty");
            if (OS.isEmpty()) errors.add("OS cannot be empty");
            if (batteryCapacity.isEmpty()) errors.add("Battery capacity cannot be empty");
            if (chipset.isEmpty()) errors.add("Chipset cannot be empty");
            if (GPUChipset.isEmpty()) errors.add("GPU chipset cannot be empty");
            if (RAMGeneration.isEmpty()) errors.add("RAM generation cannot be empty");
            if (stock.isEmpty()) errors.add("Stock cannot be empty");

            try {
                double priceValue = Double.parseDouble(price);
                if (priceValue <= 0) errors.add("Price must be positive");
            } catch (NumberFormatException e) {
                errors.add("Price must be a valid number");
            }

            try {
                int stockValue = Integer.parseInt(stock);
                if (stockValue < 0) errors.add("Stock cannot be negative");
            } catch (NumberFormatException e) {
                errors.add("Stock must be a valid integer");
            }

            if (!supportBluetooth.equalsIgnoreCase("yes") && !supportBluetooth.equalsIgnoreCase("no")) {
                errors.add("Bluetooth support must be 'yes' or 'no'");
            }

            if (!hasWebCam.equalsIgnoreCase("yes") && !hasWebCam.equalsIgnoreCase("no")) {
                errors.add("Webcam must be 'yes' or 'no'");
            }

            if (!errors.isEmpty()) {
                for (String error : errors) {
                    System.out.println(ERROR + "Error: " + error + RESET);
                }
                return false;
            }

            return true;
        }

        @Override
        Product createProduct(Seller seller) {
            return new LopTop(brand, price, internalStorage, RAM, OS, batteryCapacity,
                    chipset, GPUChipset, supportBluetooth.equals("yes"),
                    hasWebCam.equals("yes"), RAMGeneration,
                    Integer.parseInt(stock), seller.getAgencyCode());
        }
    }

    private static class PhoneInfo extends ProductInfo {
        String internalStorage = "";
        String RAM = "";
        String OS = "";
        String batteryCapacity = "";
        String chipset = "";
        String mainCamResolution = "";
        String frontCamResolution = "";
        String networkInfo = "";
        String supportSDCard = "";

        @Override
        String[] getFields() {
            return new String[]{brand, price, internalStorage, RAM, OS, batteryCapacity,
                    chipset, mainCamResolution, frontCamResolution,
                    networkInfo, supportSDCard, stock, description};
        }

        @Override
        String[] getFieldLabels() {
            return new String[]{"1. Brand", "2. Price", "3. Internal Storage", "4. RAM",
                    "5. OS", "6. Battery Capacity", "7. Chipset", "8. Main Camera Res",
                    "9. Front Camera Res", "10. Network Info", "11. SD Card Support",
                    "12. Stock", "13. Description"};
        }

        @Override
        boolean validate() {
            List<String> errors = new ArrayList<>();

            if (brand.isEmpty()) errors.add("Brand cannot be empty");
            if (price.isEmpty()) errors.add("Price cannot be empty");
            if (internalStorage.isEmpty()) errors.add("Internal storage cannot be empty");
            if (RAM.isEmpty()) errors.add("RAM cannot be empty");
            if (OS.isEmpty()) errors.add("OS cannot be empty");
            if (batteryCapacity.isEmpty()) errors.add("Battery capacity cannot be empty");
            if (chipset.isEmpty()) errors.add("Chipset cannot be empty");
            if (mainCamResolution.isEmpty()) errors.add("Main camera resolution cannot be empty");
            if (frontCamResolution.isEmpty()) errors.add("Front camera resolution cannot be empty");
            if (networkInfo.isEmpty()) errors.add("Network info cannot be empty");
            if (stock.isEmpty()) errors.add("Stock cannot be empty");

            try {
                double priceValue = Double.parseDouble(price);
                if (priceValue <= 0) errors.add("Price must be positive");
            } catch (NumberFormatException e) {
                errors.add("Price must be a valid number");
            }

            try {
                int stockValue = Integer.parseInt(stock);
                if (stockValue < 0) errors.add("Stock cannot be negative");
            } catch (NumberFormatException e) {
                errors.add("Stock must be a valid integer");
            }

            if (!supportSDCard.equalsIgnoreCase("yes") && !supportSDCard.equalsIgnoreCase("no")) {
                errors.add("SD card support must be 'yes' or 'no'");
            }


            if (!errors.isEmpty()) {
                for (String error : errors) {
                    System.out.println(ERROR + "Error: " + error + RESET);
                }
                return false;
            }

            return true;
        }

        @Override
        Product createProduct(Seller seller) {
            return new Phone(brand, internalStorage, RAM, OS, batteryCapacity,
                    chipset, mainCamResolution, frontCamResolution,
                    networkInfo, supportSDCard.equals("yes"), price,
                    Integer.parseInt(stock), seller.getAgencyCode());
        }
    }

    public Book addBook(Seller seller) {
        return (Book) addProduct(new BookInfo(), "BOOK INFORMATION", seller);
    }

    public LopTop addLaptop(Seller seller) {
        return (LopTop) addProduct(new LaptopInfo(), "LAPTOP INFORMATION", seller);
    }

    public Phone addPhone(Seller seller) {
        return (Phone) addProduct(new PhoneInfo(), "PHONE INFORMATION", seller);
    }

    private Product addProduct(ProductInfo info, String title, Seller seller) {
        while (true) {
            printProductInfoMenu(info, title);

            System.out.print(PROMPT + "Enter your choice: " + RESET + HIGHLIGHT);
            String choice = scan.nextLine().trim();
            System.out.print(RESET);

            int lastOption = info.getFieldLabels().length + 1;
            int backOption = lastOption + 1;

            if (choice.equals(String.valueOf(backOption))) {
                return null;
            }

            if (choice.equals(String.valueOf(lastOption))) {
                if (info.validate()) {
                    Product product = info.createProduct(seller);
                    System.out.println(SUCCESS + "Product validated successfully!" + RESET);
                    return product;
                } else {
                    showError("Please fix the validation errors");
                    pause(2000);
                }
            } else {
                processProductInput(choice, info);
            }
        }
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

    private void printProductInfoMenu(ProductInfo info, String title) {
        final int BOX_WIDTH = 60;
        final int LABEL_WIDTH = 25;
        final int VALUE_WIDTH = 30;

        System.out.println(MENU + "╔" + "═".repeat(BOX_WIDTH - 2) + "╗");

        String centeredTitle = centerText(title);
        System.out.println("║ " + BOLD + centeredTitle + RESET + MENU + " ║");

        System.out.println("╠" + "═".repeat(BOX_WIDTH - 2) + "╣");

        String[] fields = info.getFields();
        String[] labels = info.getFieldLabels();

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i].substring(3);
            String value = fields[i].isEmpty() ? "Empty" : fields[i];

            if (value.length() > VALUE_WIDTH) {
                value = value.substring(0, VALUE_WIDTH - 3) + "...";
            }

            System.out.printf("║ " + OPTION + "%-" + LABEL_WIDTH + "s: " +
                    HIGHLIGHT + "%-" + VALUE_WIDTH + "s" +
                    MENU + "║\n", label, value);
        }

        System.out.println("╠" + "═".repeat(BOX_WIDTH - 2) + "╣");
        System.out.printf("║ " + OPTION + "%d. Confirm Information" +
                        MENU + "%" + (BOX_WIDTH - 24 - String.valueOf(labels.length + 1).length()) + "s║\n",
                labels.length + 1, "");
        System.out.printf("║ " + OPTION + "%d. Back" +
                        MENU + "%" + (BOX_WIDTH - 9 - String.valueOf(labels.length + 2).length()) + "s║\n",
                labels.length + 2, "");
        System.out.println("╚" + "═".repeat(BOX_WIDTH - 2) + "╝" + RESET);
    }

    private String centerText(String text) {
        if (text.length() >= 56) return text.substring(0, 56);
        int leftPadding = (56 - text.length()) / 2;
        int rightPadding = 56 - text.length() - leftPadding;
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }

    private void processProductInput(String choice, ProductInfo info) {
        try {
            int fieldIndex = Integer.parseInt(choice) - 1;
            String[] labels = info.getFieldLabels();
            if (fieldIndex >= 0 && fieldIndex < labels.length) {
                String prompt = labels[fieldIndex].substring(3) + ": ";
                System.out.print(PROMPT + prompt + RESET + HIGHLIGHT);
                String input = scan.nextLine().trim();

                setField(info, fieldIndex, input);
                System.out.print(RESET);
            } else {
                showError("Please enter a valid choice!");
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid number!");
        }
    }

    private void setField(ProductInfo info, int index, String value) {
        try {
            if (info instanceof BookInfo book) {
                switch (index) {
                    case 0 -> book.fullName = value;
                    case 1 -> book.price = value;
                    case 2 -> book.author = value;
                    case 3 -> book.genre = value;
                    case 4 -> book.numberOfPage = value;
                    case 5 -> book.ISBN = value;
                    case 6 -> book.stock = value;
                    case 7 -> book.description = value;
                }
            } else if (info instanceof LaptopInfo lap) {
                switch (index) {
                    case 0 -> lap.brand = value;
                    case 1 -> lap.price = value;
                    case 2 -> lap.internalStorage = value;
                    case 3 -> lap.RAM = value;
                    case 4 -> lap.OS = value;
                    case 5 -> lap.batteryCapacity = value;
                    case 6 -> lap.chipset = value;
                    case 7 -> lap.GPUChipset = value;
                    case 8 -> lap.supportBluetooth = value.toLowerCase();
                    case 9 -> lap.hasWebCam = value.toLowerCase();
                    case 10 -> lap.RAMGeneration = value;
                    case 11 -> lap.stock = value;
                    case 12 -> lap.description = value;
                }
            } else if (info instanceof PhoneInfo phone) {
                switch (index) {
                    case 0 -> phone.brand = value;
                    case 1 -> phone.price = value;
                    case 2 -> phone.internalStorage = value;
                    case 3 -> phone.RAM = value;
                    case 4 -> phone.OS = value;
                    case 5 -> phone.batteryCapacity = value;
                    case 6 -> phone.chipset = value;
                    case 7 -> phone.mainCamResolution = value;
                    case 8 -> phone.frontCamResolution = value;
                    case 9 -> phone.networkInfo = value;
                    case 10 -> phone.supportSDCard = value.toLowerCase();
                    case 11 -> phone.stock = value;
                    case 12 -> phone.description = value;
                }
            }
        } catch (Exception e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }

    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
