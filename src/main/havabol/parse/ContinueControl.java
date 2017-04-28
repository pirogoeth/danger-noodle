package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

public class ContinueControl extends ParseElement {

    private Token token;

    public ContinueControl(Token t) {
        this.token = t;
    }

    public boolean isValid() {
        return true;
    }

    public String debug(int indent) {
        return lpads(
            indent,
            String.format(
                "FLOW >> Continue - %s\n",
                this.token.getDebugInfo()
            )
        );
    }

}
