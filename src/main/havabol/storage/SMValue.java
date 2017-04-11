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

    public ReturnType getContainerType() {
        return this.retType;
    }

    // COERCION

    public PInteger coerceInt() {
        switch (this.value.getFormalType()) {
            case STRING:
                return intPrim(stringAsInt(this.value.getValue()));
            case FLOAT:
                return intPrim((PInteger) this.value);
            case INTEGER:
                return this.value;
            case BOOLEAN:
                return intPrim((PBoolean) this.value);
            default:
                // Needs to throw a CoercionException or some shit.
                break;
        }

        return null;
    }

    public PString coerceString() {
        return (PString) this.value.getRepr();
    }

    public PFloat coerceFloat() {
        switch (this.value.getFormalType()) {
            case STRING:
                return floatPrim(stringAsInt(this.value.getValue()));
            case FLOAT:
                return this.value;
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
                return this.value;
            default:
                // I really do not care enough to implement the rest right now.
                break;
        }

        return null;
    }

}
