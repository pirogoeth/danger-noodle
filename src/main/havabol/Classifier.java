package havabol;

import java.util.*;

/**
 * NOTE: Sorry about the code formatting inconsistencies. IntelliJ decided
 *       to reformat this entire file at some point.
 *
 * Created by sean on 1/29/2017.
 */
public class Classifier {

    // XXX - This is probably not the best way to do this - consider interfacing with the SymbolTable???!?!
    private static final List<String> extOps = new ArrayList<>();

    static {
        extOps.add("in");
        extOps.add("notin");
        extOps.add("not");
        extOps.add("and");
        extOps.add("or");
    }

    /**
     * Classifies a Token. Can *NOT* currently handle contextually sensitive
     * classifications (ie., strings).
     *
     * @param Token t
     */
    protected static void classifyToken(Token t) throws Exception {
        if (t.primClassif != 0 || t.subClassif != 0) {
            // There may have been contextually sensitive parsing from the
            // Scanner. *Bail*!
            return;
        }

        // Try to find token's primitive class
        // TODO - Implement `FUNCTION`, `CONTROL`, `RT_PAREN`
        if (isOperator(t)) {
            t.primClassif = Token.OPERATOR;
        } else if (isSeparator(t)) {
            t.primClassif = Token.SEPARATOR;
        } else if (isFunction(t)) {
            t.primClassif = Token.FUNCTION;
        } else if (isControl(t)) {
            t.primClassif = Token.CONTROL;
        } else if (isCloseParen(t)) {
            t.primClassif = Token.RT_PAREN;
        } else if (isEOF(t)) {
            t.primClassif = Token.EOF;
        } else if (isOperand(t)) {
            t.primClassif = Token.OPERAND;
        } else {
            throw new Exception(
                    String.format(
                            "Type unknown: \"%s\" at line %d near col %d",
                            t.tokenStr,
                            t.iSourceLineNr,
                            t.iColPos
                    )
            );
        }

        // Try to find token's sub-classification
        // TODO - Add `VOID`, `STRING`
        if (t.primClassif == Token.OPERAND) {
            if (isInt(t)) {
                t.subClassif = Token.INTEGER;
            } else if (isFloat(t)) {
                t.subClassif = Token.FLOAT;
            } else if (isBoolean(t)) {
                t.subClassif = Token.BOOLEAN;
            } else if (isDate(t)) {
                t.subClassif = Token.DATE;
            } else if (isIdentifier(t)) {
                t.subClassif = Token.IDENTIFIER;
            } else {
                throw new Exception(
                        String.format(
                                "Invalid token: \"%s\" at line %d near col %d",
                                t.tokenStr,
                                t.iSourceLineNr,
                                t.iColPos
                        )
                );
            }
        }
    }

    /*
     * PRIMITIVE CLASSIFICATION HELPERS
     */

    /**
     * Determine if the value represented by this string is an operator.
     *
     * @param String s
     *
     * @return boolean is value an operator?
     */
    private static boolean isOperator(Token t) {
        if ( t.primClassif != 0 ) return false;
        String operators = "+-*/<>=!";
        if ( operators.indexOf(t.tokenStr) != -1 ) {
            return true;
        } else if ( extOps.indexOf(t.tokenStr) != -1 ) {
            return true;
        }

        return false;
    }

    /**
     * Determine if the value represented by this string is a separator.
     *
     * @param String s
     *
     * @return boolean is value a separator?
     */
    private static boolean isSeparator(Token t) {
        if ( t.primClassif != 0 ) return false;
        String separators = "(),:;[]";
        if ( separators.indexOf(t.tokenStr) != -1 ) {
            return true;
        }

        return false;
    }

    /**
     * Determine if the value represented by this string is a function.
     *
     * @param String s
     *
     * @return boolean is value a function?
     */
    private static boolean isFunction(Token t) {
        if ( t.primClassif != 0 ) return false;
        return false;
    }

    /**
     * Determine if the value represented by this string is a control statement.
     *
     * @param String s
     *
     * @return boolean is value a control statement?
     */
    private static boolean isControl(Token t) {
        if ( t.primClassif != 0 ) return false;
        return false;
    }

    /**
     * Determine if the value represented by this string is an EOF.
     *
     * @param String s
     *
     * @return boolean is value an EOF?
     */
    private static boolean isEOF(Token t) {
        if ( t.primClassif != 0 ) return false;
        if ( t.tokenStr.equals("") ) {
            return true;
        }

        return false;
    }

    /**
     * Determine if the value represented by this string is a `)`.
     *
     * @param String s
     *
     * @return boolean is value a `)`?
     */
    private static boolean isCloseParen(Token t) {
        if ( t.primClassif != 0 ) return false;
        return false;
    }

    /**
     * Determine if the value represented by this string is an operand.
     *
     * @param String s
     *
     * @return boolean is value an operand?
     */
    private static boolean isOperand(Token t) {
        if ( t.primClassif != 0 ) return false;
        return true;
    }

    /*
     * SUB-CLASSIFICATION HELPERS
     */

    /**
     * Determine if the value represented by this string is a float.
     *
     * @param Token t
     *
     * @return boolean is s a float?
     */
    private static boolean isFloat(Token t) {
        String s = t.tokenStr;
        if ( t.primClassif != Token.OPERAND ) return false;
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
    private static boolean isInt(Token t) {
        String s = t.tokenStr;
        if ( t.primClassif != Token.OPERAND ) return false;
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Determine if the value represented by this string is a boolean.
     *
     * @param Token t
     *
     * @return boolean is s a boolean?
     */
    private static boolean isBoolean(Token t) {
        String s = t.tokenStr;
        if ( t.primClassif != Token.OPERAND ) return false;
        if ( s.equals("true") || s.equals("false") ) {
            return true;
        }

        return false;
    }

    /**
     * Determine if the value represented by this string is a date type.
     *
     * @param Token t
     *
     * @return boolean is s a date?
     */
    private static boolean isDate(Token t) {
        String s = t.tokenStr;
        if ( t.primClassif != Token.OPERAND ) return false;
        return false;
    }

    /**
     * Determine if the value represented by this string is an identifier.
     *
     * @param Token t
     *
     * @return boolean is s an identifier
     */
    private static boolean isIdentifier(Token t) {
        String s = t.tokenStr;
        if ( t.primClassif != Token.OPERAND ) return false;
        if ( s.matches("[a-zA-Z_]+[a-zA-Z0-9_]?+")) {
            return true;
        }

        return false;
    }
}
