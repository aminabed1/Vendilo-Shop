package ir.ac.kntu;

import java.util.*;

public class Customer extends Person{
    private List<Address> addressList;
    private List<String> ratedProductsList;
    private double wallet;
    private Cart cart;

    public Customer(String name, String surname, String phoneNumber, String email, String username,
                    String password) {
        super(name, surname, phoneNumber, email, username, password);
        addressList = new ArrayList<>();
        ratedProductsList = new ArrayList<>();
        cart = new Cart();
        this.setRole("Customer");
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public List<String> getRatedProductsList() {
        return ratedProductsList;
    }

    public void setRatedProductsList(List<String> ratedProductsList) {
        this.ratedProductsList = ratedProductsList;
    }

    public double getWalletBalance() {
        return wallet;
    }

    public void setWalletBalance(double wallet) {
        this.wallet = wallet;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void addProduct(Product product) {
        Cart newCart = this.getCart();
        newCart.addProduct(product);
        this.setCart(newCart);
    }

    public void displayCart() {
        Cart cart = this.getCart();
        List<Product> productList = cart.getProductList();

        if (productList.isEmpty()) {
            System.out.println("The cart is empty.\nReturning to main menu.");
            return;
        }

        Scanner input = new Scanner(System.in);

        while (true) {
            double totalPrice = 0;

            System.out.println("\n--------------------- CART CONTENT ---------------------");

            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                System.out.println(i + 1 + " ------------------------------------------------------");
                System.out.println("Product name    : " + product.getFullName());
                System.out.println("Product price   : " + product.getPrice() + " $");
                System.out.println("Product category: " + product.getCategory());
                System.out.println("--------------------------------------------------------");
                totalPrice += Double.parseDouble(product.getPrice());
            }

            System.out.println("\nTotal price: " + totalPrice + " $");
            System.out.println("Select a product by index, type DONE to finalize, or BACK to return:");

            String choice = input.nextLine().trim();

            if (choice.equalsIgnoreCase("BACK")) {
                return;
            }

            if (choice.equalsIgnoreCase("DONE")) {
                // TODO: finalize buy
                System.out.println("Finalizing purchase...");
                // مثلا چاپ فاکتور یا پرداخت
                return;
            }

            if (!choice.matches("\\d+")) {
                System.out.println("Please enter a valid index.");
                continue;
            }

            int index = Integer.parseInt(choice) - 1;

            if (index < 0 || index >= productList.size()) {
                System.out.println("Index out of range.");
                continue;
            }

            Product selectedProduct = productList.get(index);
            System.out.println("\nProduct selected: " + selectedProduct.getFullName());

            System.out.println("\n1. Display product details");
            System.out.println("2. Remove product from cart");
            System.out.println("3. Deselect product");
            System.out.print("Enter action: ");

            String action = input.nextLine().trim();

            switch (action) {
                case "1":
                    // TODO: Show full product details
                    System.out.println("Showing product details:");
                    System.out.println(selectedProduct);
                    break;

                case "2":
                    cart.removeProduct(selectedProduct);
                    this.setCart(cart);
                    System.out.println("Product removed from cart.");
                    break;

                case "3":
                    System.out.println("Product deselected.");
                    break;

                default:
                    System.out.println("Invalid action. Please try again.");
            }
        }
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }

        Customer customer = (Customer) object;

        return this.getName().equals(customer.getName()) &&
                this.getSurname().equals(customer.getSurname() )&&
                this.getPhoneNumber().equals(customer.getPhoneNumber()) &&
                this.getEmail().equals(customer.getEmail()) &&
                this.getUsername().equals(customer.getUsername()) &&
                this.getPassword().equals(customer.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getPhoneNumber(), getEmail(),
                            getUsername(), getPassword());
    }
}
