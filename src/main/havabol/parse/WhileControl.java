package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

import java.util.*;

public class WhileControl implements ParseElement {

    private Expression condition;
    private Block loopBranch;

    public WhileControl(Expression cond, Block lb) {
        this.condition = cond;
        this.loopBranch = lb;
    }

    public Expression getCondition() {
        return this.condition;
    }

    public Block getLoopBranch() {
        return this.loopBranch;
    }

    public boolean isValid() {
        return ( this.condition.isValid() ) &&
               ( this.loopBranch != null && this.loopBranch.isValid() );
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "While ~>\n"));
        sb.append(this.condition.debug(indent + 2));

        sb.append(lpads(indent + 2, "Do ~>\n"));
        sb.append(this.loopBranch.debug(indent + 4));

        return sb.toString();
    }

}
