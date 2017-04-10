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

    public boolean isValid() {
        return ( this.subscript != null && this.subscript.isValid() ) || true;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        if ( this.subscript != null ) {
            sb.append(lpads(indent,
                String.format(
                    "Identifier ~> `%s`\n",
                    this.identToken.tokenStr
                )
            ));
            sb.append(this.subscript.debug(indent));
        } else {
            sb.append(lpads(
                indent,
                String.format(
                    "Identifier ~> `%s`\n",
                    this.identToken.tokenStr
                )
            ));
        }

        return sb.toString();
    }

}
