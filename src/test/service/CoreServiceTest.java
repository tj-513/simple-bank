package test.service;

import app.model.InterestRule;
import app.model.Transaction;
import app.model.TransactionType;
import app.service.CoreService;

import java.time.LocalDate;
import java.util.List;

public class CoreServiceTest {
    CoreService coreService = new CoreService();

    private void resetCoreService() {
        this.coreService = new CoreService();
    }

    public void testAddTransactions() {
        resetCoreService();
        // Test addTransaction
        Transaction t1 = getTransaction(100, LocalDate.of(2025, 1, 17), TransactionType.DEPOSIT);

        Transaction t2 = getTransaction(150, LocalDate.of(2025, 1, 17), TransactionType.DEPOSIT);

        Transaction t3 = getTransaction(150, LocalDate.of(2025, 1, 18), TransactionType.WITHDRAWAL);

        coreService.addTransaction("1", t1);
        coreService.addTransaction("1", t2);
        List<Transaction> transactions = coreService.addTransaction("1", t3);

        assert transactions.get(0).getId().equals("20250117-01"): "Invalid transaction id";
        assert transactions.get(1).getId().equals("20250117-02"): "Invalid transaction id";
        assert transactions.get(2).getRunningBalance() == 100: "Invalid running balance calculated";

    }

    public void testAddTransactionWhenInitialIsWithdraw(){
        resetCoreService();
        Transaction t1 = getTransaction(100, LocalDate.of(2025, 1, 17), TransactionType.WITHDRAWAL);
        try {
            coreService.addTransaction("1", t1);
            assert false: "Expected exception not thrown";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Insufficient funds"): "Invalid exception message";
        }
    }

    public void testAddInterestRule() {
        resetCoreService();
        InterestRule interestRule = new InterestRule();
        interestRule.setInterestRate(0.1);
        interestRule.setDate(LocalDate.of(2025, 1, 1));
        List<InterestRule> interestRules = coreService.addInterestRule(interestRule);
        assert interestRules.size() == 1: "Invalid interest rule count";

    }

    public void testAccountSummary() {
        resetCoreService();

        Transaction t0 = getTransaction(100, LocalDate.of(2025, 5, 1), TransactionType.DEPOSIT);
        Transaction t1 = getTransaction(150, LocalDate.of(2025, 6, 1), TransactionType.DEPOSIT);
        Transaction t2 = getTransaction(20, LocalDate.of(2025, 6, 26), TransactionType.WITHDRAWAL);
        Transaction t3 = getTransaction(100, LocalDate.of(2025, 6, 26), TransactionType.WITHDRAWAL);

        InterestRule i1 = getInterestRule(1.9, LocalDate.of(2025, 5, 20));
        InterestRule i2 = getInterestRule(2.2, LocalDate.of(2025, 6, 15));

        coreService.addTransaction("1", t0);
        coreService.addTransaction("1", t1);
        coreService.addTransaction("1", t2);
        coreService.addTransaction("1", t3);
        coreService.addInterestRule(i1);
        coreService.addInterestRule(i2);

        List<Transaction> statement = coreService.getAccountStatement("1", 6, 2025);
        assert statement.size() == 4: "Invalid statement size";
        assert statement.get(statement.size()-1).getTransactionType() == TransactionType.INTEREST: "Interest transaction not added";
        assert  "0.39".equals(String.format("%.2f", statement.get(statement.size()-1).getTransactionAmount())): "Invalid interest calculated";

        for (Transaction transaction : statement) {
            assert transaction.getTransactionDate().getMonthValue() == 6 : "Transaction not in the month of June";
        }
    }

    private static Transaction getTransaction(double amount, LocalDate date, TransactionType type) {
        Transaction t3 = new Transaction();
        t3.setTransactionAmount(amount);
        t3.setTransactionDate(date);
        t3.setTransactionType(type);
        return t3;
    }

    private static InterestRule getInterestRule(double rate, LocalDate date) {
        InterestRule interestRule = new InterestRule();
        interestRule.setInterestRate(rate);
        interestRule.setDate(date);
        return interestRule;
    }


    public void runTests() {
        testAddTransactionWhenInitialIsWithdraw();
        testAddTransactions();
        testAddInterestRule();
        testAccountSummary();
        System.out.println("CoreService tests passed");
    }
}
