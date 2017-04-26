package havabol.builtins.types;

import havabol.Token;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.type.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class PFloat implements TypeInterface<Double> {

    private Double value;

    public ReturnType getFormalType() {
        return ReturnType.FLOAT;
    }

    public void setValue(Double d) {
        this.value = d;
    }

    public Double getValue() {
        return this.value;
    }

    public String debug() {
        return this.debug(0);
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Primitive type Float =>\n"));
        sb.append(lpads(indent + 2, String.format("Value ==> `%.2f`\n", this.value)));

        return sb.toString();
    }

    public String getRepr() {
        return Numerics.floatAsString(this.value);
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
                return Numerics.intPrim(this);
            case FLOAT:
                return this;
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

    public TypeInterface<Double> clone() {
        PFloat f = new PFloat();
        f.setValue(this.getValue());
        return f;
    }

}
