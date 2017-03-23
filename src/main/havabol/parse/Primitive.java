package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import havabol.util.*;

import java.util.*;

public class Primitive implements Validate {

    private static boolean isBool(Token t) {
        if ( ! Parser.tokenType(t, Primary.OPERAND, Subclass.BOOLEAN) ) {
            return false;
        }

        switch (t.tokenStr) {
            case "T":
            case "F":
                return true;
            default:
                return false;
        }
    }

    private static boolean isString(Token t) {
        if ( ! Parser.tokenType(t, Primary.OPERAND, Subclass.STRING) ) {
            return false;
        }

        return true;
    }

    private Token primToken;

    public Primitive(Token t) {
        this.primToken = t;
    }

    public boolean isValid() {
        return ( Numerics.isInt(this.primToken) ||
                 Numerics.isFloat(this.primToken) ||
                 isBool(this.primToken) ||
                 isString(this.primToken) );
    }

    public void print() {
        System.out.println(
                String.format(
                    "  Primitive ~> `%s`",
                    this.primToken.tokenStr
                )
        );
    }
}
