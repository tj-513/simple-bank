package app.model;

import app.Utils;

import java.time.LocalDate;

public class Transaction {

    private String id;
    private LocalDate transactionDate;
    private TransactionType transactionType;
    private double transactionAmount;
    private double runningBalance;

    @Override
    public String toString() {
        return "| %s\t| %s\t| %s\t\t| %.2f\t|".formatted(Utils.getFormattedDate(transactionDate), id == null ? "": id, transactionType.getCode(), transactionAmount);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public double getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(double runningBalance) {
        this.runningBalance = runningBalance;
    }
}
