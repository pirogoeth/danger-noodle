package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.function.*;
import havabol.sym.*;
import static havabol.util.Text.*;

import java.util.*;

public class FunctionCall implements ParseElement {

    private Identifier functionName = null;
    private List<Expression> argsList = new ArrayList<>();

    public FunctionCall(Identifier functionName, List<Expression> argsList) {
        this.functionName = functionName;
        this.argsList = argsList;
    }

    public FunctionInterface resolveFunctionHandle() {
        return this.resolveFunctionHandle(SymbolTable.getGlobal());
    }

    public FunctionInterface resolveFunctionHandle(SymbolTable symScope) {
        LookupTable tab = LookupTable.getFor(symScope);
        STFunction stFunc = null;
        STEntry tmp = symScope.lookupSym(this.functionName.getIdentT());
        if ( tmp instanceof STFunction ) {
            stFunc = (STFunction) tmp;
        } else {
            // XXX - Should throw an ExecException
        }

        return tab.lookupFunction(stFunc);
    }

    public List<Expression> getArgs() {
        return this.argsList;
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
