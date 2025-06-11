package ir.ac.kntu;

public class DiscountCode {
    private String code;
    private boolean isCodeActive;
    private int usableTimes;

    public DiscountCode(String code, boolean isCodeActive, int usableTimes) {
        this.code = code;
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

    public void setCodeActive(boolean codeActive) {
        isCodeActive = codeActive;
    }

    public int getUsableTimes() {
        return usableTimes;
    }

    public void setUsableTimes(int usableTimes) {
        this.usableTimes = usableTimes;
    }

    public boolean reduceUsableTimes() {
        if (usableTimes == 0) {
            return false;
        } else {
            usableTimes--;
            return true;
        }
    }

    @Override
    public String toString() {
        return "Code: " + code + ", Active: " + isCodeActive + "Usable Times: " + usableTimes;
    }
}
