package havabol.storage;

import havabol.*;
import havabol.classify.*;
import havabol.sym.*;

public class SMValue {

    private STIdentifier symbol = null;
    private Object value = null;

    SMValue(STIdentifier ident)
    {
        this.symbol = ident;
    }

    public Object getRaw() {
        return this.value;
    }

    public int getInt() {
        return (int) this.value;
    }

    public String getString() {
        return (String) this.value;
    }

    public float getFloat() {
        return (float) this.value;
    }

    public boolean getBool() {
        return (boolean) this.value;
    }

    public void set(Object o) {
        this.value = o;
    }

}
