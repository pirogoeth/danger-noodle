package havabol.parse;

import havabol.Token;

import java.util.*;

public class Declaration implements ParseElement {

    private DataType dt = null;
    private Identifier ident = null;

    public Declaration(DataType dt, Identifier ident) {
        this.dt = dt;
        this.ident = ident;
    }

    public boolean isValid() {
        return dt != null && ident != null;
    }

    public String debug() {
        StringBuilder sb = new StringBuilder();

        sb.append("Declaration ~>\n");
        sb.append("  " + this.dt.debug());
        sb.append("  " + this.ident.debug());

        return sb.toString();
    }

}
