package havabol.parse;

import havabol.Token;
import havabol.Scanner;
import havabol.SymbolTable;
import havabol.classify.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ParserException extends Exception {

    public long serialVersionUid = 1000000L;
    private Token[] tokens;

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Token...contexts) {
        super(message);
        this.tokens = contexts;
    }

    public Token[] getCausalTokens() {
        return this.tokens;
    }

}

/**
 * Parses tokens.
 *
 * @author Sean Johnson <isr830@my.utsa.edu>
 */
public class Parser {

    static boolean tokenType(Token t, Primary p, Subclass s) {
        if ( p == null ) p = Primary.UNKNOWN;
        if ( s == null ) s = Subclass.UNKNOWN;

        return t.primClassif == p.getCid() && t.subClassif == s.getCid();
    }

    private List<Token> tokens;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // ERROR MANGLEMENT

    static void reportParseError(String message, Token...contexts) throws ParserException {
        String[] contextStr = new String[contexts.length];

        for (int i = 0; i < contextStr.length; i++) {
            Token t = contexts[i];
            if ( t != null ) {
                contextStr[i] = String.format(
                        "[%s:%d:%d]: Near token `%s`",
                        Scanner.getInstance().sourceFileNm,
                        t.iSourceLineNr,
                        t.iColPos,
                        t.tokenStr
                );
            } else {
                contextStr[i] = "[invalid] Null token given to error reporter";
            }
        }

        StringBuilder excSb = new StringBuilder();

        for (String s : contextStr) {
            excSb.append(s + "\n");
        }

        throw new ParserException(
                String.format(
                    "Parse error occurred: %s\n%s",
                    message,
                    excSb
                ),
                contexts
        );
    }

    // TOKEN MANGLEMENT

    /**
     * Check the token value of the next thingle.
     */
    private boolean isNext(String s) {
        return this.isNext(this.tokens, s);
    }

    private boolean isNext(List<Token>t, String s) {
        if ( this.peekNext(t).tokenStr.equals(s) ) {
            return true;
        }

        return false;
    }

    /**
     * Forces the parser to eat the next token.
     */
    private void eatNext() {
        this.tokens.remove(0);
    }

    // Yeah this uses raw types, I know :(
    private void eatNext(List<Token> l) {
        l.remove(0);
    }

    /**
     * Conditional eat.
     */
    private boolean eatNextIfOfType(Primary prim, Subclass sub) {
        return this.eatNextIfOfType(this.tokens, prim, sub);
    }

    private boolean eatNextIfOfType(List<Token> l, Primary prim, Subclass sub) {
        if ( tokenType(this.peekNext(l), prim, sub) ) {
            this.eatNext(l);
            return true;
        }

        return false;
    }

    private boolean eatNextIfEq(String s) {
        return this.eatNextIfEq(this.tokens, s);
    }

    private boolean eatNextIfEq(List<Token> l, String s) {
        if ( this.isNext(l, s) ) {
            this.eatNext(l);
            return true;
        }

        return false;
    }

    /**
     * Pops the next token off the array.
     *
     * @return Token
     */
    private Token popNext() {
        return this.tokens.remove(0);
    }

    private Token popNext(List<Token> l) {
        return l.remove(0);
    }

