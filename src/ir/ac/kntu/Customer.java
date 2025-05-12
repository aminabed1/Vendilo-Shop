package ir.ac.kntu;

import java.util.*;

public class Customer extends Person{
    private List<String> errorList;
    private List<Address> addressList;

    public Customer(String name, String surname, String phoneNumber, String email, String username,
                    String password) {

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

        if (isValid && isUnique) {
            setName(name);
            setSurname(surname);
            setPhoneNumber(phoneNumber);
            setEmail(email);
            setUsername(username);
            setPassword(password);
            DataBase.setCustomerList(this);
        }
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public List<Address> getAddressList() {
        return addressList;
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
