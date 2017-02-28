
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
        
        while(!scanner.currentToken.tokenStr.equals(";")){
            
            //Checks if the token exists in the symbol table
            if(checkSymbol(scanner.currentToken)){
                
                //check what the current symbol is
                switch(scanner.currentToken.primClassif){
                    case Token.CONTROL:
                        scanner.getNext();
                        declareStatement(scanner.currentToken);
                        break;
                    default:
                        System.out.println(scanner.currentToken.tokenStr + "   Got here");
                    
                }
            }else{
                System.out.println("Error, symbol does not exist");
            }
            scanner.getNext();
        }
        
    }
    
    
    void declareStatement(Token token){
        
        //Chekc to make sure correct type of token was passed in
        if(token.primClassif != Token.OPERAND && token.subClassif != Token.IDENTIFIER){
            System.out.println("Error expected identifier");
            System.exit(-1);
        }
        
        //make sure symbol table entry doesn not already exist
        if(!checkSymbol(token)){
            sbTable.putSymbol(token, new STEntry(token.tokenStr, Primary.OPERAND, Subclass.IDENTIFIER));
        }else{
            System.out.println("Error double declaration");
            System.exit(-1);
        }

        
    }
}
