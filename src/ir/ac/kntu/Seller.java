package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;

public class Seller extends Person{
    private List<String> errorList;
    private String agencyCode;
    private String shopName;
    private String province;
    private String address;

    public Seller(String name, String surname, String phoneNumber, String email, String username,
                  String password, String agencyCode, String shopName, String province, String address) {

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
                cv.passwordValidation(password, errorList);
//            cv.checkAgeValidation(String.valueOf(age), errorList);

        if (isValid && isUnique) {
            this.address = address;
            this.agencyCode = agencyCode;
            this.shopName = shopName;
            this.province = province;
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

}
