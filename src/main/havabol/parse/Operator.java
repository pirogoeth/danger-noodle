package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

import java.util.*;

public class Operator extends ParseElement {

    private Token operatorToken;
    private final String[] validOperators = {
        // Short operators
        "=", "%", "*", "/", "^", "+",
        "-", "#", "<", ">", "!",

        // Long operators
        "==", ">=", "<=", "!=",

        // Grouping operators
        ")", "(",

        // Word operators
        "and", "or", "not", "in", "notin", "to",
    };

    public Operator(Token t) {
        this.operatorToken = t;
    }

    public String getOperator() {
        return this.operatorToken.tokenStr;
    }

    public Precedence getPrecedence() {
        switch (this.operatorToken.tokenStr) {
            case "^":
                return Precedence.EXPONENT;
            case "+":
            case "-":
                return Precedence.ADDITIVE;
            case "*":
            case "/":
            case "%":
                return Precedence.MULTIPLICATIVE;
            case "#":
                return Precedence.CONCAT;
            case ">":
            case "<":
            case "<=":
            case ">=":
            case "==":
            case "!=":
                return Precedence.COMPARE;
            case "and":
            case "or":
                return Precedence.COMBINE;
            default:
                return null;
        }
    }


    public boolean isValid() {
        return Arrays.asList(validOperators).contains(this.operatorToken.tokenStr);
    }

    public String debug(int indent) {
        return lpads(
            indent,
            String.format(
                "Operator ~> `%s`\n",
                this.operatorToken.tokenStr
            )
        );
    }

}
