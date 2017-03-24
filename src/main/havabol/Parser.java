
package havabol;

import havabol.classify.*;
import havabol.storage.SMValue;
import havabol.sym.*;
import havabol.util.Escapes;
import havabol.util.Numerics;
import havabol.util.debugObj;

import java.io.IOException;

/**
 *
 * @author fish
 */
public class Parser {

    SymbolTable sbTable;
    Scanner scan;
    Boolean bRun;
    boolean btest;
    debugObj debug;

    /**
     * Uses the global symbol table if none is provided.
     */
    Parser(Scanner scan){
         sbTable = SymbolTable.getGlobal();
         this.scan = scan;
    }

    Parser(SymbolTable sbTable, Scanner scan, debugObj debug){
         this.sbTable = sbTable;
         this.scan = scan;
         bRun = true;
         btest = true;
         this.debug = debug;
    }

    /**
     * THis checks to see if the Symbol currently exists in the symbol table
     * made this to simplify code later
     *
     * @param token
     * @return true if symbol is in table or false if it is not
     */
    public boolean checkSymbol(Token token){

        STEntry STEntry = sbTable.lookupSym(token);

        if(STEntry == null){
            return(false);
        }else{
            return(true);
        }
    }

    void statement() throws IOException, errorCatch{
        switch(scan.currentToken.primClassif){

            case Token.CONTROL:

                switch(scan.currentToken.subClassif){

                    case Token.DECLARE:
                        declareStatement();
                    break;

                    case Token.FLOW:
                         switch(scan.currentToken.tokenStr){
                             case("if"):
                                ifStatement();
                             break;
                             case("while"):
                                 whileStatement();
                                     
                             break;
                         }
                }
             break;

            case Token.OPERAND:
                evalExpression();
            break;

            case Token.FUNCTION:
                 evalFunction();
        }
        if(scan.currentToken.tokenStr.equals(";")){
            scan.getNext();
        }else{
           // System.out.println("Error- ';' expected");
           // System.exit(-1);
        }
    }

    //Assumes that the current token is a control declaration ie. Int, Float...
    void declareStatement() throws IOException, errorCatch{

        Token declareToken = scan.currentToken;
        STControl declareSym = (STControl) sbTable.lookupSym(declareToken);

        scan.getNext();
        Token cur = scan.currentToken;
        STIdentifier identSym;

        if(cur.primClassif != Token.OPERAND && cur.subClassif != Token.IDENTIFIER){
            System.out.println("Error expected an Identifier");
            System.exit(-1);
        } else {
            if(!checkSymbol(cur)){
                identSym = new STIdentifier(
                        cur.tokenStr,
                        declareSym,
                        Structure.PRIMITIVE,
                        ParamType.NOT_PARM,
                        0
                );
                sbTable.putSymbol(identSym);
            }else{
                System.out.println("Error double declaration");
                System.exit(-1);
            }

        }

        //get the next token to check for correct items after
        scan.getNext();

        switch(scan.currentToken.primClassif){

            //Makes sure it is folloed by a ;
            case Token.SEPARATOR:
                if(!scan.currentToken.tokenStr.equals(";")){
                   System.out.println("Error Expected a ;");
                   System.exit(-1);
                }else{

              //      System.out.println("Declared " + scan.tokenList.get(scan.tokenList.size()-2).tokenStr);

                    return;
                }

            //Doesn't do much yet added this for future use
            //This will check for an = sign for declaration and
            //setting a value to it at the same time
            case Token.OPERATOR:
                if(!scan.currentToken.tokenStr.equals("=")){
                   System.out.println("Error Expected a =");
                   System.exit(-1);
                }else{
                    //Code for proceccing statments here
                }

            default:
                System.out.println("Error Expected a ; or =");
                System.exit(-1);
        }

    }


