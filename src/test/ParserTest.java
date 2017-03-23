package test;

import havabol.Token;
import havabol.Scanner;
import havabol.SymbolTable;
import havabol.parse.*;

import java.util.*;

public class ParserTest
{
    public static void main(String[] args)
    {
        // Create the SymbolTable
        SymbolTable symbolTable = new SymbolTable();

        List<Token> tokenList = new ArrayList<>();

        try
        {
            Scanner scan = new Scanner(args[0], symbolTable);
            scan.currentToken.printToken();
            tokenList.add(scan.currentToken);

            while (! scan.getNext().isEmpty())
            {
                scan.currentToken.printToken();
                tokenList.add(scan.currentToken);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Parser p = new Parser(tokenList);
        while ( p.canParse() ) {
            Statement s = p.parse();
            s.print();
        }
    }
}
