package havabol.sym;

import havabol.classify.Primary;
import havabol.classify.Subclass;

/**
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public class STControl extends STEntry {

    /**
     * Symbol-type subclass.
     *
     * @var Subclass
     */
    private Subclass subClass = Subclass.UNKNOWN;

    public STControl(String sym, Subclass subClass)
    {
        super(sym, Primary.CONTROL);

        this.subClass = subClass;
    }

    public Subclass getSubClass() {
        return this.subClass;
    }
}
