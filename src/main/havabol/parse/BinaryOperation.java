package havabol.parse;

import havabol.Token;
import havabol.classify.*;
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

    /**
     * If this is the child of another binary operation, this var should
     * hold a reference to the parent.
     */
    private BinaryOperation parent = null;

    private boolean explicitlyGrouped = false;

    public BinaryOperation(Expression lhs, Operator operator, Expression rhs) {
        this.lhs = lhs;
        this.operator = operator;
        this.rhs = rhs;

        if ( this.lhs.getExpressionType() == ExpressionType.BINARY_OP ) {
            this.lhs.getBinaryOperation().setParent(this);
        }

        if ( this.rhs.getExpressionType() == ExpressionType.BINARY_OP ) {
            this.rhs.getBinaryOperation().setParent(this);
        }
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

    public boolean isRoot() {
        return this.parent == null;
    }

    public BinaryOperation getParent() {
        return this.parent;
    }

    public boolean isExplicitlyGrouped() {
        return this.explicitlyGrouped;
    }

    public void setExplicitlyGrouped(boolean b) {
        this.explicitlyGrouped = b;
    }

    /**
     * Can only set the value if the parent is currently null.
     * NO REASSIGNMENT!
     */
    public void setParent(BinaryOperation parent) {
        if ( this.parent != null ) {
            // System.out.println("===> TRIED TO SET PARENT ON ALREADY OWNED CHILD BINOP");
            return;
        }

        this.parent = parent;
    }

    public void clearParent() {
        this.parent = null;
    }

    public boolean isValid() {
        return ( this.lhs.isValid() &&
                 this.operator.isValid() &&
                 this.rhs.isValid() );
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "BinaryOperation ~>\n"));
        if ( this.isRoot() ) {
            sb.append(lpads(indent, ":: ROOT BINOP ::\n"));
        }
        sb.append(lpads(indent + 2, "LHS ~>\n"));
        sb.append(this.lhs.debug(indent + 4));
        sb.append(this.operator.debug(indent + 2));
        sb.append(lpads(indent + 2, "RHS ~>\n"));
        sb.append(this.rhs.debug(indent + 4));

        return sb.toString();
    }

}
