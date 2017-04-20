package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

import java.util.*;

public class Declaration extends ParseElement {

    private DataType dt = null;
    private Identifier ident = null;

    private boolean isArray;
    private Expression arrayBound;

    public Declaration(DataType dt, Identifier ident) {
        this(dt, ident, false, null);
    }

    public Declaration(DataType dt, Identifier ident, boolean isAry) {
        this(dt, ident, isAry, null);
    }

    public Declaration(DataType dt, Identifier ident, boolean isAry, Expression aryBound) {
        this.dt = dt;
        this.ident = ident;
        this.isArray = isAry;
        this.arrayBound = aryBound;
    }

    public boolean isArray() {
        return this.isArray;
    }

    public boolean isUnboundedArray() {
        return this.arrayBound == null;
    }

    public Expression getArrayBound() {
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
                sb.append(lpads(indent, String.format("Array declaration ~>\n")));
                sb.append(lpads(indent + 2, String.format("BOUNDS ::\n")));
                sb.append(this.arrayBound.debug(indent + 4));
            }
        } else {
            sb.append(lpads(indent, "Declaration ~>\n"));
        }

        sb.append(this.dt.debug(indent + 2));
        sb.append(this.ident.debug(indent + 2));

        return sb.toString();
    }

}
