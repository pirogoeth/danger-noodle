package havabol.eval;

import havabol.classify.*;
import havabol.parse.*;
import havabol.storage.*;
import havabol.sym.*;
import havabol.util.*;

import java.util.*;

public class Evaluator {

    private List<Statement> stmtList;

    public Evaluator(List<Statement> stmts) {
        this.stmtList = stmts;
    }

    public boolean canEval() {
        return ( this.stmtList != null && ! this.stmtList.isEmpty() );
    }

}
