
package havabol;

import havabol.classify.*;
import havabol.sym.*;

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

    /**
     * Uses the global symbol table if none is provided.
     */
    Parser(Scanner scan){
         sbTable = SymbolTable.getGlobal();
         this.scan = scan;
    }

    Parser(SymbolTable sbTable, Scanner scan){
         this.sbTable = sbTable;
         this.scan = scan;
         bRun = true;
         btest = true;
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

                    System.out.println("Declared " + scan.tokenList.get(scan.tokenList.size()-2).tokenStr);

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
        String first = "";
        if(checkSymbol(scan.currentToken)){
            first = scan.currentToken.tokenStr;
        }else{
            System.out.println("Operand not declared");
            System.exit(-1);
        }

        scan.getNext();
        if(scan.currentToken.tokenStr.equals("=")){
            scan.getNext();
            simpleExpression();
            if(bRun)
               System.out.println("Need to set value of " + first + " to the value from simple expression");
        }
    }
    //assumes current token is an operand
    void simpleExpression() throws IOException, errorCatch{
        String first = "";
        String second = "";

        first = scan.currentToken.tokenStr;

        scan.getNext();

        if(scan.currentToken.primClassif == Token.OPERATOR){
            switch(scan.currentToken.tokenStr){
                case "^":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken.tokenStr;
                         if(bRun)
                            System.out.println("Need to raise " + first + " by power of " + second + " and return it");
                         scan.getNext();
                     }
                break;
                case "*":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken.tokenStr;
                         if(bRun)
                            System.out.println("Need to multiply " + first + " by " + second + " and return it");
                         scan.getNext();
                     }
                break;
                case "-":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken.tokenStr;
                         if(bRun)
                            System.out.println("Need to subtract " + second + " from " + first + " and return it");
                         scan.getNext();
                     }
                break;
                case "==":
                    scan.getNext();
                    if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken.tokenStr;
                         if(bRun)
                            System.out.println("Need to check equality of " + first + " and " + second + " and return True or Flase");
                         scan.getNext();
                     }
                break;
                case "!=":
                    scan.getNext();
                    if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken.tokenStr;
                         if(bRun)
                            System.out.println("Need to check inequality of " + first + " and " + second + " and return True or Flase");
                         scan.getNext();
                     }
                break;
                case "+":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken.tokenStr;
                         if(bRun)
                            System.out.println("Need to add " + first + " and " + second + " and return it");
                         scan.getNext();
                     }
                break;
                case ">=":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken.tokenStr;
                         if(bRun)
                            System.out.println("Need to check if " + first + " is greater than or equal to " + second + " and return Ture or False");
                         scan.getNext();
                     }
                break;
                case "<=":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken.tokenStr;
                         if(bRun)
                            System.out.println("Need to check if " + first + " is less than or equal to " + second + " and return Ture or False");
                         scan.getNext();
                     }
                break;
                case "<":
                     scan.getNext();
                     if(scan.currentToken.primClassif != Token.OPERAND){
                         System.out.println("Error, expected an operand");
                         System.exit(-1);
                     }else{
                         second = scan.currentToken.tokenStr;
                         if(bRun)
                            System.out.println("Need to check if " + first + " is less than " + second + " and return Ture or False");
                         scan.getNext();
                     }
                break;
            }
        }else{


            //Checks for operands followed by , or ) found in print(thing1, thing2);
            // if(scan.currentToken.tokenStr.equals(",") |scan.currentToken.tokenStr.equals(")")
            //        | scan.currentToken.tokenStr.equals(";")){
            if(scan.currentToken.primClassif == Token.SEPARATOR){
                Operand(first);
            }else{
                System.out.println("Error expected a separator");
                System.exit(-1);
            }
        }

    }
    void Operand(String first){
        if(first.matches("-.*")){

                   String[] mSplit;
                   mSplit = first.split("-");
                   first = mSplit[1];
                   if(bRun)
                     System.out.println("Need to get value of " + first + " negate it, and return it");
                }else{
                   if(bRun)
                     System.out.println("Need to get value of " + first + " and return it");
                }
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
                      System.out.print(scan.currentToken.tokenStr);
               }else{
                   simpleExpression();
                   if(bRun)
                      System.out.println("Need to print what is returned by simpleExpression");
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
            simpleExpression();
            if(scan.currentToken.tokenStr.equals(":")){
                scan.getNext();
                //System.out.println("For now, will always do statements, need to check if it will actually do them first");
                while(scan.currentToken.subClassif != Token.END){
                   statement();
                  if(scan.currentToken.tokenStr.equals("else")){
                      System.out.println("Found else switch run mode if switch is permited");
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
        simpleExpression();
        if(scan.currentToken.tokenStr.equals(":")){
            scan.getNext();
            while(!scan.currentToken.tokenStr.equals("endwhile")) {
                statement();
           }
           if(btest){
               btest = false;
               System.out.println("Srcond while ------------\n\n");
               scan.setLine(line);
           }
           if(scan.currentToken.tokenStr.equals("endwhile")){
               scan.getNext();
           }
        }else{
            System.out.println("Error expected a :");
            System.exit(-1);
        }
    }

}
