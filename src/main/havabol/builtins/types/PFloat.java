package havabol.builtins.types;

import havabol.Token;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.type.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class PFloat implements TypeInterface<Float> {

    private Float value;

    public ReturnType getFormalType() {
        return ReturnType.FLOAT;
    }

    public void setValue(Float f) {
        this.value = f;
    }

    public Float getValue() {
        return this.value;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Primitive type Boolean =>\n"));
        sb.append(lpads(indent + 2, String.format("Value ==> `%b`\n", this.value)));

        return sb.toString();
    }

}
