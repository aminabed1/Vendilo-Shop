package ir.ac.kntu;


import java.io.Serializable;

public class Book extends Product implements Serializable {
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

    public String  getAuthor() {
        return author;
    }


    public String getNumberOfPage() {
        return numberOfPage;
    }

    public String getGenre() {
        return genre;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

}
