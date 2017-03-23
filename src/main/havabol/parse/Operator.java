package havabol.parse;

import havabol.Token;

import java.util.*;

public class Operator implements Validate {

    private Token operatorToken;
    private final String[] validOperators = {
        // Short operators
        "=", "%", "*", "/", "^", "+",
        "-", "#", "<", ">", "!",

        // Long operators
        "==", ">=", "<=", "!=",

        // Word operators
        "and", "or", "not", "in", "notin",
    };

    public Operator(Token t) {
        this.operatorToken = t;
    }

    public boolean isValid() {
        return Arrays.asList(validOperators).contains(this.operatorToken.tokenStr);
    }

    public void print() {
        System.out.println(
                String.format(
                    "Operator ~> `%s`",
                    this.operatorToken.tokenStr
                )
        );
    }
}
