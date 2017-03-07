
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
        
         //check what the current symbol is
        switch(scan.currentToken.primClassif){
                   
           //Look for control tokens
           case Token.CONTROL:
                        
           //If the control token is a declare token, add it to the
           //Symbol table
           switch(scan.currentToken.subClassif){
               
               //if I dentifier run declare staement code
               case Token.DECLARE:
                   declareStatement();
                   
                   
           }
           break;
           case Token.OPERAND:
               
               //System.out.println(scanner.currentToken.tokenStr + " <<<<");
               //processExpression();
               
           break;               
           default:
                       //System.out.println(scanner.currentToken.tokenStr + "   Got here");
                    
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
    
    //Assumes that the token sent in is an operand identifier 
    /**
    void processExpression() throws IOException, errorCatch{
        
        //store the opearand for storing;
        String store = scan.currentToken.tokenStr;
        
        //checks for = sign                
        scan.getNext();
        
        System.out.println(scan.currentToken.tokenStr + " *****");
            //expression(scan);
        
    }
    **/
    
    
    /**
    private ResultValue expr(String endSeparator){
        scan.getNext();
        ResultValue res = products();
        ResultValue temp = new ResultValue();
        while(scan.currentToken.tokenStr.equals("+")){
        
           scan.getNext();
           if(scan.currentToken.primClassif != Token.OPERAND)
               //error
           temp = products();
           res= Utility.add(this, res, temp);
         }
         return res;
    }
    
    **/
    /**
    private ResultValue products() throws IOException, errorCatch
    {
        ResultValue res = operand();
        ResultValue temp = new ResultValue();
        while(scan.currentToken.tokenStr.equals(*){
              String operation = currentToken.tokenStr;
              scan.getNext();
              if(scan.currentToken.primCalssif != Token.OPERAND){
                 //error
              }
              temp = operand();
              res = Utility.multiply(this, res, temp);
        }
        return res;
     
    }
     
    /**
    private ResultValue operand() throws IOException, errorCatch
    {
        if(scan.currentToken.primClassif == Token.OPERAND){
            switch(scan.currentToken.subClassif){
                case Token.IDENTIFIER:
                    //Get value from the memory manager and return in
                    //res = storageMgr.getVariableValue(this,
                    //scan.currentToken.tokenStr);
                    //scan.getNext();
                    //return res;
                case Token.INTEGER:
                case Token.FLOAT:
                case Token.DATE:
                case Token.STRING:
                case Token.BOOLEAN:
                     res = scan.currentToken.toResult();
                     scan.getNext();
                     return res;
                    
            }
        }
        System.out.println("Error: bad Operator");
        return null;
    }
    **/
}
