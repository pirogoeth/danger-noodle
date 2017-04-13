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
     * Attempts to turn a token into a boolean.
     *
     * @param Token t
     *
     * @return boolean
     */
    public static boolean tokenAsBoolean(Token t) {
        return stringAsBoolean(t.tokenStr);
    }

    /**
     * Attempts to turn a token into a boolean.
     *
     * @param Token t
     *
     * @return boolean
     */
    public static boolean stringAsBoolean(String s) {
        // Initially set to false to please the Java Gods from yelling
        // Will return correctly no matter what set to here.
        boolean b = false;

        switch(s) {
            case "T":
                b = true;
                break;
            case "F":
                // Not setting to false, because already initialized to false
                break;
            default:
                throw new IllegalArgumentException(
                    String.format(
                        "String '%s' is not a Bool",
                        s
                    )
                );
        }

        return b;
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

    /**
     * Turns a boolean into a string.
     *
     * @param boolean b
     *
     * @return String
     */
    public static String booleanAsString(boolean b) {
        return b ? "T" : "F";
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

    public static PInteger intPrim(PInteger i) {
        return i;
    }

    public static PInteger intPrim(PString s) {
        return intPrim(stringAsInt(s.getValue()));
    }

    public static PInteger intPrim(double d) {
        return intPrim(new Double(d).intValue());
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

    public static PFloat floatPrim(PFloat f) {
        return f;
    }

    public static PFloat floatPrim(PString s) {
        return floatPrim(stringAsFloat(s.getValue()));
    }

    public static PFloat floatPrim(int i) {
        return floatPrim(new Integer(i).doubleValue());
    }

    // def boolPrim.

    public static PBoolean boolPrim(boolean b) {
        PBoolean pb = new PBoolean();
        pb.setValue(b);
        return pb;
    }

    public static PBoolean boolPrim(PBoolean b) {
        return b;
    }

    public static PBoolean boolPrim(String s) {
        PBoolean b = new PBoolean();
        b.setValue(stringAsBoolean(s));
        return b;
    }

    public static PBoolean boolPrim(PString s) {
        PBoolean b = new PBoolean();
        b.setValue(stringAsBoolean(s.getValue()));
        return b;
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

    public static PString strPrim(PString s) {
        return s;
    }

    public static PString strPrim(String s) {
        PString ps = new PString();
        ps.setValue(s);
        return ps;
    }

}
