/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package havabol.util;

import havabol.builtins.types.*;
import havabol.common.type.*;
import havabol.classify.*;
import havabol.eval.*;
import static havabol.eval.Evaluator.reportEvalError;
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
                    case STRING:
                        floatB = floatPrim((PString) second);
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
                    case STRING:
                        intB = intPrim((PString) second);
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


    public static TypeInterface sub(TypeInterface first, TypeInterface second) {
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
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                floatRes = floatPrim(((PFloat) first).getValue() - floatB.getValue());
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
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                intRes = intPrim(((PInteger) first).getValue() - intB.getValue());
                return intRes;
            default:
                // XXX - We need a narrow exception class for this.
                return null;
        }
    }

    public static TypeInterface mult(TypeInterface first, TypeInterface second) {
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
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                floatRes = floatPrim(((PFloat) first).getValue() * floatB.getValue());
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
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                intRes = intPrim(((PInteger) first).getValue() * intB.getValue());
                return intRes;
            default:
                // XXX - We need a narrow exception class for this.
                return null;
        }
    }

    public static TypeInterface div(TypeInterface first, TypeInterface second) throws EvalException{
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
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }

                if ( floatB.getValue() == 0.0 ) {
                    // Throw DivZero Error
                    reportEvalError(
                        String.format(
                            "Can not perform assignment - divide by zero error would occur (given %s)",
                            floatB.getValue()
                        ),
                        second
                    );

                    return null;
                }

                floatRes = floatPrim(((PFloat) first).getValue() / floatB.getValue());
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
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }

                if ( intB.getValue() == 0 ) {
                    // Throw DivZero Error
                    reportEvalError(
                        String.format(
                            "Can not perform assignment - divide by zero error would occur (given %s)",
                            intB.getValue()
                        ),
                        second
                    );

                    return null;
                }

                intRes = intPrim(((PInteger) first).getValue() / intB.getValue());
                return intRes;
            default:
                // XXX - We need a narrow exception class for this.
                return null;
        }
    }

    public static TypeInterface concat(TypeInterface first, TypeInterface second) {
        PString stringA, stringB, stringRes;
        stringA = strPrim(first.getRepr());
        stringB = strPrim(second.getRepr());

        stringRes = strPrim(stringA.getValue() + stringB.getValue());
        return stringRes;
    }

    public static TypeInterface equal(TypeInterface first, TypeInterface second) {
        PBoolean res = new PBoolean();
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        floatB = floatPrim((PInteger) second);
                        break;
                    case FLOAT:
                        floatB = (PFloat) second;
                        break;
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                if( ((PFloat) first).getValue() == floatB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case INTEGER:
                PInteger intB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        intB = (PInteger) second;
                        break;
                    case FLOAT:
                        intB = intPrim((PFloat) second);
                        break;
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }

                if( ((PInteger) first).getValue() == intB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case BOOLEAN:
                switch (second.getFormalType()){
                    case BOOLEAN:
                        if((PBoolean) first.getValue() != (PBoolean) second.getValue() ){
                            res.setValue(Boolean.TRUE);
                        }else{
                            res.setValue(Boolean.FALSE);
                        }
                        return res;

                    case STRING:
                        if(second.getValue().equals("T") || second.getValue().equals("F")){
                            if(first.getValue().toString().equals("T")){
                                res.setValue(Boolean.TRUE);
                            }
                            if(first.getValue().toString().equals("F")){
                                res.setValue(Boolean.FALSE);
                            }
                        }

                    default:
                        return null;
                }
            case STRING:
                if(((PString) first).getValue().equals(((PString) second).getValue())){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            default:
                return null;
        }
    }

    public static TypeInterface notEqual(TypeInterface first, TypeInterface second) throws EvalException {
        PBoolean res = new PBoolean();
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        floatB = floatPrim((PInteger) second);
                        break;
                    case FLOAT:
                        floatB = (PFloat) second;
                        break;
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                if( ((PFloat) first).getValue() != floatB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case INTEGER:
                PInteger intB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        intB = (PInteger) second;
                        break;
                    case FLOAT:
                        intB = intPrim((PFloat) second);
                        break;
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }

                if( ((PInteger) first).getValue() != intB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case BOOLEAN:
                switch(second.getFormalType()){
                    case BOOLEAN:
                        if((PBoolean) first.getValue() != (PBoolean) second.getValue() ){
                            res.setValue(Boolean.TRUE);
                        }else{
                            res.setValue(Boolean.FALSE);
                        }
                        return res;

                    case STRING:
                        if(second.getValue().equals("T") || second.getValue().equals("F")){
                            if(first.getValue().toString().equals("T")){
                                res.setValue(Boolean.FALSE);
                            }
                            if(first.getValue().toString().equals("F")){
                                res.setValue(Boolean.TRUE);
                            }
                        }

                    default:
                        reportEvalError("Can not compare booleans with no boolean or string");
                        return null;
                }

            case STRING:
                if(!((PString) first).getValue().equals(((PString) second).getValue())){

                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            default:
                return null;
        }
    }

    public static TypeInterface greaterThan(TypeInterface first, TypeInterface second) throws EvalException {
        int intFirst;
        int intSecond;
        PBoolean res = new PBoolean();
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        floatB = floatPrim((PInteger) second);
                        break;
                    case FLOAT:
                        floatB = (PFloat) second;
                        break;
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }
                if( ((PFloat) first).getValue() > floatB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case INTEGER:
                PInteger intB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        intB = (PInteger) second;
                        break;
                    case FLOAT:
                        intB = intPrim((PFloat) second);
                        break;
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }

                if( ((PInteger) first).getValue() > intB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case BOOLEAN:
                reportEvalError("Error can not do greater than comparision of booleans");
                return null;
            case STRING:
                intFirst = Numerics.intPrim((PString)first).getValue();

                switch (second.getFormalType()) {
                    case INTEGER:
                        intSecond = Numerics.intPrim((PInteger)second).getValue();
                        break;
                    case FLOAT:
                        intSecond = Numerics.intPrim((PFloat) second).getValue();
                        break;
                    case STRING:
                        intSecond = Numerics.intPrim((PString)second).getValue();
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }

                if ( intFirst > intSecond ) {
                    res.setValue(Boolean.TRUE);
                } else {
                    res.setValue(Boolean.FALSE);
                }

                return res;
            default:
                System.out.println("SHITTING SHIT FUCK");
                return null;
        }
    }

    public static TypeInterface lessThan(TypeInterface first, TypeInterface second) throws EvalException {
        int intFirst;
        int intSecond;
        PBoolean res = new PBoolean();
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        floatB = floatPrim((PInteger) second);
                        break;
                    case FLOAT:
                        floatB = (PFloat) second;
                        break;
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }
                if( ((PFloat) first).getValue() < floatB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case INTEGER:
                PInteger intB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        intB = (PInteger) second;
                        break;
                    case FLOAT:
                        intB = intPrim((PFloat) second);
                        break;
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }

                if( ((PInteger) first).getValue() < intB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case BOOLEAN:
                reportEvalError("Error can not do greater than comparision of booleans");
                return null;
            case STRING:
                intFirst = Numerics.intPrim((PString)first).getValue();

                switch (second.getFormalType()) {
                    case INTEGER:
                        intSecond = Numerics.intPrim((PInteger)second).getValue();
                        break;
                    case FLOAT:
                        intSecond = Numerics.intPrim((PFloat) second).getValue();
                        break;
                    case STRING:
                        intSecond = Numerics.intPrim((PString)second).getValue();
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }

                if ( intFirst < intSecond ) {
                    res.setValue(Boolean.TRUE);
                } else {
                    res.setValue(Boolean.FALSE);
                }

                return res;
            default:
                System.out.println("SHITTING SHIT FUCK");
                return null;
        }

    }

    public static TypeInterface lessThanEqual(TypeInterface first, TypeInterface second) throws EvalException {
        int intFirst;
        int intSecond;
        PBoolean res = new PBoolean();
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        floatB = floatPrim((PInteger) second);
                        break;
                    case FLOAT:
                        floatB = (PFloat) second;
                        break;
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }
                if( ((PFloat) first).getValue() <= floatB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case INTEGER:
                PInteger intB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        intB = (PInteger) second;
                        break;
                    case FLOAT:
                        intB = intPrim((PFloat) second);
                        break;
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }

                if( ((PInteger) first).getValue() <= intB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case BOOLEAN:
                reportEvalError("Error can not do greater than comparision of booleans");
                return null;
            case STRING:
                intFirst = Numerics.intPrim((PString)first).getValue();

                switch (second.getFormalType()) {
                    case INTEGER:
                        intSecond = Numerics.intPrim((PInteger)second).getValue();
                        break;
                    case FLOAT:
                        intSecond = Numerics.intPrim((PFloat) second).getValue();
                        break;
                    case STRING:
                        intSecond = Numerics.intPrim((PString)second).getValue();
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }

                if ( intFirst <= intSecond ) {
                    res.setValue(Boolean.TRUE);
                } else {
                    res.setValue(Boolean.FALSE);
                }

                return res;
            default:
                System.out.println("SHITTING SHIT FUCK");
                return null;
        }
    }

    public static TypeInterface greaterThanEqual(TypeInterface first, TypeInterface second) throws EvalException {
        int intFirst;
        int intSecond;
        PBoolean res = new PBoolean();
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        floatB = floatPrim((PInteger) second);
                        break;
                    case FLOAT:
                        floatB = (PFloat) second;
                        break;
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }
                if( ((PFloat) first).getValue() >= floatB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case INTEGER:
                PInteger intB;
                switch (second.getFormalType()) {
                    case INTEGER:
                        intB = (PInteger) second;
                        break;
                    case FLOAT:
                        intB = intPrim((PFloat) second);
                        break;
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }

                if( ((PInteger) first).getValue() >= intB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case BOOLEAN:
                reportEvalError("Error can not do greater than comparision of booleans");
                return null;
            case STRING:
                intFirst = Numerics.intPrim((PString)first).getValue();

                switch (second.getFormalType()) {
                    case INTEGER:
                        intSecond = Numerics.intPrim((PInteger)second).getValue();
                        break;
                    case FLOAT:
                        intSecond = Numerics.intPrim((PFloat) second).getValue();
                        break;
                    case STRING:
                        intSecond = Numerics.intPrim((PString)second).getValue();
                        break;
                    default:
                        // XXX - EXPLODE!
                        System.out.println("FLOAT NULL");
                        return null;
                }

                if ( intFirst >= intSecond ) {
                    res.setValue(Boolean.TRUE);
                } else {
                    res.setValue(Boolean.FALSE);
                }

                return res;
            default:
                System.out.println("SHITTING SHIT FUCK");
                return null;
        }

    }

    public static TypeInterface modulo(TypeInterface first, TypeInterface second) {
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
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                floatRes = floatPrim(((PFloat) first).getValue() % floatB.getValue());
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
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                intRes = intPrim(((PInteger) first).getValue() % intB.getValue());
                return intRes;
            default:
                // XXX - We need a narrow exception class for this.
                return null;
        }
    }

    public static TypeInterface power(TypeInterface first, TypeInterface second) {
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
                    case STRING:
                        floatB = floatPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                floatRes = floatPrim(Math.pow(((PFloat) first).getValue(), floatB.getValue()));
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
                    case STRING:
                        intB = intPrim((PString) second);
                        break;
                    default:
                        // XXX - EXPLODE!
                        return null;
                }
                intRes = intPrim(Math.pow(((PInteger) first).getValue(), intB.getValue()));
                return intRes;
            default:
                // XXX - We need a narrow exception class for this.
                return null;
        }
    }

    public static TypeInterface contains(TypeInterface item, TypeInterface container) throws EvalException {
        ArrayType ary;
        ReturnType typeBound;

        PString str;

        if ( container.getFormalType() == ReturnType.ARRAY ) {
            ary = (ArrayType) container;
            typeBound = ary.getBoundType();
        } else if ( container.getFormalType() == ReturnType.STRING ) {
            str = (PString) container;
            typeBound = ReturnType.STRING;
        } else {
            reportEvalError(
                String.format(
                    "Cannot check membership of type `%s`",
                    container.getFormalType().name()
                ),
                container,
                item
            );
            return null;
        }

        if ( item.getFormalType() != typeBound ) {
            return boolPrim(false);
        }

        switch (container.getFormalType()) {
            case STRING:
                String s = ((PString) container).getValue();
                return boolPrim(s.contains(((PString) item).getValue()));
            case ARRAY:
                break;
        }

        return boolPrim(false);
    }

    public static TypeInterface notContains(TypeInterface item, TypeInterface container) throws EvalException {
        return boolPrim(! ((PBoolean) contains(item, container)).getValue());
    }

    public static TypeInterface and(TypeInterface a, TypeInterface b) throws EvalException {
        boolean left;
        boolean right;
        if ( a.getFormalType() == ReturnType.BOOLEAN)
        {  // get the bool value

            left = ((PBoolean) b).getValue();
        }else{
            reportEvalError(
                    String.format(
                        "a is not a bool `%s`",
                        a.getFormalType().name()
                        ),
                    a
                    );
            return null;
        }

        if(b.getFormalType() == ReturnType.BOOLEAN ){

            right = ((PBoolean) b).getValue();


        }else{
            reportEvalError(
                    String.format(
                        "b is not a bool `%s`",
                        b.getFormalType().name()
                        ),
                    b
                    );
            return null;
        }
        return boolPrim(left && right);

    }

    public static TypeInterface or(TypeInterface a, TypeInterface b) throws EvalException {
        boolean left;
        boolean right;
        if ( a.getFormalType() == ReturnType.BOOLEAN)
        {  // get the bool value

            left = ((PBoolean) b).getValue();
        }else{
            reportEvalError(
                    String.format(
                        "a is not a bool `%s`",
                        a.getFormalType().name()
                        ),
                    a
                    );
            return null;
        }

        if(b.getFormalType() == ReturnType.BOOLEAN ){

            right = ((PBoolean) b).getValue();


        }else{
            reportEvalError(
                    String.format(
                        "b is not a bool `%s`",
                        b.getFormalType().name()
                        ),
                    b
                    );
            return null;
        }
        return boolPrim(left || right);

    }
}
