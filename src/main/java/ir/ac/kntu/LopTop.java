package ir.ac.kntu;

import java.io.Serializable;

public class LopTop extends DigitalProduct implements Serializable {
    private String GPUChipset;
    private boolean supportBluetooth;
    private boolean hasWebCam;
    private String RAMGeneration;

    public LopTop(String brand,String price, String internalStorage, String RAM, String OS, String batteryCapacity,
                  String chipset, String GPUChipset, boolean supportBluetooth, boolean hasWebCam, String RAMGeneration, int stock, String sellerAgencyCode) {
        super(brand, price, internalStorage, RAM, OS, batteryCapacity, chipset, stock, sellerAgencyCode);
        this.GPUChipset = GPUChipset;
        this.supportBluetooth = supportBluetooth;
        this.hasWebCam = hasWebCam;
        this.RAMGeneration = RAMGeneration;
    }

    public String getGPUChipset() {
        return GPUChipset;
    }

    public void setGPUChipset(String GPUChipset) {
        this.GPUChipset = GPUChipset;
    }

    public boolean isSupportBluetooth() {
        return supportBluetooth;
    }

    public void setSupportBluetooth(boolean supportBluetooth) {
        this.supportBluetooth = supportBluetooth;
    }

    public boolean isHasWebCam() {
        return hasWebCam;
    }

    public void setHasWebCam(boolean hasWebCam) {
        this.hasWebCam = hasWebCam;
    }

    public String getRAMGeneration() {
        return RAMGeneration;
    }

    public void setRAMGeneration(String RAMGeneration) {
        this.RAMGeneration = RAMGeneration;
    }
}
