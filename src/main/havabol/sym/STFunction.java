package havabol.sym;

import havabol.SymbolTable;
import havabol.classify.*;

/**
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public class STFunction extends STEntry {

    /**
     * Return types for this function.
     *
     * @var Subclass[]
     */
    private Subclass[] returnTypes;

    /**
     * Parameter types for this function.
     *
     * @var Subclass[]
     */
    private Subclass[] paramTypes;

    /**
     * Symbol table belonging to this function.
     *
     * @var SymbolTable
     */
    private SymbolTable symTab;

    /**
     * Initializes a function entry for the symbol table.
     *
     * @param sym Token string symbol
     * @param subClass Type of function - user or builtin
     * @param returnTypes Array of return types for this function
     * @param argTypes Array of parameter types for this function
     */
    public STFunction(String sym, Subclass subClass, Subclass[] returnTypes, Subclass...argTypes)
    {
        super(sym, Primary.FUNCTION, subClass);

        this.returnTypes = returnTypes;
        this.paramTypes = argTypes;
    }

    /**
     * Initializes a function entry for the symbol table.
     *
     * @param sym Token string symbol
     * @param subClass Type of function - user or builtin
     * @param returnType Return type for this function
     * @param argTypes Array of parameter types for this function
     */
    public STFunction(String sym, Subclass subClass, Subclass returnType, Subclass...argTypes)
    {
        this(sym, subClass, new Subclass[] {returnType}, argTypes);
    }

    public Subclass[] getReturnTypes()
    {
        return this.returnTypes;
    }

    public int getReturnCount()
    {
        return this.returnTypes.length;
    }

    public Subclass[] getParameterTypes()
    {
        return this.paramTypes;
    }

    public int getParameterCount()
    {
        // This should return the "formal" parameter count - ie., each parameter of a type.
        // For functions like (String thing, Float[] thing, String... things)
        // the formal count is three while the passable count is 3+ (String.. basically equals String[])
        return this.paramTypes.length;
    }

}
