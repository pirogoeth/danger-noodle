package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class FunctionCall implements Validate {

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

    public void print() {
        System.out.println("FunctionCall ~>");
        this.functionName.print();
        this.argsList
            .stream()
            .forEach((expr) -> expr.print());
    }
}
