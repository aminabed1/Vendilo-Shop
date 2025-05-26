package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;

public class Book extends Product {
    private String author;
    private String numberOfPage;
    private String genre;
    private String ISBN;
    private String publishDate;
    private String weight;

    public Book(String fullName, String price, String description, String author, String numberOfPage,
                String genre, String ISBN, String publishDate, String weight, int stock, String sellerAgencyCode) {
        super("Book", sellerAgencyCode);
        this.author = author;
        this.numberOfPage = numberOfPage;
        this.genre = genre;
        this.ISBN = ISBN;
        this.publishDate = publishDate;
        this.setFullName(fullName);
        this.setPrice(price);
        this.setDescription(description);
        this.setWeight(weight);
        this.setStock(stock);
        this.setSellerAgencyCode(sellerAgencyCode);
    }

    public String  getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNumberOfPage() {
        return numberOfPage;
    }

    public void setNumberOfPage(String numberOfPage) {
        this.numberOfPage = numberOfPage;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

}
