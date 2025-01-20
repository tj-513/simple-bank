package app.model;

import app.Utils;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Account {
    private String id;
    private List<Transaction> transactions = new LinkedList<>();

    public Account(String id) {
        this.id = id;
    }

    public double getCurrentBalance() {
        if (transactions.isEmpty()) {
            return 0;
        }
        return transactions.get(transactions.size()-1).getRunningBalance();
    }

    public String getNewTransactionId(LocalDate date) {
        int transactionsForDate = (int) transactions.stream()
                .filter(t -> t.getTransactionDate().equals(date))
                .count();
        return "%s-%02d".formatted(Utils.getFormattedDate(date), transactionsForDate + 1);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
