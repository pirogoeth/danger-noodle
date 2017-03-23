package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class Assignment implements Validate {

    private Declaration declaration;
    private Operator operator;
    private Expression value;

    private BinaryOperation simpleAssign;

    public Assignment(BinaryOperation binOp) {
        this.simpleAssign = binOp;
    }

    public Assignment(Declaration decl, Operator oper, Expression expr) {
        this.declaration = decl;
        this.operator = oper;
        this.value = expr;
    }

    public boolean isValid() {
        if ( this.simpleAssign != null ) {
            return this.simpleAssign.isValid();
        } else {
            return ( this.declaration.isValid() &&
                     this.operator.isValid() &&
                     this.value.isValid() );
        }
    }

    public void print() {
        System.out.println("Assignment ~>");
        if ( this.simpleAssign != null ) {
            this.simpleAssign.print();
        } else {
            this.declaration.print();
            this.operator.print();
            this.value.print();
        }
    }

}
