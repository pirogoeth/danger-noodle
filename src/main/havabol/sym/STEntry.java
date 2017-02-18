package havabol.sym;

import havabol.classify.Primary;
import havabol.classify.Subclass;

/**
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public class STEntry {

    /**
     * String representing the symbol.
     *
     * @var String
     */
    protected String symbol;

    /**
     * Primary symbol-type class.
     *
     * @var Primary
     */
    protected Primary primaryClass = Primary.UNKNOWN;

    /**
     * Symbol-type subclass.
     * Also known as `definedBy` - either Subclass.USER or Subclass.BUILTIN
     *
     * @var Subclass
     */
    private Subclass subClass = Subclass.UNKNOWN;

    public STEntry(String token, Primary primClass)
    {
        this.symbol = token;
        this.primaryClass = primClass;
    }

    public STEntry(String token, Primary primClass, Subclass subClass)
    {
        this(token, primClass);

        this.subClass = subClass;
    }

    public String getSymbol()
    {
        return this.symbol;
    }

    public Primary getPrimaryClass()
    {
        return this.primaryClass;
    }

    public Subclass getSubClass()
    {
        return this.subClass;
    }
}
