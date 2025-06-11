package ir.ac.kntu;

import java.io.Serializable;

public class DigitalProduct extends Product implements Serializable {
    private final String Brand;
    private final String internalStorage;
    private final String RAM;
    private final String OS;
    private final String batteryCapacity;
    private final String chipset;

    public DigitalProduct(String brand, String price, String internalStorage, String RAM, String OS, String batteryCapacity, String chipset, int stock, String sellerAgencyCode) {
        super("Digital Product", sellerAgencyCode);
        this.Brand = brand;
        this.setFullName(brand);
        this.internalStorage = internalStorage;
        this.RAM = RAM;
        this.OS = OS;
        this.batteryCapacity = batteryCapacity;
        this.chipset = chipset;
        this.setStock(stock);
        this.setPrice(price);
    }

    public String getBrand() {
        return Brand;
    }

    public String getInternalStorage() {
        return internalStorage;
    }

    public String getRAM() {
        return RAM;
    }

    public String getOS() {
        return OS;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public String getChipset() {
        return chipset;
    }
}
