package havabol.builtins.types;

import havabol.Token;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.type.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class PBoolean implements TypeInterface<Boolean> {

    private Boolean value;

    public ReturnType getFormalType() {
        return ReturnType.BOOLEAN;
    }

    public void setValue(Boolean b) {
        this.value = b;
    }

    public Boolean getValue() {
        return this.value;
    }

    public String debug() {
        return this.debug(0);
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Primitive type Boolean =>\n"));
        sb.append(lpads(indent + 2, String.format("Value ==> `%b`\n", this.value)));

        return sb.toString();
    }

    public String getRepr() {
        if ( this.value ) {
            return "T";
        } else {
            return "F";
        }
    }

    public boolean coercibleTo(ReturnType target) {
        switch (target) {
            case ARRAY:
            case INTEGER:
            case FLOAT:
                return false;
            case STRING:
            case BOOLEAN:
                return true;
            default:
                return false;
        }
    }

    public TypeInterface coerceTo(ReturnType target) {
        switch (target) {
            case STRING:
                return Numerics.strPrim(this);
            case BOOLEAN:
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

    public TypeInterface<Boolean> clone() {
        PBoolean b = new PBoolean();
        b.setValue(this.getValue());
        return b;
    }

}
