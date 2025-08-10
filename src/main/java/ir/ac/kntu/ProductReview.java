package ir.ac.kntu;

public class ProductReview {
    private final double rating;
    private final String reviewText;

    public ProductReview(double rating, String reviewText) {
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public String getReviewText() {
        return reviewText;
    }

    public double getRating() {
        return rating;
    }
}
