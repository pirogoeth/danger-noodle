package havabol.classify;

/**
 * This enum defines structure type for stored "objects".
 *
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public enum Structure {
    UNKNOWN(0),
    PRIMITIVE(1),
    FIXED_ARY(2),
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
