package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class SelectControl extends ParseElement {

    private Expression condition;

    public boolean isValid() {
        return true;
    }

    public String debug(int indent) {
        return "Control ~> not implemented";
    }

}
