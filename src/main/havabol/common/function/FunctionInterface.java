package havabol.common.function;

import havabol.common.*;
import havabol.common.type.*;
import havabol.classify.*;
import havabol.eval.*;
import havabol.sym.*;

public interface FunctionInterface extends Debuggable {

    Structure getParameterArity();
    STFunction getSymbolTableEntry();
    ReturnType getFunctionalReturnType();

    EvalResult execute(EvalResult...args);
    boolean validateArguments(EvalResult...args);

}
