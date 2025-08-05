package ir.ac.kntu;

import java.io.*;
import java.util.*;
//TODO make methods non-
public class DataBase implements Serializable {
    @Serial
    private final long serialVersionUID = 1L;
    private int priorityNumber = 0;
    private static final DataBase dataBase = new DataBase();
    private final List<Person> personList = new ArrayList<>();
    private final List<Product> productList = new ArrayList<>();
    private final List<Request> requestList = new ArrayList<>();
    private final List<Order> orderList = new ArrayList<>();
    private final List<DiscountCode> discountCodeList = new ArrayList<>();
    private final List<Notification> notificationList = new ArrayList<>();

    public static DataBase getInstance() {
        return dataBase;
    }
//    private  final String DATA_FILE = "data.db";

    public  int getPriorityNumber() {
        return priorityNumber;
    }
    
    public void setPriorityNumber(int priorityNumber) {
        this.priorityNumber = priorityNumber;
    }
    
    public  List<Person> getPersonList() {
        return personList;
    }

    public  void addPerson(Person person) {
        personList.add(person);
    }

    public  List<Product> getProductList() {
        return productList;
    }

    public  void addProduct(Product product) {
        productList.add(product);
    }

    public  List<Request> getRequestList() {
        return requestList;
    }

    public  void addRequest(Request request) {
        requestList.add(request);
    }

    public  List<Order> getOrderList() {
        return orderList;
    }

    public  void addOrder(Order order) {
        orderList.add(order);
    }

    public  List<DiscountCode> getDiscountCodeList() {
        return discountCodeList;
    }

    public  void addDiscountCode(DiscountCode discountCode) {
        discountCodeList.add(discountCode);
    }

    public  List<Notification> getNotificationList() {
        return notificationList;
    }

    public  void addNotification(Notification notification) {
        notificationList.add(notification);
    }
/*
    public  void saveData() {
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
    public  void loadData() {
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

 */
}
