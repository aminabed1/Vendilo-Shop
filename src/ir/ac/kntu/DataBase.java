package ir.ac.kntu;

import java.util.*;
//import java.io.*;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;

public class DataBase {
    private static List<Person> personList = new ArrayList<>();
    private static List<Product> productList = new ArrayList<>();
    private static List<Request> requestList = new ArrayList<>();

    public static List<Person> getPersonList() {
        return personList;
    }

    public static void addPerson(Person person) {
        personList.add(person);
    }

    public static List<Product> getProductList() {
        return productList;
    }

    public static void addProduct(Product product) {
        productList.add(product);
    }

    public static List<Request> getRequestList() {
        return requestList;
    }

    public static void addRequest(Request request) {
        requestList.add(request);
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product product : productList) {
            if (category.equals("Digital") && product instanceof DigitalProduct) {
                result.add(product);
            } else if (category.equals("Book") && product instanceof Book) {
                result.add(product);
            }
        }
        return result;
    }

//
//    public void savaData() {
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.db"))) {
//            out.writeObject(DataBase.getPersonList());
//            out.writeObject(DataBase.getProductList());
//        } catch (IOException e) {
//            //nothing
//        }
//    }
//
//    public void loadData() {
//        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.db"))) {
//            List<Person> persons = (List<Person>) in.readObject();
//            List<Product> products = (List<Product>) in.readObject();
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
