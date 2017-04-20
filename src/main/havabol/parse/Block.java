package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

import java.util.*;

public class Block implements ParseElement {

    private List<Statement> stmts;

    public Block() {
        this.stmts = new ArrayList<>();
    }

    public Block(List<Statement> stmts) {
        this.stmts = stmts;
    }

    public void addStmt(Statement stmt) {
        this.stmts.add(stmt);
    }

    public List<Statement> getStmts() {
        return this.stmts;
    }

    public boolean isValid() {
        for (Statement stmt : this.stmts) {
            if ( ! stmt.isValid() ) {
                return false;
            }
        }

        return true;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Block ~>\n"));

        for (Statement stmt : this.stmts) {
            sb.append(stmt.debug(indent + 2));
        }

        return sb.toString();
    }

}
