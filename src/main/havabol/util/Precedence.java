package havabol.util;

import havabol.classify.*;
import havabol.eval.*;
import havabol.parse.*;
import static havabol.eval.Evaluator.reportEvalError;
import static havabol.util.Text.*;

import java.util.*;

public class Precedence {

    public static BinaryOperation rebuildWithPrecedence(BinaryOperation head) throws EvalException {
        // System.out.println("REBUILDING BINOP TO LINEAR FORM");
        ArrayList<ParseElement> elms = binTreeToLinear(head);
        // elms.stream().forEach(pe -> System.out.print(pe.debug(30)));
        // System.out.println();

        // System.out.println("BUILDING BINOP WITH PRECEDENCE");
        // System.out.print(head.debug(0));
        // System.out.println();

        ArrayDeque<ParseElement> output = buildPostfix(elms);

        // XXX DEBUG - Dump opers and exprs
        // System.out.println("FINAL");
        // output.stream().forEach(pe -> System.out.print(pe.debug(20)));

        // System.out.println("CONVERTING BACK TO BINOP");
        // System.out.println();

        ArrayDeque<ParseElement> stack = new ArrayDeque<>();

        // After this loop is completed, `output` should be empty and
        // `stack` should contain a single BinaryOperation.
        while ( ! output.isEmpty() ) {
            ParseElement ele = output.pop();
            if ( ele instanceof Operator ) {
                // current element is an operator, do processing things
                Expression resL, resR;

                // System.out.println("GOT OPERATOR, USING VALS FROM STACK. STACK CONTENTS:");
                // stack.stream().forEach(pe -> System.out.print(pe.debug(20)));

                resR = (Expression) stack.pop();
                resL = (Expression) stack.pop();
                stack.push(new Expression(new BinaryOperation(resL, (Operator) ele, resR)));
            } else {
                // otherwise, just work on outputted binOp.
                // System.out.println("values push");
                stack.push(ele);
            }
        }

        if ( stack.size() == 1 ) {
            Expression expr = (Expression) stack.pop();
            if ( expr.getExpressionType() != ExpressionType.BINARY_OP ) {
                reportEvalError(
                    String.format(
                        "Expected final stack element to be BINARY_OP, actually is `%s`",
                        expr.getExpressionType().name()
                    ),
                    expr
                );
                return null;
            }

            BinaryOperation op = expr.getBinaryOperation();

            // System.out.println("FINAL RESULTING BINOP");
            // System.out.println(op.debug(35));
            // System.out.println();

            return op;
        } else {
            reportEvalError(
                String.format(
                    "Expected a final stack size of 1 - got `%d`",
                    stack.size()
                ),
                head
            );
            return null;
        }
    }

    private static ArrayList<ParseElement> binTreeToLinear(BinaryOperation op) {
        if ( op == null ) {
            return null;
        }

        ArrayList<ParseElement> elms = new ArrayList<>();

        Expression lhs = op.getLHS();
        switch (lhs.getExpressionType()) {
            case BINARY_OP:
                elms.addAll(binTreeToLinear(lhs.getBinaryOperation()));
                break;
            default:
                elms.add(lhs);
                break;
        }

        elms.add(op.getOper());

        Expression rhs = op.getRHS();
        switch (rhs.getExpressionType()) {
            case BINARY_OP:
                elms.addAll(binTreeToLinear(rhs.getBinaryOperation()));
                break;
            default:
                elms.add(rhs);
                break;
        }

        return elms;
    }

    private static ArrayDeque<ParseElement> buildPostfix(ArrayList<ParseElement> elems) {
        if ( elems == null ) {
            return null;
        }

        ArrayDeque<Operator> stack = new ArrayDeque<>();
        ArrayDeque<ParseElement> output = new ArrayDeque<>();

        for (ParseElement pe : elems) {
            if ( pe instanceof Operator ) {
                Operator current = (Operator) pe;
                while ( ! stack.isEmpty() ) {
                    if ( current.getPrecedence().getPriority() < stack.peekFirst().getPrecedence().getPriority() ) {
                        break;
                    }

                    output.add(stack.pop());
                }

                stack.push(current);
            } else {
                output.add(pe);
            }
        }

        while ( ! stack.isEmpty() ) {
            output.add(stack.pop());
        }

        return output;
    }

}
