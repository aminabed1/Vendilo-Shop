package ir.ac.kntu;

import java.util.*;
import ir.ac.kntu.*;

public class DataBase {
    private static List<Customer> customerList = new ArrayList<>();

    public static List<Customer> getCustomerList() {
        return customerList;
    }

    public static void setCustomerList(Customer customer) {
        customerList.add(customer);
    }

}
