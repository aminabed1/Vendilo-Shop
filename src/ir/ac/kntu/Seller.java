package ir.ac.kntu;

import java.util.*;

public class Seller extends Person{
    private String agencyCode;
    private String shopName;
    private String sellerID;
    private String province;
    private boolean isValidSeller = false;
    private Wallet wallet;
    private List<Order> orders = new ArrayList<>();


    public Seller(String name, String surname, String phoneNumber, String email, String username,
                  String password, String shopName,String sellerID, String province, String agencyCode) {
        super(name, surname, phoneNumber, email, username, password);
        this.agencyCode = agencyCode;
        this.shopName = shopName;
        this.province = province;
        this.sellerID = sellerID;
        wallet = new Wallet();
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

    public boolean getIsValidSeller() {
        return isValidSeller;
    }

    public void setIsValidSeller(boolean validSeller) {
        isValidSeller = validSeller;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSellerID() {
        return sellerID;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }


    public void makeRequest() {

    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrders(Order order) {
        orders.add(order);
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
                this.getSellerID().equals(seller.getSellerID()) &&
                this.province.equals(seller.getProvince());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getPhoneNumber(), getEmail(),
                getUsername(), getPassword());
    }
}