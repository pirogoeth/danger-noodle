package havabol.sym;

import havabol.classify.*;

/**
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public class STIdentifier extends STEntry {

    /**
     * Defines the backing type structure for this identifier.
     *
     * @var Structure
     */
    private Structure backingType = Structure.UNKNOWN;

    /**
     * Defines if this identifier is a function param, and if it is
     * passed by ref or by val.
     *
     * @var ParamType
     */
    private ParamType paramDefin = ParamType.UNKNOWN;

    /**
     * Base address of this symbol in the scope.
     *
     * @var int
     */
    private int baseAddress;

    /**
     *
     * @param sym Token symbol string
     * @param declaredType STControl symbol for declared type lookup
     * @param structure Backing data structure for this identifier
     * @param paramType Parameter type
     * @param baseAddr Non-local base address field
     */
    public STIdentifier(String sym, STControl declaredType, Structure structure, ParamType paramType, int baseAddr)
    {
        super(sym, Primary.OPERAND, Subclass.IDENTIFIER);

        this.backingType = structure;
        this.paramDefin = paramType;
        this.baseAddress = baseAddr;
    }

    /**
     * Shortcut initializer to create an identifier that is not a parameter.
     *
     * @param sym Token symbol string
     * @param declaredType STControl symbol for declared type lookup
     * @param structure Backing data structure for this identifier
     * @param baseAddr Non-local base address field
     */
    public STIdentifier(String sym, STControl declaredType, Structure structure, int baseAddr)
    {
        this(sym, declaredType, structure, ParamType.NOT_PARM, baseAddr);
    }

}
