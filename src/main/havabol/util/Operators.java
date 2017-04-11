/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package havabol.util;

import havabol.builtins.types.*;
import havabol.common.type.*;
import havabol.classify.*;
import static havabol.util.Numerics.*;
import static havabol.util.Text.*;

/**
 *
 * @author fish
 */
public class Operators {
    
    
    
    
    
    
    public static TypeInterface add(TypeInterface first, TypeInterface second) {
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB, floatRes;
                switch (second.getFormalType()) {
                    case INTEGER:
                        floatB = floatPrim((PInteger) second);
                        break;
                    case FLOAT:
                        floatB = (PFloat) second;
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                floatRes = floatPrim(((PFloat) first).getValue() + floatB.getValue());
                return floatRes;
            case INTEGER:
                PInteger intB, intRes;
                switch (second.getFormalType()) {
                    case INTEGER:
                        intB = (PInteger) second;
                        break;
                    case FLOAT:
                        intB = intPrim((PFloat) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                intRes = intPrim(((PInteger) first).getValue() + intB.getValue());
                return intRes;
            default:
                // XXX - We need a narrow exception class for this.
                return null;
        }
    }
}
