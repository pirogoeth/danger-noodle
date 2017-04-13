package havabol.parse;

import havabol.Token;
import havabol.builtins.types.*;
import havabol.classify.*;
import havabol.common.type.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class ForExpr implements ParseElement {

    public enum Type {
        COUNTING,
        ITERATIVE;
    }

    // common
    private Identifier cVar;
    private Type type;

    // counting
    private Expression initial;
    private Expression max;
    private Expression step;

    // iterative
    private Expression container;

    public ForExpr(Identifier cVar, Expression container) {
        this.cVar = cVar;
        this.container = container;

        this.type = Type.ITERATIVE;
    }

    public ForExpr(Identifier cVar, Expression initial, Expression max, Expression step) {
        this.cVar = cVar;
        this.initial = initial;
        this.max = max;
        this.step = step;

        this.type = Type.COUNTING;
    }

    public Type getType() {
        return this.type;
    }

    public Identifier getControl() {
        return this.cVar;
    }

    public Expression getInitial() {
        return this.initial;
    }

    public Expression getMax() {
        return this.max;
    }

    public Expression getStep() {
        return this.step;
    }

    public Expression getContainer() {
        return this.container;
    }

    public boolean isValid() {
        return ( this.cVar != null && this.cVar.isValid() ) &&
               (
                 (
                   ( this.container != null && this.container.isValid() )
                 )
                 ||
                 (
                   ( this.initial != null && this.initial.isValid() ) &&
                   ( this.max != null && this.max.isValid() )
                 )
               );
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "For Cond =>\n"));
        sb.append(lpads(indent, ":: CONTROL VAR\n"));
        sb.append(this.cVar.debug(indent + 2));
        switch (this.type) {
            case ITERATIVE:
                sb.append(lpads(indent, ":: CONTAINER\n"));
                sb.append(this.container.debug(indent + 2));
                break;
            case COUNTING:
                sb.append(lpads(indent, ":: INITIAL\n"));
                sb.append(this.initial.debug(indent + 2));
                sb.append(lpads(indent, ":: MAXIMUM\n"));
                sb.append(this.max.debug(indent + 2));
                if ( this.step != null ) {
                    sb.append(lpads(indent, ":: STEPPING\n"));
                    sb.append(this.step.debug(indent + 2));
                }
                break;
            default:
                return "";
        }

        return sb.toString();
    }

}
