package havabol.classify;

/**
 * This enum defines parameter types for function parms.
 *
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public enum ParamType {
    /**
     * UNKNOWN means the parameter type is not known.
     * This should not occur naturally.
     */
    UNKNOWN(0),

    /**
     * NOT_PARM means the parameter type is not a param. (wat)
     * This will likely occur in the case of automatics.
     */
    NOT_PARM(1),

    /**
     * BY_REF means the parameter was passed by reference and
     * changes will propagate back to the caller.
     */
    BY_REF(2),

    /**
     * BY_VAL means the parameter was passed by value and
     * changes will not propagate back to the caller.
     */
    BY_VAL(3);

    private int classId;

    ParamType(int classId)
    {
        this.classId = classId;
    }

    public int getCid()
    {
        return this.classId;
    }
}

