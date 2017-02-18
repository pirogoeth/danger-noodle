package havabol;
import java.io.*;
import java.util.*;

/**
 *
 * @author William Brandon Fisher mex208
 * 
 * This is the Scanner Class for Program 1
 * It contains the methods getNext(), getLine(), getToken()
 * This class also contains
 * <p>
 */
class Scanner{

    String sourceFileNm;             //stores the file name
    SymbolTable symbolTable;         //store the symbol table (useless now)
    char[] textLineM;                //stores the scaned in line
    int iSourceLineNr = 0;           //keeps track of line number
    int iColPos;                     //keeps track of current token starting point
    int iScanPos;                    //current scan position
    int iMaxPos;                     // the max positon of the line
    Token currentToken;              //holds the current token
    String szLine;                   //helps to get line
    String szQuote;                  // helps to determine if " or ' is used
    String error;                    //helps in building errors
    private final File fInput;           //The file to open
    private final FileReader fReader;    //File reader to get lines from
    private final BufferedReader brBuffer;    //Buffer to get lines from
    private final static String delimiters = " \t;:()\'\"=!<>+-*/[]#,^\n";   //delimiters to separate tokens 
    private final static String newOperators = "and or not in notin"; 
    private final static String controlDeclare = "Int Float String Bool";
    private final static String controlFLow = "if else while for";
    private final static String controlEnd = "endif endwhile endfor";
    /**
     * Constructor for the scanner object
     * <p>
     * @param sourceFileNm  file to get lines from
     * @param symbolTable   symbol table not in use yet
     * @return   new Scanner object
     * @throws IOException  throws an error if trouble reading from file
     *  
     */
    Scanner(String sourceFileNm, SymbolTable symbolTable) throws IOException {
 
      this.symbolTable = symbolTable;       //Symbol table 
      this.sourceFileNm = sourceFileNm;     //stores the file name
      fInput = new File(sourceFileNm);      //open the file
      szQuote = new String();               //make a empty string for later
      error = new String();                 //make a empty string for errors later
      fReader = new FileReader(fInput);     //create a new filereader
      brBuffer = new BufferedReader(fReader);   //create a newuffered reader
      textLineM = getLine();                    //get a line to store for scanning later
     
      
    }

    
    
     
    /**
     * Checks the next token of the line and returns a
     * String of the token found.This also classifies 
     * the token while scanning it
     * <p>
     * 
     * @return currentToken.tokenStr returns the current token string
     * @throws IOException  Exception if trouble getting a new line
     * @throws errorCatch   Exception if an error is found while scanning a token
     */
    String getNext() throws IOException, errorCatch{
        //clear token and get a new one
        currentToken = new Token();
        currentToken.tokenStr = getToken(textLineM, currentToken);
        
        //if the token is empty, get the next line and return if no new line found
        if(currentToken.tokenStr.equals("")){
            
            textLineM = getLine();
            
            if(textLineM ==null){
               return "";
            }
              
            currentToken.tokenStr = getToken(textLineM, currentToken);
            
        }
       
       return currentToken.tokenStr;
    }
    
