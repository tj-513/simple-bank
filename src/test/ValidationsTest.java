package test;

import app.Validations;

public class ValidationsTest {

    public static void main(String[] args) {
        testValidInterestRuleTokens();
        testInvalidInterestRuleFormat();
        testInvalidInterestRuleDate();
        testInvalidInterestRate();
        testInvalidInterestRateFormat();
        System.out.println("All tests passed.");

        testValidTransactionTokens();
        testInvalidTransactionFormat();
        testInvalidTransactionDate();
        testInvalidAccountId();
        testInvalidTransactionType();
        testInvalidTransactionAmount();
        testNegativeTransactionAmount();
        System.out.println("All tests passed.");
    }

    public static void testValidInterestRuleTokens() {
        String[] validTokens = {"20230101", "RULE01", "1.95"};
        try {
            Validations.validateInterestRuleTokens(validTokens);
        } catch (IllegalArgumentException e) {
            assert false : "Valid tokens should not throw an exception";
        }
    }

    public static void testInvalidInterestRuleFormat() {
        String[] invalidTokens = {"20230101", "RULE01"};
        try {
            Validations.validateInterestRuleTokens(invalidTokens);
            assert false : "Invalid format should throw an exception";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid interest rule format") : "Expected 'Invalid interest rule format' message";
        }
    }

    public static void testInvalidInterestRuleDate() {
        String[] invalidDateTokens = {"202301", "RULE01", "1.95"};
        try {
            Validations.validateInterestRuleTokens(invalidDateTokens);
            assert false : "Invalid date should throw an exception";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid interest rule date") : "Expected 'Invalid interest rule date' message";
        }
    }

    public static void testInvalidInterestRate() {
        String[] invalidRateTokens = {"20230101", "RULE01", "101"};
        try {
            Validations.validateInterestRuleTokens(invalidRateTokens);
            assert false : "Invalid rate should throw an exception";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid interest rate") : "Expected 'Invalid interest rate' message";
        }
    }

    public static void testInvalidInterestRateFormat() {
        String[] invalidRateFormatTokens = {"20230101", "RULE01", "rate"};
        try {
            Validations.validateInterestRuleTokens(invalidRateFormatTokens);
            assert false : "Invalid rate format should throw an exception";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid interest rate") : "Expected 'Invalid interest rate' message";
        }
    }


    public static void testValidTransactionTokens() {
        String[] validTokens = {"20230101", "ACC01", "D", "100.00"};
        try {
            Validations.validateTransactionTokens(validTokens);
        } catch (IllegalArgumentException e) {
            assert false : "Valid tokens should not throw an exception";
        }
    }

    public static void testInvalidTransactionFormat() {
        String[] invalidTokens = {"20230101", "ACC01", "D"};
        try {
            Validations.validateTransactionTokens(invalidTokens);
            assert false : "Invalid format should throw an exception";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid transaction format") : "Expected 'Invalid transaction format' message";
        }
    }

    public static void testInvalidTransactionDate() {
        String[] invalidDateTokens = {"202301", "ACC01", "D", "100.00"};
        try {
            Validations.validateTransactionTokens(invalidDateTokens);
            assert false : "Invalid date should throw an exception";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid transaction date") : "Expected 'Invalid transaction date' message";
        }
    }

    public static void testInvalidAccountId() {
        String[] invalidAccountIdTokens = {"20230101", "", "D", "100.00"};
        try {
            Validations.validateTransactionTokens(invalidAccountIdTokens);
            assert false : "Invalid account id should throw an exception";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid account id") : "Expected 'Invalid account id' message";
        }
    }

    public static void testInvalidTransactionType() {
        String[] invalidTypeTokens = {"20230101", "ACC01", "X", "100.00"};
        try {
            Validations.validateTransactionTokens(invalidTypeTokens);
            assert false : "Invalid transaction type should throw an exception";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid transaction type") : "Expected 'Invalid transaction type' message";
        }
    }

    public static void testInvalidTransactionAmount() {
        String[] invalidAmountTokens = {"20230101", "ACC01", "D", "amount"};
        try {
            Validations.validateTransactionTokens(invalidAmountTokens);
            assert false : "Invalid transaction amount should throw an exception";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid transaction amount") : "Expected 'Invalid transaction amount' message";
        }
    }

    public static void testNegativeTransactionAmount() {
        String[] negativeAmountTokens = {"20230101", "ACC01", "D", "-100.00"};
        try {
            Validations.validateTransactionTokens(negativeAmountTokens);
            assert false : "Negative transaction amount should throw an exception";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().equals("Invalid transaction amount") : "Expected 'Invalid transaction amount' message";
        }
    }
}