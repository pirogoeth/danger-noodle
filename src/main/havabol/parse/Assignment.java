package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

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

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Assignment ~>\n"));
        if ( this.simpleAssign != null ) {
            sb.append(this.simpleAssign.debug(indent + 2));
        } else {
            sb.append(this.declaration.debug(indent + 2));
            sb.append(this.operator.debug(indent + 2));
            sb.append(this.value.debug(indent + 2));
        }

        return sb.toString();
    }

}
