package ir.ac.kntu;

import java.util.*;

public class Customer extends Person{
    private List<String> errorList;
    private Province province;
    private Address address;

    public Customer(String name, String surname, int age, String phoneNumber, String email, String username, String password, Province province, Address address) {
        errorList = new ArrayList<>();
        PersonValidator cv = new PersonValidator();

        boolean isUnique = cv.emailUniquementCheck(email, errorList) &&
            cv.phoneNumberUniquementCheck(phoneNumber, errorList) &&
            cv.usernameUniquementCheck(username, errorList);

        boolean isValid = cv.checkNameAndSurnameValidation(name, errorList) &&
            cv.checkNameAndSurnameValidation(surname, errorList) &&
            cv.phoneNumberValidation(phoneNumber, errorList) &&
            cv.emailValidation(email, errorList) &&
            cv.usernameValidation(username, errorList) &&
            cv.passwordValidation(password, errorList) &&
            cv.checkAgeValidation(String.valueOf(age), errorList);

        if (isValid && isUnique) {
            this.province = province;
            this.address = address;
            setName(name);
            setSurname(surname);
            setAge(age);
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
}
