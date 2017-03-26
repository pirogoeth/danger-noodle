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
        boolean argsListValid = true;
        for ( Expression expr : this.argsList ) {
            if ( expr.isValid() && argsListValid ) {
                continue;
            } else if ( !expr.isValid() && argsListValid ) {
                argsListValid = false;
            } else if ( !argsListValid ) {
                break;
            }
        }

        return this.functionName.isValid() && argsListValid;
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
