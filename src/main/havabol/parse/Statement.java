package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

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
        return this.debug(0);
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Statement ~~>\n"));
        if ( this.decl != null ) {
            sb.append(this.decl.debug(indent + 2));
        } else if ( this.expr != null ) {
            sb.append(this.expr.debug(indent + 2));
        } else if ( this.assign != null ) {
            sb.append(this.assign.debug(indent + 2));
        } else if ( this.flow != null ) {
            sb.append(this.flow.debug(indent + 2));
        } else if ( this.block != null ) {
            sb.append(this.block.debug(indent + 2));
        }

        return sb.toString();
    }

}
