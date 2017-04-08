package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

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

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "FunctionCall ~>\n"));
        sb.append(this.functionName.debug(indent + 2));
        sb.append(lpads(indent + 2, "Args ~>\n"));
        this.argsList
            .stream()
            .forEach((expr) -> sb.append(expr.debug(indent + 4)));

        return sb.toString();
    }
}
