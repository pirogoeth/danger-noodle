package havabol.parse;

import havabol.Token;
import static havabol.util.Text.*;

public class BreakControl extends ParseElement {

    private Token token;

    public BreakControl(Token t) {
        this.token = t;
    }

    public boolean isValid() {
        return true;
    }

    public String debug(int indent) {
        return lpads(
            indent,
            String.format(
                "FLOW >> Break - %s\n",
                this.token.getDebugInfo()
            )
        );
    }

}

