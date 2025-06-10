package ir.ac.kntu;

import java.io.Serializable;

public class Address implements Serializable {
    private String province;
    private String street;
    private String city;
    private String postalCode;
    private String plateNumber;
    private String details;

    public Address(String province, String street, String city, String postalCode, String plateNumber, String details) {
        this.province = province;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.plateNumber = plateNumber;
        this.details = details;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
