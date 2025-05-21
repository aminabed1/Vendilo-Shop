package ir.ac.kntu;

public class Phone extends DigitalProduct{
    private final String mainCamResolution;
    private final String frontCamResolution;
    private final String networkInfo;
    private final boolean supportSDCard;

    public Phone(String brand, String internalStorage, String RAM, String OS, String batteryCapacity, String chipset,
                 String mainCamResolution, String frontCamResolution, String networkInfo, boolean supportSDCard, String sellerAgencyCode) {
        super(brand, internalStorage, RAM, OS, batteryCapacity, chipset, sellerAgencyCode);
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
}
