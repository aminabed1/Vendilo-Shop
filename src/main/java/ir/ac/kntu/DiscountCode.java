package ir.ac.kntu;

public class DiscountCode {
    private final boolean isCodeActive;
    private String code;
    private int usableTimes;
    private String purpose;

    public DiscountCode(String code, String purpose, boolean isCodeActive, int usableTimes) {
        this.code = code;
        this.purpose = purpose;
        this.isCodeActive = isCodeActive;
        this.usableTimes = usableTimes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean getIsCodeActive() {
        return isCodeActive;
    }

    public int getUsableTimes() {
        return usableTimes;
    }

    public void setUsableTimes(int usableTimes) {
        this.usableTimes = usableTimes;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Override
    public String toString() {
        return "Code: " + code + ", Active: " + isCodeActive + " Usable Times: " + usableTimes;
    }
}
