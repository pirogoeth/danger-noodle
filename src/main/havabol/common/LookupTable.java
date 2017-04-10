package havabol.common;

import havabol.builtins.functions.*;
import havabol.builtins.types.*;
import havabol.common.function.*;
import havabol.common.type.*;
import havabol.sym.*;

import java.util.*;

/**
 * Lookup table for builtin/user type and function mapping.
 */
public class LookupTable {

    private static final Map<SymbolTable, LookupTable> lookupMap = new HashMap<>();

    private final Map<STFunction, FunctionInterface> funcMap = new HashMap<>();
    private final Map<STControl, TypeInterface> typeMap = new HashMap<>();
    private SymbolTable symTab;

    public static LookupTable getFor(SymbolTable st) {
        LookupTable l = lookupMap.get(st);
        if ( l == null ) {
            return new LookupTable(st);
        }

        return l;
    }

    LookupTable(SymbolTable st) {
        this.symTab = st;
        lookupMap.put(st, this);

        this.init();
    }

    private void init() {
        // Register builtins first
        funcMap.put(
            (STFunction) this.symTab.getSymbol("print"),
            new PrintFunction()
        );

        funcMap.put(
            (STFunction) this.symTab.getSymbol("SPACES"),
            new SpacesFunction()
        );

        funcMap.put(
            (STFunction) this.symTab.getSymbol("LENGTH"),
            new LengthFunction()
        );

        funcMap.put(
            (STFunction) this.symTab.getSymbol("MAXELEM"),
            new MaxElemFunction()
        );

        funcMap.put(
            (STFunction) this.symTab.getSymbol("ELEM"),
            new ElemFunction()
        );

        // XXX - Register builtin types here
        //
    }

    public FunctionInterface lookupFunction(STFunction stFunc) {
        return this.funcMap.get(stFunc);
    }

}
