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
    CONTROL(6),
    EOF(7),
    RT_PAREN(8);

    private int classId;

    Primary(int classId)
    {
        this.classId = classId;
    }

    public int getCid()
    {
        return this.classId;
    }
}
