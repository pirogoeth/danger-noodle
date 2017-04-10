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

    // XXX - This should not actually be void.
    EvalResult execute(EvalResult...args);
    boolean validateArguments(EvalResult...args);

}
