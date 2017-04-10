package havabol.builtins.types;

import havabol.Token;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.type.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class ArrayType implements TypeInterface<List<TypeInterface>> {

    private List<TypeInterface> value;

    public ReturnType getFormalType() {
        return ReturnType.ARRAY;
    }

    public void setValue(List<TypeInterface> l) {
        this.value = l;
    }

    public List<TypeInterface> getValue() {
        return this.value;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Array Type =>\n"));
        sb.append(lpads(indent + 2, "Items =>\n"));

        for (TypeInterface tVal : this.value) {
            sb.append(tVal.debug(indent + 4));
        }

        return sb.toString();
    }

}
