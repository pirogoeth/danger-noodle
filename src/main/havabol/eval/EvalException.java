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
    private Exception cause = null;

    public EvalException(String message) {
        super(message);
    }

    public EvalException(String message, Debuggable...contexts) {
        super(message);
        this.ctxts = contexts;
    }

    public EvalException(String message, Exception cause, Debuggable...contexts) {
        super(message);
        this.ctxts = contexts;
        this.cause = cause;
    }

    public Debuggable[] getCausalContexts() {
        return this.ctxts;
    }

    public Exception getCausalException() {
        return this.cause;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();

        if ( this.cause != null ) {
            System.out.printf("\nCaused by: ");
            this.cause.printStackTrace();
        }
    }

}

