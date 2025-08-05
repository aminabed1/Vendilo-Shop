package ir.ac.kntu;

import java.io.Serializable;

public class Book extends Product implements Serializable {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";

    final private String author;
    final private String numberOfPage;
    final private String genre;
    final private String ISBN;
    final private String publishDate;
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

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String displayField(String key, String value) {
        return super.displayField(key, value);
    }

    public String toString() {
        return super.toString() +
                "\n" + ANSI_CYAN + "        ── Book Details ──" + ANSI_RESET +
                "\n\n" +
                displayField("Author", author) +
                displayField("Pages", numberOfPage) +
                displayField("Genre", genre) +
                displayField("ISBN", ISBN) +
                displayField("Publish Date", publishDate) +
                displayField("Weight", weight);
    }
}
