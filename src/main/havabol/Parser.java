
package havabol;

import havabol.classify.Primary;
import havabol.classify.Subclass;
import havabol.sym.STEntry;
import java.io.IOException;

/**
 *
 * @author fish
 */
public class Parser {
    
    SymbolTable sbTable;
    Scanner scan;

    Parser(Scanner scan){
         sbTable = new SymbolTable();  
         this.scan = scan;
    }
    
    Parser(SymbolTable sbTable, Scanner scan){
         this.sbTable = sbTable;  
         this.scan = scan;
    }
    
    /**
     * THis checks to see if the Symbol currently exists in the symbol table
     * made this to simplify code later
     * 
     * @param token
     * @return true if symbol is in table or false if it is not 
     */
    Boolean checkSymbol(Token token){
        
        STEntry STEntry = sbTable.lookupSym(token);
        
        if(STEntry == null){
            return(false);
        }else{
            return(true);
        }
    }
    
    
    void statement() throws IOException, errorCatch{
       // System.out.println(scan.currentToken.tokenStr);
        switch(scan.currentToken.primClassif){
            
            case Token.CONTROL:
                
                switch(scan.currentToken.subClassif){
                    
                    case Token.DECLARE:
                        declareStatement();
                }
             break;
             
            case Token.OPERAND:
                evalExpression(scan);
            break;
            
            case Token.FUNCTION:
                 evalFunction(scan);
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
        
        scan.getNext();
                
        if(scan.currentToken.primClassif != Token.OPERAND && scan.currentToken.subClassif != Token.IDENTIFIER){
            System.out.println("Error expected and Identifier");
            System.exit(-1);
        }else{
            if(!checkSymbol(scan.currentToken)){
                sbTable.putSymbol(scan.currentToken, 
                new STEntry(scan.currentToken.tokenStr, Primary.OPERAND, Subclass.IDENTIFIER));
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
    void evalExpression(Scanner scan)throws IOException, errorCatch{
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
            simpleExpression(scan);
            System.out.println("Need to set value of " + first + " to the value from simple expression");
        }
    }
    //assumes current token is an operand 
    void simpleExpression(Scanner scan) throws IOException, errorCatch{
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
                         System.out.println("Need to multiply " + first + " by " + second + " and return it");
                         scan.getNext();
                     }
                break;
                     
            }
        }else{
            if(scan.currentToken.tokenStr.equals(";")){
               System.out.println("Need to get value of " + first + " and return it");
            }
            //Checks for operands followed by , or ) found in print(thing1, thing2);
             if(scan.currentToken.tokenStr.equals(",") |scan.currentToken.tokenStr.equals(")")){
                System.out.println("Need to get value of " + first + " and return it2");
            }
        }
        
    }
    
    void evalFunction(Scanner scan)throws IOException, errorCatch{
        switch(scan.currentToken.subClassif){
        
            case Token.BUILTIN:
                if(scan.currentToken.tokenStr.equals("print")){
                    scan.getNext();
                    processPrint(scan);
                }
        }
    }
    
    //expects to be ( token just after print
    void processPrint(Scanner scan)throws IOException, errorCatch{
        //check for (
        if(scan.currentToken.tokenStr.equals("(")){
            
            scan.getNext();
            
            while(!scan.currentToken.tokenStr.equals(")")){
               if(scan.currentToken.subClassif != Token.IDENTIFIER){
                   //scan.currentToken.printToken();
                   System.out.print(scan.currentToken.tokenStr);
               }else{
                   simpleExpression(scan);
                   System.out.println("Need to print what is returned by simpleExpression");
               }
               
               //put this here to prevent one to many advances
               if(!scan.currentToken.tokenStr.equals(")"))
                  scan.getNext();
            }
            if(scan.currentToken.tokenStr.equals(")")){             
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
}
