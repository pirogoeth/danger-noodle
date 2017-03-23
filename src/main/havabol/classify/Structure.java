package havabol.classify;

/**
 * This enum defines structure type for stored "objects".
 *
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public enum Structure {
    /**
     * UNKNOWN means the backing structure is not known.
     * This *should not* occur naturally.
     */
    UNKNOWN(0),

    /**
     * PRIMITIVE means the backing structure is a language native,
     * such as Int, String, etc.
     */
    PRIMITIVE(1),

    /**
     * FIXED_ARY means the backing structure is a fixed arity array
     * of homogenous values.
     */
    FIXED_ARY(2),

    /**
     * VARIA_ARY means the backing structure is a resizable, variable arity
     * array of homogenous values.
     */
    VARIA_ARY(3);

    private int classId;

    Structure(int classId)
    {
        this.classId = classId;
    }

    public int getCid()
    {
        return this.classId;
    }
}
