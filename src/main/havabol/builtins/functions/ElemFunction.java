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

public class ElemFunction implements FunctionInterface {

    private final String functionName = "ELEM";
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
        int lastElemIndex = 0;
        ArrayList<TypeInterface> array;

        for (EvalResult arg : args) {
            TypeInterface ti = arg.getResult();

            if( ti == null ) {
                // Array declared, but not initialized
                return null;
            }

            switch (arg.getResultType()) {
                case ARRAY:
                    array = ((ArrayType) ti).getValue();

                    // Get max capacity of array, then iterate backwards over the array 
                    // looking for the first value that is not null
                    int capacity = ((ArrayType) ti).getCapacity();
                    TypeInterface t;

                    long nullCount = 0;
                    for (TypeInterface i : array) {
                        if ( i == null ) {
                            nullCount++;
                        }
                    }

                    if ( nullCount == capacity ) {
                        EvalResult res = new EvalResult(ReturnType.INTEGER);
                        res.setResult(Numerics.intPrim(0));
                        return res;
                    }

                    for ( int i = capacity - 1; i >= 0; i-- ) {
                        t = array.get(i);

                        if( t == null ) {
                            // Value at that index may be null
                            continue;
                        }

                        if ( t.getValue() != null ) {
                            lastElemIndex = i;
                            break;
                        }
                    }

                    break;
                default:
                    // wat -- this should probably raise an error
                    return null;
            }
        }

        EvalResult res = new EvalResult(ReturnType.INTEGER);
        res.setResult(Numerics.intPrim(lastElemIndex + 1));

        return res;
    }

    public boolean validateArguments(EvalResult...args) {
        // Shoud only have 1 argument passed to MAXELEM function
        if( args.length != 1 ) {
            return false;
        }
        // The argument should be an ARRAY
        else if ( args[0].getResultType() != ReturnType.ARRAY ) {
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
