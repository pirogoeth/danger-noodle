package havabol.util;

import havabol.classify.*;
import havabol.parse.*;
import static havabol.util.Text.*;

import java.util.*;

public class Precedence {

    public static BinaryOperation rebuildWithPrecedence(BinaryOperation head) {
        System.out.println("BUILDING BINOP WITH PRECEDENCE");
        System.out.print(head.debug(0));
        System.out.println();

        ArrayDeque<Operator> opers = new ArrayDeque<>();
        ArrayDeque<ParseElement> output = buildPostfix(opers, head);

        Expression resv = null;
        BinaryOperation op = null;
        Operator tmp = null;

        while ( ! output.isEmpty() ) {
            ParseElement ele = output.removeFirst();
            if ( ele instanceof Operator ) {
                // current element is an operator, do processing things
                Expression resL, resR;
                resR = (Expression) output.removeFirst();
                resL = (Expression) output.removeFirst();
                op = new BinaryOperation(resL, (Operator) ele, resR);
            } else {
                // otherwise, just work on outputted binOp.
                resv = (Expression) ele;
            }
        }

        System.out.println("FINAL RESULTING BINOP");
        System.out.println(op.debug(35));
        System.out.println();

        // XXX - THIS SHOULD CONSTRUCT A NEW THING!
        return null;
    }

    private static ArrayDeque<ParseElement> buildPostfix(ArrayDeque<Operator> opers, BinaryOperation op) {
        if ( op == null || opers == null ) {
            return null;
        }

        // System.out.println("buildPostfix");

        ArrayDeque<ParseElement> output = new ArrayDeque<>();

        Expression lhs = op.getLHS();
        switch (lhs.getExpressionType()) {
            case BINARY_OP:
                // System.out.println("lhs binop");
                output.addAll(buildPostfix(opers, lhs.getBinaryOperation()));
                break;
            default:
                // System.out.println("lhs other");
                // System.out.print(lhs.debug(30));
                output.push(lhs);
                break;
        }

        Operator oper = op.getOper();
        Operator last;
        try {
            last = opers.peek();
        } catch (NoSuchElementException exc) {
            last = null;
        }

        if ( opers.size() == 0 ) {
            // System.out.println("cheeky");
            opers.push(oper);
        } else if ( last != null && oper.getPrecedence().getPriority() > last.getPrecedence().getPriority() ) {
            // This should perform the postfix pops and push precedence-sorted exprs into a ArrayDeque.
            // XXX - Implement!
            // System.out.println("nandos");
            while ( ! opers.isEmpty() ) {
                output.push(opers.pop());
            }
            opers.push(oper);
        } else {
            // Just push this operator to the stack.
            // System.out.println("irl");
            opers.push(oper);
        }

        Expression rhs = op.getRHS();
        switch (rhs.getExpressionType()) {
            case BINARY_OP:
                // System.out.println("rhs binop");
                output.addAll(buildPostfix(opers, rhs.getBinaryOperation()));
                break;
            default:
                // System.out.println("rhs other");
                // System.out.print(rhs.debug(30));
                output.push(rhs);
                break;
        }

        while ( ! opers.isEmpty() ) {
            output.push(opers.pop());
        }

        // XXX DEBUG - Dump opers and exprs
        System.out.println("FINAL");
        output.stream().forEach(pe -> System.out.print(pe.debug(20)));

        // By now, the stacks should likely be populated.
        // We should keep a reference to the lowest expr in the new binOp tree so we do not have
        // to search for an insertion point.

        return output;
    }

}
