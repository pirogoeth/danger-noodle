package havabol.eval;

import havabol.*;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.function.*;
import havabol.common.type.*;
import havabol.parse.*;
import havabol.storage.*;
import havabol.sym.*;
import havabol.util.*;

import java.util.ArrayList;
import java.util.List;

public class Evaluator {

    private List<Statement> stmtList;

    /**
     * Creates and throws an EvalException with a set of context passed
     * from statements.
     *
     * @param String message
     * @param Statement[] contexts
     *
     * @return void
     *
     * @throws EvalException
     */
    static void reportEvalError(String message, Debuggable...contexts) throws EvalException {
        String[] contextStr = new String[contexts.length];

        for (int i = 0; i < contextStr.length; i++) {
            Debuggable d = contexts[i];
            if ( d != null ) {
                contextStr[i] = String.format(
                        "[%s]: Near or at statement:\n%s",
                        Scanner.getInstance().sourceFileNm,
                        d.debug(2)
                );
            } else {
                contextStr[i] = "[invalid] Null debuggable given to error reporter";
            }
        }

        StringBuilder excSb = new StringBuilder();

        for (String s : contextStr) {
            excSb.append(s + "\n");
        }

        throw new EvalException(
                String.format(
                    "Evaluation error occurred: %s\n%s",
                    message,
                    excSb
                ),
                contexts
        );
    }

    public Evaluator(List<Statement> stmts) {
        this.stmtList = stmts;
    }

    private Statement popNext() {
        return this.stmtList.remove(0);
    }

    public boolean canEval() {
        return ( this.stmtList != null && ! this.stmtList.isEmpty() );
    }

    public EvalResult evaluate() throws ParserException, EvalException {
        if ( ! this.canEval() ) {
            return null;
        }

        Statement stmt = this.popNext();
        if ( stmt == null ) {
            // This really should not *ever* happen.
            reportEvalError(
                "statement evaluator was given a null statement",
                stmt
            );
            return null;
        }

        return this.evalStatement(stmt);
    }

    private EvalResult evalStatement(Statement stmt) throws ParserException, EvalException {
        if ( stmt == null ) {
            // This really should not *ever* happen.
            reportEvalError(
                "statement evaluator was given a null statement",
                stmt
            );
            return null;
        }

        switch (stmt.getStatementType()) {
            case ASSIGN:
                return this.evaluateAssignment(stmt);
            case BLOCK:
                return this.evaluateBlock(stmt);
            case DECLARATION:
                return this.evaluateDeclaration(stmt);
            case EXPRESSION:
                return this.evaluateExpression(stmt);
            case FLOW_CTRL:
                return this.evaluateFlowControl(stmt);
            default:
                reportEvalError(
                    "Unknown statement type: " + stmt.getStatementType().name(),
                    stmt
                );
                break;
        }

        reportEvalError(
            "evaluator fell out of switch while trying to eval a statement",
            stmt
        );

        return null;
    }

    private EvalResult evaluateAssignment(Statement stmt) throws EvalException {
        return null;
    }

    private EvalResult evaluateBlock(Statement stmt) throws EvalException {
        return null;
    }

    private EvalResult evaluateDeclaration(Statement stmt) throws EvalException {
        return null;
    }

    private EvalResult evaluateExpression(Statement stmt) throws ParserException, EvalException {
        Expression expr = stmt.getExpression();
        if ( expr == null ) {
            // This really should not *ever* happen.
            reportEvalError(
                "expression evaluator was given a statement with null expression",
                stmt
            );
            return null;
        }

        return this.evaluateExpression(expr);
    }

    private EvalResult evaluateExpression(Expression expr) throws ParserException, EvalException {
        if ( expr == null ) {
            // This really should not *ever* happen.
            reportEvalError(
                "expression evaluator was given a null expression"
            );
            return null;
        }

        switch (expr.getExpressionType()) {
            case FUNC_CALL:
                FunctionCall fc = expr.getFunctionCall();
                FunctionInterface fi = fc.resolveFunctionHandle();

                List<Expression> argExprs = fc.getArgs();
                EvalResult[] args = new EvalResult[argExprs.size()];
                for (int i = 0; i < args.length; i++) {
                    args[i] = this.evaluateExpression(argExprs.get(i));
                }

                EvalResult fcRes = fi.execute(args);
                fcRes.setSource(expr);

                return fcRes;
            case PRIMITIVE:
                Primitive prim = expr.getPrimitive();
                EvalResult primRes = prim.getEvaluable();
                primRes.setSource(expr);

                return primRes;
            default:
                break;
        }

        reportEvalError(
            "expression evaluator fell out of switch while trying to eval a statement",
            expr
        );
        return null;
    }

    private EvalResult evaluateFlowControl(Statement stmt) throws EvalException {
        return null;
    }

}
