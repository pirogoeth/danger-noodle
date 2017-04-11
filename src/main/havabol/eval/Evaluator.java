package havabol.eval;

import havabol.*;
import havabol.builtins.types.*;
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

    private SymbolTable symTab;
    private StorageManager store;
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
        this(stmts, SymbolTable.getGlobal());
    }

    public Evaluator(List<Statement> stmts, SymbolTable symTab) {
        this.stmtList = stmts;
        this.symTab = symTab;
        this.store = StorageManager.scoped(this.symTab);
    }

    private Statement popNext() {
        return this.stmtList.remove(0);
    }

    public boolean canEval() {
        return ( this.stmtList != null && ! this.stmtList.isEmpty() );
    }

    public EvalResult evaluate() throws Exception, ParserException, EvalException {
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

    private EvalResult evalStatement(Statement stmt) throws Exception, ParserException, EvalException {
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

    private EvalResult evaluateAssignment(Statement stmt) throws Exception, ParserException, EvalException {
        Assignment assign = stmt.getAssignment();
        if ( assign == null ) {
            // This really should not *ever* happen.
            reportEvalError(
                "assignment evaluator was given a statement with null assignment",
                stmt
            );
            return null;
        }

        EvalResult res = this.evaluateAssignment(assign);
        res.setSource(stmt);
        return res;
    }

    private EvalResult evaluateAssignment(Assignment assign) throws Exception, ParserException, EvalException {
        EvalResult res, target, val;

        if ( assign.isCompoundAssignment() ) {  // declaration
            Declaration decl = assign.getDeclaration();

            if ( decl.isArray() ) {
                res = new EvalResult(ReturnType.ARRAY);
            } else {
                res = new EvalResult(decl.getDataType().getReturnType());
            }
            target = this.evaluateDeclaration(decl);
            val = this.evaluateExpression(assign.getAssignedExpr());
            if ( val.getResultType() == ReturnType.ARRAY ) {
                ((ArrayType) val.getResult()).setBoundType(decl.getDataType().getReturnType());
            }

            STIdentifier newIdent = target.getResultIdent();
            SMValue stVal = this.store.getOrInit(newIdent);
            if ( val.getResultType() != res.getResultType() ) {
                reportEvalError(
                    String.format(
                        "Can not perform assignment - type mismatch (given %s, expected %s)",
                        val.getResultType().name(),
                        res.getResultType().name()
                    ),
                    assign
                );
                return null;
            }

            TypeInterface valRet = val.getReturn();

            stVal.set(valRet);
            res.setReturn(valRet);
            res.setResultIdent(newIdent);
        } else {  // simple assignment
            target = this.evaluateExpression(assign.getAssigneeExpr());

            STIdentifier setIdent = target.getResultIdent();
            STControl stDecl = setIdent.getDeclared();
            res = new EvalResult(stDecl.getDataType());

            SMValue stVal = this.store.get(setIdent);
            if ( stVal == null ) {
                reportEvalError(
                    String.format(
                        "Tried to assign undeclared variable `%s`",
                        setIdent.getSymbol()
                    ),
                    assign
                );
                return null;
            }

            val = this.evaluateExpression(assign.getAssignedExpr());
            if ( val.getResultType() != stDecl.getDataType() ) {
                reportEvalError(
                    String.format(
                        "Can not perform assignment - type mismatch (given %s, expected %s)",
                        val.getResultType().name(),
                        res.getResultType().name()
                    ),
                    assign
                );
                return null;
            }

            TypeInterface valRet = val.getReturn();

            stVal.set(val.getReturn());
            res.setReturn(valRet);
            res.setResultIdent(setIdent);
        }

        return res;
    }

    private EvalResult evaluateBlock(Statement stmt) throws ParserException, EvalException {
        return null;
    }

    private EvalResult evaluateDeclaration(Statement stmt) throws ParserException, EvalException {
        Declaration decl = stmt.getDeclaration();
        if ( decl == null ) {
            // This should actually *never* happen.
            reportEvalError(
                "declaration evaluator was given a statement with null declaration",
                stmt
            );
            return null;
        }

        EvalResult res = this.evaluateDeclaration(decl);
        res.setSource(stmt);
        return res;
    }

    private EvalResult evaluateDeclaration(Declaration decl) throws ParserException, EvalException {
        // Declarations are simple. Just grab the TypeInterface from the DataType,
        // create a new symbol table entry, and set the SMValue to the new TypeInterface.
        //
        // Declarations need to return an affected symbol through EvalResult, most likely.

        EvalResult res = new EvalResult(ReturnType.VOID);
        DataType dt = decl.getDataType();
        Identifier ident = decl.getIdentifier();

        Token identT = ident.getIdentT();
        Structure backing;
        if ( decl.isArray() ) {
            if ( decl.isUnboundedArray() ) {
                backing = Structure.VARIA_ARY;
            } else {
                backing = Structure.FIXED_ARY;
            }
        } else {
            backing = Structure.PRIMITIVE;
        }

        // XXX - Need to get STControl for DataType.
        STIdentifier identS = new STIdentifier(
            identT.tokenStr,
            dt.getSTControl(),
            backing,
            this.symTab.getBaseAddress()
        );
        this.symTab.putSymbol(identS);
        SMValue v = this.store.get(identS);
        v.set(null);

        res.setResultIdent(identS);

        return res;
    }

    private EvalResult evaluateExpression(Statement stmt) throws Exception, ParserException, EvalException {
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

    private EvalResult evaluateExpression(Expression expr) throws Exception, ParserException, EvalException {
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

                if ( ! fi.validateArguments(args) ) {
                    reportEvalError(
                        "Could not execute function call",
                        fc,
                        fi
                    );
                    return null;
                }

                EvalResult fcRes = fi.execute(args);
                fcRes.setSource(expr);

                return fcRes;
            case PRIMITIVE:
                Primitive prim = expr.getPrimitive();
                EvalResult primRes = prim.getEvaluable();

                primRes.setSource(expr);

                return primRes;
            case IDENTIFIER:
                Identifier identExpr = expr.getIdentifier();
                STIdentifier identS = (STIdentifier) this.symTab.lookupSym(identExpr.getIdentT());
                SMValue identV = this.store.get(identS);

                EvalResult iRes = new EvalResult(identV.get().getFormalType());
                iRes.setResultIdent(identS);
                iRes.setReturn(identV.get());
                return iRes;
            case ARRAY:
                Array aryExpr = expr.getArray();
                EvalResult aryRes = aryExpr.getEvaluable();

                aryRes.setSource(expr);
                return aryRes;
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
