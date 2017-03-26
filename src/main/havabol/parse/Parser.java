package havabol.parse;

import havabol.Token;
import havabol.Scanner;
import havabol.SymbolTable;
import havabol.classify.*;

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
                return this.parseControl(this.popNext());
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
                return null;
            case END:
                return null;
            case DECLARE:
                return this.parseDeclaration(head);
            default:
                this.reportParseError(
                        "Failed building control elements",
                        head
                );
                return null;
        }

    }

    private Statement parseDeclaration(Token head) throws ParserException {
        List<Token> tokens = this.popStatement();

        if ( tokenType(head, Primary.CONTROL, Subclass.DECLARE) &&
             tokenType(this.peekNext(tokens), Primary.OPERAND, Subclass.IDENTIFIER) ) {

            Token next = this.popNext(tokens);

            DataType dt = new DataType(head);
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

    private BinaryOperation parseBinaryOperation(Token lhs, Token operator) throws ParserException {
        Token rhs = this.popNext();

        if ( tokenType(lhs, Primary.OPERAND, Subclass.IDENTIFIER) &&
             tokenType(operator, Primary.OPERATOR, null) ) {

            Expression lhsExpr = new Expression(lhs);
            Operator operatorExpr = new Operator(operator);
            Expression rhsExpr = new Expression(rhs);

            BinaryOperation binOp = new BinaryOperation(lhsExpr, operatorExpr, rhsExpr);
            if ( binOp.isValid() ) {
                return binOp;
            }

            this.reportParseError(
                    "Binary operator is invalid",
                    lhs,
                    operator,
                    rhs
            );
            return null;
        }

        return null;
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

    private Expression parseExpression(List<Token> arg) throws ParserException {
        if ( arg.isEmpty() ) {
            this.reportParseError("Expression parsing failed!");
            return null;
        }

        Token head = arg.remove(0);
        if ( arg.isEmpty() ) {
            return new Expression(head);
        }

        Token next = arg.get(0);

        switch (Primary.primaryFromInt(head.primClassif)) {
            case OPERAND:
                if ( next.primClassif == Primary.OPERATOR.getCid() ) {
                    Expression lhs = new Expression(head);
                    Operator oper = new Operator(arg.remove(0));
                    Expression rhs = this.parseExpression(arg);
                    BinaryOperation binOp = new BinaryOperation(lhs, oper, rhs);

                    if ( next.tokenStr.equals("=") ) {
                        // binOp is an assignment
                        Assignment a = new Assignment(binOp);
                        if ( !a.isValid() ) {
                            this.reportParseError(
                                    "Building assignment failed",
                                    head,
                                    next
                            );
                            return null;
                        }

                        return new Expression(a);
                    } else {
                        if ( !binOp.isValid() ) {
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
