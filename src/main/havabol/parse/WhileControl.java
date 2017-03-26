package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class WhileControl implements ParseElement {

    private Expression condition;
    private Block loopBranch;

    public WhileControl(Expression cond, Block lb) {
        this.condition = cond;
        this.loopBranch = lb;
    }

    public boolean isValid() {
        return ( this.condition.isValid() ) &&
               ( this.loopBranch != null && this.loopBranch.isValid() );
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();

        sb.append("While ~>\n");
        sb.append("  " + this.condition.debug());

        sb.append("Do ~>\n");
        sb.append("  " + this.loopBranch.debug());

        return sb.toString();
    }

}
