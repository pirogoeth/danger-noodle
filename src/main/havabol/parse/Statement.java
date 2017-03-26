package havabol.parse;

import havabol.Token;

import java.util.*;

public class Statement implements ParseElement {

    private Assignment assign = null;
    private Declaration decl = null;
    private Expression expr = null;

    public Statement(Assignment assign) {
        this.assign = assign;
    }

    public Statement(Declaration decl) {
        this.decl = decl;
    }

    public Statement(Expression expr) {
        this.expr = expr;
    }

    public boolean isValid() {
        return ( this.decl != null ||
                 this.expr != null ||
                 this.assign != null );
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();

        sb.append("Statement ~~>\n");
        if ( this.decl != null ) {
            sb.append("  " + this.decl.debug());
        } else if ( this.expr != null ) {
            sb.append("  " + this.expr.debug());
        } else if ( this.assign != null ) {
            sb.append("  " + this.assign.debug());
        }

        return sb.toString();
    }

}
