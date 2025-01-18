package app.service;

import app.model.*;

import java.util.List;

public class CoreService {
    private static CoreService instance;
    Bank bank = new Bank();
    private CoreService() {}

    public static CoreService getInstance() {
        if (instance == null) {
            instance = new CoreService();
        }
        return instance;
    }

    public List<Transaction> addTransaction(String accountId, Transaction transaction) {
        Account account = bank.getAccounts().computeIfAbsent(accountId, Account::new);
        double currentBalance = account.getCurrentBalance();

        double newBalance;
        if(TransactionType.DEPOSIT == transaction.getTransactionType()) {
            newBalance = currentBalance + transaction.getTransactionAmount();
        } else {
            newBalance = currentBalance - transaction.getTransactionAmount();
            if (newBalance < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }
        }
        transaction.setId(account.getNewTransactionId(transaction.getTransactionDate()));
        transaction.setRunningBalance(newBalance);
        account.getTransactions().add(transaction);
        return account.getTransactions();
    }

    public void addInterestRule(InterestRule interestRule) {
        // add interest rule
    }

    public void getTransactions(String accountId, String year, String month) {
        // Get transactions from bank
    }


}
