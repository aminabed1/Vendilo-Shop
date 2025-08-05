package ir.ac.kntu;

import java.io.Serializable;

public class Phone extends DigitalProduct implements Serializable {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";

    private final String mainCamResolution;
    private final String frontCamResolution;
    private final String networkInfo;
    private final boolean supportSDCard;

    public Phone(String brand, String internalStorage, String RAM, String OS, String batteryCapacity, String chipset,
                 String mainCamResolution, String frontCamResolution, String networkInfo, boolean supportSDCard,String price, int stock, String sellerAgencyCode) {
        super(brand, price, internalStorage, RAM, OS, chipset, stock, sellerAgencyCode);
        this.mainCamResolution = mainCamResolution;
        this.frontCamResolution = frontCamResolution;
        this.networkInfo = networkInfo;
        this.supportSDCard = supportSDCard;
    }
    
    public String getMainCamResolution() {
        return mainCamResolution;
    }

    public String getFrontCamResolution() {
        return frontCamResolution;
    }

    public String getNetworkInfo() {
        return networkInfo;
    }

    public boolean isSupportSDCard() {
        return supportSDCard;
    }

    @Override
    public String displayField(String key, String value) {
        return super.displayField(key, value);
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + ANSI_CYAN + "── Phone Specifications ──" + ANSI_RESET +
                displayField("Main Camera", mainCamResolution) +
                displayField("Front Camera", frontCamResolution) +
                displayField("Network", networkInfo) +
                displayField("SD Card Support", isSupportSDCard() ? "Yes" : "No") +
                ANSI_CYAN + "==================================================" + ANSI_RESET;
    }
}
