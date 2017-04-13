package havabol;

import java.io.*;
import java.util.*;

import havabol.classify.*;
import havabol.sym.*;
import havabol.util.Escapes;
import havabol.util.debugObj;

/**
 *
 * @author William Brandon Fisher mex208
 *
 * This is the Scanner Class for Program 1
 * It contains the methods getNext(), getLine(), getToken()
 * This class also contains
 * <p>
 */
public class Scanner{

    private static Scanner instance = null;

    public static Scanner getInstance() {
        return instance;
    }

    public String sourceFileNm;             //stores the file name
    SymbolTable symbolTable;         //store the symbol table (useless now)
    String checkLine;
    char[] textLineM;                //stores the scaned in line
    public int iSourceLineNr = 0;           //keeps track of line number
    public int iColPos = 0;                     //keeps track of current token starting point
    public int iScanPos;                    //current scan position
    public int iMaxPos;                     // the max positon of the line
    public Token currentToken = new Token();              //holds the current token
    public Token nextToken = new Token();              //holds the current token
    String szLine;                   //helps to get line
    String szQuote;                  // helps to determine if " or ' is used
    String szBrace = new String();                  //helps with () | [] | {}
    String error;                    //helps in building errors
    private final File fInput;           //The file to open
    private final FileReader fReader;    //File reader to get lines from
    private final BufferedReader brBuffer;    //Buffer to get lines from
    private final static String delimiters = " \t;:()\'\"=!<>+-*/[]#,^\n{}";   //delimiters to separate tokens
    private final static String[] newOperators = {"and", "or", "not", "in", "notin", "to"};  //new operators added for part 2
    private final static String[] controlDeclare = {"Int", "Float", "String", "Bool"}; //control declare values
    private final static String[] controlFLow = {"if", "else", "while", "for"};        //control flow values
    private final static String[] controlEnd = {"endif", "endwhile", "endfor"};        //control end values
    ArrayList <Token> tokenList = new ArrayList<>();
    ArrayList <String> lineList = new ArrayList<>();
    debugObj debug = debugObj.get();

    /**
     * Constructor for the scanner object
     * <p>
     * @param sourceFileNm  file to get lines from
     * @param symbolTable   symbol table not in use yet
     * @return   new Scanner object
     * @throws IOException  throws an error if trouble reading from file
     *
     */
    public Scanner(String sourceFileNm) throws IOException, errorCatch {
      this(new File(sourceFileNm));
    }

    public Scanner(File f) throws IOException, errorCatch {
      this.sourceFileNm = f.getName();
      this.symbolTable = SymbolTable.getGlobal();       //Symbol table
      fInput = f;                                       //open the file
      szQuote = new String();                           //make a empty string for later
      error = new String();                             //make a empty string for errors later
      fReader = new FileReader(fInput);                 //create a new filereader
      brBuffer = new BufferedReader(fReader);           //create a newuffered reader
      readFile();                                       //Get the lines from the file
      textLineM = getLine();                            //get a line to store for scanning later
      this.getNext();

      instance = this;
    }

