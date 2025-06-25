package ir.ac.kntu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Product implements Serializable {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";

    private String fullName;
    private String price;
    private String description;
    private List<String> errorList;
    private int stock;
    private String category;
    private String sellerAgencyCode;
    private final String serialNumber;
    private HashMap<Person, Double> ratingMap;


    public Product(String category, String sellerAgencyCode) {
        int serialNumber = (int) (Math.random() * 899999 + 100000);
        this.serialNumber = String.valueOf(serialNumber);
        this.sellerAgencyCode = sellerAgencyCode;
        this.category = category;
        ratingMap = new HashMap<>();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSellerAgencyCode() {
        return sellerAgencyCode;
    }

    public void setSellerAgencyCode(String sellerAgencyCode) {
        this.sellerAgencyCode = sellerAgencyCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public HashMap<Person, Double> getRatingMap() {
        return ratingMap;
    }

    public boolean addRating(Person person, Double rating) {
        if (ratingMap.containsKey(person)) {
            return false;
        }
        ratingMap.put(person, rating);
        return true;
    }

    public String displayField(String key, String value) {
        String formattedKey = String.format("%-20s: ", key);
        if (value == null || value.isEmpty()) {
            return ANSI_YELLOW + formattedKey + ANSI_RED + "Not Available\n" + ANSI_RESET;
        } else {
            return ANSI_GREEN + formattedKey + ANSI_RESET + value + "\n";
        }
    }

    private String displayAverageRating() {
        if (ratingMap == null || ratingMap.isEmpty()) {
            return ANSI_YELLOW + "⭐ Average Rating: " + ANSI_RED +  "No ratings yet" + ANSI_RESET;
        }

        double sum = 0;
        for (double rating : ratingMap.values()) {
            sum += rating;
        }
        double average = sum / ratingMap.size();
        String formattedAverage = String.format("%.2f/5 (%d ratings)%s\n", average, ratingMap.size(), average < 2 ? " 😞" : average < 4 ? " 🙂" : " 😃");
        return ANSI_YELLOW + "⭐ Average Rating: " + ANSI_GREEN + formattedAverage + ANSI_RESET;
    }

    @Override
    public String toString() {
        return ANSI_CYAN + "==================== Product View ===================" + ANSI_RESET +
                "\n" +
                displayField("Name", fullName) +
                displayField("Price", price) +
                displayField("Stock", String.valueOf(stock)) +
                displayField("Serial Number", serialNumber) +
                displayField("Description", description) +
                "\n" +
                displayAverageRating() +
                "\n";
    }
}
