package ir.ac.kntu;

import java.util.List;

public abstract class Product {
    private String fullName;
    private String price;
    private String description;
    private List<String> errorList;
    private int stock;
    private String category;
    private String sellerAgencyCode;
    private String serialNumber;

    public Product(String category, String sellerAgencyCode) {
        int serialNumber = (int) (Math.random() * 899999 + 100000);
        this.serialNumber = String.valueOf(serialNumber);
        this.category = category;
        this.sellerAgencyCode = sellerAgencyCode;
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

}