    /**
     * This method reads a line from the file stored in the scanner object
     * returns the line as a char[] or throws an exception if trouble reading
     * from the file
     * <p>
     * 
     * @return textLineM  returns the line read from file as a char[]
     * @throws IOException  throws an exception if trouble reading from file
     */
    char[] getLine() throws IOException{
        
        iSourceLineNr++;
        iColPos = 0;
        
        //get a new line
        szLine = brBuffer.readLine();
        
        //check if the new line is null
        if(szLine == null){
          return null;
        }
        
        //print a header for the line
        System.out.format("%3d %s\n", iSourceLineNr, szLine );
        
        //if the line is blank, get a new line after printing a header
        if(szLine.matches("\\s*")){
            getLine();
        }
        
        //calculate the max length and convert to char[] for return 
        iMaxPos = szLine.length();
        textLineM = szLine.toCharArray();
        return textLineM;   
    }
    
    
    /**
     * This method get the next token from the passed in char[]
     * It uses the iScanPos to determine what is being scanned in.
     * It throws and error if an invalid, string, float, or INT are found
     * <p>
     * 
     * @param lineM This is the line to get a token from
     * @param token The token to have it's data uploaded
     * @return szBuffer This is the string of the token found
     * @throws errorCatch  Throws a custom error if one is found
     */
    String getToken(char[] lineM, Token token) throws errorCatch{
        
        String szBuffer = new String();  //Stores the token string
        iScanPos = iColPos;              //sets the scan position to the current position
        
        while(iScanPos != iMaxPos){
           
            //Checks for delimeters
            if(!delimiters.contains(Character.toString(lineM[iScanPos]))){
               
               //checks for numbers
               if(Character.toString(lineM[iScanPos]).matches("\\d")){
                  
                  //If first character, set the token to integer before adding the char
                  if(szBuffer.equals("")){
                      token.primClassif = Token.OPERAND;
                      token.subClassif = Token.INTEGER;
                      szBuffer += Character.toString(lineM[iScanPos]);
                      iScanPos++;
                
                  }else{
                      
                      //CAllows for .12 float numbers
                      if(szBuffer.equals(".")){
                          token.primClassif = Token.OPERAND;
                          token.subClassif = Token.FLOAT; 
                      }
                      
                      szBuffer += Character.toString(lineM[iScanPos]);
                      iScanPos++;
                  }
                 
               //Found a character not a number or delimeter
               }else{
                   
                    //Check for specific characters
                    switch(Character.toString(lineM[iScanPos])){
                      
                        //handles .    
                        case ".":
           
                            switch(token.subClassif){
                                
                                //if subClass is INT, change to FLOAT before adding , to buffer
                                case Token.INTEGER:
                                   
                                   token.subClassif = Token.FLOAT; 
                                   szBuffer += Character.toString(lineM[iScanPos]);
                                   iScanPos++;   
                                   break;
                                
                                //If already a FLOAT, print an error for double .    
                                case Token.FLOAT:
                                    error += "Imcompatable Float\n" + "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;    
                                    throw new errorCatch(error);
                                    
                                //Allows for . in String literals
                                default:
                                    szBuffer += Character.toString(lineM[iScanPos]);
                                    iScanPos++;
                                    
                            }
                            break;
                            
                        default:
                          
                          //Handles adding characters to buffer and error trapping
                          if(token.subClassif == Token.STRING){
                              szBuffer += Character.toString(lineM[iScanPos]);
                              iScanPos++; 
                              break;
                          }
                          
                          //If token is float, characters cause error
                          if(token.subClassif == Token.FLOAT){
                              error += "Imcompatable Float\n"+ "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;    
                              throw new errorCatch(error);
                          }
                          
                          //If token is int, characters cause error
                          if(token.subClassif == Token.INTEGER){
                              error += "Imcompatable Integer\n"+ "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;    
                              throw new errorCatch(error);
                          }
                          
                             //update token and buffer
                             token.primClassif = Token.OPERAND;
                             token.subClassif = Token.IDENTIFIER;
                             szBuffer += Character.toString(lineM[iScanPos]);
                             iScanPos++;  
                    }

                }

            //handles delimeters   
            }else{
                
                if(token.subClassif == Token.STRING){
                    
                    // Checks for \" in string literals
                    if(Character.toString(lineM[iScanPos]).equals(szQuote) && !Character.toString(lineM[iScanPos - 1]).equals("\\") ){
                        iScanPos++;
                        iColPos = iScanPos;
                        szQuote = "";
                        return szBuffer; 
                    }
                        //adds delemeters to string literals
                        szBuffer += Character.toString(lineM[iScanPos]);
                        iScanPos++;
                        continue;
                }
     
                //check for certain delemiters
                switch(Character.toString(lineM[iScanPos])){
                    
                    //spaces return current buffer as token and are skipped over
                    case " ":
                        if(szBuffer.isEmpty()){
                            iScanPos++; 
                            iColPos = iScanPos;
                            continue;
                        }else {
                            iScanPos++;
                            iColPos = iScanPos;
                            
                            //Handles the new operators
                             
                            if(newOperators.contains(szBuffer)){
                                token.primClassif = Token.OPERATOR;
                                token.subClassif = 0;
                            }     
                            
                            checkBuffer(szBuffer, token);  
                           
   
                            
                            
                            //System.out.println(szBuffer);
                            return szBuffer;
                        }
                    //handles the " delimeter    
                    case "\"":
                         if(token.subClassif != Token.STRING){
                            token.subClassif = Token.STRING; 
                            token.primClassif = Token.OPERAND;
                            iScanPos++;
                            szQuote = "\"";
                            continue;
                         }
                         
                    //handles the ' delmiter
                    case "\'":
                         if(token.subClassif != Token.STRING){
                            token.subClassif = Token.STRING; 
                            token.primClassif = Token.OPERAND;
                            iScanPos++;
                            szQuote = "\'";
                            continue;
                         }
                         
                    //handles the ; delemiter    
                    case ";" :
                        //Empties the buffer before processing itself
                        //Ends the reading of a line
                        if(szBuffer.isEmpty()){
                            
                           szBuffer += Character.toString(lineM[iScanPos]);
                           if(iScanPos <= iMaxPos){
                              iScanPos++;
                              iColPos = iScanPos;
                              token.primClassif = Token.SEPARATOR;
                              return szBuffer;
                           }else{
                               iScanPos++;
                               return ";";
                           }
                        }else{
                           iColPos = iScanPos; 
                           if(szBuffer.equals("t")){
                                token.primClassif = Token.OPERATOR;
                                token.subClassif = Token.BOOLEAN;
                            }else{
                               checkBuffer(szBuffer, token);     
                            }
                           return szBuffer;
                        }                        
                    //Handles and other delemiter
                    default:
                        if(szBuffer.isEmpty()){
                            
                           szBuffer += Character.toString(lineM[iScanPos]);
                           
                           //Checks for Operators, if not found, it is classified as separator
                           if("+ - * / < > ! = # ^ and or not in notin".contains(Character.toString(lineM[iScanPos]))){
                               token.primClassif = Token.OPERATOR;
                           }else{
                               token.primClassif = Token.SEPARATOR;
                           }
                           iScanPos++;
                           iColPos = iScanPos;
                           return szBuffer;
                        }else{
                           iColPos = iScanPos; 
                           checkBuffer(szBuffer, token);  
                           return szBuffer;
                        }
                }
            }
        }
        
        //Error if String Literal is not ended by the end of the line
        if(token.subClassif == Token.STRING && iScanPos == iMaxPos){
            error += "Unclosed String Literal\n" + "Did not find a second " + szQuote +
                    "\nString Literals must be ended on same line\n" + "Error on line " + iSourceLineNr 
                    + "\nAt token in position " + iColPos;    
            throw new errorCatch(error);
   
        }
        return "";
    }
    
    
    void checkControl(String szWord, Token token){
        
        if(controlDeclare.contains(szWord)){
            token.primClassif = Token.CONTROL;
            token.subClassif = Token.DECLARE;
            return;
        }
        
        if(controlFLow.contains(szWord)){
            token.primClassif = Token.CONTROL;
            token.subClassif = Token.FLOW;
            return;
        }
                            
        if(controlEnd.contains(szWord)){
            token.primClassif = Token.CONTROL;
            token.subClassif = Token.END;
            return;
        }
        
    }
    
    void checkBuffer(String szWord, Token token){
        if(checkBoolean(szWord, token) == false){
               checkControl(szWord, token);
               checkFunction(szWord, token);                                 
         } 
    }
    /**
     * This only checks for print now, this was added to make things 
     * easier later
     * @param szWord
     * @param token 
     */
    void checkFunction(String szWord, Token token){
        if(szWord.equals("print")){
            token.primClassif = Token.FUNCTION;
            token.subClassif = Token.BUILTIN;
        }
    }
    
    boolean checkBoolean(String szWord, Token token){
        if(szWord.equals("t")){
              token.primClassif = Token.OPERAND;
              token.subClassif = Token.BOOLEAN;
              return true;
         }
        return false;
}
        
}


/**
 * This small class allows for custom error messages
 * @author fish
 */
class errorCatch extends Exception{
    public errorCatch(String msg){
        super(msg);
    }
}
