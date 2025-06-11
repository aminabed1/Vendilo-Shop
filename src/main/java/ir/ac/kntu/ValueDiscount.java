package ir.ac.kntu;

public class ValueDiscount extends DiscountCode {
    private double value;

    public ValueDiscount(String code, boolean isCodeActive, int usableTimes, double value) {
        super(code, isCodeActive, usableTimes);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Code: " + this.getCode() + ", Active: " + this.getIsCodeActive() + "Usable Times: " + this.getUsableTimes() + ", Value: " + this.getValue();
    }
}
