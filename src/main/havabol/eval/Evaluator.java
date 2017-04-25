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
import static havabol.util.Numerics.*;
import static havabol.util.Precedence.rebuildWithPrecedence;

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
    public static void reportEvalError(String message, Debuggable...contexts) throws EvalException {
        reportEvalError(message, null, contexts);
    }

    public static void reportEvalError(String message, Exception cause, Debuggable...contexts) throws EvalException {
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
                cause,
                contexts
        );
    }

    public Evaluator(List<Statement> stmts) {
        this(stmts, SymbolTable.getGlobal());
    }

    public Evaluator(List<Statement> stmts, SymbolTable symTab) {
        this.stmtList = new ArrayList<Statement>(stmts);
        this.symTab = symTab;
        this.store = StorageManager.scoped(this.symTab);
    }

    private Statement popNext() {
        return this.stmtList.remove(0);
    }

    private void putStmts(List<Statement> stmts) {
        this.stmtList.addAll(stmts);
    }

    public boolean canEval() {
        return ( this.stmtList != null && ! this.stmtList.isEmpty() );
    }

    private EvalResult applySubscript(EvalResult res) throws Exception, EvalException {
        EvalResult newRes;
        EvalResult.EvalSubscript sub;
        TypeInterface val = res.getResult();

        // tmp vars
        PString s;
        ArrayType a;

        if ( res.isSubscripted() ) {
            sub = res.getSubscript();
            switch (sub.type) {
                case SINGLE:
                    switch (res.getResultType()) {
                        case STRING:
                            s = (PString) val;
                            newRes = s.get(sub.beginIdx);
                            break;
                        case ARRAY:
                            a = (ArrayType) val;
                            newRes = a.get(sub.beginIdx);
                            break;
                        default:
                            reportEvalError(
                                "Tried to apply subscript to un-subscriptable value",
                                res
                            );
                            return null;
                    }
                    break;
                case RANGE:
                    switch (res.getResultType()) {
                        case STRING:
                            s = (PString) val;
                            newRes = s.getSlice(sub.beginIdx, sub.endIdx);
                            break;
                        case ARRAY:
                            a = (ArrayType) val;
                            newRes = a.getSlice(sub.beginIdx, sub.endIdx);
                            break;
                        default:
                            reportEvalError(
                                "Tried to apply subscript to un-subscriptable value",
                                res
                            );
                            return null;
                    }
                    break;
                default:
                    reportEvalError(
                        String.format(
                            "Invalid subscript type - `%s`",
                            sub.type.name()
                        )
                    );
                    return null;
            }
        } else {
            newRes = res;
        }

        return newRes;
    }

    public EvalResult evaluate() throws EvalException {
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

        try {
            return this.evalStatement(stmt);
        } catch (EvalException e) {
            throw e;
        } catch (Exception e) {
            reportEvalError(
                "Could not evaluate statement",
                e,
                stmt
            );
        }

        return null;
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
            case NO_OP:
                return new EvalResult(ReturnType.VOID);
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

    private EvalResult evaluateAssignment(Statement stmt) throws Exception, EvalException {
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

    private EvalResult evaluateAssignment(Assignment assign) throws Exception, EvalException {
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
                ArrayType ary = (ArrayType) val.getResult();
                ary.setBoundType(decl.getDataType().getReturnType());
            }

            STIdentifier newIdent = target.getResultIdent();
            SMValue stVal = this.store.get(newIdent);
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

            TypeInterface valRet = val.getResult();

            // NOTE: If array size is inferred from the RHS array literal,
            // stVal will have a `null` TypeInterface.
            if ( val.getResultType() == ReturnType.ARRAY && decl.isArray() ) {
                ArrayType tgtAry = (ArrayType) stVal.get();
                // System.out.println("ARY TARGET");
                // System.out.print(target.debug(2));
                // System.out.println("ARY VAL");
                // System.out.print(val.debug(2));
                // System.out.println();

                if ( tgtAry.isBounded() && tgtAry.getCapacity() == -1 ) {
                    // Initialize the array here.
                    tgtAry.initialize(((ArrayType) valRet).getCapacity());
                }

                tgtAry.setFromArray((ArrayType) valRet);
                res.setResult(tgtAry);
            } else {
                stVal.set(valRet);
                res.setResult(valRet);
            }
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
            if ( val.isSubscripted() ) {
                val = this.applySubscript(val);
            }

            // XXX - PROBLEM IS RAISED HERE
            if ( val.getResultType() != stDecl.getDataType() && stVal.get().getFormalType() != ReturnType.ARRAY ) {
                reportEvalError(
                    String.format(
                        "Can not perform assignment - type mismatch (given %s, expected %s)",
                        val.getResultType().name(),
                        res.getResultType().name()
                    ),
                    assign
                );
                return null;
            } else if ( val.getResultType() == ReturnType.ARRAY ) {
                // Compare the BOUND TYPES!
                // XXX - NEEDS MORE ARRAY COERCION
                if ( res.getResultType() != ((ArrayType) val.getResult()).getBoundType() ) {
                    reportEvalError(
                        String.format(
                            "Can not perform assignment - mismatched array bound types (given %s, expected %s)",
                            ((ArrayType) val.getResult()).getBoundType().name(),
                            res.getResultType().name()
                        ),
                        assign
                    );
                    return null;
                }
            }

            TypeInterface valRet = val.getResult();

            if ( stVal.get().getFormalType() == ReturnType.ARRAY ) {
                ArrayType ary = (ArrayType) stVal.get();
                if ( target.isSubscripted() ) {
                    EvalResult.EvalSubscript ss = target.getSubscript();
                    if ( ss.endIdx == -1 ) {
                        res.setResult(ary.set(ss.beginIdx, val.getResult()).getResult());
                    } else {
                        if ( val.getResultType() == ReturnType.ARRAY ) {
                            res.setResult(ary.setSlice(ss.beginIdx, ss.endIdx, (ArrayType) val.getResult()).getResult());
                        } else {
                            res.setResult(ary.setSliceScalar(ss.beginIdx, ss.endIdx, val.getResult()).getResult());
                        }
                    }
                } else {
                    if ( val.getResultType() == ReturnType.ARRAY ) {
                        res.setResult(ary.setFromArray((ArrayType) val.getResult()).getResult());
                    } else {
                        res.setResult(ary.setFromScalar(val.getResult()).getResult());
                    }
                }
            } else if ( stVal.get().getFormalType() == ReturnType.STRING ) {
                PString str = (PString) stVal.get();
                if ( target.isSubscripted() ) {
                    EvalResult.EvalSubscript ss = target.getSubscript();
                    if ( ss.endIdx == -1 ) {
                        res.setResult(str.set(ss.beginIdx, strPrim(valRet.getRepr())).getResult());
                    } else {
                        // XXX - String slice set???
                        res.setResult(null);
                    }
                } else {
                    stVal.set(val.getResult());
                    res.setResult(valRet);
                }
            } else {
                stVal.set(val.getResult());
                res.setResult(valRet);
            }
            res.setResultIdent(setIdent);
        }

        return res;
    }

    private EvalResult evaluateBlock(Statement stmt) throws Exception, ParserException, EvalException {
        return null;
    }

    private EvalResult evaluateDeclaration(Statement stmt) throws Exception, EvalException {
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

    private EvalResult evaluateDeclaration(Declaration decl) throws Exception, EvalException {
        // Declarations are simple. Just grab the TypeInterface from the DataType,
        // create a new symbol table entry, and set the SMValue to the new TypeInterface.
        //
        // Declarations need to return an affected symbol through EvalResult, most likely.

        DataType dt = decl.getDataType();
        Identifier ident = decl.getIdentifier();

        Token identT = ident.getIdentT();
        Structure backing;
        TypeInterface typeIf;
        if ( decl.isArray() ) {
            if ( decl.isUnboundedArray() ) {
                backing = Structure.VARIA_ARY;
            } else {
                backing = Structure.FIXED_ARY;
            }
            ArrayType aryTmp = new ArrayType();
            aryTmp.setBoundType(dt.getReturnType());

            if ( decl.getArrayBound() == null ) {
                // This happens when the subscript says VARIA_ARY but
                // the capacity is actually inferred from the initialized
                // arguments, if any.
            } else {
                TypeInterface arrayBound = this.evaluateExpression(decl.getArrayBound()).getResult();
                if ( arrayBound.getFormalType() != ReturnType.INTEGER ) {
                    reportEvalError(
                        String.format(
                            "Invalid subscript type - expected `%s` got `%s`",
                            ReturnType.INTEGER.name(),
                            arrayBound.getFormalType().name()
                        ),
                        dt,
                        ident
                    );
                    return null;
                }
                PInteger arrayBoundInt = (PInteger) arrayBound;
                aryTmp.initialize(arrayBoundInt.getValue());
            }

            typeIf = aryTmp;
        } else {
            backing = Structure.PRIMITIVE;
            typeIf = dt.getType();
        }

        // XXX - Need to get STControl for DataType.
        STIdentifier identS = new STIdentifier(
            identT.tokenStr,
            dt.getSTControl(),
            backing,
            this.symTab.getBaseAddress()
        );
        this.symTab.putSymbol(identS);
        SMValue v = this.store.getOrInit(identS);
        v.set(typeIf);

        EvalResult res = new EvalResult(ReturnType.VOID);
        res.setResultIdent(identS);

        return res;
    }

    private EvalResult evaluateExpression(Statement stmt) throws Exception, EvalException {
        Expression expr = stmt.getExpression();
        if ( expr == null ) {
            // This really should not *ever* happen.
            reportEvalError(
                "expression evaluator was given a statement with null expression",
                stmt
            );
            return null;
        }

        EvalResult res = this.evaluateExpression(expr);
        res.setSource(stmt);
        return res;
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
            case ASSIGNMENT:
                return this.evaluateAssignment(expr.getAssignment());
            case BINARY_OP:
                return this.evaluateBinaryOp(expr.getBinaryOperation());
            case UNARY_OP:
                return this.evaluateUnaryOp(expr.getUnaryOperation());
            case FUNC_CALL:
                FunctionCall fc = expr.getFunctionCall();
                FunctionInterface fi = fc.resolveFunctionHandle();

                List<Expression> argExprs = fc.getArgs();
                EvalResult[] args = new EvalResult[argExprs.size()];
                for (int i = 0; i < args.length; i++) {
                    args[i] = this.evaluateExpression(argExprs.get(i));
                    if ( args[i].isSubscripted() ) {
                        args[i] = this.applySubscript(args[i]);
                    }
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

                // Look up the identifier symbol from the symbol table
                STIdentifier identS = (STIdentifier) this.symTab.lookupSym(identExpr.getIdentT());
                if ( identS == null ) {
                    // Symbol lookup failed -- not declared?
                    reportEvalError(
                        String.format(
                            "Tried to use variable `%s` before declaring it!",
                            identExpr.getIdentT().tokenStr
                        ),
                        expr
                    );
                    return null;
                }

                // Load the symbol's stored value & create an eval res
                SMValue identV = this.store.get(identS);
                EvalResult iRes = new EvalResult(identV.get().getFormalType());
                iRes.setResultIdent(identS);

                // Create the subscript, if any
                Subscript identSubsc = expr.getIdentifier().getSubscript();
                int beginIdx = -1;
                int endIdx = -1;
                if ( identSubsc != null ) {
                    EvalResult b, e;
                    b = this.evaluateExpression(identSubsc.getBeginExpr());
                    if ( b.isSubscripted() ) {
                        b = this.applySubscript(b);
                    }

                    if ( b.getResultType() != ReturnType.INTEGER ) {
                        reportEvalError(
                            String.format(
                                "Invalid begin expression - requires return type `Int` - given `%s`",
                                b.getResultType().name()
                            ),
                            identSubsc,
                            b
                        );
                        return null;
                    }

                    beginIdx = ((PInteger) b.getResult()).getValue();

                    if ( identSubsc.getEndExpr() == null ) {
                        endIdx = -1;
                        iRes.setSubscript(beginIdx);
                    } else {
                        e = this.evaluateExpression(identSubsc.getEndExpr());

                        if ( e == null ) {
                            endIdx = -1;
                        } else if ( e.getResultType() != ReturnType.INTEGER ) {
                            reportEvalError(
                                "Invalid end expression - requires return type `Int`",
                                identSubsc
                            );
                            return null;
                        }

                        endIdx = ((PInteger) b.getResult()).getValue();
                        iRes.setSubscript(beginIdx, endIdx);
                    }
                }

                if ( iRes.isSubscripted() && iRes.getResultType() == ReturnType.STRING ) {
                    switch (iRes.getSubscript().type) {
                        case SINGLE:
                            iRes.setResult(((PString) identV.get()).get(beginIdx).getResult());
                            break;
                        case RANGE:
                            iRes.setResult(((PString) identV.get()).getSlice(beginIdx, endIdx).getResult());
                            break;
                    }
                } else {
                    iRes.setResult(identV.get());
                }
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
            String.format(
                "expression evaluator fell out of switch while trying to eval a statement - expr type `%s`",
                expr.getExpressionType().name()
            ),
            expr
        );
        return null;
    }

    private EvalResult evaluateBinaryOp(BinaryOperation binOp) throws Exception, EvalException, ParserException {
        EvalResult lhs, rhs, res;
        TypeInterface val;

        Subscript sub;

        // Make sure to perform precedence rebalancing from the root first!
        if ( binOp.isRoot() ) {
            binOp = rebuildWithPrecedence(binOp);
        }

        lhs = this.evaluateExpression(binOp.getLHS());
        if ( lhs.isSubscripted() ) {
            lhs = this.applySubscript(lhs);
        }

        rhs = this.evaluateExpression(binOp.getRHS());
        if ( rhs.isSubscripted() ) {
            rhs = this.applySubscript(rhs);
        }

        switch (binOp.getOper().getOperator()) {
            case "+":
                val = Operators.add(lhs.getResult(), rhs.getResult());
                break;
            case "-":
                val = Operators.sub(lhs.getResult(), rhs.getResult());
                break;
            case "*":
                val = Operators.mult(lhs.getResult(), rhs.getResult());
                break;
            case "/":
                val = Operators.div(lhs.getResult(), rhs.getResult());
                break;
            case "%":
                val = Operators.modulo(lhs.getResult(), rhs.getResult());
                break;
            case "^":
                val = Operators.power(lhs.getResult(), rhs.getResult());
                break;
            case "==":
                val = Operators.equal(lhs.getResult(), rhs.getResult());
                break;
            case "!=":
                val = Operators.notEqual(lhs.getResult(), rhs.getResult());
                break;
            case ">":
                val = Operators.greaterThan(lhs.getResult(), rhs.getResult());
                break;
            case "<":
                val = Operators.lessThan(lhs.getResult(), rhs.getResult());
                break;
            case ">=":
                val = Operators.greaterThanEqual(lhs.getResult(), rhs.getResult());
                break;
            case "<=":
                val = Operators.lessThanEqual(lhs.getResult(), rhs.getResult());
                break;
            case "#":
                val = Operators.concat(lhs.getResult(), rhs.getResult());
                break;
            case "in":
                val = Operators.contains(lhs.getResult(), rhs.getResult());
                break;
            case "notin":
                val = Operators.notContains(lhs.getResult(), rhs.getResult());
                break;
            case "and":
                val = Operators.and(lhs.getResult(), rhs.getResult());
                break;
            case "or":
                val = Operators.or(lhs.getResult(), rhs.getResult());
                break;
            default:
                return null;
        }

        res = new EvalResult(val.getFormalType());
        res.setResult(val);

        return res;
    }

    private EvalResult evaluateUnaryOp(UnaryOperation unOp) throws Exception, EvalException, ParserException {
        EvalResult lhs, rhs, res;
        TypeInterface val;

        Subscript sub;

        rhs = this.evaluateExpression(unOp.getRHS());
        if ( rhs.isSubscripted() ) {
            rhs = this.applySubscript(rhs);
        }

        switch (unOp.getOper().getOperator()) {
            case "-":
                val = Operators.arithNegate(rhs.getResult());
                break;
            case "not":
                val = Operators.logicNegate(rhs.getResult());
                break;
            default:
                return null;
        }

        res = new EvalResult(val.getFormalType());
        res.setResult(val);

        return res;
    }

    private EvalResult evaluateFlowControl(Statement stmt) throws Exception, EvalException, ParserException {
        FlowControl fcStmt = stmt.getFlowControl();
        if ( fcStmt == null ) {
            reportEvalError(
                "flow control evaluator was passed a null flow statement",
                stmt
            );
            return null;
        }

        EvalResult res = this.evaluateFlowControl(fcStmt);
        res.setSource(stmt);
        return res;
    }

    private EvalResult evaluateFlowControl(FlowControl flow) throws Exception, EvalException, ParserException {
        switch (flow.getFlowType()) {
            case IF:
                return this.evaluateIfStmt(flow.getIf());
            case FOR:
                return this.evaluateForStmt(flow.getFor());
            case WHILE:
                return this.evaluateWhileStmt(flow.getWhile());
            default:
                reportEvalError(
                    "Invalid flow control block",
                    flow
                );
                return null;
        }
    }

    private EvalResult evaluateIfStmt(IfControl flow) throws Exception, EvalException, ParserException {
        Expression cond = flow.getCondition();
        Block trueBranch = flow.getTrueBranch();
        Block elseBranch = flow.getElseBranch();

        EvalResult condRes = this.evaluateExpression(cond);
        if ( ((PBoolean) condRes.getResult()).getValue() ) {
            Evaluator blockEval = new Evaluator(trueBranch.getStmts(), this.symTab.getNewChild());
            while ( blockEval.canEval() ) {
                blockEval.evaluate();
            }
        } else {
            if ( elseBranch != null ) {
                Evaluator blockEval = new Evaluator(elseBranch.getStmts(), this.symTab.getNewChild());
                while ( blockEval.canEval() ) {
                    blockEval.evaluate();
                }
            }
        }

        return new EvalResult(ReturnType.VOID);
    }

    private EvalResult evaluateWhileStmt(WhileControl flow) throws Exception, EvalException, ParserException {
        Expression cond = flow.getCondition();
        Block loopBranch = flow.getLoopBranch();

        EvalResult condRes = this.evaluateExpression(cond);
        Evaluator blockEval = new Evaluator(loopBranch.getStmts(), this.symTab.getNewChild());

        while ( ((PBoolean) condRes.getResult()).getValue() ) {
            while ( blockEval.canEval() ) {
                blockEval.evaluate();
            }

            blockEval.putStmts(loopBranch.getStmts());
            condRes = this.evaluateExpression(cond);
        }

        return new EvalResult(ReturnType.VOID);
    }

    private EvalResult evaluateForStmt(ForControl flow) throws Exception, EvalException, ParserException {
        ForExpr cond = flow.getCondition();
        Block body = flow.getBody();

        switch (cond.getType()) {
            case COUNTING:
                return this.evaluateCountingFor(flow);
            case ITERATIVE:
                return this.evaluateIterativeFor(flow);
            default:
                reportEvalError(
                    String.format(
                        "Invalid for condition type - %s",
                        cond.getType().name()
                    ),
                    flow
                );
                return null;
        }
    }

    private EvalResult evaluateCountingFor(ForControl flow) throws Exception, EvalException, ParserException {
        ForExpr expr = flow.getCondition();
        Block body = flow.getBody();

        Identifier ident = expr.getControl();
        EvalResult maxR = this.evaluateExpression(expr.getMax());
        STIdentifier identS = (STIdentifier) this.symTab.lookupSym(ident.getIdentT());
        if ( identS == null ) {
            identS = Infer.identFromNumber(this.symTab, ident, maxR.getResultType());
            this.symTab.putSymbol(identS);
        }

        SMValue cVar = this.store.getOrInit(identS);
        switch (identS.getDeclaredType()) {
            case INTEGER:
                cVar.set(new PInteger());
                break;
            case FLOAT:
                cVar.set(new PFloat());
                break;
            default:
                // Invalid type!
                reportEvalError(
                    "Invalid type in for loop counter var!",
                    expr,
                    ident,
                    maxR
                );
                return null;
        }

        int init, max, step;

        EvalResult initR = this.evaluateExpression(expr.getInitial());
        if ( expr.getStep() != null ) {
            EvalResult stepR = this.evaluateExpression(expr.getStep());
            step = ((PInteger) stepR.getResult()).getValue();
        } else {
            step = 1;
        }

        Evaluator blockEval = new Evaluator(body.getStmts(), this.symTab.getNewChild());

        init = ((PInteger) initR.getResult()).getValue();
        max = ((PInteger) maxR.getResult()).getValue();

        int cur = init;
        ((PInteger) cVar.get()).setValue(cur);

        while ( cur < max ) {
            while ( blockEval.canEval() ) {
                blockEval.evaluate();
            }

            blockEval.putStmts(body.getStmts());
            cur = cur + step;
            ((PInteger) cVar.get()).setValue(cur);
        }

        return new EvalResult(ReturnType.VOID);
    }

    @SuppressWarnings("unchecked")
    private EvalResult evaluateIterativeFor(ForControl flow) throws Exception, EvalException, ParserException {
        ForExpr expr = flow.getCondition();
        Block body = flow.getBody();

        EvalResult container = this.evaluateExpression(expr.getContainer());

        Identifier ident = expr.getControl();
        STIdentifier identS = (STIdentifier) this.symTab.lookupSym(ident.getIdentT());
        // This should take care of implicit symbol definition.
        if ( identS == null ) {
            identS = Infer.identFromContainer(this.symTab, ident, container);
            this.symTab.putSymbol(identS);
        }

        SMValue cVar = this.store.getOrInit(identS);
        switch (identS.getDeclaredType()) {
            case STRING:
                cVar.set(new PString());
                break;
            case INTEGER:
                cVar.set(new PInteger());
                break;
            case FLOAT:
                cVar.set(new PFloat());
                break;
            case BOOLEAN:
                cVar.set(new PBoolean());
                break;
            case ARRAY:
                cVar.set(new ArrayType());
                break;
        }

        if ( ! container.getResult().isIterable() ) {
            reportEvalError(
                String.format(
                    "Container is not iterable - type `%s`",
                    container.getResult().getFormalType().name()
                ),
                flow
            );
            return null;
        }

        Evaluator blockEval = new Evaluator(body.getStmts(), this.symTab.getNewChild());

        TypeInterface contTI = container.getResult();
        int idx = 0;
        switch (contTI.getFormalType()) {
            case ARRAY:
                ArrayType ary = (ArrayType) contTI;
                while ( idx < ary.getCapacity() ) {
                    EvalResult curVal = ary.getUnsafe(idx);
                    if ( curVal == null ) {
                        idx++;
                        continue;
                    }

                    cVar.get().setValue(curVal.getResult().getValue());

                    while ( blockEval.canEval() ) {
                        blockEval.evaluate();
                    }

                    blockEval.putStmts(body.getStmts());
                    idx++;
                }
                break;
            case STRING:
                PString str = (PString) contTI;
                while ( idx < str.getValue().length() ) {
                    cVar.get().setValue(str.get(idx).getResult().getValue());

                    while ( blockEval.canEval() ) {
                        blockEval.evaluate();
                    }

                    blockEval.putStmts(body.getStmts());
                    idx++;
                }
                break;
        }

        return new EvalResult(ReturnType.VOID);
    }

}
