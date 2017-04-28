package havabol.builtins.functions;

import havabol.builtins.types.*;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.function.*;
import havabol.common.type.*;
import havabol.eval.*;
import havabol.storage.*;
import havabol.sym.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class SpacesFunction implements FunctionInterface {

    private final String functionName = "SPACES";
    private final Structure paramArity = Structure.FIXED_ARY;
    private final ReturnType retType = ReturnType.BOOLEAN;
    private SymbolTable global = SymbolTable.getGlobal();

    public Structure getParameterArity() {
        return this.paramArity;
    }

    public STFunction getSymbolTableEntry() {
        STEntry thisSym = global.getSymbol(this.functionName);
        if ( thisSym instanceof STFunction ) {
            return (STFunction) thisSym;
        }

        // XXX - This should not return null.
        return null;
    }

    public ReturnType getFunctionalReturnType() {
        return this.retType;
    }

    public EvalResult execute(EvalResult...args) {
        EvalResult arg = args[0];
        TypeInterface ti = arg.getResult();

        if ( ti == null ) {
            // Array declared, but not initialized
            return null;
        }

        String currentString;
        boolean isSpaces = false;

        if ( ti == null ) {
            // String declared, but not initialized
            return null;
        }

        if ( ti.getFormalType() != ReturnType.STRING ) {
            if ( ti.coercibleTo(ReturnType.STRING) ) {
                ti = ti.coerceTo(ReturnType.STRING);
            } else {
                // wat -- this should probably raise an error
                return null;
            }
        }

        switch (ti.getFormalType()) {
            case STRING:
                currentString = ((PString) ti).getValue();
                isSpaces = currentString == null || currentString.isEmpty() || currentString.trim().isEmpty();
                break;
            default:
                // wat -- this should probably raise an error
                return null;
        }

        EvalResult res = new EvalResult(ReturnType.BOOLEAN);
        res.setResult(Numerics.boolPrim(isSpaces));

        return res;
    }

    public boolean validateArguments(EvalResult...args) {
        // Shoud only have 1 argument passed to LENGTH function
        if ( args.length != 1 ) {
            return false;
        }

        TypeInterface arg = args[0].getResult();

        if ( arg.getFormalType() != ReturnType.STRING ) {
            if ( ! arg.coercibleTo(ReturnType.STRING) ) {
                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    public String debug() {
        return this.debug(0);
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Function =>\n"));
        sb.append(lpads(indent + 2, String.format("Handle => %s\n", this.functionName)));
        sb.append(lpads(indent + 2, String.format("Arity  => %s\n", this.paramArity.name())));
        sb.append(lpads(indent + 2, String.format("Rtype  => %s\n", this.retType.name())));

        return sb.toString();
    }
}
