package ir.ac.kntu;

import ir.ac.kntu.util.Calendar;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class VendiloPlusAccount {
    private boolean isActive;
    private Instant startDate = null;
    private Instant endDate = null;

    // public VendiloPlusAccount(boolean isActive, Instant startDate, Instant endDate) {
    //     this.isActive = isActive;
    //     this.startDate = startDate;
    //     this.endDate = endDate;
    // }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public void setPremiumAccountDate(Instant startDate, Instant endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        isActive = true;
    }

    @Override
    public String toString() {
        long remainingDays;
        StringBuilder sb = new StringBuilder();

        sb.append("Active Account: ").append(isActive ? "Yes" : "No").append("\n");
        if (isActive) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime startDateTime = LocalDateTime.ofInstant(startDate, ZoneId.systemDefault());
            LocalDateTime endDateTime = LocalDateTime.ofInstant(endDate, ZoneId.systemDefault());
            remainingDays = ChronoUnit.DAYS.between(Calendar.now(), endDate);
            sb.append("Start Date: ").append(startDateTime.format(formatter)).append("\n");
            sb.append("End Date: ").append(endDateTime.format(formatter)).append("\n");
            sb.append("Remaining Days: ").append(remainingDays).append(" days");
        } else {
            sb.append("Remaining Days: ").append(0).append(" days");
        }
        return sb.toString();
    }

    public void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
