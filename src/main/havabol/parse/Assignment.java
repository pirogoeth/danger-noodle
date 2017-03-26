package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class Assignment implements ParseElement {

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

    public String debug() {
        StringBuilder sb = new StringBuilder();

        sb.append("Assignment ~>\n");
        if ( this.simpleAssign != null ) {
            sb.append("  " + this.simpleAssign.debug());
        } else {
            sb.append("  " + this.declaration.debug());
            sb.append("  " + this.operator.debug());
            sb.append("  " + this.value.debug());
        }

        return sb.toString();
    }

}
