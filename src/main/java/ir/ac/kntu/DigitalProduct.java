package ir.ac.kntu;

import java.io.Serializable;

public class DigitalProduct extends Product implements Serializable {
    private final String brand;
    private final String internalStorage;
    private final String ram;
    private final String OS;
    private final String batteryCapacity;
    private final String chipset;

    public DigitalProduct(String brand, String price, String internalStorage, String ram, String OS, String batteryCapacity, String chipset, int stock, String sellerAgencyCode) {
        super("Digital Product", sellerAgencyCode);
        this.brand = brand;
        this.setFullName(brand);
        this.internalStorage = internalStorage;
        this.ram = ram;
        this.OS = OS;
        this.batteryCapacity = batteryCapacity;
        this.chipset = chipset;
        this.setStock(stock);
        this.setPrice(price);
    }

    public String getBrand() {
        return brand;
    }

    public String getInternalStorage() {
        return internalStorage;
    }

    public String getRAM() {
        return ram;
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

    @Override
    public String displayField(String key, String value) {
        return super.displayField(key, value);
    }

    @Override
    public String toString() {
        return super.toString() +
                displayField("Brand", brand) +
                displayField("Storage", internalStorage) +
                displayField("RAM", ram) +
                displayField("OS", OS) +
                displayField("Battery", brand) +
                displayField("Chipset", chipset);
    }
}
