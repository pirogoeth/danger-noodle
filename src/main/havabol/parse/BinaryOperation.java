package havabol.parse;

import havabol.Token;

import java.util.*;

public class BinaryOperation implements Validate {

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

    public boolean isValid() {
        return ( this.lhs.isValid() &&
                 this.operator.isValid() &&
                 this.rhs.isValid() );
    }

    public void print() {
        System.out.println("BinaryOperation ~>");
        this.lhs.print();
        this.operator.print();
        this.rhs.print();
    }

}
