package ir.ac.kntu;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager("Mohammad Amin", "Abdollahi", "amin", "12345678aA@", 0);
        Customer customer = new Customer("Amin", "Abed", "09144593348", "amin.m.abdollahi@gmail.com","12345678aA@");
        Support support = new Support("Amin", "abdollahi", "aminabed1", "12345678aA@");
        Seller seller = new Seller("Ali", "sadeghi", "09144593347", "adsv@gmail.com", "12345678aA@", "Book Shop", "14569874588", "TEHRAN","AA2S3D4");
        Book book = new Book("Hezar o yek Shab", "121", "--", "me", "121", "druma", "1231", "", "2", 0, "AA2S3D4");
        Book book2 = new Book("Boof koor", "122", "--", "me", "121", "druma", "123333", "", "1", 87, "AA2S3D4");
        LopTop loptop = new LopTop("ASUS", "4000", "512", "16", "Windows", "75W", "Core i7", "RTX 5090", false, false, "DDR5", 2, "AA2S3D4");

        DiscountCode dc = new PercentDiscount("ABCD", "09144593348", true, 10, 10);
        DataBase.getInstance().addDiscountCode(dc);
        customer.addToDiscountCodeList(dc);

        DataBase.getInstance().addPerson(manager);
        DataBase.getInstance().addPerson(customer);
        DataBase.getInstance().addPerson(seller);
        DataBase.getInstance().addPerson(support);

        customer.addProduct(book);
        customer.addProduct(book2);
        DataBase.getInstance().addProduct(book);
        DataBase.getInstance().addProduct(book2);
        DataBase.getInstance().addProduct(loptop);

        LoginPage login = new LoginPage();
        login.loginPage();
    }
}