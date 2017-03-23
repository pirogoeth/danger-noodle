package havabol.parse;

import havabol.Token;
import havabol.Scanner;
import havabol.SymbolTable;
import havabol.classify.*;

import java.util.ArrayList;
import java.util.List;

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

    private void reportParseError(String message, Token...contexts) {
        String[] contextStr = new String[contexts.length];

        for (int i = 0; i < contextStr.length; i++) {
            Token t = contexts[i];
            contextStr[i] = String.format(
                    "[%s:%d:%d]: Near token `%s`",
                    Scanner.getInstance().sourceFileNm,
                    t.iSourceLineNr,
                    t.iColPos,
                    t.tokenStr
            );
        }
    }

    // TOKEN MANGLEMENT

    /**
     * Forces the parser to eat the next token.
     */
    private void eatNext() {
        this.tokens.remove(0);
    }

    /**
     * Pops the next token off the array.
     *
     * @return Token
     */
    private Token popNext() {
        return this.tokens.remove(0);
    }

    /**
     * Allows for single lookahead of the next token without popping.
     *
     * @return Token
     */
    private Token peekNext() {
        return this.tokens.get(0);
    }

    // PARSER MANGLEMENT

    public boolean canParse() {
        return !this.tokens.isEmpty();
    }

    public Statement parse() {
        Token t = this.popNext();

        Expression expr = null;
        switch (Primary.primaryFromInt(t.primClassif)) {
            case CONTROL:
                return this.parseControl(t);
            case OPERAND:
                expr = this.parseOperand(t);
                if ( !expr.isValid() ) {
                    // Expr not valid?
                    return null;
                }
                return new Statement(expr);
            case FUNCTION:
                FunctionCall funcCall = this.parseFunctionCall(t);
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
                System.out.println("fuck parsing");
                return null;
        }
    }

    private Statement parseControl(Token t) {
        Token next = this.popNext();

        switch (Subclass.subclassFromInt(t.subClassif)) {
            case FLOW:
                return null;
            case END:
                return null;
            case DECLARE:
                Statement decl = this.parseDeclaration(t, next);
                if ( decl != null && tokenType(this.peekNext(), Primary.SEPARATOR, null) ) {
                    // Eat the next token because it is a semicolon.
                    this.eatNext();
                }
                return decl;
            default:
                System.out.println("fuck control");
                return null;
        }

    }

    private Expression parseOperand(Token t) {
        Token next = this.popNext();

        switch (Subclass.subclassFromInt(t.subClassif)) {
            case IDENTIFIER:
                // <identifier> <operator> <identifier|const>
                BinaryOperation binOp = this.parseBinaryOperation(t, next);
                if ( binOp != null && tokenType(this.peekNext(), Primary.SEPARATOR, null) ) {
                    // Eat the next token because it is a semicolon.
                    this.eatNext();
                }
                return new Expression(binOp);
            case FLOAT:
            case INTEGER:
            case STRING:
            case BOOLEAN:
                if ( Primary.primaryFromInt(next.primClassif) == Primary.SEPARATOR ) {
                    return new Expression(t);
                }

                // XXX - Process a remaining expression here!
            default:
                System.out.println("fuck operands ");
                return null;
        }
    }

    private Statement parseDeclaration(Token p1, Token p2) {
        if ( tokenType(p1, Primary.CONTROL, Subclass.DECLARE) &&
             tokenType(p2, Primary.OPERAND, Subclass.IDENTIFIER) ) {

            DataType dt = new DataType(p1);
            Identifier ident = new Identifier(p2);
            Declaration decl = new Declaration(dt, ident);

            // Peek at the next token to see if this is a compound declaration
            Token next = this.peekNext();
            if ( tokenType(next, Primary.OPERATOR, null) && next.tokenStr.equals("=") ) {
                return new Statement(this.parseComplexDeclaration(p1, p2));
            }

            if ( dt.isValid() && ident.isValid() && decl.isValid() ) {
                return new Statement(decl);
            } else {
                System.out.println("fuck validity");
                return null;
            }
        } else {
            System.out.println("fuck declarations");
            return null;
        }
    }

    private Assignment parseComplexDeclaration(Token typeT, Token identT) {
        DataType dtype = new DataType(typeT);
        Identifier ident = new Identifier(identT);
        Declaration decl = new Declaration(dtype, ident);

        Token operatorT = this.popNext();
        Operator oper = null;

        if ( tokenType(operatorT, Primary.OPERATOR, null) && operatorT.tokenStr.equals("=") ) {
            oper = new Operator(operatorT);
        }

        Token operandT = this.popNext();
        Expression expr = this.parseOperand(operandT);

        Assignment a = new Assignment(decl, oper, expr);
        if ( a == null || !a.isValid() ) {
            System.out.println("fuck complex declarations");
            return null;
        }

        return a;
    }

    private BinaryOperation parseBinaryOperation(Token lhs, Token operator) {
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

            System.out.println("fuck binary operations");
            return null;
        }

        return null;
    }

    private FunctionCall parseFunctionCall(Token handle) {
        Identifier funcName = new Identifier(handle);
        if ( !funcName.isValid() ) {
            System.out.println("fuck function handles");
            return null;
        }

        Token openParen = this.popNext();
        if ( tokenType(openParen, Primary.SEPARATOR, null) && openParen.tokenStr.equals("(") ) {
            Token t;
            List<Expression> argsList = new ArrayList<>();
            List<Token> argTokens = new ArrayList<>();

            while ((t = this.popNext()) != null) {
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

            if ( this.peekNext().tokenStr.equals(";") ) {
                this.eatNext();
            }

            FunctionCall fCall = new FunctionCall(funcName, argsList);
            if ( !fCall.isValid() ) {
                System.out.println("fuck function calls");
                return null;
            }

            return fCall;
        }

        return null;
    }

    private Expression parseExpression(List<Token> arg) {
        if ( arg.isEmpty() ) {
            System.out.println("fucking expression arg is empty");
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

                    if ( !binOp.isValid() ) {
                        System.out.println("fuck expressions getting turned into binary operations");
                        return null;
                    }

                    return new Expression(binOp);
                }

                break;
            default:
                System.out.println("fuck expressions!");
                return null;
        }

        return null;
    }
}
