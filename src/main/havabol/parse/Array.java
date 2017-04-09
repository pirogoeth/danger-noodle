package havabol.parse;

import havabol.Token;
import havabol.classify.*;
import static havabol.util.Text.*;

import java.util.*;
import java.util.stream.*;

public class Array implements ParseElement {

    private List<Primitive> elements;

    public Array() {
        this.elements = new ArrayList<>();
    }

    public Array(List<Primitive> prims) {
        this.elements = prims;
    }

    public Stream<Primitive> stream() {
        return this.elements.stream();
    }

    public void addItem(Primitive pr) {
        this.elements.add(pr);
    }

    public boolean isValid() {
        return this.elements.stream()
            .map(prim -> prim.isValid())
            .reduce(true, (a, b) -> a && b);
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
