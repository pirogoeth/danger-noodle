package havabol.sym;

import havabol.classify.*;
import havabol.storage.*;

/**
 * * @author Sean Johnson <sean.johnson@maio.me>
 */
public class STIdentifier extends STEntry {

    /**
     * Defines the data type for the identifier.
     *
     * @var STControl
     */
    private STControl stDeclared = null;

    /**
     * Alternative data type holder for implicit identifiers.
     *
     * @var ReturnType
     */
    private ReturnType stReturn = null;

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
     * @param sym Token symbol string
     * @param declaredType STControl symbol for declared type lookup
     * @param structure Backing data structure for this identifier
     * @param paramType Parameter type
     * @param baseAddr Non-local base address field
     */
    public STIdentifier(String sym, STControl declaredType, Structure structure, ParamType paramType, int baseAddr) {
        super(sym, Primary.OPERAND, Subclass.IDENTIFIER);

        this.stDeclared = declaredType;
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
    public STIdentifier(String sym, STControl declaredType, Structure structure, int baseAddr) {
        this(sym, declaredType, structure, ParamType.NOT_PARM, baseAddr);
    }

    /**
     * Shortcut for implicitly defined identifiers.
     *
     * @param sym Token symbol string
     * @param retType ReturnType type of value
     * @param structure Backing data structure for this identifier
     * @param baseAddr non-local base address field
     */
    public STIdentifier(String sym, ReturnType retType, Structure structure, int baseAddr) {
        super(sym, Primary.OPERAND, Subclass.IDENTIFIER);

        this.stDeclared = null;
        this.backingType = structure;
        this.paramDefin = ParamType.NOT_PARM;
        this.baseAddress = baseAddr;
        this.stReturn = retType;
    }

    public STControl getDeclared() {
        return this.stDeclared;
    }

    public ReturnType getDeclaredType() {
        if ( this.stDeclared != null ) {
            return this.stDeclared.getDataType();
        } else if ( this.stReturn != null ) {
            return this.stReturn;
        } else {
            return null;
        }
    }

    public SMValue getStoredValue(StorageManager store) {
        return store.get(this);
    }

}
