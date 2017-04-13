package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

import java.util.*;

public class UnaryOperation implements ParseElement {

    /**
     * Middle should ALWAYS be an operator.
     */
    private Operator operator;

    /**
     * Right-hand side can be an expression.
     * An expression is defined as anything that returns a value, which means:
     * * constants
     * * function call
     * * binary/unary operations
     * * string literals
     */
    private Expression rhs;

    public UnaryOperation(Operator operator, Expression rhs) {
        this.operator = operator;
        this.rhs = rhs;
    }

    public Operator getOper() {
        return this.operator;
    }

    public Expression getRHS() {
        return this.rhs;
    }

    public boolean isValid() {
        return ( this.operator.isValid() &&
                 this.rhs.isValid() );
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "UnaryOperation ~>\n"));
        sb.append(this.operator.debug(indent + 2));
        sb.append(lpads(indent + 2, "RHS ~>\n"));
        sb.append(this.rhs.debug(indent + 4));

        return sb.toString();
    }

}
