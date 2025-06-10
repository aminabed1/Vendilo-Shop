package ir.ac.kntu;

import java.io.*;
import java.util.*;
//TODO make methods non-static
public class DataBase implements Serializable {
    private static final long serialVersionUID = 1L;

    private static List<Person> personList = new ArrayList<>();
    private static List<Product> productList = new ArrayList<>();
    private static List<Request> requestList = new ArrayList<>();
    private static List<Order> orderList = new ArrayList<>();

    private static final String DATA_FILE = "data.db";

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

    public static List<Order> getOrderList() {
        return orderList;
    }

    public static void addOrder(Order order) {
        orderList.add(order);
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product product : productList) {
            if ("Digital".equalsIgnoreCase(category) && product instanceof DigitalProduct) {
                result.add(product);
            } else if ("Book".equalsIgnoreCase(category) && product instanceof Book) {
                result.add(product);
            }
        }
        return result;
    }

    public static void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(personList);
            out.writeObject(productList);
            out.writeObject(requestList);
            out.writeObject(orderList);
            System.out.println("Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Data file not found, starting with empty database.");
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            personList = (List<Person>) in.readObject();
            productList = (List<Product>) in.readObject();
            requestList = (List<Request>) in.readObject();
            orderList = (List<Order>) in.readObject();
            System.out.println("Data loaded successfully from " + DATA_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}
