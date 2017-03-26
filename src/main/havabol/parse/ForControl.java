package havabol.parse;

import havabol.Token;
import havabol.classify.*;

import java.util.*;

public class ForControl implements ParseElement {

    private Expression condition;

    public boolean isValid() {
        return true;
    }

    public String debug() {
        return "Control ~> not implemented";
    }

}
