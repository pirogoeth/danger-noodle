package havabol.parse;

import havabol.Token;
import havabol.builtins.types.*;
import havabol.classify.*;
import havabol.common.type.*;
import havabol.eval.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class Primitive extends ParseElement {

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

    private static boolean asBool(Token t) throws ParserException {
        if ( ! Parser.tokenType(t, Primary.OPERAND, Subclass.BOOLEAN) ) {
            Parser.reportParseError(
                "Tried to convert non-boolean token to boolean",
                t
            );
            return false;
        }

        switch (t.tokenStr) {
            case "T":
                return true;
            case "F":
                return false;
            default:
                Parser.reportParseError(
                    "Token classified as boolean is not actually boolean!",
                    t
                );
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

    public EvalResult getEvaluable() throws ParserException {
        EvalResult res;

        if ( isString(this.primToken) ) {
            res = new EvalResult(ReturnType.STRING);
            PString ti = new PString();
            ti.setValue(this.primToken.tokenStr);
            res.setResult(ti);
        } else if ( isBool(this.primToken) ) {
            res = new EvalResult(ReturnType.BOOLEAN);
            PBoolean ti = new PBoolean();
            ti.setValue(asBool(this.primToken));
            res.setResult(ti);
        } else if ( Numerics.isInt(this.primToken) ) {
            res = new EvalResult(ReturnType.INTEGER);
            PInteger ti = new PInteger();
            ti.setValue(Numerics.tokenAsInt(this.primToken));
            res.setResult(ti);
        } else if ( Numerics.isFloat(this.primToken) ) {
            res = new EvalResult(ReturnType.FLOAT);
            PFloat ti = new PFloat();
            ti.setValue(Numerics.tokenAsFloat(this.primToken));
            res.setResult(ti);
        } else {
            Parser.reportParseError(
                "Token is of unknown type, could not marshal to EvalResult",
                this.primToken
            );
            return null;
        }

        return res;
    }

    public boolean isValid() {
        return ( Numerics.isInt(this.primToken) ||
                 Numerics.isFloat(this.primToken) ||
                 isBool(this.primToken) ||
                 isString(this.primToken) );
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(
            indent,
            String.format(
                "Primitive ~> `%s`\n",
                this.primToken.tokenStr
            )
        ));

        sb.append(lpads(
            indent + 2,
            "TOKEN :: " + this.primToken.getDebugInfo() + "\n"
        ));

        return sb.toString();
    }

}