    /**
     * Gets two tokens if no tokens. If the current token is empty, gets
     * a new line and then gets two tokens. Allows access to the current and
     * next tokens
     * <p>
     *
     * @return currentToken.tokenStr returns the current token string
     * @throws IOException  Exception if trouble getting a new line
     * @throws errorCatch   Exception if an error is found while scanning a token
     */
    public String getNext() throws IOException, errorCatch{

        //get the first two tokens
        //if(iSourceLineNr == 1 && iColPos == 0 ){
        //System.out.println(textLineM);
        if(currentToken.tokenStr.equals("") && nextToken.tokenStr.equals("")){
            currentToken.tokenStr = getToken(textLineM, currentToken);
            nextToken.tokenStr = getToken(textLineM, nextToken);
            //if the current token is null, throw an error
            if(currentToken.tokenStr == null){
            throw new errorCatch(error);
            }

            //check for comments or double operators
            checkComment(currentToken, nextToken, textLineM);
            checkDoubleOperator(currentToken, nextToken, textLineM);
            checkUnaryMinus(currentToken, nextToken, textLineM);

        }else{
            //store the next token into current token and get a new
            //  next token
            currentToken = nextToken;
            nextToken = new Token();
            nextToken.tokenStr = getToken(textLineM, nextToken);

            //if the current token is null, throw an error
            if(currentToken.tokenStr == null){
                throw new errorCatch(error);
            }

            //check for comments or double operators
            checkComment(currentToken, nextToken, textLineM);
            checkDoubleOperator(currentToken, nextToken, textLineM);
            checkUnaryMinus(currentToken, nextToken, textLineM);
        }

        //if the current token is empty, get the next line and return if no new line found
        if(currentToken.tokenStr.equals("")){

            textLineM = getLine();
            if(textLineM== null){
               return "";
            }

            //Get two new tokens
            currentToken.tokenStr = getToken(textLineM, currentToken);
            nextToken.tokenStr = getToken(textLineM, nextToken);
            checkComment(currentToken, nextToken, textLineM);
            checkDoubleOperator(currentToken, nextToken, textLineM);
            checkUnaryMinus(currentToken, nextToken, textLineM);

            //if the current token is null, throw an error
            if(currentToken.tokenStr == null){
                throw new errorCatch(error);
            }

        }
       //return the current token
       currentToken.iSourceLineNr = iSourceLineNr;
       nextToken.iSourceLineNr = iSourceLineNr;


       tokenList.add(currentToken);
       if ( this.debug.bShowToken ) {
           System.out.println("\t\t... " + currentToken.tokenStr);
       }

       return currentToken.tokenStr;
    }

    public void readFile() throws IOException{
        String line;
        line = brBuffer.readLine();
        while(line != null){
            lineList.add(line);
            line = brBuffer.readLine();
        }

    }

