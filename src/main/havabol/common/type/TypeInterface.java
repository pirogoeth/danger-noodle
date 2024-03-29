package havabol.common.type;

import havabol.classify.*;
import havabol.common.*;

public interface TypeInterface<T> extends Debuggable {

    ReturnType getFormalType();

    void setValue(T val);
    T getValue();

    String getRepr();
    TypeInterface<T> clone();
    boolean isEqual(Object o);

    boolean isIterable();

    boolean coercibleTo(ReturnType target);
    TypeInterface coerceTo(ReturnType target);

}
