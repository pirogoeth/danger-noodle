package havabol.classify;

public enum Precedence {

    EXPONENT(1),
    MULTIPLY(2),
    DIVIDE(3),
    ADD(4),
    SUBTRACT(5);

    private int priority;

    Precedence(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

}
