package havabol.parse;

import havabol.Token;
import havabol.builtins.types.*;
import havabol.classify.*;
import havabol.common.type.*;
import havabol.sym.*;
import static havabol.util.Text.*;

import java.util.*;

public class DataType implements ParseElement {

    private Token typeToken;

    public DataType(Token dt) {
        this.typeToken = dt;
    }

    public TypeInterface getType() {
        switch (this.typeToken.tokenStr) {
            case "Int":
                return new PInteger();
            case "Float":
                return new PFloat();
            case "String":
                return new PString();
            case "Bool":
                return new PBoolean();
            default:
                return null;
        }
    }

    public ReturnType getReturnType() {
        switch (this.typeToken.tokenStr) {
            case "Int":
                return ReturnType.INTEGER;
            case "Float":
                return ReturnType.FLOAT;
            case "String":
                return ReturnType.STRING;
            case "Bool":
                return ReturnType.BOOLEAN;
            default:
                return null;
        }
    }

    public STControl getSTControl() {
        SymbolTable globSt = SymbolTable.getGlobal();

        switch (this.typeToken.tokenStr) {
            case "Int":
            case "Float":
            case "String":
            case "Bool":
                return (STControl) globSt.getSymbol(this.typeToken.tokenStr);
            default:
                return null;
        }
    }

    public boolean isValid() {
        switch (this.typeToken.tokenStr) {
            case "Int":
            case "Float":
            case "String":
            case "Bool":
                return true;
            default:
                return false;
        }
    }

    public String debug(int indent) {
        return lpads(
            indent,
            String.format(
                "DataType ~> `%s`\n",
                this.typeToken.tokenStr
            )
        );
    }

}
