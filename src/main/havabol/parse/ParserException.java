package havabol.parse;

import havabol.Token;

/**
 * ParserException exposes a series of tokens and a message
 * that can be used to generate context for finding the error
 * inside code.
 */
public class ParserException extends Exception {

    public static final long serialVersionUID = 1000000L;
    private Token[] tokens;

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Token...contexts) {
        super(message);
        this.tokens = contexts;
    }

    public Token[] getCausalTokens() {
        return this.tokens;
    }

}


