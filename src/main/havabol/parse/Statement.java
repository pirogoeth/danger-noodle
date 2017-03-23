package havabol.parse;

import havabol.Token;

import java.util.*;

public class Statement implements Validate {

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

    public void print() {
        System.out.println("Statement ~~>");
        if ( this.decl != null ) {
            this.decl.print();
        } else if ( this.expr != null ) {
            this.expr.print();
        } else if ( this.assign != null ) {
            this.assign.print();
        }
    }

}
