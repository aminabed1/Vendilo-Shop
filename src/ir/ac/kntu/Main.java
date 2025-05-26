package ir.ac.kntu;

public class Main {
    public static void main(String[] args) {
        Person person = new Customer("amin", "abddd", "09144593348", "aafdsz@adsf", "amin1amin1","12345678aA@");
        Person seller = new Seller("amin", "aaaaaa", "09144593347", "adsv@gmail.com", "123456789abcd", "12345678aA@", "aa", "14569874588", "TEHRAN","AA2S3D4");
        Book book = new Book("hezar o yek Shab", "121", "dafdsfjafadsfadsf", "unknown", "121", "druma", "1231", "afd", "99", 12, "AA2S3D4");
        Book book2 = new Book("hezar sdo yek Shab", "122", "dafdsfjafadsfadsf", "unknod", "121", "druma", "123333", "587", "87", 87, "AA2S3D4");


        DataBase.addPerson(person);
        DataBase.addPerson(seller);
        Customer customer = (Customer) person;

        customer.addProduct(book);
        customer.addProduct(book2);
        DataBase.addProduct(book);
        DataBase.addProduct(book2);

        LoginPage login = new LoginPage();
        login.loginPage();
    }
}