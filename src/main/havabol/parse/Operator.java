package havabol.parse;

import havabol.Token;

import java.util.*;

public class Operator implements ParseElement {

    private Token operatorToken;
    private final String[] validOperators = {
        // Short operators
        "=", "%", "*", "/", "^", "+",
        "-", "#", "<", ">", "!",

        // Long operators
        "==", ">=", "<=", "!=",

        // Word operators
        "and", "or", "not", "in", "notin", "to",
    };

    public Operator(Token t) {
        this.operatorToken = t;
    }

    public boolean isValid() {
        return Arrays.asList(validOperators).contains(this.operatorToken.tokenStr);
    }

    public String debug() {
        return String.format("Operator ~> `%s`\n", this.operatorToken.tokenStr);
    }
}
