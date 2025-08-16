package ir.ac.kntu;

import java.util.*;

public class Seller extends OrdinaryUsers {
    private String agencyCode;
    private String shopName;
    private String sellerID;
    private String province;
    private boolean isValidSeller = false;
    private Wallet wallet;
    private List<Order> orders = new ArrayList<>();

    public Seller(String name, String surname, String phoneNumber, String email, String password, String shopName,String sellerID,
                  String province, String agencyCode) {
        super(name, surname, password, phoneNumber, email, true);
        this.agencyCode = agencyCode;
        this.shopName = shopName;
        this.province = province;
        this.sellerID = sellerID;
        this.setRole(Role.Seller);
        wallet = new Wallet();
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
    public String toString() {
        return String.format(
                "\n%s%sShop Name         :%s %s\n" +
                        "%sAgency Code       :%s %s\n" +
                        "%sSeller ID         :%s %s\n" +
                        "%sValidation Status :%s %s",
                super.toString(),
                YELLOW, RESET, shopName,
                YELLOW, RESET, agencyCode,
                YELLOW, RESET, sellerID,
                YELLOW, RESET, isValidSeller ? "Valid" : "Invalid Or InActive"
        );
    }

}