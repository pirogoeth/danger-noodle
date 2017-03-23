package havabol.parse;

import havabol.Token;

import java.util.*;

public class Identifier implements Validate {

    private Token identToken;

    public Identifier(Token id) {
        this.identToken = id;
    }

    public boolean isValid() {
        return true;
    }

    public void print() {
        System.out.println(
                String.format(
                    "  Identifier ~> `%s`",
                    this.identToken.tokenStr
                )
        );
    }

}
