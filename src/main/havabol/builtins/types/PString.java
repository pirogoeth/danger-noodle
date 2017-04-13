package havabol.builtins.types;

import havabol.Token;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.type.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class PString implements TypeInterface<String> {

    private String value;

    public ReturnType getFormalType() {
        return ReturnType.STRING;
    }

    public void setValue(String s) {
        this.value = s;
    }

    public String getValue() {
        return this.value;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Primitive type String =>\n"));
        sb.append(lpads(indent + 2, String.format("Value ==> `%s`\n", this.value)));

        return sb.toString();
    }

    public String getRepr() {
        return this.value;
    }

    public boolean isIterable() {
        return true;
    }

    public TypeInterface<String> clone() {
        PString s = new PString();
        s.setValue(this.getValue());
        return s;
    }

}
