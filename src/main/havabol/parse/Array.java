package havabol.parse;

import havabol.Token;
import havabol.builtins.types.*;
import havabol.classify.*;
import havabol.eval.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class Array extends ParseElement {

    private List<Primitive> elements;

    public Array() {
        this.elements = new ArrayList<>();
    }

    public Array(List<Primitive> prims) {
        this.elements = prims;
    }

    public EvalResult getEvaluable() throws Exception, ParserException {
        EvalResult res = new EvalResult(ReturnType.ARRAY);

        ArrayType ary = new ArrayType();
        ary.initialize(this.elements.size());
        for (Primitive p : this.elements) {
            ary.append(p.getEvaluable().getResult());
        }

        res.setResult(ary);
        return res;
    }

    public void addItem(Primitive pr) {
        this.elements.add(pr);
    }

    public boolean isValid() {
        for (Primitive prim : this.elements) {
            if ( ! prim.isValid() ) {
                return false;
            }
        }

        return true;
    }

    public String debug(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(lpads(indent, "Array ~>\n"));

        for (Primitive element : this.elements) {
            sb.append(element.debug(indent + 2));
        }

        return sb.toString();
    }
}
