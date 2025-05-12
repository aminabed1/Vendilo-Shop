package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Seller extends Person{
    private List<String> errorList;
    private String agencyCode;
    private String shopName;
    private Address address;

    public Seller(String name, String surname, String phoneNumber, String email, String username,
                  String password, String agencyCode, String shopName) {

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
            this.agencyCode = agencyCode;
            this.shopName = shopName;
            setName(name);
            setSurname(surname);
            setPhoneNumber(phoneNumber);
            setEmail(email);
            setUsername(username);
            setPassword(password);
            DataBase.setSellerList(this);
        }
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }

        Seller seller = (Seller) object;

        return this.getName().equals(seller.getName()) &&
                this.getSurname().equals(seller.getSurname() )&&
                this.getPhoneNumber().equals(seller.getPhoneNumber()) &&
                this.getEmail().equals(seller.getEmail()) &&
                this.getUsername().equals(seller.getUsername()) &&
                this.getPassword().equals(seller.getPassword()) &&
                this.address.equals(seller.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getPhoneNumber(), getEmail(),
                getUsername(), getPassword());
    }
}
