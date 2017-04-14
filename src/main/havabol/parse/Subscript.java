package havabol.parse;

import java.util.*;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

public class Subscript implements ParseElement {

    private Expression beginVal;
    private Expression endVal;

    public Subscript() {
        this.beginVal = null;
        this.endVal = null;
    }

    public Subscript(Expression b) {
        this.beginVal = b;
    }

    public Subscript(Expression b, Expression e) {
        this(b);

        this.endVal = e;
    }

    public Expression getBeginExpr() {
        return this.beginVal;
    }

    public Expression getEndExpr() {
        return this.endVal;
    }

    public boolean isValid() {
        return true;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Subscript ~>\n"));
        if ( this.beginVal != null ) {
            sb.append(lpads(indent, "SRC ::\n"));
            sb.append(this.beginVal.debug(indent + 2));
        }

        if ( this.endVal != null ) {
            sb.append(lpads(indent, "DST ::\n"));
            sb.append(this.endVal.debug(indent + 2));
        }

        return sb.toString();
    }

}
