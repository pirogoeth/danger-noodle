package havabol.util;

import havabol.Token;
import havabol.builtins.types.*;
import havabol.classify.*;
import havabol.eval.*;
import havabol.parse.*;
import havabol.storage.*;
import havabol.sym.*;

/**
 * Utilities for inferring types, in the case of implicit variables
 * during iterative for.
 */
public class Infer {

    /**
     * Infers the type for the identifier in cases where an implicit
     * variable is used to iterate over a container.
     *
     * A container is either a String or an Array.
     *
     * @param SymbolTable st
     * @param Identifier ident
     * @param EvalResult container
     *
     * @return STIdentifier identifier with inferred type
     *
     * @throws EvalException
     */
    public static STIdentifier identFromContainer(SymbolTable st, Identifier ident, EvalResult container) throws EvalException {
        if ( st == null || ident == null || container == null ) {
            Evaluator.reportEvalError(
                "Expected (SymbolTable, Identifier, EvalResult), received a null",
                ident,
                container
            );
            return null;
        }

        // Check type of the supposed container
        switch (container.getResultType()) {
            case ARRAY:
            case STRING:
                break;
            default:
                Evaluator.reportEvalError(
                    String.format(
                        "Type `%s` is not a container",
                        container.getResultType().name()
                    ),
                    ident,
                    container
                );
                return null;
        }

        // Actually perform the inferencing
        STIdentifier inferred;
        ReturnType infRet;
        switch (container.getResultType()) {
            case STRING:
                infRet = container.getResultType();
                break;
            case ARRAY:
                infRet = ((ArrayType) container.getResult()).getBoundType();
                break;
            default:
                infRet = null;
                break;
        }

        if ( infRet == null ) {
            return null;
        }

        inferred = new STIdentifier(
            ident.getIdentT().tokenStr,
            infRet,
            Structure.PRIMITIVE,
            st.getBaseAddress()
        );

        return inferred;
    }

    /**
     * Infers the type for the identifier in cases where an implicit
     * control var for a counting loop is needed.
     *
     * `numerType` is either a FLOAT or INTEGER.
     *
     * @param SymbolTable st
     * @param Identifier ident
     * @param ReturnType numerType
     *
     * @return STIdentifier identifier with inferred type
     *
     * @throws EvalException
     */
    public static STIdentifier identFromNumber(SymbolTable st, Identifier ident, ReturnType numerType) throws EvalException {
        if ( st == null || ident == null || numerType == null ) {
            Evaluator.reportEvalError(
                "Expected (SymbolTable, Identifier, ReturnType), received a null",
                ident
            );
            return null;
        }

        // Check type of the supposed container
        switch (numerType) {
            case FLOAT:
            case INTEGER:
                break;
            default:
                Evaluator.reportEvalError(
                    String.format(
                        "Type `%s` is not a numeric",
                        numerType.name()
                    ),
                    ident
                );
                return null;
        }

        // Actually perform the inferencing
        STIdentifier inferred;

        inferred = new STIdentifier(
            ident.getIdentT().tokenStr,
            numerType,
            Structure.PRIMITIVE,
            st.getBaseAddress()
        );

        return inferred;
    }
}
