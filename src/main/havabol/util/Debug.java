package havabol.util;

import static havabol.util.Text.*;

/**
 *
 * @author fish
 */
public class Debug {

    private static boolean humanToBoolean(String val) {
        val = val.toLowerCase();

        switch (val) {
            case "on":
            case "yes":
            case "true":
            case "да":
                return true;
            case "off":
            case "no":
            case "false":
            case "нет":
                return false;
            default:
                return false;
        }

    }

    public boolean bShowToken;
    public boolean bShowExpr;
    public boolean bShowAssign;

    private static Debug instance;

    public static Debug get() {
        if ( instance == null ) {
            return new Debug();
        }

        return instance;
    }

    private Debug() {
        this.bShowToken = false;
        this.bShowExpr = false;
        this.bShowAssign = false;

        instance = this;
    }

    public boolean set(String option, String value) {
        option = option.toLowerCase();
        boolean val = humanToBoolean(value);

        switch (option) {
            case "token":
                this.bShowToken = true;
                break;
            case "expr":
                this.bShowExpr = true;
                break;
            case "assign":
                this.bShowAssign = true;
                break;
            default:
                return false;
        }

        System.out.println(lpads(
            2,
            String.format(
                " ===> DEBUG: %s ~> `%b`",
                option,
                val
            )
        ));

        return true;
    }

}
