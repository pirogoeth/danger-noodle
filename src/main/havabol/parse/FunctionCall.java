package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class FunctionCall implements ParseElement {

    private Identifier functionName = null;
    private List<Expression> argsList = new ArrayList<>();

    public FunctionCall(Identifier functionName, List<Expression> argsList) {
        this.functionName = functionName;
        this.argsList = argsList;
    }

    public boolean isValid() {
        return this.functionName.isValid() && this.argsList.stream()
            .map(expr -> expr.isValid())
            .reduce(true, (a, b) -> a && b);
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();

        sb.append("FunctionCall ~>\n");
        sb.append("  " + this.functionName.debug());
        this.argsList
            .stream()
            .forEach((expr) -> sb.append("  " + expr.debug()));

        return sb.toString();
    }
}
