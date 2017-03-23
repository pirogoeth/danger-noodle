package havabol.sym;

import havabol.Token;
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
     * Underlying Token instance.
     *
     * @var Token
     */
    protected Token token;

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

    public STEntry(Token t)
    {
        this.token = t;
        this.symbol = t.tokenStr;

        this.primaryClass = Primary.primaryFromInt(t.primClassif);
        this.subClass = Subclass.subclassFromInt(t.subClassif);
    }

    public STEntry(String token, Primary primClass)
    {
        this.token = new Token(token);

        this.symbol = token;
        this.primaryClass = primClass;

        this.token.primClassif = primClass.getCid();
    }

    public STEntry(String token, Primary primClass, Subclass subClass)
    {
        this(token, primClass);

        this.subClass = subClass;

        this.token.subClassif = subClass.getCid();
    }

    public Token getToken()
    {
        return this.token;
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
