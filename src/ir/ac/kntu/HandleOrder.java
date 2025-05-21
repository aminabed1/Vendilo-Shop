package ir.ac.kntu;

import java.time.Instant;
import java.util.*;
public class HandleOrder {
    private final static Scanner scan = new Scanner(System.in);
    private static double finalPrice;
    private static List<Product> products;
    private static List<Person> sellerList;
    private static List<String> sellersAgencyCode;
    private static Address selectedAddress;


    public static HandleOrder getInstance() {
        return new HandleOrder();
    }

    public void handleOrder(Person person) {
        finalPrice = 0;
        products = new ArrayList<>();
        selectedAddress = null;

        if (person instanceof Customer) {
            customerOrder(person);
        } else {
            sellerOrder(person);
        }
    }

    public void customerOrder(Person person) {
        if (!checkProductsStock(((Customer) person).getCart())) {
            return;
        }
        Cart cart = ((Customer) person).getCart();
        selectedAddress = addressSelection((Customer) person);
        double postingPrice = calcPostingPrice(selectedAddress.getProvince(), ((Customer) person).getCart());
        double finalPrice = getFinalPrice(cart, postingPrice);
        finalDisplayingCart(cart);
        option(person);

    }

    public void sellerOrder(Person person) {

    }

    public Boolean checkProductsStock(Cart cart) {
        List<Product> productList = cart.getProductList();

        for (Product product : productList) {
            if (product.getStock() == 0) {
                System.out.println("Product : " + product.getFullName());
//                System.out.println("Product is out of stock. Removing from cart");
                System.out.println("Product is out of stock");
//                cart.removeProduct(product);
                return false;
            }
        }
        return true;
    }

    public Address addressSelection(Customer customer) {
        List<Address> addressList = customer.getAddressList();
        PersonInfo personInfo = new PersonInfo();
        while (true) {
            if (addressList.isEmpty()) {
                System.out.println("No address has been registered yet");
                System.out.println("Do you want to add an address ? (y / n)");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("y")) {
                    personInfo.addNewAddress(customer);
                } else if (choice.equalsIgnoreCase("n")) {
                    return null;
                } else {
                    System.out.println("Please enter a valid option");
                }
            } else {
                System.out.println("Address List : \n");
                personInfo.displayAddresses(addressList);
                System.out.println("Please select an address by it's index");
                //TODO complete here
                int selectedIndex = scan.nextInt();
                if (selectedIndex < 0 || selectedIndex > addressList.size()) {
                    System.out.println("Please enter a valid index");
                    continue;
                }

                return addressList.get(selectedIndex - 1);
            }
        }
    }
    public double calcPostingPrice(String province, Cart cart) {
        final double basePrice = 15;
        double finalPrice;
        products = cart.getProductList();
        for (Product product : products) {
            String agencyCode = product.getSellerAgencyCode();
            if (!(getSellerProvince(agencyCode).equals(province))) {
                 return getFinalPostingPrice(cart, "No");
            }
        }

        return getFinalPostingPrice(cart, "No");
    }

    public String getSellerProvince(String agencyCode) {
        sellerList = DataBase.getPersonList();

        for (Person person : sellerList) {
            if (!(person instanceof Seller)) {
                continue;
            }
            if (agencyCode.equals(((Seller) person).getAgencyCode())) {
                return ((Seller) person).getProvince();
            }
        }

        return null;
    }

    public String getShopName(String agencyCode) {
        sellerList = DataBase.getPersonList();

        for (Person person : sellerList) {
            if (!(person instanceof Seller)) {
                continue;
            }
            if (agencyCode.equals(((Seller) person).getAgencyCode())) {
                return ((Seller) person).getShopName();
            }
        }

        return null;
    }

    public double getFinalPostingPrice(Cart cart, String coordinate) {
        products = cart.getProductList();

        for (Product product : products) {
            finalPrice += Double.parseDouble(product.getPrice());
        }

        return (coordinate.equals("No") ? finalPrice : finalPrice / 3);
    }

    public double getFinalPrice(Cart cart, double postingPrice) {
        double finalPrice = postingPrice;

        for (Product product : cart.getProductList()) {
            finalPrice += Double.parseDouble(product.getPrice());
        }

        return finalPrice;
    }

    public void finalDisplayingCart(Cart cart) {


        for (Product product : products) {
            System.out.println("=====================================================================================");
            System.out.println("Product : " + product.getFullName());
            System.out.println("Category : " + product.getCategory());
            System.out.println("Price : " + product.getPrice());
//            System.out.println("=====================================================================================\n");
        }
    }

    public void option(Person person) {
        while(true) {
            System.out.println("Do you want to finalize your order ? (y / n)");
            String choice = scan.nextLine();
            if (choice.equalsIgnoreCase("y")) {
                if (checkEnoughBalance(person)) {
                    finalizeOrder(person);
                }
                break;
            } else if (choice.equalsIgnoreCase("n")) {
                return;
            } else {
                System.out.println("Please enter a valid option");
            }
        }
    }

    public boolean checkEnoughBalance(Person person) {
        double personBalance = ((Customer) person).getWalletBalance();
        if (personBalance < finalPrice) {
            System.out.println("You do not have enough balance. Returning...");
            return false;
        }
        return true;
    }

    public void setSellersCodeList() {
        sellersAgencyCode = new ArrayList<>();
        for (Product product : products) {
            sellersAgencyCode.add(product.getSellerAgencyCode());
        }
    }

    public void finalizeOrder(Person person) {
        setSellersCodeList();
        ((Customer) person).addOrder(new Order(products, Instant.now(), sellersAgencyCode, selectedAddress));
        System.out.println("Order completed successfully");
    }
}