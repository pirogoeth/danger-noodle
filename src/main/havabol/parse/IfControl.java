package havabol.parse;

import havabol.Token;
import havabol.classify.*;

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

    public String debug() {
        StringBuilder sb = new StringBuilder();

        sb.append("If ~>\n");
        sb.append("  " + this.condition.debug());
        sb.append("  " + this.trueBranch.debug());

        if ( this.elseBranch != null ) {
            sb.append("  Else ~>\n");
            sb.append("  " + this.elseBranch.debug());
        }

        return sb.toString();
    }

}
