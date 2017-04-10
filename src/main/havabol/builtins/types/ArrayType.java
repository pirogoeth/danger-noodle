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

    public String getRepr() {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for (int i = 0; i < this.value.size() ; i++) {
            sb.append(this.value.get(i).getRepr());
            if ( i < this.value.size() - 1 ) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

}
