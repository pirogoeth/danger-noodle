package havabol.util;

import havabol.Token;

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
     * Determine if the value represented by this string is a float.
     *
     * @param String s
     *
     * @return boolean is s a float?
     */
    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
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
                float f = Float.parseFloat(s);
                return (int) f;
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
     * @return float
     */
    public static float tokenAsFloat(Token t) {
        return stringAsFloat(t.tokenStr);
    }

    /**
     * Attempts to turn a token into a float.
     *
     * @param Token t
     *
     * @return float
     */
    public static float stringAsFloat(String s) {
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
                return (float) i;
            }
        } else {
            return Float.parseFloat(s);
        }
    }

    /**
     * Turns a float into a string.
     *
     * @param float f
     *
     * @return String
     */
    public static String floatAsString(float f) {
        return Float.toString(f);
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
}
