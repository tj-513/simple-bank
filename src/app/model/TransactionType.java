package app.model;

public enum TransactionType {

    DEPOSIT("D"),
    WITHDRAWAL("W"),
    INTEREST("I");

    private final String code;

    TransactionType(String type) {
        this.code = type;
    }

    public String getCode() {
        return code;
    }

    public static TransactionType fromString(String type) {
        for (TransactionType t : TransactionType.values()) {
            if (t.getCode().equalsIgnoreCase(type)) {
                return t;
            }
        }
        return null;
    }
}
