package app;

import app.model.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CoreService {

    Bank bank = new Bank();
    public CoreService() {}

    /**
     * Add a new account to the bank
     */
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

    /**
     * Add interest rule to the bank
     */
    public List<InterestRule> addInterestRule(InterestRule interestRule) {
        Iterator<InterestRule> iterator = bank.getInterestRules().iterator();
        while (iterator.hasNext()) {
            InterestRule existingRule = iterator.next();
            if (existingRule.getDate().isEqual(interestRule.getDate())) {
                iterator.remove();
                break;
            }
        }

        bank.getInterestRules().add(interestRule);
        bank.getInterestRules().sort(Comparator.comparing(InterestRule::getDate));
        return bank.getInterestRules();
    }

    /**
     * Get account statement for the given account and month
     */
    public List<Transaction> getAccountStatement(String accountId, int month, int year) {
        LocalDate firstOfMonth = YearMonth.of(year, month).atDay(1);
        LocalDate lastOfMonth = YearMonth.of(year, month).atEndOfMonth();

        List<Transaction> applicableTransactions = getApplicableTransactions(accountId, firstOfMonth, lastOfMonth);
        List<Double> dailyBalances = getDailyBalances(applicableTransactions, firstOfMonth, lastOfMonth);
        List<Double> dailyInterestRates = getDailyInterestRates(firstOfMonth, lastOfMonth);

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


    /**
     * Get a list of applicable transactions for the given time period
     */
    private List<Transaction> getApplicableTransactions(String accountId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = bank.getAccounts().get(accountId).getTransactions();
        LinkedList<Transaction> applicableTransactions = new LinkedList<>();

        for(int i= transactions.size(); i >0; i--) {
            Transaction transaction = transactions.get(i-1);
            if(transaction.getTransactionDate().isBefore(endDate) || transaction.getTransactionDate().isEqual(endDate)) {
                applicableTransactions.push(transaction);
            }

            if (transaction.getTransactionDate().isBefore(startDate)) {
                break;
            }
        }
        return applicableTransactions;
    }

    /**
     * Get a list of daily interest rates for the given time period
     */
    private List<Double> getDailyInterestRates(LocalDate startDate, LocalDate endDate) {

        long between = ChronoUnit.DAYS.between(startDate, endDate)+1;

        List<Double> dailyInterestRates = new ArrayList<>(Collections.nCopies((int) between, 0.0));
        for(InterestRule rule: bank.getInterestRules()) {
            // if rule is effective before month apply for all days
            if(rule.getDate().isBefore(startDate)) {
                dailyInterestRates.replaceAll(a -> rule.getInterestRate());
            }
            else {
                // if rule is effective within month apply for days after rule effective date
                dailyInterestRates
                        .subList(rule.getDate().getDayOfMonth()-1, dailyInterestRates.size())
                        .replaceAll(a -> rule.getInterestRate());
            }
        }
        return dailyInterestRates;
    }

    /**
     * Get a list of daily EOD account balances for the given time period
     */
    private List<Double> getDailyBalances(List<Transaction> applicableTransactions, LocalDate startDate, LocalDate endDate) {
        long between = ChronoUnit.DAYS.between(startDate, endDate)+1;

        List<Double> dailyAccountBalances = new ArrayList<>(Collections.nCopies((int) between, 0.0));
        for(Transaction transaction: applicableTransactions) {
            if(transaction.getTransactionDate().isBefore(startDate)){
                dailyAccountBalances.replaceAll(a -> transaction.getRunningBalance());
            } else {
                dailyAccountBalances
                        .subList(transaction.getTransactionDate().getDayOfMonth()-1, dailyAccountBalances.size())
                        .replaceAll(a -> transaction.getRunningBalance());
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

