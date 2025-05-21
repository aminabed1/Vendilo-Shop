package ir.ac.kntu;

public class DigitalProduct extends Product {
    private String Brand;
    private String internalStorage;
    private String RAM;
    private String OS;
    private String batteryCapacity;
    private String chipset;

    public DigitalProduct(String brand, String internalStorage, String RAM, String OS, String batteryCapacity, String chipset, String sellerAgencyCode) {
        super("Digital Product", sellerAgencyCode);
        this.Brand = brand;
        this.internalStorage = internalStorage;
        this.RAM = RAM;
        this.OS = OS;
        this.batteryCapacity = batteryCapacity;
        this.chipset = chipset;
    }
    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getInternalStorage() {
        return internalStorage;
    }

    public void setInternalStorage(String internalStorage) {
        this.internalStorage = internalStorage;
    }

    public String getRAM() {
        return RAM;
    }

    public void setRAM(String RAM) {
        this.RAM = RAM;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(String batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }
}
