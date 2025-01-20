package app.service;

import app.model.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class CoreService {

    Bank bank = new Bank();
    public CoreService() {}


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
        account.getTransactions().sort(Comparator.comparing(Transaction::getTransactionDate));
        return account.getTransactions();
    }

    public List<InterestRule> addInterestRule(InterestRule interestRule) {
        bank.getInterestRules().add(interestRule);
        bank.getInterestRules().sort(Comparator.comparing(InterestRule::getDate));
        return bank.getInterestRules();
    }

    public List<Transaction> getAccountStatement(String accountId, int month, int year) {
        List<Transaction> applicableTransactions = getApplicableTransactions(accountId, month, year);
        List<Double> dailyBalances = getDailyBalances(applicableTransactions, month, year);
        List<Double> dailyInterestRates = getDailyInterestRates(month, year);
        double interest = calculateInterest(dailyBalances, dailyInterestRates);

        Transaction interestTransaction = new Transaction();
        interestTransaction.setTransactionDate(YearMonth.of(year, month).atEndOfMonth());
        interestTransaction.setTransactionType(TransactionType.INTEREST);
        interestTransaction.setTransactionAmount(interest);
        interestTransaction.setRunningBalance(dailyBalances.get(dailyBalances.size()-1) + interest);

        applicableTransactions.add(interestTransaction);

        // remove first transaction if it is not in the month
        Transaction firstTxn = applicableTransactions.get(0);
        if(firstTxn.getTransactionDate().getMonthValue() != month || firstTxn.getTransactionDate().getYear() != year) {
            applicableTransactions.remove(0);
        }
        return applicableTransactions;
    }



    private List<Double> getDailyInterestRates(int month, int year) {
        List<InterestRule> interestRules = bank.getInterestRules();
        LinkedList<InterestRule> applicableRules = new LinkedList<>();

        LocalDate lastOfMonth = YearMonth.of(year, month).atEndOfMonth();
        LocalDate firstOfMonth = YearMonth.of(year, month).atDay(1);

        for(int i= interestRules.size(); i >0; i--) {
            InterestRule rule = interestRules.get(i-1);
            if(rule.getDate().isBefore(lastOfMonth) || rule.getDate().isEqual(lastOfMonth)) {
                // pushing so that most recent rule is at the top
                applicableRules.push(rule);
            }

            if (rule.getDate().isBefore(firstOfMonth)) {
                break;
            }
        }

        if(applicableRules.isEmpty()) {
            return new ArrayList<>(Collections.nCopies(lastOfMonth.getDayOfMonth(), null));
        }
        List<Double> dailyInterestRates = new ArrayList<>(Collections.nCopies(lastOfMonth.getDayOfMonth(), 1.0));
        for(InterestRule rule: applicableRules) {
            // if rule is effective before month apply for all days
            if(rule.getDate().isBefore(firstOfMonth)) {
                dailyInterestRates.replaceAll(_ -> rule.getInterestRate());
            }
            else {
                // if rule is effective within month apply for days after rule effective date
                dailyInterestRates
                        .subList(rule.getDate().getDayOfMonth()-1, dailyInterestRates.size())
                        .replaceAll(_ -> rule.getInterestRate());
            }
        }
        return dailyInterestRates;
    }

    private List<Transaction> getApplicableTransactions(String accountId, int month, int year) {
        List<Transaction> transactions = bank.getAccounts().get(accountId).getTransactions();
        LinkedList<Transaction> applicableTransactions = new LinkedList<>();
        LocalDate lastOfMonth = YearMonth.of(year, month).atEndOfMonth();
        LocalDate firstOfMonth = YearMonth.of(year, month).atDay(1);

        for(int i= transactions.size(); i >0; i--) {
            Transaction transaction = transactions.get(i-1);
            if(transaction.getTransactionDate().isBefore(lastOfMonth) || transaction.getTransactionDate().isEqual(lastOfMonth)) {
                applicableTransactions.push(transaction);
            }

            if (transaction.getTransactionDate().isBefore(firstOfMonth)) {
                break;
            }
        }
        return applicableTransactions;
    }

    private List<Double> getDailyBalances(List<Transaction> applicableTransactions, int month, int year) {
        LocalDate lastOfMonth = YearMonth.of(year, month).atEndOfMonth();
        LocalDate firstOfMonth = YearMonth.of(year, month).atDay(1);
        if(applicableTransactions.isEmpty()) {
            return new ArrayList<>(Collections.nCopies(lastOfMonth.getDayOfMonth(), null));
        }
        List<Double> dailyAccountBalances = new ArrayList<>(Collections.nCopies(lastOfMonth.getDayOfMonth(), 0.0));
        for(Transaction rule: applicableTransactions) {
            // if transaction is done before month apply for all days
            if(rule.getTransactionDate().isBefore(firstOfMonth)) {
                dailyAccountBalances.replaceAll(_ -> rule.getRunningBalance());
            }
            else {
                // if transaction done within month apply for days after rule effective date
                dailyAccountBalances
                        .subList(rule.getTransactionDate().getDayOfMonth()-1, dailyAccountBalances.size())
                        .replaceAll(_ -> rule.getRunningBalance());
            }
        }
        return dailyAccountBalances;
    }

    private double calculateInterest(List<Double> dailyBalances, List<Double> dailyInterestRates) {
        double interest = 0;
        for(int i=0; i<dailyBalances.size(); i++) {
            interest += dailyBalances.get(i) * dailyInterestRates.get(i)/100;
        }
        return interest / 365;
    }

}

