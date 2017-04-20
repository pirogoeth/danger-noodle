package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

import java.util.*;

public class Statement extends ParseElement {

    private StatementType stmtType = StatementType.UNKNOWN;

    private Assignment assign = null;
    private Block block = null;
    private Declaration decl = null;
    private Expression expr = null;
    private FlowControl flow = null;

    public Statement() {
        this.stmtType = StatementType.NO_OP;
    }

    public Statement(Assignment assign) {
        this.assign = assign;

        this.stmtType = StatementType.ASSIGN;
    }

    public Statement(Block b) {
        this.block = b;

        this.stmtType = StatementType.BLOCK;
    }

    public Statement(Declaration decl) {
        this.decl = decl;

        this.stmtType = StatementType.DECLARATION;
    }

    public Statement(Expression expr) {
        this.expr = expr;

        this.stmtType = StatementType.EXPRESSION;
    }

    public Statement(FlowControl flow) {
        this.flow = flow;

        this.stmtType = StatementType.FLOW_CTRL;
    }

    public StatementType getStatementType() {
        return this.stmtType;
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

    // Getters for all statement types

    public Assignment getAssignment() {
        return this.assign;
    }

    public Block getBlock() {
        return this.block;
    }

    public Declaration getDeclaration() {
        return this.decl;
    }

    public Expression getExpression() {
        return this.expr;
    }

    public FlowControl getFlowControl() {
        return this.flow;
    }

}
