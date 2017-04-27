package havabol.parse;

import havabol.Token;
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

    Parenthese(Token t) {
        super(t);
    }

}
