package havabol.classify;

/**
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public enum Subclass {
    UNKNOWN(0),

    // Operand subclasses
    IDENTIFIER(1),
    INTEGER(2),
    FLOAT(3),
    BOOLEAN(4),
    STRING(5),
    DATE(6),
    VOID(7),

    // Control subclasses
    FLOW(10),
    END(11),
    DECLARE(12),
    STATEMENT(15),

    // Function subclasses
    BUILTIN(13),
    USER(14);

    private int classId;
    private static final Subclass[] revMap = {
        UNKNOWN,
        IDENTIFIER,
        INTEGER,
        FLOAT,
        BOOLEAN,
        STRING,
        DATE,
        VOID,
        null,
        null,
        FLOW,
        END,
        DECLARE,
        BUILTIN,
        USER,
        STATEMENT,
    };

    public static Subclass subclassFromInt(int i) {
        return revMap[i];
    }

    Subclass(int classId)
    {
        this.classId = classId;
    }

    public int getCid()
    {
        return this.classId;
    }

}
