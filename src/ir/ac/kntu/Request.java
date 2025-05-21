package ir.ac.kntu;

import java.util.List;

public class Request {
    private String sellerUsername;
    private String description;

    public Request(String sellerUsername) {
        this.sellerUsername = sellerUsername;
        DataBase.addRequest(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public Person getSellerByUsername(String username) {
        Person seller = null;
        List<Person> sellerList = DataBase.getPersonList();
        for (Person person : sellerList) {
            if (person.getUsername().equals(username)) {
                seller = person;
                break;
            }
        }
        return seller;
    }
}
