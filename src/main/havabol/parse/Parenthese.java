package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

public class Parenthese extends Operator {

    public static Parenthese OPEN() {
        Token t = new Token("(");
        t.primClassif = Token.SEPARATOR;
        return new Parenthese(t);
    }

    public static Parenthese CLOSE() {
        Token t = new Token(")");
        t.primClassif = Token.SEPARATOR;
        return new Parenthese(t);
    }

    private boolean stacked = false;

    Parenthese(Token t) {
        super(t);
    }

    public boolean isStacked() {
        return this.stacked;
    }

    public void setStacked(boolean b) {
        this.stacked = b;
    }

    public Precedence getPrecedence() {
        if ( this.stacked ) {
            return Precedence.PARENTHESES_LOW;
        } else {
            return Precedence.PARENTHESES;
        }
    }

}
