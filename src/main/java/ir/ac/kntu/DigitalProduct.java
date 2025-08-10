package ir.ac.kntu;

import java.io.Serializable;

public class DigitalProduct extends Product implements Serializable {
    private final String brand;
    private final String internalStorage;
    private final String ram;
    private final String operatingSystem;
    private final String chipset;

    public DigitalProduct(String brand, String price, String internalStorage, String ram, String operatingSystem, String chipset, int stock, String sellerAgencyCode) {
        super("Digital Product", sellerAgencyCode);
        this.brand = brand;
        this.setFullName(brand);
        this.internalStorage = internalStorage;
        this.ram = ram;
        this.operatingSystem = operatingSystem;
        this.chipset = chipset;
        this.setStock(stock);
        this.setPrice(price);
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
                displayField("OS", operatingSystem) +
                displayField("Battery", brand) +
                displayField("Chipset", chipset);
    }
}
