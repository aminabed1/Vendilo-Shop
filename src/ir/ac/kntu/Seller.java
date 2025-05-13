package ir.ac.kntu;

import java.util.List;
import java.util.Objects;

public class Seller extends Person{
//    private List<String> errorList;
    private String agencyCode;
    private String shopName;
    private Address address;
    private boolean isValidSeller = false;
    private double wallet;

    public Seller(String name, String surname, String phoneNumber, String email, String username,
                  String password, String shopName) {
        super(name, surname, phoneNumber, email, username, password);
        this.agencyCode = agencyCode;
        this.shopName = shopName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean getValidSeller() {
        return isValidSeller;
    }

    public void setValidSeller(boolean validSeller) {
        isValidSeller = validSeller;
    }

    public double getWalletBalance() {
        return wallet;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }

        Seller seller = (Seller) object;

        return this.getName().equals(seller.getName()) &&
                this.getSurname().equals(seller.getSurname() )&&
                this.getPhoneNumber().equals(seller.getPhoneNumber()) &&
                this.getEmail().equals(seller.getEmail()) &&
                this.getUsername().equals(seller.getUsername()) &&
                this.getPassword().equals(seller.getPassword()) &&
                this.address.equals(seller.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getPhoneNumber(), getEmail(),
                getUsername(), getPassword());
    }
}
