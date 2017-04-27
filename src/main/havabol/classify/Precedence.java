package havabol.classify;

public enum Precedence {

    PARENTHESES(0),
    EXPONENT(1),
    MULTIPLICATIVE(2),
    ADDITIVE(4),
    CONCAT(6),
    COMPARE(7),
    NEGATE(8),
    COMBINE(9);

    private int priority;

    Precedence(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

}
