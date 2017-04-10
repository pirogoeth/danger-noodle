package havabol.eval;

import havabol.common.*;

/**
 * EvalException exposes a series of statements and a message
 * that can be used to generate context for finding the error
 * inside code.
 */
public class EvalException extends Exception {

    public static final long serialVersionUID = 1000005L;
    private Debuggable[] ctxts;

    public EvalException(String message) {
        super(message);
    }

    public EvalException(String message, Debuggable...contexts) {
        super(message);
        this.ctxts = contexts;
    }

    public Debuggable[] getCausalContexts() {
        return this.ctxts;
    }

}

