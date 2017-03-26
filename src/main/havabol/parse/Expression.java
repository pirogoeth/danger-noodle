package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class Expression implements ParseElement {

    private Token t;

    private Assignment assignment;
    private Primitive primitive;
    private BinaryOperation binOp;
    private Identifier ident;
    private FunctionCall functionCall;

    public Expression(Token t) throws ParserException {
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
                        Parser.reportParseError(
                                "Expression could not parse token - secondary",
                                t
                        );
                        break;
                }
                break;
            default:
                Parser.reportParseError(
                        "Expression could not parse token - primary",
                        t
                );
                break;
        }
    }

    public Expression(Primitive prim) {
        this.primitive = prim;
    }

    public Expression(Assignment a) {
        this.assignment = a;
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
        return ( this.binOp != null && this.binOp.isValid() ) ||
               ( this.ident != null && this.ident.isValid() ) ||
               ( this.primitive != null && this.primitive.isValid() ) ||
               ( this.functionCall != null && this.functionCall.isValid() ) ||
               ( this.assignment != null && this.assignment.isValid() );
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();

        sb.append("Expression ~>\n");
        if ( this.ident != null ) {
            sb.append("  " + this.ident.debug());
        } else if ( this.binOp != null ) {
            sb.append("  " + this.binOp.debug());
        } else if ( this.primitive != null ) {
            sb.append("  " + this.primitive.debug());
        } else if ( this.functionCall != null ) {
            sb.append("  " + this.functionCall.debug());
        } else if ( this.assignment != null ) {
            sb.append("  " + this.assignment.debug());
        }

        return sb.toString();
    }

}
