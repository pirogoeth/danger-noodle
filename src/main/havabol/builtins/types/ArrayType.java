package havabol.builtins.types;

import havabol.Token;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.type.*;
import havabol.eval.*;
import havabol.util.*;
import static havabol.util.Text.*;

import static java.lang.Math.abs;
import java.util.*;

public class ArrayType implements TypeInterface<ArrayList<TypeInterface>> {

    private ArrayList<TypeInterface> value;
    private ReturnType boundType;
    private boolean isBounded = true;
    private int maxCap = -1;

    private static int wrapIndex(int size, int index) {
        return (size - (abs(index) % size));
    }

    public ReturnType getBoundType() {
        return this.boundType;
    }

    // XXX - NEEDS NARROWER EXCEPTION TYPES
    public void setBoundType(ReturnType ret) throws Exception {
        if ( this.boundType != null ) {
            // XXX - Needs a narrow exception type of its own.
            throw new Exception("Can not overwrite already bound type of array!");
        }

        this.boundType = ret;
    }

    public ReturnType getFormalType() {
        return ReturnType.ARRAY;
    }

    private void fillWithValue(TypeInterface typeIf) {
        this.value.clear();

        for (int i = 0 ; i < this.maxCap ; i++) {
            if ( typeIf != null ) {
                this.value.add(0, typeIf.clone());
            } else {
                this.value.add(0, typeIf);
            }
        }
    }

    /**
     * Sets up the underlying array and limits.
     */
    public void initialize(int capacity) {
        this.isBounded = true;
        this.maxCap = capacity;

        this.value = new ArrayList<>(this.maxCap);
        this.fillWithValue(null);
    }

    public void setValue(ArrayList<TypeInterface> l) {
        this.value = l;
    }

    public ArrayList<TypeInterface> getValue() {
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
        sb.append("]");

        return sb.toString();
    }

    // XXX - NEEDS NARROWER EXCEPTION TYPES
    public TypeInterface<ArrayList<TypeInterface>> clone() {
        try {
            ArrayType a = new ArrayType();
            a.setBoundType(this.getBoundType());
            a.initialize(this.maxCap);

            for (TypeInterface item : this.value) {
                a.append(item.clone());
            }

            return a;
        } catch (Exception ex) {  // XXX TODO - NARROW THIS EXCEPTION DOWN!!!
            /*
             * Okay so realistically, this should NEVER happen. If we are cloning
             * one ArrayType as another, all of the elements should shift over nicely.
             * Let's figure out the real way of handling this later without forcing
             * all `clone()` in the `TypeInterface` to throw Exception.
             */
        }

        return null;  // XXX - NO! BAD!
    }

    /*
     * ARRAY DATA METHODS
     */

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult append(TypeInterface item) throws Exception {
        int freeIdx = this.value.indexOf(null);
        this.insert(freeIdx, item);

        return new EvalResult(ReturnType.VOID);
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult insert(int index, TypeInterface item) throws Exception {
        if ( index < 0 ) {
            index = wrapIndex(this.maxCap, index);
        }

        if ( this.getBoundType() != null && item.getFormalType() != this.getBoundType() ) {
            // XXX - Throw an error here because arrays must be homogenous.
            throw new Exception(
                String.format(
                    "Array must contain all of same type - expected %s, got %s",
                    this.getBoundType().name(),
                    this.getFormalType().name()
                )
            );
        }

        this.value.set(index, item);

        return new EvalResult(ReturnType.VOID);
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult get(int index) throws Exception {
        if ( index < 0 ) {
            index = wrapIndex(this.maxCap, index);
        }

        if ( index >= this.maxCap ) {
            // XXX - Throw an error here because arrays have a max index of capacity - 1.
            throw new Exception(
                String.format(
                    "Array index out of bounds - max %d, got %d",
                    this.maxCap - 1,
                    index
                )
            );
        }

        TypeInterface val = this.value.get(index);
        if ( val == null ) {
            // XXX - Should we throw an exception here???
            throw new Exception(
                String.format(
                    "Array is empty at position %d",
                    index
                )
            );
        }

        EvalResult res = new EvalResult(this.getBoundType());
        res.setReturn(val);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult remove(int index) throws Exception {
        if ( index < 0 ) {
            index = wrapIndex(this.maxCap, index);
        }

        if ( index >= this.maxCap ) {
            // XXX - Throw an error here because arrays have a max index of capacity - 1.
            throw new Exception(
                String.format(
                    "Array index out of bounds - max %d, got %d",
                    this.maxCap - 1,
                    index
                )
            );
        }

        TypeInterface val = this.value.remove(index);
        EvalResult res = new EvalResult(this.getBoundType());
        res.setReturn(val);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult getSlice(int beginIndex, int endIndex) throws Exception {
        final List<TypeInterface> tmpL = this.value.subList(beginIndex, endIndex);

        ArrayType slice = new ArrayType();
        slice.initialize(tmpL.size());
        slice.setBoundType(this.getBoundType());
        slice.setValue(new ArrayList<TypeInterface>(tmpL));

        EvalResult res = new EvalResult(this.getFormalType());
        res.setReturn(slice);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult setSlice(int beginIndex, int endIndex, ArrayType slice) throws Exception {
        // XXX - IMPLEMENT!
        //

        EvalResult res = new EvalResult(ReturnType.VOID);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult setFromArray(ArrayType ary) throws Exception {
        // XXX - IMPLEMENT!
        //

        EvalResult res = new EvalResult(ReturnType.VOID);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult setFromScalar(TypeInterface item) throws Exception {
        if ( this.getBoundType() != null && item.getFormalType() != this.getBoundType() ) {
            // XXX - Throw an error here because arrays must be homogenous.
            throw new Exception(
                String.format(
                    "Array must contain all of same type - expected %s, got %s",
                    this.getBoundType().name(),
                    this.getFormalType().name()
                )
            );
        }

        this.fillWithValue(item);

        EvalResult res = new EvalResult(this.getFormalType());
        res.setReturn(this);
        return res;
    }

}
