package test;

import havabol.Token;
import havabol.Scanner;
import havabol.classify.*;
import havabol.eval.*;
import havabol.parse.*;
import havabol.sym.*;

import java.util.*;

public class ParserTest
{
    public static void main(String[] args)
    {
        // Create the SymbolTable
        SymbolTable symbolTable = SymbolTable.getGlobal();

        List<Token> tokenList = new ArrayList<>();

        try
        {
            Scanner scan = new Scanner(args[0]);

            while (scan.currentToken != null)
            {
                scan.currentToken.printToken();
                tokenList.add(scan.currentToken);
                scan.getNext();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        System.out.println("\n -- LEXING FINISHED -- \n");

        List<Statement> stmts = new ArrayList<>();

        try
        {
            Parser p = new Parser(tokenList);
            while ( p.canParse() ) {
                Statement s = p.parse();
                stmts.add(s);
                System.out.println(s.debug());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("\n -- PARSING FINISHED -- \n");
    }
}
