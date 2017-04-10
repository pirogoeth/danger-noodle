package havabol.builtins.types;

import havabol.Token;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.type.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class PInteger implements TypeInterface<Integer> {

    private Integer value;

    public ReturnType getFormalType() {
        return ReturnType.INTEGER;
    }

    public void setValue(Integer i) {
        this.value = i;
    }

    public Integer getValue() {
        return this.value;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Primitive type Boolean =>\n"));
        sb.append(lpads(indent + 2, String.format("Value ==> `%b`\n", this.value)));

        return sb.toString();
    }

}
