package ir.ac.kntu;

import java.io.Serializable;
import java.util.*;

public class Search implements Serializable {
    private Customer currentCustomer;
    private final static Scanner scan = new Scanner(System.in);

    public static Search getInstance() {
        return new Search();
    }

    public void handleSearch(Customer customer) {
        currentCustomer = customer;

        while (true) {
            System.out.println("Enter search term: ");
            System.out.println("1. Search by product name");
            System.out.println("2. Search by product category");
            System.out.println("3. Combined search");
            System.out.println("4. Exit");

            String choice = scan.nextLine();
            switch (choice) {
                case "1" -> searchByName();
                case "2" -> searchByCategory();
                case "3" -> combinedSearch();
                case "4" -> {
                    return;
                }
                default -> {
                    System.out.println("Please enter a valid choice!");
                }
            }
        }
    }

    public void searchByName() {
        System.out.print("Enter Product name: ");
        String name = scan.nextLine();

        List<Product> products = DataBase.getInstance().getProductList();
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.getFullName().toLowerCase().contains(name.toLowerCase())) {
                filteredProducts.add(product);
            }
        }

        displayProducts(filteredProducts);
    }

    public void searchByCategory() {
        System.out.println("Enter Product category: ");
        System.out.println("1. Digital products");
        System.out.println("2. Books");

        String category = null;
        String choice = scan.nextLine();

        if ("1".equals(choice)) {
            category = "Digital product";
        } else if ("2".equals(choice)) {
            category = "Book";
        } else {
            System.out.println("Invalid choice. Returning to main menu.");
            return;
        }

        List<Product> products = DataBase.getInstance().getProductList();
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                filteredProducts.add(product);
            }
        }

        displayProducts(filteredProducts);
    }

    public void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        System.out.println("Search Results:");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println((i + 1) + ". Product Name : " + product.getFullName());
            System.out.println("   Price : " + product.getPrice() + " | Category : " + product.getCategory());
        }

        while (true) {
            System.out.print("Enter product index to view full details (or '0' to return): ");
            String input = scan.nextLine().trim();
            if ("0".equals(input)) {
                return;
            }

            try {
                int index = Integer.parseInt(input);
                if (index < 1 || index > products.size()) {
                    System.out.println("Invalid index. Try again.");
                } else {
                    Product selected = products.get(index - 1);
                    DisplayProduct display = DisplayProduct.getInstance();
                    display.display(selected, currentCustomer);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public void combinedSearch() {
        List<Product> filteredProducts = new ArrayList<>(DataBase.getInstance().getProductList());
        if (askYesNo("Do you want to filter by category? (y/n): ")) {
            filteredProducts = filterByCategory(filteredProducts);
        }
        if (askYesNo("Do you want to filter by name? (y/n): ")) {
            filteredProducts = filterByName(filteredProducts);
        }
        if (askYesNo("Do you want to filter by price? (y/n): ")) {
            filteredProducts = filterByPrice(filteredProducts);
        }
        if (askYesNo("Do you want to filter by rating range? (y/n): ")) {
            filteredProducts = filterByRating(filteredProducts);
        }
        if (filteredProducts.isEmpty()) {
            System.out.println("No products found in the specified filters.");
        } else {
            System.out.println("Filtered products:");
            printProductsList(filteredProducts);
            System.out.print("Choose product index to see details or 0 to go back: ");
            String input = scan.nextLine().trim();
            if ("0".equals(input)) {
                return;
            }
            handleProductSelection(input, filteredProducts, currentCustomer);
        }
    }

    private void handleProductSelection(String input, List<Product> products, Customer customer) {
        if (input == null || !input.matches("\\d+")) {
            System.out.println("Please enter a valid number!");
            return;
        }

        int index = Integer.parseInt(input);
        if (index >= 1 && index <= products.size()) {
            Product selectedProduct = products.get(index - 1);
            DisplayProduct.getInstance().display(selectedProduct, customer);
        } else {
            System.out.println("Invalid index! Try again.");
        }
    }

    private void printProductsList(List<Product> products) {
        System.out.println("╔══════════════ PRODUCTS LIST ═══════════════╗");

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.printf("║ %d. %s | Price: %s | Type: %s%n",
                    i + 1,
                    product.getFullName(),
                    product.getPrice(),
                    product.getCategory()
            );
        }
        System.out.println("║ 0. Back to category selection              ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();
    }

    private boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String choice = scan.nextLine().trim().toLowerCase();
            if ("y".equals(choice)) {
                return true;
            }
            if ("n".equals(choice)) {
                return false;
            }
            System.out.println("Please enter a valid choice!");
        }
    }

    private List<Product> filterByCategory(List<Product> products) {
        System.out.println("1. Digital products");
        System.out.println("2. Books");
        String choice = scan.nextLine();

        String category;
        if ("1".equals(choice)) {
            category = "Digital Product";
        } else if ("2".equals(choice)) {
            category = "Book";
        } else {
            System.out.println("Invalid category, skipping category filter.");
            return products;
        }

        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    private List<Product> filterByName(List<Product> products) {
        System.out.print("Enter product name: ");
        String name = scan.nextLine();

        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getFullName().toLowerCase().contains(name.toLowerCase())) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    private List<Product> filterByPrice(List<Product> products) {
        double minPrice = getValidDouble("Enter minimum price: ");
        double maxPrice = getValidDouble("Enter maximum price: ");
        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            double price = Double.parseDouble(product.getPrice());
            if (price >= minPrice && price <= maxPrice) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    private List<Product> filterByRating(List<Product> products) {
        double minRate = getValidDouble("Enter minimum rating: ");
        double maxRate = getValidDouble("Enter maximum rating: ");
        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getRatingMap().isEmpty()) {
                continue;
            }
            double rating = calcAverageRating(product);
            if (rating >= minRate && rating <= maxRate) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    private double calcAverageRating(Product product) {
        double rating = 0;
        for (ProductReview review : product.getRatingMap().values()) {
            rating += review.getRating();
        }
        return rating / product.getRatingMap().size();
    }

    private double getValidDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scan.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}
