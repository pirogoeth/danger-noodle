package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

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

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Declaration ~>\n"));
        sb.append(this.dt.debug(indent + 2));
        sb.append(this.ident.debug(indent + 2));

        return sb.toString();
    }

}
