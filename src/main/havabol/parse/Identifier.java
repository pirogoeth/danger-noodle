package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

import java.util.*;

public class Identifier implements ParseElement {

    private Token identToken;

    public Identifier(Token id) {
        this.identToken = id;
    }

    public boolean isValid() {
        return true;
    }

    public String debug(int indent) {
        return lpads(
            indent,
            String.format(
                "Identifier ~> `%s`\n",
                this.identToken.tokenStr
            )
        );
    }

}
