package ir.ac.kntu;

public class PercentDiscount extends DiscountCode {
    private double percent;

    public PercentDiscount(String code, boolean isCodeActive, int usableTimes, double percent) {
        super(code, isCodeActive, usableTimes);
        this.percent = percent;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "Code: " + this.getCode() + ", Active: " + this.getIsCodeActive() + "Usable Times: " + this.getUsableTimes() + ", Percent: " + this.percent;
    }
}
