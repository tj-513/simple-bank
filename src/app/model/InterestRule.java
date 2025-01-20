package app.model;

import app.Utils;

import java.time.LocalDate;

public class InterestRule {
    private String id;
    private LocalDate date;
    private double interestRate;

    public InterestRule() {

    }

    public String toString() {
        return String.format("| %s\t| %s\t\t| %.2f\t\t|", Utils.getFormattedDate(date), id, interestRate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