    //assumes that the current token is an operand followed by an = sign
    void evalExpression()throws IOException, errorCatch{
        Token first;
        if(checkSymbol(scan.currentToken)){
            first = scan.currentToken;
            
            scan.getNext();
            if(scan.currentToken.tokenStr.equals("=")){
            
               scan.getNext();
               resultValue result = simpleExpression();
               //Put value of simple expression
               if(!result.value.equals(""))
                   if(bRun){
                      sbTable.getStorageManager().getOrInit( (STIdentifier)sbTable.lookupSym(first)).set(result.value);           
                   //   System.out.println("Added "+ result.value );
                   }
            }
        }else{
            System.out.println("Operand not declared");
            System.exit(-1);
        }

       
    }
    //assumes current token is an operand
    resultValue simpleExpression() throws IOException, errorCatch{
        Token first;
        Token second;
        resultValue res = new resultValue();
        first = scan.currentToken;

        scan.getNext();

        if(scan.currentToken.primClassif == Token.OPERATOR){
            switch(scan.currentToken.tokenStr){
                case "^":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( Numerics.isInt(r1.value)){
                            
                                 int ret = (int) Math.pow(Numerics.stringAsInt(r1.value) , Numerics.stringAsInt(r2.value));
                                 
                                 res.value = Numerics.intAsString(ret) ;
                                 res.type = "Int";
                                 //System.out.println(res.value + " ////////////////////");
                                 
                             
                         }else {
                             if(Numerics.isFloat(r1.value)){
                                  float ret = (float) Math.pow(Numerics.stringAsInt(r1.value) , Numerics.stringAsInt(r2.value));
                                 
                                 res.value = Numerics.floatAsString(ret) ;
                                 res.type = "Float";
                                 //System.out.println(res.value + " ^^^^^^^^^^^");
                                 
                             
                             }
                             
                         }
                         }
                         //System.out.println(r1.value + " " + r2.value + " ^^^^^^^^^^");
                         scan.getNext();
                     }
                break;
                case "*":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( Numerics.isInt(r1.value)){
                            
                                 int ret = Numerics.stringAsInt(r1.value) * Numerics.stringAsInt(r2.value);
                                 
                                 res.value = Numerics.intAsString(ret) ;
                                 res.type = "Int";
                                // System.out.println(res.value + " ////////////////////");
                                 
                             
                         }else {
                             if(Numerics.isFloat(r1.value)){
                                  float ret = Numerics.stringAsFloat(r1.value) * Numerics.stringAsFloat(r2.value);
                                 
                                 res.value = Numerics.floatAsString(ret) ;
                                 res.type = "Float";
                                 //System.out.println(res.value + " ^^^^^^^^^^^");
                                 
                             
                             }
                             
                         }
                         }
                        // System.out.println(r1.value + " " + r2.value + " ^^^^^^^^^^");
                       scan.getNext();
                     }
                break;
                case "/":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( Numerics.isInt(r1.value)){
                            
                                 int ret = Numerics.stringAsInt(r1.value) / Numerics.stringAsInt(r2.value);
                                 
                                 res.value = Numerics.intAsString(ret) ;
                                 res.type = "Int";
                                // System.out.println(res.value + " ////////////////////");
                                 
                             
                         }else {
                             if(Numerics.isFloat(r1.value)){
                                  float ret = Numerics.stringAsFloat(r1.value) / Numerics.stringAsFloat(r2.value);
                                 
                                 res.value = Numerics.floatAsString(ret) ;
                                 res.type = "Float";
                                 //System.out.println(res.value + " ^^^^^^^^^^^");
                                 
                             
                             }
                             
                         }
                         }
                        // System.out.println(r1.value + " " + r2.value + " ^^^^^^^^^^");
                       scan.getNext();
                     }
                break;
                case "-":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( Numerics.isInt(r1.value)){
                            
                                 int ret = Numerics.stringAsInt(r1.value) - Numerics.stringAsInt(r2.value);
                                 
                                 res.value = Numerics.intAsString(ret) ;
                                 res.type = "Int";
                                // System.out.println(res.value + " ////////////////////");
                                 
                             
                         }else {
                             if(Numerics.isFloat(r1.value)){
                                  float ret = Numerics.stringAsFloat(r1.value) - Numerics.stringAsFloat(r2.value);
                                 
                                 res.value = Numerics.floatAsString(ret) ;
                                 res.type = "Float";
                                 //System.out.println(res.value + " ^^^^^^^^^^^");
                                 
                             
                             }
                             
                         }
                         }
                        // System.out.println(r1.value + " " + r2.value + " ^^^^^^^^^^");
                       scan.getNext();
                     }
                
                break;
                case "==":
                    scan.getNext();
                    if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                     
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( r1.type.equals("Int")){            
                                if(Numerics.stringAsInt(r1.value) == Numerics.stringAsInt(r2.value)){
                                   res.value = "T";   
                                }else{
                                    res.value = "F";
                                } 
                                 res.type = "Boolean";
                                 res.structure = "primative";      
                         }else {
                             if(r1.type.equals("Float")){
                                 
                                 if( Numerics.stringAsFloat(r1.value) == Numerics.stringAsFloat(r2.value)){
                                      res.value = "T";
                                 }else{
                                      res.value = "F";
                                 }          
                                 res.structure = "Primative";
                                 res.type = "Float";                 
                             }else{
                                 if(r1.type.equals("String")){
                                    if(r1.value.compareTo(r2.value) == 0){
                                        res.value = "T";
                                    }else{
                                         res.value = "F";
                                    }
                                 res.structure = "Primative";
                                 res.type = "String";    
                                 }
                             }
                             
                         }
                         }
                    }
                    scan.getNext();
                break;
                case "!=":
                    scan.getNext();
                    if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                     
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( r1.type.equals("Int")){            
                                if(Numerics.stringAsInt(r1.value) != Numerics.stringAsInt(r2.value)){
                                   res.value = "T";   
                                }else{
                                    res.value = "F";
                                } 
                                 res.type = "Boolean";
                                 res.structure = "primative";      
                         }else {
                             if(r1.type.equals("Float")){
                                 
                                 if( Numerics.stringAsFloat(r1.value) != Numerics.stringAsFloat(r2.value)){
                                      res.value = "T";
                                 }else{
                                      res.value = "F";
                                 }          
                                 res.structure = "Primative";
                                 res.type = "Float";                 
                             }else{
                                 if(r1.type.equals("String")){
                                    if(r1.value.compareTo(r2.value) != 0){
                                        res.value = "T";
                                    }else{
                                         res.value = "F";
                                    }
                                 res.structure = "Primative";
                                 res.type = "String";    
                                 }
                             }
                             
                         }
                         }
                    }
                    scan.getNext();
                break;
                case "+":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( Numerics.isInt(r1.value)){
                            
                                 int ret = Numerics.stringAsInt(r1.value) + Numerics.stringAsInt(r2.value);
                                 
                                 res.value = Numerics.intAsString(ret) ;
                                 res.type = "Int";
                                // System.out.println(res.value + " ////////////////////");
                                 
                             
                         }else {
                             if(Numerics.isFloat(r1.value)){
                                  float ret = Numerics.stringAsFloat(r1.value) + Numerics.stringAsFloat(r2.value);
                                 
                                 res.value = Numerics.floatAsString(ret) ;
                                 res.type = "Float";
                                 //System.out.println(res.value + " ^^^^^^^^^^^");
                                 
                             
                             }
                             
                         }
                         }
                        // System.out.println(r1.value + " " + r2.value + " ^^^^^^^^^^");
                       scan.getNext();
                     }
                
                break;
                case ">=":
                    scan.getNext();
                    if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                     
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( r1.type.equals("Int")){            
                                if(Numerics.stringAsInt(r1.value) >= Numerics.stringAsInt(r2.value)){
                                   res.value = "T";   
                                }else{
                                    res.value = "F";
                                } 
                                 res.type = "Boolean";
                                 res.structure = "primative";      
                         }else {
                             if(r1.type.equals("Float")){
                                 
                                 if( Numerics.stringAsFloat(r1.value) >= Numerics.stringAsFloat(r2.value)){
                                      res.value = "T";
                                 }else{
                                      res.value = "F";
                                 }          
                                 res.structure = "Primative";
                                 res.type = "Float";                 
                             }else{
                                 if(r1.type.equals("String")){
                                    if(r1.value.compareTo(r2.value) >= 0){
                                        res.value = "T";
                                    }else{
                                         res.value = "F";
                                    }
                                 res.structure = "Primative";
                                 res.type = "String";    
                                 }
                             }
                             
                         }
                         }
                    }
                    scan.getNext();
                break;
                case "<=":
                    scan.getNext();
                    if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                     
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( r1.type.equals("Int")){            
                                if(Numerics.stringAsInt(r1.value) <= Numerics.stringAsInt(r2.value)){
                                   res.value = "T";   
                                }else{
                                    res.value = "F";
                                } 
                                 res.type = "Boolean";
                                 res.structure = "primative";      
                         }else {
                             if(r1.type.equals("Float")){
                                 
                                 if( Numerics.stringAsFloat(r1.value) <= Numerics.stringAsFloat(r2.value)){
                                      res.value = "T";
                                 }else{
                                      res.value = "F";
                                 }          
                                 res.structure = "Primative";
                                 res.type = "Float";                 
                             }else{
                                 if(r1.type.equals("String")){
                                    if(r1.value.compareTo(r2.value) <= 0){
                                        res.value = "T";
                                    }else{
                                         res.value = "F";
                                    }
                                 res.structure = "Primative";
                                 res.type = "String";    
                                 }
                             }
                             
                         }
                         }
                    }
                    scan.getNext();
                break;
                case "<":
                    scan.getNext();
                    if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                     
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( r1.type.equals("Int")){            
                                if(Numerics.stringAsInt(r1.value) < Numerics.stringAsInt(r2.value)){
                                   res.value = "T";   
                                }else{
                                    res.value = "F";
                                } 
                                 res.type = "Boolean";
                                 res.structure = "primative";      
                         }else {
                             if(r1.type.equals("Float")){
                                 
                                 if( Numerics.stringAsFloat(r1.value) < Numerics.stringAsFloat(r2.value)){
                                      res.value = "T";
                                 }else{
                                      res.value = "F";
                                 }          
                                 res.structure = "Primative";
                                 res.type = "Float";                 
                             }else{
                                 if(r1.type.equals("String")){
                                    if(r1.value.compareTo(r2.value) < 0){
                                        res.value = "T";
                                    }else{
                                         res.value = "F";
                                    }
                                 res.structure = "Primative";
                                 res.type = "String";    
                                 }
                             }
                             
                         }
                         }
                    }
                    scan.getNext();
                break;
                case ">":
                    scan.getNext();
                    if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                     
                         second = scan.currentToken;
                         resultValue r1;
                         r1 = Operand(first);
                         resultValue r2;
                         r2 = Operand(second);
                         if(bRun){
                         if( r1.type.equals("Int")){            
                                if(Numerics.stringAsInt(r1.value) > Numerics.stringAsInt(r2.value)){
                                   res.value = "T";   
                                }else{
                                    res.value = "F";
                                } 
                                 res.type = "Boolean";
                                 res.structure = "primative";      
                         }else {
                             if(r1.type.equals("Float")){
                                 
                                 if( Numerics.stringAsFloat(r1.value) > Numerics.stringAsFloat(r2.value)){
                                      res.value = "T";
                                 }else{
                                      res.value = "F";
                                 }          
                                 res.structure = "Primative";
                                 res.type = "Float";                 
                             }else{
                                 if(r1.type.equals("String")){
                                    if(r1.value.compareTo(r2.value) > 0){
                                        res.value = "T";
                                    }else{
                                         res.value = "F";
                                    }
                                 res.structure = "Primative";
                                 res.type = "String";    
                                 }
                             }
                             
                         }
                         }
                    }
                    scan.getNext();
                break;
            }
        }else{


            //Checks for operands followed by , or ) found in print(thing1, thing2);
            // if(scan.currentToken.tokenStr.equals(",") |scan.currentToken.tokenStr.equals(")")
            //        | scan.currentToken.tokenStr.equals(";")){
            if(scan.currentToken.primClassif == Token.SEPARATOR){
                if(bRun){
                  res = Operand(first);
                  return res;
                }
            }else{
                System.out.println("Error expected a separator");
                System.exit(-1);
            }
        }
     return res;
    }
    resultValue Operand(Token first){
        resultValue result = new resultValue();
        if(first.tokenStr.matches("-.*")){

                   String[] mSplit;
                   mSplit = first.tokenStr.split("-");
                   first.tokenStr = mSplit[1];
                   if(bRun){
                       resultValue negative = Operand(first);
                       if(Numerics.isInt(negative.value)) {
                           result.type = "Int";
                           result.structure = "Primative";
                           result.value = "-" + negative.value;
                       }else{
                          if(Numerics.isFloat(negative.value)){
                             result.type = "Float";
                             result.structure = "Primative";
                             result.value = "-" + negative.value;  
                           
                          }else
                          {
                              if(negative.value.equals("T")){
                                  result.type = "Bool";
                                  result.structure = "Primative";
                                  result.value = "F";
                              }else{
                                  if(negative.value.equals("F")){
                                  result.type = "Bool";
                                  result.structure = "Primative";
                                  result.value = "T";
                              }
                          }
                       }
                   
                   }
                   }
                  //   System.out.println("Need to get value of " + first + " negate it, and return it");
        }else{
                   if(bRun){
                       
                     if(first.subClassif == Token.IDENTIFIER){
                         // Class = (STIdentifier)sbTable.lookupSym(first).;
                       // System.out.println(sbTable.lookupSym(first).);
                          SMValue temp = sbTable.getStorageManager().get((STIdentifier)sbTable.lookupSym(first));
                         
                          //sbTable.getStorageManager().get((STIdentifier)sbTable.lookupSym(first));
            
                                 result.structure = "primative";
                                 result.value = temp.getString();
                                 result.type = ((STIdentifier) sbTable.lookupSym(first)).getDeclared().getSymbol();
                     }else{
                         switch(first.subClassif){
                             case Token.BOOLEAN:
                                 result.structure = "primative";
                                 result.value = first.tokenStr;
                                 result.type = "Bool";
                             break;
                             case Token.INTEGER:
                                 result.structure = "primative";
                                 result.value = first.tokenStr;
                                 result.type = "Int";
                             break;
                             case Token.FLOAT:
                                 result.structure = "primative";
                                 result.value = first.tokenStr;
                                 result.type = "FLOAT";
                             break;  
                              case Token.STRING:
                                 result.structure = "primative";
                                 result.value = first.tokenStr;
                                 result.type = "FLOAT";
                             break;  
                         }
                         //if(first.primClassif)
                    // System.out.println("Need to get value of*** " + first.tokenStr + " and return it");
                     }
                   }
                }
        return result;
    }
    void evalFunction()throws IOException, errorCatch{
        switch(scan.currentToken.subClassif){

            case Token.BUILTIN:
                if(scan.currentToken.tokenStr.equals("print")){
                    scan.getNext();
                    processPrint();
                }
        }
    }

    //expects to be ( token just after print
    void processPrint()throws IOException, errorCatch{
        //check for (
        if(scan.currentToken.tokenStr.equals("(")){

            scan.getNext();

            while(!scan.currentToken.tokenStr.equals(")")){
               if(scan.currentToken.subClassif != Token.IDENTIFIER){
                   //scan.currentToken.printToken();
                   if(bRun)
                       if(!scan.currentToken.tokenStr.equals(","))
                           System.out.print(Escapes.generateEscapeSequences(scan.currentToken.tokenStr) + " ");
               }else{
                  resultValue print;
                  print = simpleExpression();
                  if(bRun)
                     System.out.print(print.value  + " ");
               }

               //put this here to prevent one to many advances
               if(!scan.currentToken.tokenStr.equals(")"))
                  scan.getNext();
            }
            if(scan.currentToken.tokenStr.equals(")")){
                if(bRun)
                   System.out.print("\n") ;
                scan.getNext();
            }else{
                System.out.println("Error, expected a )");
                System.exit(-1);
            }

        }else{
            System.out.println("Error expected a (");
            System.exit(-1);
        }
    }

    //assumes if statement
    void ifStatement() throws IOException, errorCatch{
            Boolean bSwitch;
  

            if(bRun){
                bSwitch = true;
            }else{
                bSwitch = false;
            }

            scan.getNext();
            resultValue check;
            check = simpleExpression();
            if(check.value.equals("F")){
                bRun = false;
            } 
            if(scan.currentToken.tokenStr.equals(":")){
                scan.getNext();
                //System.out.println("For now, will always do statements, need to check if it will actually do them first");
                while(scan.currentToken.subClassif != Token.END){
                   statement();
                  if(scan.currentToken.tokenStr.equals("else")){
                    //  System.out.println("Found else switch run mode if switch is permited");
                      scan.getNext();
                      if(bSwitch)
                          bRun = !bRun;
                      if(scan.currentToken.tokenStr.equals(":")){
                          scan.getNext();
                      }
                      else{
                           System.out.println("Error expected a :");
                           System.exit(-1);
                      }
                      //System.out.println(scan.currentToken.tokenStr);
                   }
                }
                if(scan.currentToken.tokenStr.equals("endif")){
                    scan.getNext();
                    if(bSwitch)
                        bRun = true;
                }else{
                    System.out.println("Error no endif found");
                    System.exit(-1);
                }

            }else{
                System.out.println("Error expected a :");
                System.exit(-1);
            }
    }

    //assumes whule is current token
    void whileStatement() throws IOException, errorCatch{
        int line = scan.currentToken.iSourceLineNr;
        
        scan.getNext();
        resultValue test;
        test = simpleExpression();
        if(test.value.equals("F")){
                bRun = false;
        }
        if(scan.currentToken.tokenStr.equals(":")){
            scan.getNext();
            while(!scan.currentToken.tokenStr.equals("endwhile")) {
                statement();
           }
            
          
           if(scan.currentToken.tokenStr.equals("endwhile")){
               if(bRun){
                   scan.setLine(line);
                  
               }else{
                   scan.getNext();
                  
               }
           }
        }else{
            System.out.println("Error expected a :");
            System.exit(-1);
        }
        if(test.value.equals("F")){
                bRun = true;
        }
        
       
    }

}
