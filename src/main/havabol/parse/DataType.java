package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

import java.util.*;

public class DataType implements ParseElement {

    private Token typeToken;

    public DataType(Token dt) {
        this.typeToken = dt;
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
