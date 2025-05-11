package ir.ac.kntu;

import java.util.*;

public class Customer extends Person{
    private List<String> errorList;
//    private Province province;
//    private Address address;
    private String province;
    private String address;

    public Customer(String name, String surname, String phoneNumber, String email, String username,
                    String password, String province, String address) {

        errorList = new ArrayList<>();
        PersonInfoValidator cv = new PersonInfoValidator();

        boolean isUnique = cv.emailUniquementCheck(email, errorList) &&
            cv.phoneNumberUniquementCheck(phoneNumber, errorList) &&
            cv.usernameUniquementCheck(username, errorList);

        boolean isValid = cv.checkNameAndSurnameValidation(name, errorList) &&
            cv.checkNameAndSurnameValidation(surname, errorList) &&
            cv.phoneNumberValidation(phoneNumber, errorList) &&
            cv.emailValidation(email, errorList) &&
            cv.usernameValidation(username, errorList) &&
            cv.passwordValidation(password, errorList);
//            cv.checkAgeValidation(String.valueOf(age), errorList);

        if (isValid && isUnique) {
            this.province = province;
            this.address = address;
            setName(name);
            setSurname(surname);
//            setAge(age);
            setPhoneNumber(phoneNumber);
            setEmail(email);
            setUsername(username);
            setPassword(password);
            DataBase.setCustomerList(this);
        }
    }

    public Customer() {

    }

    public Customer(String username, String password, String name, String surename) {
        setUsername(username);
        setPassword(password);
        setName(name);
        setSurname(surename);
    }

    public List<String> getErrorList() {
        return errorList;
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
                this.getPassword().equals(customer.getPassword()) &&
                this.province.equals(customer.province) &&
                this.address.equals(customer.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getPhoneNumber(), getEmail(),
                            getUsername(), getPassword(), province, address);
    }
}
