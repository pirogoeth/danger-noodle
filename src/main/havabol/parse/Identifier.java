package havabol.parse;

import havabol.Token;

import java.util.*;

public class Identifier implements ParseElement {

    private Token identToken;

    public Identifier(Token id) {
        this.identToken = id;
    }

    public boolean isValid() {
        return true;
    }

    public String debug() {
        return String.format("Identifier ~> `%s`\n", this.identToken.tokenStr);
    }

}
