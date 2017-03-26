package havabol.parse;

import havabol.Token;

import java.util.*;

public class Statement implements ParseElement {

    private Assignment assign = null;
    private Block block = null;
    private Declaration decl = null;
    private Expression expr = null;
    private FlowControl flow = null;

    public Statement(Assignment assign) {
        this.assign = assign;
    }

    public Statement(Block b) {
        this.block = b;
    }

    public Statement(Declaration decl) {
        this.decl = decl;
    }

    public Statement(Expression expr) {
        this.expr = expr;
    }

    public Statement(FlowControl flow) {
        this.flow = flow;
    }

    public boolean isValid() {
        return ( this.decl != null && this.decl.isValid() ) ||
               ( this.expr != null && this.expr.isValid() ) ||
               ( this.assign != null && this.assign.isValid() ) ||
               ( this.flow != null && this.flow.isValid() ) ||
               ( this.block != null && this.block.isValid() );
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
        } else if ( this.flow != null ) {
            sb.append("  " + this.flow.debug());
        } else if ( this.block != null ) {
            sb.append("  " + this.block.debug());
        }

        return sb.toString();
    }

}
