package ir.ac.kntu;

import java.util.List;
import java.util.Objects;

public class Seller extends Person{
    private String agencyCode;
    private String shopName;
    private String province;
    private boolean isValidSeller = false;
    private double wallet;

    public Seller(String name, String surname, String phoneNumber, String email, String username,
                  String password, String shopName, String province) {
        super(name, surname, phoneNumber, email, username, password);
        this.agencyCode = agencyCode;
        this.shopName = shopName;
        this.setRole("Seller");
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

    public void setWalletBalance(double wallet) {
        this.wallet = wallet;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void makeRequest() {

    }
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }

        Seller seller = (Seller) object;

        return this.getName().equals(seller.getName()) &&
                this.getSurname().equals(seller.getSurname()) &&
                this.getPhoneNumber().equals(seller.getPhoneNumber()) &&
                this.getEmail().equals(seller.getEmail()) &&
                this.getUsername().equals(seller.getUsername()) &&
                this.getPassword().equals(seller.getPassword()) &&
                this.province.equals(seller.getProvince());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getPhoneNumber(), getEmail(),
                getUsername(), getPassword());
    }
}
