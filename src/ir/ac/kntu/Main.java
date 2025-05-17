package ir.ac.kntu;

public class Main {
    public static void main(String[] args) {
        Person customer = new Customer("amin", "abdollahi", "09144593348", "adsfd@gmail.com", "aminabed1","12345678aA@");
        Person seller = new Seller("amin", "aaaaaa", "09144593347", "adsv@gmail.com", "123456789abcd", "12345678aA@", "aa", "TEHRAN");
        DataBase.addPerson(customer);
        DataBase.addPerson(seller);
        LoginPage.loginPage();
//        PersonInfo.infoView(customer);

    }
}