package app;

import java.util.regex.Pattern;

public class Validations {
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}\\d{2}\\d{2}");

    public static void validateTransactionTokens(String[] tokens) {
        if (tokens.length != 4) {
            throw new IllegalArgumentException("Invalid transaction format");
        }

        if (isValidDate(tokens[0])) {
            throw new IllegalArgumentException("Invalid transaction date");
        }

        if (tokens[1] == null || tokens[1].isEmpty()) {
            throw new IllegalArgumentException("Invalid account id");
        }

        if (!tokens[2].equals("D") && !tokens[2].equals("W")) {
            throw new IllegalArgumentException("Invalid transaction type");
        }
        try {
            double amount = Double.parseDouble(tokens[3]);
            if (amount < 0) {
                throw new IllegalArgumentException("Invalid transaction amount");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid transaction amount");
        }
    }

    public static void validateInterestRuleTokens(String[] tokens) {
        if (tokens.length != 3) {
            throw new IllegalArgumentException("Invalid interest rule format");
        }

        if (isValidDate(tokens[0])) {
            throw new IllegalArgumentException("Invalid interest rule date");
        }

        try {
            double rate = Double.parseDouble(tokens[2]);
            if (100  <= rate || rate <= 0) {
                throw new IllegalArgumentException("Invalid interest rate");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid interest rate");
        }
    }

    public static boolean isValidDate(String date) {
        if (date == null) {
            return true;
        }
        return !DATE_PATTERN.matcher(date).matches();
    }
}
