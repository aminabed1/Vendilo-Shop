package ir.ac.kntu;

import java.io.Serializable;

public class LopTop extends DigitalProduct implements Serializable {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";

    private final String GPUChipset;
    private final boolean supportBluetooth;
    private final boolean hasWebCam;
    private final String RAMGeneration;

    public LopTop(String brand,String price, String internalStorage, String RAM, String OS, String batteryCapacity,
                  String chipset, String GPUChipset, boolean supportBluetooth, boolean hasWebCam, String RAMGeneration, int stock, String sellerAgencyCode) {
        super(brand, price, internalStorage, RAM, OS, chipset, stock, sellerAgencyCode);
        this.GPUChipset = GPUChipset;
        this.supportBluetooth = supportBluetooth;
        this.hasWebCam = hasWebCam;
        this.RAMGeneration = RAMGeneration;
    }

    @Override
    public String displayField(String key, String value) {
        return super.displayField(key, value);
    }

    @Override
    public String toString() {
        return super.toString() +
                "\n" + ANSI_CYAN + "── Laptop Specifications ──" + ANSI_RESET +
                displayField("GPU Chipset", GPUChipset) +
                displayField("Bluetooth", supportBluetooth ? "Yes" : "No") +
                displayField("Webcam", hasWebCam ? "Yes" : "No") +
                displayField("RAM Generation", RAMGeneration) +
                ANSI_CYAN + "==================================================" + ANSI_RESET;
    }
}
