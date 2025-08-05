package ir.ac.kntu;

public class PercentDiscount extends DiscountCode {
    private final double percent;

    public PercentDiscount(String code,String purpose, boolean isCodeActive, int usableTimes, double percent) {
        super(code, purpose, isCodeActive, usableTimes);
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    @Override
    public String toString() {
        return "Code: " + this.getCode() + ", Active: " + this.getIsCodeActive() + "Usable Times: " + this.getUsableTimes() + ", Percent: " + this.percent;
    }
}
