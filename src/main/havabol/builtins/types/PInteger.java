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

    public String debug() {
        return this.debug(0);
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Primitive type Int =>\n"));
        sb.append(lpads(indent + 2, String.format("Value ==> `%d`\n", this.value)));

        return sb.toString();
    }

    public String getRepr() {
        return Numerics.intAsString(this.value);
    }

    public boolean coercibleTo(ReturnType target) {
        switch (target) {
            case INTEGER:
            case FLOAT:
            case STRING:
                return true;
            case ARRAY:
            case BOOLEAN:
            default:
                return false;
        }
    }

    public TypeInterface coerceTo(ReturnType target) {
        switch (target) {
            case STRING:
                return Numerics.strPrim(this);
            case INTEGER:
                return this;
            case FLOAT:
                return Numerics.floatPrim(this);
            default:
                // XXX - THIS SHOULD THROW AN ERROR
                return null;
        }
    }

    public boolean isEqual(Object o) {
        return this.value.equals(o);
    }

    public boolean isIterable() {
        return false;
    }

    public TypeInterface<Integer> clone() {
        PInteger i = new PInteger();
        i.setValue(this.getValue());
        return i;
    }

}
