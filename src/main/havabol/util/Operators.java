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
    
    public static TypeInterface hash(TypeInterface first, TypeInterface second) {
        if(second.getFormalType() == ReturnType.STRING){
            PString stringB, stringRes;
                stringB = strPrim( (PString) second);
                
                stringRes = strPrim(((PString) first).getValue() + stringB.getValue());
                return stringRes;
        }else{
            System.out.println("Used string append operator on non string value");
            System.exit(-1);
        }
        
        return(null);
    }
    
    public static TypeInterface equal(TypeInterface first, TypeInterface second) {
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                PBoolean BoolRes = null;
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
                    BoolRes.setValue(Boolean.TRUE);
                }else{
                    BoolRes.setValue(Boolean.FALSE);
                }
                return BoolRes;
            case INTEGER:
                PInteger intB;
                PBoolean boolRes = null;
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
                   boolRes.setValue(Boolean.TRUE);
               }else{
                    boolRes.setValue(Boolean.FALSE);
               }
                return boolRes;
            case BOOLEAN:
                switch(second.getFormalType()){
                    case BOOLEAN:
                       PBoolean boolReS = null;
                       if((PBoolean) first.getValue() != (PBoolean) second.getValue() ){
                           boolReS.setValue(Boolean.TRUE);
                       }else{
                             boolReS.setValue(Boolean.FALSE);
                       }
                       return boolReS;
                       
                    case STRING:
                        PBoolean bOOlReS = null;
                        if(second.getValue().equals("T") || second.getValue().equals("F")){
                            if(first.getValue().toString().equals("T")){
                                bOOlReS.setValue(Boolean.TRUE);
                            }
                            if(first.getValue().toString().equals("F")){
                                bOOlReS.setValue(Boolean.FALSE);
                            }
                        }
                        
                    default:
                        System.out.println("Can not compare booleans with no boolean or string");
                        System.exit(-1);
                }
            case STRING:
                PBoolean bOolReS = null;
                if(((PString) first).getValue().equals(((PString) second).getValue())){
                        
                bOolReS.setValue(Boolean.TRUE);
                }else{
                    bOolReS.setValue(Boolean.FALSE);
                }
                return bOolReS;
            default:
                return null;
        }
    }
    
    
    public static TypeInterface notequal(TypeInterface first, TypeInterface second) {
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                PBoolean BoolRes = null;
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
                    BoolRes.setValue(Boolean.TRUE);
                }else{
                    BoolRes.setValue(Boolean.FALSE);
                }
                return BoolRes;
            case INTEGER:
                PInteger intB;
                PBoolean boolRes = null;
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
                   boolRes.setValue(Boolean.TRUE);
               }else{
                    boolRes.setValue(Boolean.FALSE);
               }
                return boolRes;
            case BOOLEAN:
                switch(second.getFormalType()){
                    case BOOLEAN:
                       PBoolean boolReS = null;
                       if((PBoolean) first.getValue() != (PBoolean) second.getValue() ){
                           boolReS.setValue(Boolean.TRUE);
                       }else{
                             boolReS.setValue(Boolean.FALSE);
                       }
                       return boolReS;
                       
                    case STRING:
                        PBoolean bOOlReS = null;
                        if(second.getValue().equals("T") || second.getValue().equals("F")){
                            if(first.getValue().toString().equals("T")){
                                bOOlReS.setValue(Boolean.FALSE);
                            }
                            if(first.getValue().toString().equals("F")){
                                bOOlReS.setValue(Boolean.TRUE);
                            }
                        }
                        
                    default:
                        System.out.println("Can not compare booleans with no boolean or string");
                        System.exit(-1);
                }
    
            case STRING:
                PBoolean bOolReS = null;
                if(!((PString) first).getValue().equals(((PString) second).getValue())){
                        
                bOolReS.setValue(Boolean.TRUE);
                }else{
                    bOolReS.setValue(Boolean.FALSE);
                }
                return bOolReS;
            default:
                return null;
        }
    }
    
    public static TypeInterface greaterthan(TypeInterface first, TypeInterface second) {
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                PBoolean BoolRes = null;
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
                    BoolRes.setValue(Boolean.TRUE);
                }else{
                    BoolRes.setValue(Boolean.FALSE);
                }
                return BoolRes;
            case INTEGER:
                PInteger intB;
                PBoolean boolRes = null;
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
                   boolRes.setValue(Boolean.TRUE);
               }else{
                    boolRes.setValue(Boolean.FALSE);
               }
                return boolRes;
            case BOOLEAN:
                System.out.println("Error can not do greter than comparision of booleans");
                return null;
                
            case STRING:
                PBoolean bOolReS = null;
                if(((PString) first).getValue().compareTo(((PString) second).getValue()) > 0){
                        
                bOolReS.setValue(Boolean.TRUE);
                }else{
                    bOolReS.setValue(Boolean.FALSE);
                }
                return bOolReS;
            default:
                return null;
        }
    }
    
    public static TypeInterface lassthan(TypeInterface first, TypeInterface second) {
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                PBoolean BoolRes = null;
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
                    BoolRes.setValue(Boolean.TRUE);
                }else{
                    BoolRes.setValue(Boolean.FALSE);
                }
                return BoolRes;
            case INTEGER:
                PInteger intB;
                PBoolean boolRes = null;
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
                   boolRes.setValue(Boolean.TRUE);
               }else{
                    boolRes.setValue(Boolean.FALSE);
               }
                return boolRes;
            case BOOLEAN:
                System.out.println("Error can not do less than comparision of booleans");
                return null;
                
            case STRING:
                PBoolean bOolReS = null;
                if(((PString) first).getValue().compareTo(((PString) second).getValue()) < 0){
                        
                bOolReS.setValue(Boolean.TRUE);
                }else{
                    bOolReS.setValue(Boolean.FALSE);
                }
                return bOolReS;
            default:
                return null;
        }
    }
    
        public static TypeInterface lessthanorequal(TypeInterface first, TypeInterface second) {
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                PBoolean BoolRes = null;
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
                    BoolRes.setValue(Boolean.TRUE);
                }else{
                    BoolRes.setValue(Boolean.FALSE);
                }
                return BoolRes;
            case INTEGER:
                PInteger intB;
                PBoolean boolRes = null;
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
                   boolRes.setValue(Boolean.TRUE);
               }else{
                    boolRes.setValue(Boolean.FALSE);
               }
                return boolRes;
            case BOOLEAN:
                switch(second.getFormalType()){
                    case BOOLEAN:
                       PBoolean boolReS = null;
                       if((PBoolean) first.getValue() != (PBoolean) second.getValue() ){
                           boolReS.setValue(Boolean.TRUE);
                       }else{
                             boolReS.setValue(Boolean.FALSE);
                       }
                       return boolReS;
                       
                    case STRING:
                        PBoolean bOOlReS = null;
                        if(second.getValue().equals("T") || second.getValue().equals("F")){
                            if(first.getValue().toString().equals("T")){
                                bOOlReS.setValue(Boolean.TRUE);
                            }
                            if(first.getValue().toString().equals("F")){
                                bOOlReS.setValue(Boolean.FALSE);
                            }
                        }
                        
                    default:
                        System.out.println("Can not compare booleans with no boolean or string");
                        System.exit(-1);
                }
                
            case STRING:
                PBoolean bOolReS = null;
                if(((PString) first).getValue().compareTo(((PString) second).getValue()) <= 0){
                        
                bOolReS.setValue(Boolean.TRUE);
                }else{
                    bOolReS.setValue(Boolean.FALSE);
                }
                return bOolReS;
            default:
                return null;
        }
    }
        
    public static TypeInterface greaterthanorequal(TypeInterface first, TypeInterface second) {
        switch (first.getFormalType()) {
            case FLOAT:
                PFloat floatB;
                PBoolean BoolRes = null;
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
                    BoolRes.setValue(Boolean.TRUE);
                }else{
                    BoolRes.setValue(Boolean.FALSE);
                }
                return BoolRes;
            case INTEGER:
                PInteger intB;
                PBoolean boolRes = null;
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
                   boolRes.setValue(Boolean.TRUE);
               }else{
                    boolRes.setValue(Boolean.FALSE);
               }
                return boolRes;
            case BOOLEAN:
                switch(second.getFormalType()){
                    case BOOLEAN:
                       PBoolean boolReS = null;
                       if((PBoolean) first.getValue() != (PBoolean) second.getValue() ){
                           boolReS.setValue(Boolean.TRUE);
                       }else{
                             boolReS.setValue(Boolean.FALSE);
                       }
                       return boolReS;
                       
                    case STRING:
                        PBoolean bOOlReS = null;
                        if(second.getValue().equals("T") || second.getValue().equals("F")){
                            if(first.getValue().toString().equals("T")){
                                bOOlReS.setValue(Boolean.TRUE);
                            }
                            if(first.getValue().toString().equals("F")){
                                bOOlReS.setValue(Boolean.FALSE);
                            }
                        }
                        
                    default:
                        System.out.println("Can not compare booleans with no boolean or string");
                        System.exit(-1);
                }
                
            case STRING:
                PBoolean bOolReS = null;
                if(((PString) first).getValue().compareTo(((PString) second).getValue()) >= 0){
                        
                bOolReS.setValue(Boolean.TRUE);
                }else{
                    bOolReS.setValue(Boolean.FALSE);
                }
                return bOolReS;
            default:
                return null;
        }
    }    
        
        
     
}