    /**
     * Allows for single lookahead of the next token without popping.
     *
     * @return Token
     */
    private Token peekNext() {
        try {
            return this.tokens.get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    private Token peekNext(List<Token> l) {
        try {
            return l.get(0);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Gets and returns a list of tokens up to statement end.
     *
     * @return List<Token>
     */
    private List<Token> popStatement() {
        List<Token> tokens = this.popUntil(";");

        this.eatNext();

        return tokens;
    }

    private List<Token> popUntil(String delim) {
        List<Token> tokens = new ArrayList<>();

        while ( ! this.peekNext().tokenStr.equals(delim) ) {
            tokens.add(this.popNext());
        }

        return tokens;
    }

    private void restoreTokens(List<Token> tokens) {
        Collections.reverse(tokens);
        for (Token t : tokens) {
            this.tokens.add(0, t);
        }
    }

    // PARSER MANGLEMENT

    public boolean canParse() {
        return !this.tokens.isEmpty();
    }

    public Statement parse() throws ParserException {
        Token t = this.peekNext();

        Expression expr = null;
        switch (Primary.primaryFromInt(t.primClassif)) {
            case CONTROL:
                return this.parseControl(t);
            case OPERAND:
                expr = this.parseExpression(this.popStatement());
                if ( !expr.isValid() ) {
                    // Expr not valid?
                    return null;
                }
                return new Statement(expr);
            case FUNCTION:
                FunctionCall funcCall = this.parseFunctionCall(this.popStatement());
                if ( !funcCall.isValid() ) {
                    // Function call not valid?
                    return null;
                }

                // Up-resolve to expression
                expr = new Expression(funcCall);
                if ( !expr.isValid() ) {
                    // ..
                    return null;
                }

                return new Statement(expr);
            case SEPARATOR:
                return null;
            default:
                this.reportParseError(
                        "Could not further derive statement",
                        t
                );
                return null;
        }
    }

    private Statement parseControl(Token head) throws ParserException {
        switch (Subclass.subclassFromInt(head.subClassif)) {
            case FLOW:
                return new Statement(this.parseFlowControl(this.popUntil(":")));
            case END:
                return null;
            case DECLARE:
                this.eatNext();
                return this.parseDeclaration(head);
            default:
                this.reportParseError(
                        "Failed building control elements",
                        head
                );
                return null;
        }

    }

    private FlowControl parseFlowControl(List<Token> tokens) throws ParserException {
        // At this point, the next token should be the flow delimiter.
        this.eatNextIfEq(":");

        Token flowT = this.popNext(tokens);

        Expression cond;
        Block mainBranch;
        switch (flowT.tokenStr) {
            case "if":
                cond = this.parseExpression(tokens);
                mainBranch = this.parseBlock("else", "endif");
                Block elseBranch = null;

                if ( this.isNext("else") ) {
                    this.eatNext();
                    this.eatNextIfEq(":");
                    elseBranch = this.parseBlock("endif");
                }

                if ( ! this.eatNextIfEq("endif") ) {
                    this.reportParseError(
                            "If statement has no matching endif",
                            flowT
                    );
                    return null;
                } else {
                    if ( ! this.eatNextIfEq(";") ) {
                        this.reportParseError(
                                "Unterminated end-statement",
                                flowT
                        );
                        return null;
                    }
                }

                IfControl ifStmt = new IfControl(cond, mainBranch, elseBranch);
                return new FlowControl(ifStmt);
            case "while":
                cond = this.parseExpression(tokens);
                mainBranch = this.parseBlock("endwhile");

                if ( ! this.eatNextIfEq("endwhile") ) {
                    this.reportParseError(
                            "While statement has no matching endwhile",
                            flowT
                    );
                    return null;
                } else {
                    if ( ! this.eatNextIfEq(";") ) {
                        this.reportParseError(
                                "Unterminated end-statement",
                                flowT
                        );
                        return null;
                    }
                }

                WhileControl whileStmt = new WhileControl(cond, mainBranch);
                return new FlowControl(whileStmt);
            case "for":
                Expression expr = this.parseExpression(tokens);

                Block forBody = this.parseBlock("endfor");
                if ( ! this.eatNextIfEq("endfor") ) {
                    this.reportParseError(
                            "While statement has no matching endfor",
                            flowT
                    );
                    return null;
                } else {
                    if ( ! this.eatNextIfEq(";") ) {
                        this.reportParseError(
                                "Unterminated end-statement",
                                flowT
                        );
                        return null;
                    }
                }

                ForControl forStmt = new ForControl(expr, forBody);
                return new FlowControl(forStmt);
            case "select":
                // do the thing
                break;
            default:
                // wat
                this.reportParseError(
                        "Invalid control flow token",
                        flowT
                );
                return null;
        }

        this.reportParseError(
                "Flow control parser fell out of case",
                flowT
        );
        return null;
    }

    private Statement parseDeclaration(Token head) throws ParserException {
        List<Token> tokens = this.popStatement();

        if ( tokenType(head, Primary.CONTROL, Subclass.DECLARE) &&
             tokenType(this.peekNext(tokens), Primary.OPERAND, Subclass.IDENTIFIER) ) {

            DataType dt = new DataType(head);

            Token next = this.popNext(tokens);

            Identifier ident = new Identifier(next);
            Declaration decl = new Declaration(dt, ident);

            // Peek at the next token to see if this is a compound declaration
            Token oper = this.peekNext(tokens);
            if ( oper != null && oper.tokenStr.equals("=") ) {
                tokens.add(0, next);
                tokens.add(0, head);
                return new Statement(this.parseComplexDeclaration(tokens));
            }

            if ( dt.isValid() && ident.isValid() && decl.isValid() ) {
                return new Statement(decl);
            } else {
                this.reportParseError(
                        "Declaration is invalid",
                        head,
                        next,
                        oper
                );
                return null;
            }
        } else {
            this.reportParseError(
                    "Declaration is invalid",
                    head
            );
            return null;
        }
    }

    private Assignment parseComplexDeclaration(List<Token> tokens) throws ParserException {
        Token typeT = this.popNext(tokens);
        Token identT = this.popNext(tokens);

        DataType dtype = new DataType(typeT);
        Identifier ident = new Identifier(identT);
        Declaration decl = new Declaration(dtype, ident);

        Token operatorT = this.popNext(tokens);
        Operator oper = null;

        if ( tokenType(operatorT, Primary.OPERATOR, null) && operatorT.tokenStr.equals("=") ) {
            oper = new Operator(operatorT);
        }

        Expression expr = this.parseExpression(tokens);

        Assignment a = new Assignment(decl, oper, expr);
        if ( a == null || !a.isValid() ) {
            this.reportParseError(
                    "Declaration is invalid",
                    typeT,
                    identT,
                    operatorT
            );
            return null;
        }

        return a;
    }

    private FunctionCall parseFunctionCall(List<Token> tokens) throws ParserException {
        Token handle = this.popNext(tokens);

        Identifier funcName = new Identifier(handle);
        if ( !funcName.isValid() ) {
            this.reportParseError(
                    "Bad function handle",
                    handle
            );
            return null;
        }

        Token openParen = this.popNext(tokens);
        if ( tokenType(openParen, Primary.SEPARATOR, null) && openParen.tokenStr.equals("(") ) {
            Token t;
            List<Expression> argsList = new ArrayList<>();
            List<Token> argTokens = new ArrayList<>();

            while ((t = this.popNext(tokens)) != null) {
                if ( t.tokenStr.equals(")") ) {
                    // Flush all tokens through `parseExpression` and add to `argsList`
                    argsList.add(this.parseExpression(argTokens));

                    // Clear out argTokens
                    argTokens = new ArrayList<>();

                    break;
                }

                switch (t.tokenStr) {
                    case ",":
                        // Flush all tokens through `parseExpression` and add to `argsList`
                        argsList.add(this.parseExpression(argTokens));

                        // Clear out argTokens
                        argTokens = new ArrayList<>();

                        break;
                    default:
                        argTokens.add(t);
                        break;
                }
            }

            FunctionCall fCall = new FunctionCall(funcName, argsList);
            if ( !fCall.isValid() ) {
                this.reportParseError(
                        "Invalid function call",
                        handle,
                        openParen,
                        t
                );
                return null;
            }

            return fCall;
        }

        return null;
    }

    private Block parseBlock(String...until) throws ParserException {
        return this.parseBlock(this.tokens, until);
    }

    private Block parseBlock(List<Token> tokens, String...until) throws ParserException {
        List<String> delim = Arrays.asList(until);
        List<Statement> stmts = new ArrayList<>();

        while ( ! delim.contains(this.peekNext(tokens).tokenStr) ) {
            Statement s = this.parse();
            if ( s == null ) {
                this.reportParseError(
                        "Block parser encountered a null statement",
                        this.peekNext(tokens)
                );
                break;
            }
            stmts.add(s);
        }

        return new Block(stmts);
    }

    private Expression parseExpression(List<Token> arg) throws ParserException {
        if ( arg.isEmpty() ) {
            this.reportParseError("Expression parsing failed!");
            return null;
        }

        Token head = this.popNext(arg);
        if ( arg.isEmpty() ) {
            return new Expression(head);
        }

        Token next = this.peekNext(arg);

        switch (Primary.primaryFromInt(head.primClassif)) {
            case OPERAND:
                if ( next.primClassif == Primary.OPERATOR.getCid() ) {
                    Expression lhs = new Expression(head);
                    Operator oper = new Operator(this.popNext(arg));

                    Expression rhs = this.parseExpression(arg);
                    BinaryOperation binOp = new BinaryOperation(lhs, oper, rhs);

                    if ( next.tokenStr.equals("=") ) {
                        // binOp is an assignment
                        Assignment a = new Assignment(binOp);
                        if ( ! a.isValid() ) {
                            this.reportParseError(
                                    "Building assignment failed",
                                    head,
                                    next
                            );
                            return null;
                        }

                        return new Expression(a);
                    } else {
                        if ( ! binOp.isValid() ) {
                            this.reportParseError(
                                    "Building binary operation failed",
                                    head,
                                    next
                            );
                            return null;
                        }

                        return new Expression(binOp);
                    }
                }

                break;
            case FUNCTION:
                arg.add(0, head);
                FunctionCall fCall = this.parseFunctionCall(arg);

                if ( ! fCall.isValid() ) {
                    this.reportParseError(
                            "Building function call in from expression failed",
                            head,
                            next
                    );
                    return null;
                }

                return new Expression(fCall);
            default:
                this.reportParseError(
                        "Building expression failed",
                        head,
                        next
                );
                return null;
        }

        return null;
    }
}
