package ir.ac.kntu;

import java.util.Scanner;

public class DisplayProduct {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    public static DisplayProduct getInstance() {
        return new DisplayProduct();
    }

    public void display(Product product, Customer customer) {
        System.out.println(ANSI_CYAN + "==================== Product View ===================" + ANSI_RESET);

        displayField("Name", product.getFullName());
        displayField("Price", product.getPrice());
        displayField("Stock", String.valueOf(product.getStock()));
        displayField("Description", product.getDescription());

        if (product instanceof Book) {
            displayBookDetails((Book) product);
        } else if (product instanceof Phone) {
            displayPhoneDetails((Phone) product);
        } else if (product instanceof LopTop) {
            displayLaptopDetails((LopTop) product);
        } else if (product instanceof DigitalProduct) {
            displayDigitalProductDetails((DigitalProduct) product);
        }

        if (product.getErrorList() != null && !product.getErrorList().isEmpty()) {
            System.out.println(ANSI_RED + "\n⚠️ Validation Errors:" + ANSI_RESET);
            for (String error : product.getErrorList()) {
                System.out.println(ANSI_RED + "- " + error + ANSI_RESET);
            }
        }

        System.out.println(ANSI_CYAN + "==================================================" + ANSI_RESET);

        showActionMenu(product, customer);
    }

    private void showActionMenu(Product product, Customer customer) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n" + ANSI_PURPLE + "╔════════════════════════════════════╗");
        System.out.println("║          ACTIONS MENU           ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ " + ANSI_BLUE + "1. Add to Cart" + ANSI_PURPLE +
                "                   ║");
        System.out.println("║ " + ANSI_BLUE + "2. Back to List" + ANSI_PURPLE +
                "                  ║");
        System.out.println("╚════════════════════════════════════╝" + ANSI_RESET);

        System.out.print(ANSI_GREEN + "Enter your choice: " + ANSI_RESET);
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                if (product.getStock() > 0) {
                    customer.getCart().addProduct(product);
                    System.out.println(ANSI_GREEN + "\nProduct added to cart successfully!" + ANSI_RESET);
                } else {
                    System.out.println(ANSI_RED + "\nProduct is out of stock!" + ANSI_RESET);
                }
                break;
            case "2":

                break;
            default:
                System.out.println(ANSI_RED + "Invalid choice!" + ANSI_RESET);
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void displayField(String label, String value) {
        if (value == null || value.isEmpty()) {
            System.out.printf(ANSI_YELLOW + "%-20s: " + ANSI_RED + "Not Available\n" + ANSI_RESET, label);
        } else {
            System.out.printf(ANSI_GREEN + "%-20s: " + ANSI_RESET + "%s\n", label, value);
        }
    }

    public void displayBookDetails(Book book) {
        System.out.println("\n" + ANSI_CYAN + "── Book Details ──" + ANSI_RESET);
        displayField("Author", book.getAuthor());
        displayField("Pages", book.getNumberOfPage());
        displayField("Genre", book.getGenre());
        displayField("ISBN", book.getISBN());
        displayField("Publish Date", book.getPublishDate());
        displayField("Weight", book.getWeight());
    }

    public void displayPhoneDetails(Phone phone) {
        System.out.println("\n" + ANSI_CYAN + "── Phone Specifications ──" + ANSI_RESET);
        displayDigitalProductDetails(phone);
        displayField("Main Camera", phone.getMainCamResolution());
        displayField("Front Camera", phone.getFrontCamResolution());
        displayField("Network", phone.getNetworkInfo());
        displayField("SD Card Support", phone.isSupportSDCard() ? "Yes" : "No");
    }

    public void displayLaptopDetails(LopTop laptop) {
        System.out.println("\n" + ANSI_CYAN + "── Laptop Specifications ──" + ANSI_RESET);
        displayDigitalProductDetails(laptop);
        displayField("GPU Chipset", laptop.getGPUChipset());
        displayField("Bluetooth", laptop.isSupportBluetooth() ? "Yes" : "No");
        displayField("Webcam", laptop.isHasWebCam() ? "Yes" : "No");
        displayField("RAM Generation", laptop.getRAMGeneration());
    }

    public void displayDigitalProductDetails(DigitalProduct digitalProduct) {
        displayField("Brand", digitalProduct.getBrand());
        displayField("Storage", digitalProduct.getInternalStorage());
        displayField("RAM", digitalProduct.getRAM());
        displayField("OS", digitalProduct.getOS());
        displayField("Battery", digitalProduct.getBatteryCapacity());
        displayField("Chipset", digitalProduct.getChipset());
    }
}