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

    public static TypeInterface div(TypeInterface first, TypeInterface second) {
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
                        return null;
                }

                if( ((PInteger) first).getValue() > intB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case BOOLEAN:
                reportEvalError("Error can not do greter than comparision of booleans");
                return null;

            case STRING:
                if(((PString) first).getValue().compareTo(((PString) second).getValue()) > 0){

                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            default:
                return null;
        }
    }

    public static TypeInterface lessThan(TypeInterface first, TypeInterface second) throws EvalException {
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
                        return null;
                }

                if( ((PInteger) first).getValue() < intB.getValue()){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            case BOOLEAN:
                reportEvalError("Error can not do less than comparision of booleans");
                return null;

            case STRING:
                if(((PString) first).getValue().compareTo(((PString) second).getValue()) < 0){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            default:
                return null;
        }
    }

    public static TypeInterface lessThanEqual(TypeInterface first, TypeInterface second) throws EvalException {
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
                        return null;
                }

                if( ((PInteger) first).getValue() <= intB.getValue()){
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
                                res.setValue(Boolean.TRUE);
                            }
                            if(first.getValue().toString().equals("F")){
                                res.setValue(Boolean.FALSE);
                            }
                        }

                    default:
                        reportEvalError("Can not compare booleans with no boolean or string");
                        return null;
                }

            case STRING:
                if(((PString) first).getValue().compareTo(((PString) second).getValue()) <= 0){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            default:
                return null;
        }
    }

    public static TypeInterface greaterThanEqual(TypeInterface first, TypeInterface second) throws EvalException {
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
                        return null;
                }

                if( ((PInteger) first).getValue() >= intB.getValue()){
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
                                res.setValue(Boolean.TRUE);
                            }
                            if(first.getValue().toString().equals("F")){
                                res.setValue(Boolean.FALSE);
                            }
                        }

                    default:
                        reportEvalError("Can not compare booleans with no boolean or string");
                        return null;
                }

            case STRING:
                if(((PString) first).getValue().compareTo(((PString) second).getValue()) >= 0){
                    res.setValue(Boolean.TRUE);
                }else{
                    res.setValue(Boolean.FALSE);
                }
                return res;
            default:
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

}
