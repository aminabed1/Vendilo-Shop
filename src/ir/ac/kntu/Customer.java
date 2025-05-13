package ir.ac.kntu;

import java.util.*;

public class Customer extends Person{
//    private List<String> errorList;
    private List<Address> addressList;
    private List<String> ratedProductsList;
    private double wallet;

    //TODO instantiate errorList???
    public Customer(String name, String surname, String phoneNumber, String email, String username,
                    String password) {
        super(name, surname, phoneNumber, email, username, password);
//        errorList = new ArrayList<>();
        addressList = new ArrayList<>();
        ratedProductsList = new ArrayList<>();
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
