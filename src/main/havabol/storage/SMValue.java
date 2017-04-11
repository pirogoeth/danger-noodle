package havabol.storage;

import havabol.*;
import havabol.builtins.types.*;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.type.*;
import havabol.sym.*;
import havabol.util.*;
import static havabol.util.Numerics.*;

public class SMValue {

    private ReturnType containType = ReturnType.VOID;

    private STIdentifier symbol = null;
    private TypeInterface value = null;

    SMValue(STIdentifier ident) {
        this.symbol = ident;
    }

    public void set(TypeInterface i) {
        this.value = i;
    }

    public TypeInterface get() {
        return this.value;
    }

    // COERCION

    public PInteger coerceInt() {
        switch (this.value.getFormalType()) {
            case STRING:
                return intPrim(stringAsInt((String) this.value.getValue()));
            case FLOAT:
                return intPrim((PFloat) this.value);
            case INTEGER:
                return (PInteger) this.value;
            case BOOLEAN:
                return intPrim((PBoolean) this.value);
            default:
                // Needs to throw a CoercionException or some shit.
                break;
        }

        return null;
    }

    public PString coerceString() {
        PString repr = new PString();
        repr.setValue(this.value.getRepr());
        return repr;
    }

    public PFloat coerceFloat() {
        switch (this.value.getFormalType()) {
            case STRING:
                return floatPrim(stringAsInt((String) this.value.getValue()));
            case FLOAT:
                return (PFloat) this.value;
            case INTEGER:
                return floatPrim((PInteger) this.value);
            case BOOLEAN:
                return floatPrim((PBoolean) this.value);
            default:
                // Needs to throw a CoercionException or some shit.
                break;
        }

        return null;
    }

    public PBoolean coerceBool() {
        switch (this.value.getFormalType()) {
            case BOOLEAN:
                return (PBoolean) this.value;
            default:
                // XXX - I really do not care enough to implement the rest right now.
                break;
        }

        return null;
    }

}
