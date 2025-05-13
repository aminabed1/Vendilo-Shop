package ir.ac.kntu;

import java.util.*;

public class DataBase {
    private static List<Person> personList = new ArrayList<>();
    private static List<Product> productList = new ArrayList<>();


    public static List<Person> getPersonList() {
        return personList;
    }

    public static void setPersonList(Person person) {
        personList.add(person);
    }

    public static List<Product> getProductList() {
        return productList;
    }

    public static void setProductList(Product product) {
        productList.add(product);
    }

}
