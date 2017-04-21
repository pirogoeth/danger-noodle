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
    private int length = 0;

    private static int wrapIndex(int size, int index) {
        return (size - (abs(index) % size));
    }

    public ReturnType getBoundType() {
        return this.boundType;
    }

    public int getLength(){
        return this.length;
    }
    
    // XXX - NEEDS NARROWER EXCEPTION TYPES
    public void setBoundType(ReturnType ret) throws Exception {
        if (this.boundType != null) {
            // XXX - Needs a narrow exception type of its own.
            throw new Exception("Can not overwrite already bound type of array!");
        }

        this.boundType = ret;
    }

    public int getCapacity() {
        return this.maxCap;
    }

    public boolean isBounded() {
        return this.isBounded;
    }

    public ReturnType getFormalType() {
        return ReturnType.ARRAY;
    }

    private void fillWithValue(TypeInterface typeIf) {
        this.value.clear();

        for (int i = 0; i < this.maxCap; i++) {
            if (typeIf != null) {
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

    public String debug() {
        return this.debug(0);
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Array Type =>\n"));
        sb.append(lpads(indent,
                String.format(
                        "CAPACITY :: %d\n",
                        this.maxCap
                )
        ));
        if (this.getBoundType() != null) {
            sb.append(lpads(indent,
                    String.format(
                            "BOUND TYPE :: %s\n",
                            this.getBoundType().name()
                    )
            ));
        }
        sb.append(lpads(indent + 2, "Items =>\n"));

        for (TypeInterface tVal : this.value) {
            if (tVal != null) {
                sb.append(tVal.debug(indent + 4));
            } else {
                sb.append(lpads(indent + 4, ":: NULL ::\n"));
            }
        }

        return sb.toString();
    }

    public String getRepr() {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for (int i = 0; i < this.value.size(); i++) {
            sb.append(this.value.get(i).getRepr());
            if (i < this.value.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }

    public boolean isIterable() {
        return true;
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

    public void setUnbounded() {
        this.isBounded = false;
    }

    public void setBounded() {
        this.isBounded = true;
    }

    public boolean isEqual(Object o) {
        return this.value.equals(o);
    }

    /*
     * ARRAY DATA METHODS
     */
    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    //append will always enter value at the end of the array list
    public EvalResult append(TypeInterface item) throws Exception {
        int freeIdx = this.length;

        //code for bounded
        if (this.isBounded) {

            //index is not at the end
            if (freeIdx < this.maxCap) {
                this.set(freeIdx, item);
                this.length++;
                //index is end of the lsit
            } else {
                throw new Exception(
                        String.format(
                                "Atempting to set elemnt out of bounds of array - index = %d maxbound = %d",
                                freeIdx,
                                this.maxCap
                        )
                );
            }

            //code for unbounded
        } else {
            //if the length is less than max, insert it into the null spot
            if (freeIdx < this.maxCap) {
                this.set(freeIdx, item);
                this.length++;

                //if index is maxCap or greater, add it to the array list and increase the maxcap
                // possible since array is unbounded
            } else {
                this.value.add(item);
                this.length++;
                this.maxCap++;

            }
        }
        //this.set(freeIdx, item);

        return new EvalResult(ReturnType.VOID);
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    // XXX - NEEDS REWORKING
    public EvalResult set(int index, TypeInterface item) throws Exception {
        if (index < 0) {
            index = wrapIndex(this.maxCap, index);
        }

        if (this.getBoundType() != null && item.getFormalType() != this.getBoundType()) {
            // XXX - Throw an error here because arrays must be homogenous.
            throw new Exception(
                    String.format(
                            "Array must contain all of same type - expected %s, got %s",
                            this.getBoundType().name(),
                            this.getFormalType().name()
                    )
            );
        }

        //code for bounded arrays
        if (this.isBounded) {
            if (index < this.maxCap) {
                this.value.set(index, item);
            } else {
                throw new Exception(
                        String.format(
                                "Atempting to add elemnt out of bounds of array - index = %d maxbound = %d",
                                index,
                                this.maxCap
                        )
                );
            }

            //code for unbounded arrays
        } else {
            if (index < this.maxCap) {
                this.value.set(index, item);
            } else {
                //CHeck this line for correctness
                for (int i = this.maxCap; i < index; i++) {
                    this.append(null);
                }
                this.value.set(index, item);
                this.maxCap = index + 1;
            }

        }

        return new EvalResult(ReturnType.VOID);
    }

    //This is the old set, left it incase we need a spot for an insert method
    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    /*
    public EvalResult set(int index, TypeInterface item) throws Exception {
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

        EvalResult res = new EvalResult(this.getBoundType());
        res.setResult(item);
        return res;
    }
     */
    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult get(int index) throws Exception {
        if (index < 0) {
            index = wrapIndex(this.maxCap, index);
        }

        if (index >= this.maxCap) {
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
        if (val == null) {
            // XXX - Should we throw an exception here???
            throw new Exception(
                    String.format(
                            "Array is empty at position %d",
                            index
                    )
            );
        }

        EvalResult res = new EvalResult(this.getBoundType());
        res.setResult(val);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult getUnsafe(int index) throws Exception {
        if (index < 0) {
            index = wrapIndex(this.maxCap, index);
        }

        if (index >= this.maxCap) {
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
        if (val == null) {
            return null;
        }

        EvalResult res = new EvalResult(this.getBoundType());
        res.setResult(val);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult remove(int index) throws Exception {
        if (index < 0) {
            index = wrapIndex(this.maxCap, index);
        }

        if (index >= this.maxCap) {
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
        res.setResult(val);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult getSlice(int beginIndex, int endIndex) throws Exception {

        if (beginIndex < endIndex) {
            final List<TypeInterface> tmpL = this.value.subList(beginIndex, endIndex);

            ArrayType slice = new ArrayType();
            slice.initialize(tmpL.size());
            slice.setBoundType(this.getBoundType());
            slice.setValue(new ArrayList<TypeInterface>(tmpL));

            EvalResult res = new EvalResult(this.getFormalType());
            res.setResult(slice);
            return res;
        } else {
            final List<TypeInterface> tmpL1 = this.value.subList(0, beginIndex);
            final List<TypeInterface> tmpL2 = this.value.subList(endIndex, this.value.size() - 1);
            ArrayType slice = new ArrayType();
            slice.setUnbounded();

            slice.initialize(tmpL2.size());
            slice.setBoundType(this.getBoundType());

            for (int i = 0; i < tmpL2.size(); i++) {
                slice.set(i, tmpL2.get(i));
            }
            for (int i = 0; i < tmpL1.size(); i++) {
                slice.append(tmpL1.get(i));
            }

            EvalResult res = new EvalResult(this.getFormalType());
            res.setResult(slice);
            return res;
        }
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult setSlice(int beginIndex, int endIndex, ArrayType slice) throws Exception {
        // XXX - IMPLEMENT!
        //

        EvalResult res = new EvalResult(ReturnType.VOID);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult setSliceScalar(int beginIndex, int endIndex, TypeInterface item) throws Exception {
        // XXX - IMPLEMENT!
        //

        EvalResult res = new EvalResult(ReturnType.VOID);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult setFromArray(ArrayType ary) throws Exception {
        if (this.getBoundType() != ary.getBoundType()) {
            // XXX - Throw an error here because arrays must be homogenous.

            switch (this.getBoundType()) {

                case STRING:
                    copyAllToString(ary);
                    break;

                case INTEGER:

                    switch (ary.getBoundType()) {

                        case FLOAT:
                            copyFloatToInt(ary);
                        case STRING:
                            copyStringToInt(ary);
                        default:
                            throw new Exception(
                                    String.format(
                                            "Can not set int array eqaul to boolean array. THis type = `%s` other arry = got `%s`",
                                            this.getBoundType().name(),
                                            ary.getBoundType().name()
                                    )
                            );
                    }

                case FLOAT:
                    switch (ary.getBoundType()) {

                        case INTEGER:
                            copyIntToFLoat(ary);
                        case STRING:
                            copyStringToFloat(ary);
                        default:
                            throw new Exception(
                                    String.format(
                                            "Can not set float array eqaul to boolean array. THis type = `%s` other arry = got `%s`",
                                            this.getBoundType().name(),
                                            ary.getBoundType().name()
                                    )
                            );
                    }

                case BOOLEAN:

                    switch (ary.getBoundType()) {

                        case STRING:
                            copyStringToBoolean(ary);
                        default:
                            throw new Exception(
                                    String.format(
                                            "Incompatable array types. This type = `%s` other arry = got `%s`",
                                            this.getBoundType().name(),
                                            ary.getBoundType().name()
                                    )
                            );
                    }

            }

        }

        if (ary.getCapacity() > this.getCapacity()) {
            ArrayList<TypeInterface> valSlice = new ArrayList<>(ary.getValue().subList(0, this.getCapacity() - 1));
            this.setValue(valSlice);
        } else if (ary.getCapacity() == this.getCapacity()) {
            this.setValue(ary.getValue());
        } else { // ary cap < this cap
            ArrayList<TypeInterface> newL = new ArrayList<>();
            newL.addAll(ary.getValue());

            // Extend newL to the current capacity
            while (newL.size() < this.getCapacity()) {
                newL.add(null);
            }

            this.setValue(newL);
        }

        EvalResult res = new EvalResult(this.getFormalType());
        res.setResult(this);
        return res;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult setFromScalar(TypeInterface item) throws Exception {
        if (this.getBoundType() != null && item.getFormalType() != this.getBoundType()) {
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
        res.setResult(this);
        return res;
    }

    private void copyAllToString(ArrayType ary) throws Exception {
        if (this.maxCap < ary.maxCap) {

            //make array size equal if smaller arry is bounded, allows insert
            if (!this.isBounded) {
                this.maxCap = ary.maxCap;
            }

            for (int i = 0; i < this.maxCap; i++) {
                PString temp = new PString();
                temp.setValue(ary.get(i).getResult().getRepr());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }

        } else {
            int i;
            //cpy items fro ary
            for (i = 0; i < ary.maxCap; i++) {
                PString temp = new PString();
                temp.setValue(ary.get(i).getResult().getRepr());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }
            this.length = ary.maxCap;

            //set the rest of th earray null
            for (int temp = i; temp < this.maxCap; temp++) {
                this.set(temp, null);
            }

        }
    }

    private void copyFloatToInt(ArrayType ary) throws Exception {
        if (this.maxCap < ary.maxCap) {

            //make array size equal if smaller arry is bounded, allows insert
            if (!this.isBounded) {
                this.maxCap = ary.maxCap;
            }
            for (int i = 0; i < this.maxCap; i++) {
                PInteger temp;
                temp = (PInteger) Numerics.intPrim((PFloat) ary.get(i).getResult());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }

        } else {
            int i;

            for (i = 0; i < ary.maxCap; i++) {

                PInteger temp;
                temp = (PInteger) Numerics.intPrim((PFloat) ary.get(i).getResult());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }
            this.length = ary.maxCap;

            //set the rest of th earray null
            for (int temp = i; temp < this.maxCap; temp++) {
                this.set(temp, null);
            }

        }
    }

    private void copyStringToInt(ArrayType ary) throws Exception {
        if (this.maxCap < ary.maxCap) {

            //make array size equal if smaller arry is bounded, allows insert
            if (!this.isBounded) {
                this.maxCap = ary.maxCap;
            }

            for (int i = 0; i < this.maxCap; i++) {
                PInteger temp;
                temp = (PInteger) Numerics.intPrim((PString) ary.get(i).getResult());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }

        } else {
            int i;

            for (i = 0; i < ary.maxCap; i++) {

                PInteger temp;
                temp = (PInteger) Numerics.intPrim((PString) ary.get(i).getResult());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }
            this.length = ary.maxCap;

            //set the rest of th earray null
            for (int temp = i; temp < this.maxCap; temp++) {
                this.set(temp, null);
            }

        }

    }

    private void copyStringToFloat(ArrayType ary) throws Exception {
        if (this.maxCap < ary.maxCap) {

            //make array size equal if smaller arry is bounded, allows insert
            if (!this.isBounded) {
                this.maxCap = ary.maxCap;
            }
            for (int i = 0; i < this.maxCap; i++) {
                PFloat temp;
                temp = (PFloat) Numerics.floatPrim((PString) ary.get(i).getResult());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }

        } else {
            int i;

            for (i = 0; i < ary.maxCap; i++) {

                PFloat temp;
                temp = (PFloat) Numerics.floatPrim((PString) ary.get(i).getResult());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }
            this.length = ary.maxCap;

            //set the rest of th earray null
            for (int temp = i; temp < this.maxCap; temp++) {
                this.set(temp, null);
            }

        }

    }

    private void copyIntToFLoat(ArrayType ary) throws Exception {
        if (this.maxCap < ary.maxCap) {

            //make array size equal if smaller arry is bounded, allows insert
            if (!this.isBounded) {
                this.maxCap = ary.maxCap;
            }
            for (int i = 0; i < this.maxCap; i++) {
                PFloat temp;
                temp = (PFloat) Numerics.floatPrim((PInteger) ary.get(i).getResult());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }

        } else {
            int i;

            for (i = 0; i < ary.maxCap; i++) {

                PFloat temp;
                temp = (PFloat) Numerics.floatPrim((PInteger) ary.get(i).getResult());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }
            this.length = ary.maxCap;

            //set the rest of th earray null
            for (int temp = i; temp < this.maxCap; temp++) {
                this.set(temp, null);
            }

        }
    }

    private void copyStringToBoolean(ArrayType ary) throws Exception {
        if (this.maxCap < ary.maxCap) {
            //make array size equal if smaller arry is bounded, allows insert
            if (!this.isBounded) {
                this.maxCap = ary.maxCap;
            }
            for (int i = 0; i < this.maxCap; i++) {
                PBoolean temp;
                temp = (PBoolean) Numerics.boolPrim((PString) ary.get(i).getResult());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }

        } else {
            int i;

            for (i = 0; i < ary.maxCap; i++) {

                PBoolean temp;
                temp = (PBoolean) Numerics.boolPrim((PString) ary.get(i).getResult());
                this.set(i, temp);
                if (ary.get(i) != null) {
                    this.length = i;
                }
            }
            this.length = ary.maxCap;

            //set the rest of th earray null
            for (int temp = i; temp < this.maxCap; temp++) {
                this.set(temp, null);
            }

        }

    }

}