    public void setLine(int lineNumber) throws IOException, errorCatch{
        //did this because using arraylist
        iSourceLineNr = lineNumber -1;
        currentToken.tokenStr = "";
        nextToken.tokenStr = "";
        textLineM = getLine();
        getNext();
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
    public char[] getLine() throws IOException{

        //col pos
        iColPos = 0;

        if(iSourceLineNr < lineList.size()){
        //get a new line
        szLine = lineList.get(iSourceLineNr);
        }else{
            return null;
        }
        //System.out.println(szLine);
        //Update to line pos (did this because using array list now)
        iSourceLineNr++;


        //check if the new line is null
        if(szLine == null){
          return null;
        }

        //print a header for the line
       // System.out.format("%3d %s\n", iSourceLineNr, szLine);

        //if the line is blank, get a new line after printing a header
        if(szLine.matches("\\s*")){
            if(getLine() == null){
               return null;
            }
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
    @SuppressWarnings("fallthrough")
    public String getToken(char[] lineM, Token token) throws errorCatch{

        String szBuffer = new String();  //Stores the token string
        iScanPos = iColPos;              //sets the scan position to the current position

        while(iScanPos != iMaxPos){

            //Checks for delimeters
            if(!delimiters.contains(Character.toString(lineM[iScanPos]))){

               //checks for numbers
               if(Character.toString(lineM[iScanPos]).matches("\\d")){

                  //If thefirst character, set the token to integer before adding the char
                  if(szBuffer.equals("")){
                      if(token.subClassif != Token.STRING){
                         token.primClassif = Token.OPERAND;
                         token.subClassif = Token.INTEGER;
                      }
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
                                    error += "Imcompatible Float\n" + "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;
                                    return null;

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
                              error += "Imcompatible Float\n"+ "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;
                              return null;
                          }

                          //If token is int, characters cause error
                          if(token.subClassif == Token.INTEGER){
                              error += "Imcompatible Integer\n"+ "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;
                              return null;
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
                        token.iColPos = iColPos;
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

                    //handles spaces
                    case " ":

                        //update the scan pos and skip the space
                        if(szBuffer.isEmpty()){
                            iScanPos++;
                            iColPos = iScanPos;
                            continue;
                        }else {
                            token.iColPos = iColPos;
                            //update the scan pos and return the buffer
                            iScanPos++;
                            iColPos = iScanPos;

                            //Handles the new operators
                            for(String szOperator : newOperators){
                                if(szOperator.equals(szBuffer)){
                                   token.primClassif = Token.OPERATOR;
                                   token.subClassif = 0;
                                }
                            }

                            //check the buffer for new control values or function values
                            checkBuffer(szBuffer, token);

                            //return the buffer
                            return szBuffer;
                        }

                    //handles the " delimeter
                    case "\"":
                         if(token.subClassif != Token.STRING){
                            token.subClassif = Token.STRING;
                            token.primClassif = Token.OPERAND;
                            iScanPos++;
                            
                            if(szQuote == "\"" && szBuffer.isEmpty()){
                                szBuffer += "";
                            }
                            szQuote = "\"";
                            continue;
                         }

                    //handles the ' delmiter
                    case "\'":
                         if(token.subClassif != Token.STRING){
                            token.subClassif = Token.STRING;
                            token.primClassif = Token.OPERAND;
                            iScanPos++;
                            if(szQuote == "\'" && szBuffer.isEmpty()){
                                szBuffer += "";
                            }
                            szQuote = "\'";
                            continue;
                        }

                    //handles (
                    case "(":
                          token.iColPos = iColPos;
                          if(szBuffer.isEmpty()){

                           szBuffer += Character.toString(lineM[iScanPos]);
                           if(iScanPos <= iMaxPos){
                              iScanPos++;
                              iColPos = iScanPos;
                              token.primClassif = Token.SEPARATOR;
                              //this helps with checking for matching braces
                              szBrace += "(";
                              return szBuffer;
                           }/*else{
                               System.out.println("I got here!!!!!");
                               token.iColPos = iColPos;
                               iScanPos++;
                               szBrace = "(";
                               return "(";
                           }*/
                        }else{
                           token.iColPos = iColPos;
                           iColPos = iScanPos;
                           checkBuffer(szBuffer, token);
                           return szBuffer;
                        }

                    //handles )
                    case ")":
                        token.iColPos = iColPos;
                        //checks that a ( was found
                        if(szBrace.endsWith("(")){
                            if(szBuffer.isEmpty()){
                             szBuffer += Character.toString(lineM[iScanPos]);
                            if(iScanPos <= iMaxPos){
                                iScanPos++;
                                iColPos = iScanPos;
                                token.primClassif = Token.SEPARATOR;
                                szBrace = szBrace.substring(0, szBrace.length() - 1);
                                return szBuffer;
                            }/*else{
                                iScanPos++;
                                szBrace = "";
                                return ")";
                            }*/
                        }else{
                           iColPos = iScanPos;
                           checkBuffer(szBuffer, token);
                           return szBuffer;
                        }
                        //( was not found
                        }else{
                            //accounts for a missing (
                            if(szBrace.isEmpty()){
                                 error = "missing (\n"+ "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;
                                 return null;
                            //counts for a mismatched bracket, { followed by )
                            }else{
                                error = "mismatch brace\n" + szBrace + " was found first\n" +  "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;
                                return null;

                            }

                        }

                    //handles { same as ( but differnet symbol
                    case "{":
                          token.iColPos = iColPos;
                          if(szBuffer.isEmpty()){

                           szBuffer += Character.toString(lineM[iScanPos]);
                           if(iScanPos <= iMaxPos){
                              iScanPos++;
                              iColPos = iScanPos;
                              token.primClassif = Token.SEPARATOR;
                              //this helps with checking for matching braces
                              szBrace += "{";
                              return szBuffer;
                           }/*else{
                               iScanPos++;
                               szBrace = "{";
                               return "{";
                           }*/
                        }else{
                           iColPos = iScanPos;
                           checkBuffer(szBuffer, token);
                           return szBuffer;
                        }

                    case "}":
                        token.iColPos = iColPos;
                        //checks that a ( was found
                        if(szBrace.endsWith("{")){
                            if(szBuffer.isEmpty()){
                                szBuffer += Character.toString(lineM[iScanPos]);
                                if(iScanPos <= iMaxPos){
                                    iScanPos++;
                                    iColPos = iScanPos;
                                    token.primClassif = Token.SEPARATOR;
                                    szBrace = szBrace.substring(0, szBrace.length() - 1);
                                    return szBuffer;
                                }/*else{
                                    iScanPos++;
                                    szBrace = "";
                                    return "}";
                                }*/
                            }else{
                                iColPos = iScanPos;
                                checkBuffer(szBuffer, token);
                                return szBuffer;
                            }
                            //{ was not found
                        }else{
                            //accounts for a missing {
                            if(szBrace.isEmpty()){
                                error = "missing {\n"+ "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;
                                return null;
                                //counts for a mismatched bracket, [ followed by }
                            }else{
                                error = "mismatch brace\n" + szBrace + " was found first\n" +  "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;
                                return null;

                            }

                        }

                    case "[":
                        token.iColPos = iColPos;
                        if(szBuffer.isEmpty()){

                            szBuffer += Character.toString(lineM[iScanPos]);
                            if(iScanPos <= iMaxPos){
                                iScanPos++;
                                iColPos = iScanPos;
                                token.primClassif = Token.SEPARATOR;
                                //this helps with checking for matching braces
                                szBrace += "[";
                                return szBuffer;
                            }/*else{
                                iScanPos++;
                                szBrace = "[";
                                return "[";
                            }*/
                        }else{
                            iColPos = iScanPos;
                            checkBuffer(szBuffer, token);
                            return szBuffer;
                        }

                    case "]":
                        token.iColPos = iColPos;
                        //checks that a ( was found
                        if(szBrace.endsWith("[")){
                            if(szBuffer.isEmpty()){

                                szBuffer += Character.toString(lineM[iScanPos]);
                                if(iScanPos <= iMaxPos){
                                    iScanPos++;
                                    iColPos = iScanPos;
                                    token.primClassif = Token.SEPARATOR;
                                    szBrace = szBrace.substring(0, szBrace.length() - 1);
                                    return szBuffer;
                                }/*else{
                                    iScanPos++;
                                    szBrace = "";
                                    return "]";
                                }*/
                            }else{
                               iColPos = iScanPos;
                               checkBuffer(szBuffer, token);
                               return szBuffer;
                            }
                        //[ was not found
                        }else{
                            //accounts for a missing [
                            if(szBrace.isEmpty()){
                                 error = "missing [\n"+ "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;
                                 return null;
                            //counts for a mismatched bracket, { followed by ]
                            }else{
                                error = "mismatch brace\n" + szBrace + " was found first\n" +  "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;
                                return null;

                            }
                        }

                    //handles the ; delemiter
                    case ";" :
                        token.iColPos = iColPos;
                        //Empties the buffer before processing itself
                        //Ends the reading of a line
                        if(szBuffer.isEmpty()){

                           szBuffer += Character.toString(lineM[iScanPos]);
                           if(iScanPos <= iMaxPos){
                              iScanPos++;
                              iColPos = iScanPos;
                              token.primClassif = Token.SEPARATOR;
                              if(!szBrace.isEmpty()){
                                error = "A brace wasn't closed\n" + szBrace + " was found\n" +  "Error on line " + iSourceLineNr + "\nAt token in position " + iColPos;
                                return null;

                              }
                              return szBuffer;
                           }else{
                               iScanPos++;

                               return ";";

                           }
                        }else{
                           iColPos = iScanPos;
                           checkBuffer(szBuffer, token);

                           return szBuffer;
                        }
                    //Handles and other delemiter
                    default:
                        token.iColPos = iColPos;
                        if(szBuffer.isEmpty()){

                           szBuffer += Character.toString(lineM[iScanPos]);

                           //Checks for Operators, if not found, it is classified as separator
                           if("+ - * / < > ! = # ^ and or not in notin to".contains(Character.toString(lineM[iScanPos]))){
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
            error = "Unclosed String Literal\n" + "Did not find a second " + szQuote +
                    "\nString Literals must be ended on same line\n" + "Error on line " + iSourceLineNr
                    + "\nAt token in position " + iColPos;
            return null;

        }

        //This accounts for lines that do not end with a ;
        if(iColPos != iMaxPos){
            token.iColPos = iColPos;
            iColPos = iScanPos;
            return szBuffer;
        }else{
             return "";
        }
    }

    /**
     * This method checks the newly added control declare, flow, and end values and sets
     * the token values if appropriate
     * <p>
     * @param szWord  the token str to check
     * @param token   the token to update the information
     */
    private void checkControl(String szWord, Token token){

        for(String temp : controlDeclare){
           if(szWord.matches(temp)){
               token.primClassif = Token.CONTROL;
               token.subClassif = Token.DECLARE;
               return;
           }
        }

        for(String temp : controlFLow){
           if(szWord.matches(temp)){
               token.primClassif = Token.CONTROL;
               token.subClassif = Token.FLOW;
               return;
           }
        }
        for(String temp : controlEnd){
           if(szWord.matches(temp)){
               token.primClassif = Token.CONTROL;
               token.subClassif = Token.END;
           }
        }

    }

    /**
     * This method calls two other methods to check for type of token.
     * This was made to save time typing these two lines multiple times
     * <p>
     * @param szWord  the token str to check
     * @param token   the token to update the information
     */
    private void checkBuffer(String szWord, Token token){
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
    private void checkFunction(String szWord, Token token){
        STEntry funcSym = symbolTable.lookupSym(szWord);
        if ( funcSym == null ) {
            return;
        }

        if ( funcSym instanceof STFunction ) {
            token.primClassif = funcSym.getPrimaryClass().getCid();
            token.subClassif = funcSym.getSubClass().getCid();
        }
    }

    /**
     * Checks if the token is either t or f and therefore a boolean
     * <p>
     * @param szWord
     * @param token
     * @return boolean  true or false if the token is t or f
     */
    private boolean checkBoolean(String szWord, Token token){
        if(szWord.equals("T") || szWord.equals("F")){
              token.primClassif = Token.OPERAND;
              token.subClassif = Token.BOOLEAN;
              return true;
         }
        return false;
    }

    /**
     * CHecks for comments
     *
     * @param current first token
     * @param next    next token
     * @param line    used to update the scan line if comment found
     * @throws IOException
     * @throws errorCatch
     */
    private void checkComment(Token current, Token next, char[] line) throws IOException, errorCatch{

         if(current.tokenStr.equals("/") && next.tokenStr.equals("/")){
             line = getLine();
             if(line == null){
                 current.tokenStr = "";
                 return;
             }
             current.tokenStr = getToken(line, current);
             next.tokenStr = getToken(line, next);
             checkComment(currentToken, nextToken, textLineM);
         }

    }

    /**
     * checks for double operators
     * <p>
     * @param current  current token
     * @param next     next token
     * @param line     used to get next token if doubleoperator is found
     * @throws errorCatch
     */
    private void checkDoubleOperator(Token current, Token next, char[] line) throws errorCatch{

           if(current.primClassif == Token.OPERATOR && next.primClassif == Token.OPERATOR ){
               switch(current.tokenStr){
                   case "<":
                       if(next.tokenStr.equals("=")){
                           current.tokenStr += next.tokenStr;
                           next.tokenStr = getToken(line, next);
                       }
                       break;
                   case ">":
                       if(next.tokenStr.equals("=")){
                           current.tokenStr += next.tokenStr;
                           next.tokenStr = getToken(line, next);
                       }
                       break;
                   case "=":
                       if(next.tokenStr.equals("=")){
                          current.tokenStr += next.tokenStr;
                          next.tokenStr = getToken(line, next);
                       }
                       break;
                   case "!":
                       if(next.tokenStr.equals("=")){
                           current.tokenStr += next.tokenStr;
                           next.tokenStr = getToken(line, next);
                       }
                       break;
                }
            }

    }

    private void checkUnaryMinus(Token current, Token next, char[] line) throws errorCatch, IOException{
         //Check that currentToken is -
         if(current.tokenStr.equals("-")){

             //Check if previous token is an operator as well
             //This will determine if - is urnary or not
             if(tokenList.get(tokenList.size()-1).primClassif == Token.OPERATOR){

                 //Check that next token is float, int, or operand
                 if(next.primClassif == Token.OPERAND){
                      getNext();
                      currentToken.tokenStr = "-" + currentToken.tokenStr;
                      //System.out.println(currentToken.tokenStr);
                 }
             }
         }
    }
}



/**
 * This small class allows for custom error messages
 * @author fish
 */
class errorCatch extends Exception {
    public static final long serialVersionUID = 10000L;
    public errorCatch(String msg){
        super(msg);
    }
}
