package ir.ac.kntu;

import java.util.*;

public class DataBase {
    private static List<Customer> customerList = new ArrayList<>();
    private static List<Support> supportList = new ArrayList<>();
    private static List<Seller> sellerList = new ArrayList<>();


    public static List<Customer> getCustomerList() {
        return customerList;
    }

    public static void setCustomerList(Customer customer) {
        customerList.add(customer);
    }

    public static List<Support> getSupportList() {
        return supportList;
    }

    public static void setSupportList(Support support) {
        supportList.add(support);
    }

    public static List<Seller> getSellerList() {
        return sellerList;
    }
    public static void setSellerList(Seller seller) {
        sellerList.add(seller);
    }


}
