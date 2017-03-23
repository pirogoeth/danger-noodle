package havabol.parse;

import havabol.Token;

import java.util.*;

public class Declaration implements Validate {

    private DataType dt = null;
    private Identifier ident = null;

    public Declaration(DataType dt, Identifier ident) {
        this.dt = dt;
        this.ident = ident;
    }

    public boolean isValid() {
        return dt != null && ident != null;
    }

    public void print() {
        System.out.println("Declaration ~>");
        this.dt.print();
        this.ident.print();
    }

}
