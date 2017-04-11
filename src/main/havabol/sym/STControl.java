package havabol.sym;

import havabol.classify.*;

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

    /**
     * Only works if this is a Subclass.DECLARE.
     *
     * @return ReturnType
     */
    public ReturnType getDataType()
    {
        if ( this.subClass != Subclass.DECLARE ) {
            return null;
        }

        switch (this.symbol) {
            case "Int":
                return ReturnType.INTEGER;
            case "Float":
                return ReturnType.FLOAT;
            case "Bool":
                return ReturnType.BOOLEAN;
            case "String":
                return ReturnType.STRING;
            default:
                return null;
        }
    }

    public Subclass getSubClass() {
        return this.subClass;
    }
}
