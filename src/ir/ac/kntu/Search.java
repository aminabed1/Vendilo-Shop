package ir.ac.kntu;

import java.util.*;

public class Search {
    private Customer currentCustomer;
    private final static Scanner scan = new Scanner(System.in);
    public static Search getInstance() {
        return new Search();
    }

    public void handleSearch(Customer customer) {
        currentCustomer = customer;

        while (true) {
            System.out.print("Enter search term: ");
            System.out.println("1. Search by product name");
            System.out.println("2. Search by product category");
            System.out.println("3. Combined search");
            System.out.println("4. Exit");

            String choice = scan.nextLine();
            switch (choice) {
                case "1" -> searchByName();
                case "2" -> searchByCategory();
                case "3" -> combinedSearch();
                case "4" -> {return;}
                default -> {
                    System.out.println("Please enter a valid choice!");
                    continue;
                }
            };
        }

    }

    public void searchByName() {
        System.out.print("Enter Product name: ");
        String name = scan.nextLine();

        List<Product> products = DataBase.getProductList();
        List<Product> filteredProducts = new ArrayList<>();
        DisplayProduct display = DisplayProduct.getInstance();

        for (Product product : products) {
            if (product.getFullName().toLowerCase().contains(name.toLowerCase())) {
                display.display(product, currentCustomer);
            }
        }
    }

    public void searchByCategory() {
        System.out.print("Enter Product category: ");
        System.out.println("1. Digital products");
        System.out.println("2. Books");

        String category = null;
        String choice = scan.nextLine();

        if (choice.equals("1")) {
            category = "Digital products";
        } else {
            category = "Books";
        }

        List<Product> products = DataBase.getProductList();
        List<Product> filteredProducts = new ArrayList<>();
        DisplayProduct display = DisplayProduct.getInstance();

        for (Product product : products) {
            if (product.getCategory().toLowerCase().contains(category.toLowerCase())) {
                display.display(product, currentCustomer);
            }
        }
    }

    public void combinedSearch() {
        List<Product> filteredProducts = new ArrayList<>(DataBase.getProductList());

        while (true) {
            System.out.print("Do you want to filter by category? (y/n): ");
            String choice = scan.nextLine().trim().toLowerCase();

            if (choice.equals("y")) {
                filteredProducts = filterByCategory(filteredProducts);
                break;
            } else if (choice.equals("n")) {
                break;
            } else {
                System.out.println("Please enter a valid choice!");
            }
        }

        while (true) {
            System.out.print("Do you want to filter by name? (y/n): ");
            String choice = scan.nextLine().trim().toLowerCase();

            if (choice.equals("y")) {
                filteredProducts = filterByName(filteredProducts);
                break;
            } else if (choice.equals("n")) {
                break;
            } else {
                System.out.println("Please enter a valid choice!");
            }
        }

        double minPrice = getValidDouble("Enter minimum price: ");
        double maxPrice = getValidDouble("Enter maximum price: ");

        List<Product> finalFiltered = new ArrayList<>();
        for (Product product : filteredProducts) {
            if (Double.parseDouble(product.getPrice()) >= minPrice && Double.parseDouble(product.getPrice()) <= maxPrice) {
                finalFiltered.add(product);
            }
        }

        if (finalFiltered.isEmpty()) {
            System.out.println("No products found in the specified filters.");
        } else {
            System.out.println("Filtered products:");
            DisplayProduct display = DisplayProduct.getInstance();
            for (Product product : finalFiltered) {
                display.display(product, currentCustomer);
            }

        }
    }

    private List<Product> filterByCategory(List<Product> products) {
        System.out.println("1. Digital products");
        System.out.println("2. Books");
        String choice = scan.nextLine();

        String category = null;
        if (choice.equals("1")) {
            category = "Digital products";
        } else if (choice.equals("2")) {
            category = "Books";
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
