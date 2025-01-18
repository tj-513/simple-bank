package app.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Bank {
    private final Map<String,Account> accounts = new HashMap<>();
    private final List<InterestRule> interestRules = new LinkedList<>();

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public List<InterestRule> getInterestRules() {
        return interestRules;
    }
}
