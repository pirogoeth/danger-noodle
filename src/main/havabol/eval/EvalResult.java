package havabol.eval;

import havabol.*;
import havabol.classify.*;
import havabol.common.*;
import havabol.common.type.*;
import havabol.parse.*;
import havabol.storage.*;
import havabol.sym.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class EvalResult implements Debuggable {

    private ReturnType resultType;

    private Statement srcStmt;
    private Expression srcExpr;

    private TypeInterface retVal;

    public EvalResult(ReturnType rtype) {
        this.resultType = rtype;
    }

    public ReturnType getResultType() {
        return this.resultType;
    }

    public TypeInterface getResult() {
        return this.retVal;
    }

    public void setSource(Statement src) {
        this.srcStmt = src;
    }

    public void setSource(Expression src) {
        this.srcExpr = src;
    }

    public void setReturn(TypeInterface ret) {
        this.retVal = ret;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Evaluation =>\n"));
        if ( this.srcStmt != null ) {
            sb.append(lpads(indent, "STATEMENT ::\n"));
            sb.append(this.srcStmt.debug(indent + 2));
        } else if ( this.srcExpr != null ) {
            sb.append(lpads(indent, "EXPRESSION ::\n"));
            sb.append(this.srcExpr.debug(indent + 2));
        }

        if ( this.resultType != ReturnType.VOID ) {
            sb.append(lpads(indent, "RETURNS ::\n"));
            sb.append(this.retVal.debug(indent + 2));
        } else {
            sb.append(lpads(indent, ":: VOID ::\n"));
        }

        return sb.toString();
    }

}
