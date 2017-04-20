package havabol.parse;

import havabol.Token;
import havabol.builtins.types.PBoolean;
import havabol.classify.ReturnType;
import havabol.common.type.TypeInterface;
import havabol.eval.EvalException;
import static havabol.eval.Evaluator.reportEvalError;
import static havabol.util.Text.*;

import java.util.*;

public class UnaryOperation extends ParseElement {

    /**
     * Middle should ALWAYS be an operator.
     */
    private Operator operator;

    /**
     * Right-hand side can be an expression.
     * An expression is defined as anything that returns a value, which means:
     * * constants
     * * function call
     * * binary/unary operations
     * * string literals
     */
    private Expression rhs;

    public UnaryOperation(Operator operator, Expression rhs) {
        this.operator = operator;
        this.rhs = rhs;
    }

    public Operator getOper() {
        return this.operator;
    }

    public Expression getRHS() {
        return this.rhs;
    }

    public boolean isValid() {
        return ( this.operator.isValid() &&
                 this.rhs.isValid() );
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "UnaryOperation ~>\n"));
        sb.append(this.operator.debug(indent + 2));
        sb.append(lpads(indent + 2, "RHS ~>\n"));
        sb.append(this.rhs.debug(indent + 4));

        return sb.toString();
    }
    
    public boolean not(TypeInterface reverse) throws EvalException{
         boolean switched;
         if(reverse.getFormalType() == ReturnType.BOOLEAN ){
            
            switched = ((PBoolean) reverse).getValue();
            return !switched;
            
        }else{
             reportEvalError(
                String.format(
                    "b is not a bool `%s`",
                    reverse.getFormalType().name()
                ),
                reverse
            );
            return false;
        }
    }

}
