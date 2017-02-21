package havabol.classify;

/**
 * This enum defines parameter types for function parms.
 *
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public enum ParamType {
    UNKNOWN(0),
    NOT_PARM(1),
    BY_REF(2),
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

