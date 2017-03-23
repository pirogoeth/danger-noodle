package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class Expression implements Validate {

    private Token t;

    private Primitive primitive;
    private BinaryOperation binOp;
    private Identifier ident;
    private FunctionCall functionCall;

    public Expression(Token t) {
        switch (Primary.primaryFromInt(t.primClassif)) {
            case OPERAND:
                switch (Subclass.subclassFromInt(t.subClassif)) {
                    case IDENTIFIER:
                        this.ident = new Identifier(t);
                        break;
                    case INTEGER:
                    case STRING:
                    case BOOLEAN:
                    case FLOAT:
                        this.primitive = new Primitive(t);
                        break;
                    default:
                        t.printToken();
                        System.out.println("fuck subclasses - in expression");
                        break;
                }
                break;
            default:
                t.printToken();
                System.out.println("fuck primary classes - in expression");
                break;
        }
    }

    public Expression(Identifier ident) {
        this.ident = ident;
    }

    public Expression(BinaryOperation binOp) {
        this.binOp = binOp;
    }

    public Expression(FunctionCall funcCall) {
        this.functionCall = funcCall;
    }

    public boolean isValid() {
        return ( ( this.binOp != null && this.binOp.isValid() ) ||
                 ( this.ident != null && this.ident.isValid() ) ||
                 ( this.primitive != null && this.primitive.isValid() ) ||
                 ( this.functionCall != null && this.functionCall.isValid() ) );
    }

    public void print() {
        System.out.println("Expression ~>");
        if ( this.ident != null ) {
            this.ident.print();
        } else if ( this.binOp != null ) {
            this.binOp.print();
        } else if ( this.primitive != null ) {
            this.primitive.print();
        } else if ( this.functionCall != null ) {
            this.functionCall.print();
        }
    }

}
