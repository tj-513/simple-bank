package app;

import app.model.InterestRule;
import app.model.Transaction;
import app.model.TransactionType;

import java.util.List;
import java.util.Scanner;

public class InputProcessor {
    private final CoreService service = new CoreService();
    Scanner inputScanner = new Scanner(System.in);
    public void startWorkflow() {

        System.out.println("Welcome to AwesomeGIC Bank! What would you like to do?");
        while (true) {
            printOptions();

            String input = inputScanner.nextLine();
            processOption(input);
        }
    }

    private void printOptions() {
        System.out.println("[T] Input transactions");
        System.out.println("[I] Define interest rules");
        System.out.println("[P] Print statement");
        System.out.println("[Q] Quit");
        System.out.print(">");
    }

    private void processOption(String option) {
        switch (option) {
            case "T":
            case "t":
                processTransaction();
                break;
            case "I":
            case "i":
                processInterestRule();
                break;
            case "P":
            case "p":
                processPrintStatement();
                break;
            case "Q":
            case "q":
                System.out.println("Thank you for using AwesomeGIC Bank!");
                System.out.println("Have a nice day!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void processTransaction() {
        System.out.println("Please enter transaction details in <Date> <Account> <Type> <Amount> format");
        System.out.println("(or enter blank to go back to main menu):");
        System.out.print(">");
        String input = inputScanner.nextLine();
        if (input.isEmpty()) {
            return;
        }
        String[] transactionDetails = input.split(" ");
        try {
            Validations.validateTransactionTokens(transactionDetails);
            List<Transaction> transactions = service
                    .addTransaction(
                            transactionDetails[1], parseTransaction(transactionDetails)
                    );
            System.out.println("Account: " + transactionDetails[1]);
            System.out.println("| Date\t\t| Txn Id\t\t| Type\t| Amount\t|");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
            System.out.println("\nIs there anything else you'd like to do?\n");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void processInterestRule() {
        System.out.println("Please enter interest rule details in <Date> <RuleId> <Rate in %> format");
        System.out.println("(or enter blank to go back to main menu):");
        System.out.print(">");
        String input = inputScanner.nextLine();
        String [] interestRuleDetails = input.split(" ");
        if (input.isEmpty()) {
            return;
        }
        try {
            Validations.validateInterestRuleTokens(interestRuleDetails);
            List<InterestRule> interestRules = service.addInterestRule(parseInterestRule(interestRuleDetails));
            System.out.println("Account: " + interestRuleDetails[1]);
            System.out.println("| Date\t\t| Rule Id\t|Rate (%)\t|");
            for (InterestRule rule : interestRules) {
                System.out.println(rule);
            }
            System.out.println("\nIs there anything else you'd like to do?");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void processPrintStatement() {
        System.out.println("Please enter account and month to generate the statement <Account> <Year><Month>");
        System.out.println("(or enter blank to go back to main menu):");
        System.out.print(">");
        String input = inputScanner.nextLine();
        String [] printStatementDetails = input.split(" ");
        if (input.isEmpty()) {
            return;
        }
        try {
            Validations.validatePrintStatementTokens(printStatementDetails);
            List<Transaction> transactions = service.getAccountStatement(printStatementDetails[0], Integer.parseInt(printStatementDetails[1].substring(4)), Integer.parseInt(printStatementDetails[1].substring(0, 4)));
            System.out.println("Account: " + printStatementDetails[0]);
            System.out.println("| Date\t\t| Txn Id\t\t| Type\t| Amount\t| Balance\t|");
            for (Transaction transaction : transactions) {
                System.out.print(transaction);
                System.out.printf("\t%.2f|", transaction.getRunningBalance());
            }
            System.out.println("\nIs there anything else you'd like to do?");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private Transaction parseTransaction(String[] tokens) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(Utils.convertToDate(tokens[0]));
        transaction.setTransactionType(TransactionType.fromString(tokens[2]));
        transaction.setTransactionAmount(Double.parseDouble(tokens[3]));
        return transaction;
    }

    private InterestRule parseInterestRule(String[] tokens) {
        InterestRule interestRule = new InterestRule();
        interestRule.setDate(Utils.convertToDate(tokens[0]));
        interestRule.setId(tokens[1]);
        interestRule.setInterestRate(Double.parseDouble(tokens[2]));
        return interestRule;
    }
}
