package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

import java.util.*;

public class IfControl implements ParseElement {

    private Expression condition;
    private Block trueBranch;
    private Block elseBranch;

    public IfControl(Expression cond, Block tb) {
        this.condition = cond;
        this.trueBranch = tb;
    }

    public IfControl(Expression cond, Block tb, Block eb) {
        this(cond, tb);

        this.elseBranch = eb;
    }

    public boolean isValid() {
        return ( this.condition.isValid() ) &&
               ( this.trueBranch != null && this.trueBranch.isValid() ) &&
               ( this.elseBranch != null && this.elseBranch.isValid() );
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "If ~>\n"));
        sb.append(lpads(indent, "COND ::\n"));
        sb.append(this.condition.debug(indent + 2));
        sb.append(lpads(indent, "EXEC ::\n"));
        sb.append(this.trueBranch.debug(indent + 2));

        if ( this.elseBranch != null ) {
            sb.append(lpads(indent, "Else ~>\n"));
            sb.append(lpads(indent, "EXEC ::\n"));
            sb.append(this.elseBranch.debug(indent + 2));
        }

        return sb.toString();
    }

}
