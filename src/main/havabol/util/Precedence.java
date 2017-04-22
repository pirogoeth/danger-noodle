package havabol.util;

import havabol.classify.*;
import havabol.parse.*;
import static havabol.util.Text.*;

import java.util.*;

public class Precedence {

    public BinaryOperation rebuildWithPrecedence(BinaryOperation head) {
        Stack<Expression> exprs = new Stack<>();
        Stack<Operator> opers = new Stack<>();

        Expression lhs = head.getLHS();

        return null;
    }

    private Stack<?> buildPostfix(Stack<Expression> exprs, Stack<Operator> opers, BinaryOperation op) {
        if ( exprs == null || opers == null || op == null ) {
            return null;
        }

        Expression lhs = head.getLHS();
        switch (lhs.getExpressionType()) {
            case BINARY_OP:
                buildPostfix(exprs, opers, lhs);
                break;
            default:
                exprs.push(lhs);
                break;
        }

        Expression rhs = head.getRHS();
        switch (rhs.getExpressionType()) {
            case BINARY_OP:
                buildPostfix(exprs, opers, rhs);
                break;
            default:
                exprs.push(rhs);
                break;
        }

        Operator oper = op.getOper();
        Operator last = opers.peek();
        if ( last != null && oper.getPrecedence().getPriority() > last.getPrecedence().getPriority() ) {
            // This should perform the postfix pops and push precedence-sorted exprs into a Stack.
            // XXX - Implement!
        } else {
            // Just push this operator to the stack.
            opers.push(oper);
        }

        // By now, the stacks should likely be populated and a new binOp should exist.
        // We should keep a reference to the lowest expr in the new binOp tree so we do not have
        // to search for an insertion point.

        return null;
    }

}
