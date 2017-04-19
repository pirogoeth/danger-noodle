package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

import java.util.*;

public class Identifier implements ParseElement {

    private Token identToken;
    private Subscript subscript;

    public Identifier(Token id) {
        this.identToken = id;
    }

    public Identifier(Token id, Subscript sub) {
        this(id);

        this.subscript = sub;
    }

    public Token getIdentT() {
        return this.identToken;
    }

    public Subscript getSubscript() {
        return this.subscript;
    }

    public boolean isValid() {
        return ( this.subscript != null && this.subscript.isValid() ) || true;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent,
            String.format(
                "Identifier ~> `%s`\n",
                this.identToken.tokenStr
            )
        ));

        sb.append(lpads(
            indent + 2,
            String.format(
                "TOKEN :: %s\n",
                this.identToken.getDebugInfo()
            )
        ));

        if ( this.subscript != null ) {
            sb.append(this.subscript.debug(indent + 2));
        }

        return sb.toString();
    }

}
