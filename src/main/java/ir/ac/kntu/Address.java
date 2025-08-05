package ir.ac.kntu;

import java.io.Serializable;

public class Address implements Serializable {
    private final String province;
    private final String city;
    private final String postalCode;
    private final String plateNumber;
    private final String street;
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

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
