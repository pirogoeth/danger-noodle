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

public class PrintFunction implements FunctionInterface {

    private final String functionName = "print";
    private final Structure paramArity = Structure.VARIA_ARY;
    private final ReturnType retType = ReturnType.VOID;
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
        for (EvalResult arg : args) {
            TypeInterface ti = arg.getResult();
            switch (arg.getResultType()) {
                case STRING:
                    String s = ((PString) ti).getValue();
                    s = Escapes.generateEscapeSequences(s);
                    System.out.print(s);
                    break;
                case INTEGER:
                    int i = ((PInteger) ti).getValue();
                    System.out.print(String.format("%d", i));
                    break;
                case FLOAT:
                    double d = ((PFloat) ti).getValue();
                    System.out.print(String.format("%.2f", d));
                    break;
                case BOOLEAN:
                    boolean b = ((PBoolean) ti).getValue();
                    System.out.print(String.format("%s", b ? "T" : "F"));
                    break;
                case ARRAY:
                    System.out.print(((ArrayType) ti).getRepr());
                    break;
                default:
                    // wat -- this should probably raise an error
                    return null;
            }

            System.out.print(" ");
        }

        System.out.println();

        EvalResult res = new EvalResult(ReturnType.VOID);
        return res;
    }

    public boolean validateArguments(EvalResult...args) {
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
