package test.service;

import app.model.Transaction;
import app.model.TransactionType;
import app.service.CoreService;

import java.time.LocalDate;
import java.util.List;

public class CoreServiceTest {
    CoreService coreService = CoreService.getInstance();

    public void testAddTransactions() {
        // Test addTransaction
        Transaction t1 = getTransaction(100, LocalDate.of(2025, 1, 17), TransactionType.DEPOSIT);

        Transaction t2 = getTransaction(200, LocalDate.of(2025, 1, 17), TransactionType.DEPOSIT);

        Transaction t3 = getTransaction(100, LocalDate.of(2025, 1, 18), TransactionType.WITHDRAWAL);

        coreService.addTransaction("1", t1);
        coreService.addTransaction("1", t2);
        List<Transaction> transactions = coreService.addTransaction("1", t3);

        assert transactions.get(0).getId().equals("20250117-01"): "Invalid transaction id";
        assert transactions.get(1).getId().equals("20250117-02"): "Invalid transaction id";
        assert transactions.get(2).getRunningBalance() == 200: "Invalid running balance calculated";

    }

    public void testAddTransactionWhenInitialIsWithdraw(){
        Transaction t1 = getTransaction(100, LocalDate.of(2025, 1, 17), TransactionType.WITHDRAWAL);
        try {
            coreService.addTransaction("1", t1);
            assert false: "Expected exception not thrown";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Insufficient funds"): "Invalid exception message";
        }
    }

    private static Transaction getTransaction(double amount, LocalDate date, TransactionType type) {
        Transaction t3 = new Transaction();
        t3.setTransactionAmount(amount);
        t3.setTransactionDate(date);
        t3.setTransactionType(type);
        return t3;
    }


    public void runTests() {
        testAddTransactionWhenInitialIsWithdraw();
        testAddTransactions();
        System.out.println("CoreService tests passed");
    }
}
