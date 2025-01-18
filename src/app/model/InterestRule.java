package app.model;

public class InterestRule {
    private String id;
    private String date;
    private double interestRate;

    public InterestRule(String id, String date, double interestRate) {
        this.id = id;
        this.date = date;
        this.interestRate = interestRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
