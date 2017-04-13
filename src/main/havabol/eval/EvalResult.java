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

    public static class EvalSubscript {

        public enum Type {
            SINGLE,
            RANGE;
        }

        public final int beginIdx, endIdx;
        public final Type type;

        public EvalSubscript(int b) {
            this.type = Type.SINGLE;
            this.beginIdx = b;
            this.endIdx = -1;
        }

        public EvalSubscript(int b, int e) {
            this.type = Type.RANGE;
            this.beginIdx = b;
            this.endIdx = e;
        }
    }

    private ReturnType resultType;

    private Statement srcStmt;
    private Expression srcExpr;

    private TypeInterface retVal;
    private STIdentifier resultIdentifier;
    private EvalSubscript subscript = null;

    public EvalResult(ReturnType rtype) {
        this.resultType = rtype;
    }

    public boolean isSubscripted() {
        return this.subscript != null;
    }

    public EvalSubscript getSubscript() {
        return this.subscript;
    }

    public ReturnType getResultType() {
        return this.resultType;
    }

    public TypeInterface getResult() {
        return this.retVal;
    }

    public STIdentifier getResultIdent() {
        return this.resultIdentifier;
    }

    public void setSource(Statement src) {
        this.srcStmt = src;
    }

    public void setSource(Expression src) {
        this.srcExpr = src;
    }

    public void setResult(TypeInterface ret) {
        this.retVal = ret;
    }

    public void setResultIdent(STIdentifier ident) {
        this.resultIdentifier = ident;
    }

    public void unsetSubscript() {
        this.subscript = null;
    }

    public void setSubscript(int b) {
        this.subscript = new EvalSubscript(b);
    }

    public void setSubscript(int b, int e) {
        this.subscript = new EvalSubscript(b, e);
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
            if ( this.retVal != null ) {
                sb.append(this.retVal.debug(indent + 2));
            } else {
                sb.append(lpads(indent, ":: NULL ::\n"));
            }
        } else {
            sb.append(lpads(indent, ":: VOID ::\n"));
        }

        return sb.toString();
    }

}
