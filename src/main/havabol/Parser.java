
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

    Parser(){
         sbTable = new SymbolTable();  
    }
    
    Parser(SymbolTable sbTable){
         this.sbTable = sbTable;  
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
    
    
    void statement(Scanner scanner) throws IOException, errorCatch{
        
         //check what the current symbol is
        switch(scanner.currentToken.primClassif){
                   
           //Look for control tokens
           case Token.CONTROL:
                        
           //If the control token is a declare token, add it to the
           //Symbol table
           switch(scanner.currentToken.subClassif){
               
               //if I dentifier run declare staement code
               case Token.DECLARE:
                   
                   declareStatement(scanner);
           }
                           
           default:
                       // System.out.println(scanner.currentToken.tokenStr + "   Got here");
                    
                }
            
            
        
        
    }
    
    //Assumes that the current token is a control declaration ie. Int, Float...
    void declareStatement(Scanner scan) throws IOException, errorCatch{
        
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
}
