package havabol.parse;

import havabol.Token;
import havabol.common.*;

import java.util.*;

public abstract class ParseElement implements Debuggable {

    public abstract boolean isValid();
    public abstract String debug(int indent);

    public String debug() {
        return this.debug(0);
    }

}
