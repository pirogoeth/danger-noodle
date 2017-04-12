package havabol.util;

import havabol.Token;
import havabol.builtins.types.*;

import java.util.*;

public class Numerics
{
    /**
     * Determine if the value represented by this string is a float.
     *
     * @param Token t
     *
     * @return boolean is s a float?
     */
    public static boolean isFloat(Token t) {
        String s = t.tokenStr;
        if ( t.primClassif != Token.OPERAND ) return false;
        return isFloat(s);
    }

    /**
     * Determine if the value represented by this string is a primitive float (java double).
     *
     * @param String s
     *
     * @return boolean is s a float?
     */
    public static boolean isFloat(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Determine if the value represented by this string is an int.
     *
     * @param Token t
     *
     * @return boolean is s an int?
     */
    public static boolean isInt(Token t) {
        String s = t.tokenStr;
        if ( t.primClassif != Token.OPERAND ) return false;
        return isInt(s);
    }

    /**
     * Determine if the value represented by this string is an int.
     *
     * @param String s
     *
     * @return boolean is s an int?
     */
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Attempts to turn a string into an integer.
     * If the value inside s is a float, it will be truncated.
     *
     * @param String s
     *
     * @return int
     */
    public static int tokenAsInt(Token t) {
        return stringAsInt(t.tokenStr);
    }

    /**
     * Attempts to turn a string into an integer.
     * If the value inside s is a float, it will be truncated.
     *
     * @param String s
     *
     * @return int
     */
    public static int stringAsInt(String s) {
        if ( !isInt(s) ) {
            // Try to coerce to float before int
            if ( !isFloat(s) ) {
                throw new IllegalArgumentException(
                    String.format(
                        "String '%s' is not an integer or truncatable float",
                        s
                    )
                );
            } else {
                double d = Double.parseDouble(s);
                return (int) d;
            }
        } else {
            return Integer.parseInt(s);
        }
    }

    /**
     * Attempts to turn a token into a float.
     *
     * @param Token t
     *
     * @return double
     */
    public static double tokenAsFloat(Token t) {
        return stringAsFloat(t.tokenStr);
    }

    /**
     * Attempts to turn a token into a float.
     *
     * @param Token t
     *
     * @return double
     */
    public static double stringAsFloat(String s) {
        if ( !isFloat(s) ) {
            // Try to coerce to int before float
            if ( !isInt(s) ) {
                throw new IllegalArgumentException(
                    String.format(
                        "String '%s' is not a float or compatible integer",
                        s
                    )
                );
            } else {
                int i = Integer.parseInt(s);
                return (double) i;
            }
        } else {
            return Double.parseDouble(s);
        }
    }

    /**
     * Turns a float into a string.
     *
     * @param float f
     *
     * @return String
     */
    public static String floatAsString(double d) {
        return Double.toString(d);
    }

    /**
     * Turns an int into a string.
     *
     * @param int i
     *
     * @return String
     */
    public static String intAsString(int i) {
        return Integer.toString(i);
    }

    /*
     * PRIMITIVE NUMERICS FUNCTIONS
     */

    // def intPrim.

    public static PInteger intPrim(int i) {
        PInteger pi = new PInteger();
        pi.setValue(i);
        return pi;
    }

    public static PInteger intPrim(PFloat f) {
        PInteger i = new PInteger();
        i.setValue(new Double(f.getValue()).intValue());
        return i;
    }

    public static PInteger intPrim(boolean b) {
        return b ? intPrim(1) : intPrim(0);
    }

    public static PInteger intPrim(PBoolean b) {
        return intPrim(b.getValue());
    }

    public static PInteger intPrim(PInteger i) {
        return i;
    }

    public static PInteger intPrim(PString s) {
        return intPrim(stringAsInt(s.getValue()));
    }

    // def floatPrim.

    public static PFloat floatPrim(double d) {
        PFloat f = new PFloat();
        f.setValue(d);
        return f;
    }

    public static PFloat floatPrim(PInteger i) {
        PFloat f = new PFloat();
        f.setValue(new Integer(i.getValue()).doubleValue());
        return f;
    }

    public static PFloat floatPrim(boolean b) {
        return b ? floatPrim(1d) : floatPrim(0d);
    }

    public static PFloat floatPrim(PBoolean b) {
        return floatPrim(b.getValue());
    }

    public static PFloat floatPrim(PFloat f) {
        return f;
    }

    public static PFloat floatPrim(PString s) {
        return floatPrim(stringAsFloat(s.getValue()));
    }

    // def strPrim.

    public static PString strPrim(PInteger i) {
        PString s = new PString();
        s.setValue(i.getRepr());
        return s;
    }

    public static PString strPrim(PFloat f) {
        PString s = new PString();
        s.setValue(f.getRepr());
        return s;
    }

    public static PString strPrim(PBoolean b) {
        PString s = new PString();
        s.setValue(b.getRepr());
        return s;
    }

    public static PString strPrim(int i) {
        PString s = new PString();
        s.setValue(intAsString(i));
        return s;
    }

    public static PString strPrim(double d) {
        PString s = new PString();
        s.setValue(floatAsString(d));
        return s;
    }

    public static PString strPrim(boolean b) {
        PString s = new PString();
        s.setValue(b ? "T" : "F");
        return s;
    }

}
