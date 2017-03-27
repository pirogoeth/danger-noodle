package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class ForControl implements ParseElement {

    private Expression cond;
    private Block body;

    public ForControl(Expression cond, Block body) {
        this.cond = cond;
        this.body = body;
    }

    public boolean isValid() {
        return this.cond.isValid() && this.body.isValid();
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();

        sb.append("For ~>\n");
        sb.append("  Cond " + this.cond.debug());
        sb.append("  " + this.body.debug());

        return sb.toString();
    }

}
