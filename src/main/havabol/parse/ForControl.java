package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

import java.util.*;

public class ForControl implements ParseElement {

    private ForExpr cond;
    private Block body;

    public ForControl(ForExpr cond, Block body) {
        this.cond = cond;
        this.body = body;
    }

    public ForExpr getCondition() {
        return this.cond;
    }

    public Block getBody() {
        return this.body;
    }

    public boolean isValid() {
        return this.cond.isValid() && this.body.isValid();
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "For ~>\n"));
        sb.append(lpads(indent, "COND ::\n"));
        sb.append(this.cond.debug(indent + 2));
        sb.append(lpads(indent, "EXEC ::\n"));
        sb.append(this.body.debug(indent + 2));

        return sb.toString();
    }

}
