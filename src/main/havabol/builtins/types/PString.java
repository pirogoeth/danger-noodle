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

public class PString implements TypeInterface<String> {

    private String value = "";

    private static int wrapIndex(int size, int index) {
        return (size - (abs(index) % size));
    }

    public ReturnType getFormalType() {
        return ReturnType.STRING;
    }

    public void setValue(String s) {
        this.value = s;
    }

    public String getValue() {
        return this.value;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Primitive type String =>\n"));
        sb.append(lpads(indent + 2, String.format("Value ==> `%s`\n", this.value)));

        return sb.toString();
    }

    public String getRepr() {
        return this.value;
    }

    public boolean isEqual(Object o) {
        return this.value.equals(o);
    }

    public boolean isIterable() {
        return true;
    }

    public TypeInterface<String> clone() {
        PString s = new PString();
        s.setValue(this.getValue());
        return s;
    }

    // XXX - THIS NEEDS NARROWER EXCEPTION TYPES
    public EvalResult get(int index) throws Exception {
        if ( index < 0 ) {
            index = wrapIndex(this.value.length(), index);
        }

        if ( index >= this.value.length() ) {
            // XXX - Throw an error here because strings have a max index of length() - 1
            throw new Exception(
                String.format(
                    "String index out of bounds - max %d, got %d",
                    this.value.length() - 1,
                    index
                )
            );
        }

        String val = String.valueOf(this.value.charAt(index));
        if ( val == null ) {
            // XXX - Should we throw an exception here???
            throw new Exception(
                String.format(
                    "String is empty at position %d",
                    index
                )
            );
        }

        EvalResult res = new EvalResult(this.getFormalType());
        res.setResult(Numerics.strPrim(val));
        return res;
    }

    public EvalResult set(int index, PString strVal) throws Exception {
        if ( index < 0 ) {
            index = wrapIndex(this.value.length(), index);
        }

        if ( index >= this.value.length() ) {
            // XXX - Throw an error here because strings have a max index of length() - 1
            throw new Exception(
                String.format(
                    "String index out of bounds - max %d, got %d",
                    this.value.length() - 1,
                    index
                )
            );
        }

        String p1, p2, pn;

        p1 = this.value.substring(0, index);
        p2 = this.value.substring(index + 1);
        pn = strVal.getValue();

        String newVal = p1 + pn + p2;
        this.setValue(newVal);

        EvalResult res = new EvalResult(this.getFormalType());
        res.setResult(this);
        return res;
    }

    public EvalResult getSlice(int beginIdx, int endIdx) throws Exception {
        if ( beginIdx == endIdx ) {
            return this.get(beginIdx);
        }

        if ( beginIdx < 0 ) {
            beginIdx = wrapIndex(this.value.length(), beginIdx);
        }

        if ( endIdx < 0 ) {
            endIdx = wrapIndex(this.value.length(), endIdx);
        }

        if ( beginIdx >= this.value.length() || endIdx >= this.value.length() ) {
            // XXX - Throw an error here because strings have a max index of length() - 1
            throw new Exception(
                String.format(
                    "String index out of bounds - max %d, got [%d:%s]",
                    this.value.length() - 1,
                    beginIdx,
                    endIdx
                )
            );
        }

        String sel = this.value.substring(beginIdx, endIdx);
        EvalResult res = new EvalResult(this.getFormalType());
        res.setResult(Numerics.strPrim(sel));
        return res;
    }

}
