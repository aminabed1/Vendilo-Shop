package ir.ac.kntu;

import ir.ac.kntu.UI.LoginPageUI;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Customer customer = new Customer("amin", "abdollahi", "09144593348", "adsfd@gmail.com", "amin","1234");
        List<Person> customerList = DataBase.getPersonList();
        System.out.println(customerList.size());
        DataBase.setPersonList(customer);
        Application.launch(LoginPageUI.class, args);
    }
}
