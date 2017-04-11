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
    private final ReturnType retType = ReturnType.INTEGER;
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
        String currentString;
        boolean isSpaces = false;

        for (EvalResult arg : args) {
            TypeInterface ti = arg.getResult();
            switch (arg.getResultType()) {
                case STRING:
                    currentString = ((PString) ti).getValue();
                    isSpaces = currentString == null || currentString.isEmpty() || currentString.trim().isEmpty();
                    break;
                default:
                    // wat -- this should probably raise an error
                    return null;
            }
        }

        EvalResult res = new EvalResult(ReturnType.BOOLEAN);
        PBoolean returnVal = new PBoolean();
        returnVal.setValue(isSpaces);
        res.setReturn(returnVal);
        return res;
    }

    public boolean validateArguments(EvalResult...args) {
        // Shoud only have 1 argument passed to SPACES function
        if( args.length > 1 ) {
            return false;
        }
        // The argument should be a STRING
        else if ( args[0].getResultType() != ReturnType.STRING ) {
            return false;
        }

        return true;
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
