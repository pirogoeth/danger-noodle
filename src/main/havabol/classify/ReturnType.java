package havabol.classify;

import havabol.common.type.*;
import havabol.builtins.types.*;

/**
 * Represents a subset of Subclass classifications that can be used
 * to denote the return type of a function or expression.
 */
public enum ReturnType {

    // Array return type should actually have a different Subclass depending
    // on underlying backed element type.
    ARRAY(null),

    // Normal primitives.
    INTEGER(Subclass.INTEGER),
    FLOAT(Subclass.FLOAT),
    BOOLEAN(Subclass.BOOLEAN),
    STRING(Subclass.STRING),

    // No return!
    VOID(Subclass.VOID);

    private Subclass sub;

    ReturnType(Subclass sub) {
        this.sub = sub;
    }

    public Subclass getSubclass() {
        return this.sub;
    }

    public int getSubCid() {
        return this.sub.getCid();
    }

    public TypeInterface newInstance() {
        switch (this) {
            case INTEGER:
                return new PInteger();
            case FLOAT:
                return new PFloat();
            case STRING:
                return new PString();
            case BOOLEAN:
                return new PBoolean();
            case ARRAY:
                return new ArrayType();
            case VOID:
            default:
                return null;
        }
    }

}
