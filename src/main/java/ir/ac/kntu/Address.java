package ir.ac.kntu;

import java.util.*;
import ir.ac.kntu.*;

public class Address {
    private Province province;
    private String street;
    private String city;
    private String postalCode;
    private String plateNumber;

    public Address(Province province, String street, String city, String postalCode, String plateNumber) {
        this.province = province;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.plateNumber = plateNumber;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
}
