package havabol.parse;

import havabol.Token;
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

        // Word operators
        "and", "or", "not", "in", "notin", "to",
    };

    public Operator(Token t) {
        this.operatorToken = t;
    }

    public String getOperator() {
        return this.operatorToken.tokenStr;
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
