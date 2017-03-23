package havabol.classify;

/**
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public enum Primary {
    UNKNOWN(0),
    OPERAND(1),
    OPERATOR(2),
    SEPARATOR(3),
    FUNCTION(4),
    CONTROL(5),
    EOF(6),
    RT_PAREN(7);

    private int classId;
    private static final Primary[] revMap = {
        UNKNOWN,
        OPERAND,
        OPERATOR,
        SEPARATOR,
        FUNCTION,
        CONTROL,
        EOF,
        RT_PAREN,
    };

    public static Primary primaryFromInt(int i) {
        return revMap[i];
    }

    Primary(int classId)
    {
        this.classId = classId;
    }

    public int getCid()
    {
        return this.classId;
    }
}
