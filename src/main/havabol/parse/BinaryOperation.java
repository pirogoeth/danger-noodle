package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

import java.util.*;

public class BinaryOperation extends ParseElement {

    /**
     * Left-hand side can be an expression, in the case of comparing two constants (but why?)
     */
    private Expression lhs;

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

    public BinaryOperation(Expression lhs, Operator operator, Expression rhs) {
        this.lhs = lhs;
        this.operator = operator;
        this.rhs = rhs;
    }

    public Expression getLHS() {
        return this.lhs;
    }

    public Operator getOper() {
        return this.operator;
    }

    public Expression getRHS() {
        return this.rhs;
    }

    public boolean isValid() {
        return ( this.lhs.isValid() &&
                 this.operator.isValid() &&
                 this.rhs.isValid() );
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "BinaryOperation ~>\n"));
        sb.append(lpads(indent + 2, "LHS ~>\n"));
        sb.append(this.lhs.debug(indent + 4));
        sb.append(this.operator.debug(indent + 2));
        sb.append(lpads(indent + 2, "RHS ~>\n"));
        sb.append(this.rhs.debug(indent + 4));

        return sb.toString();
    }

}
