package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

import java.util.*;

public class Declaration implements ParseElement {

    private DataType dt = null;
    private Identifier ident = null;

    private boolean isArray = false;
    private int arrayBound = -1;

    public Declaration(DataType dt, Identifier ident) {
        this.dt = dt;
        this.ident = ident;
    }

    public Declaration(DataType dt, Identifier ident, boolean isAry) {
        this(dt, ident, isAry, -1);
    }

    public Declaration(DataType dt, Identifier ident, boolean isAry, int aryBound) {
        this(dt, ident);

        this.isArray = isAry;
        this.arrayBound = aryBound;
    }

    public boolean isArray() {
        return this.isArray;
    }

    public boolean isUnboundedArray() {
        return this.arrayBound == -1;
    }

    public int getArrayBound() {
        return this.arrayBound;
    }

    public DataType getDataType() {
        return this.dt;
    }

    public Identifier getIdentifier() {
        return this.ident;
    }

    public boolean isValid() {
        return dt != null && ident != null;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        if ( this.isArray() ) {
            if ( this.isUnboundedArray() ) {
                sb.append(lpads(indent, "Unbounded array declaration ~>\n"));
            } else {
                sb.append(lpads(indent, String.format("Array declaration (size %d) ~>\n", this.getArrayBound())));
            }
        } else {
            sb.append(lpads(indent, "Declaration ~>\n"));
        }

        sb.append(this.dt.debug(indent + 2));
        sb.append(this.ident.debug(indent + 2));

        return sb.toString();
    }

}
