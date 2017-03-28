package test;

import havabol.Token;
import havabol.Scanner;
import havabol.SymbolTable;
import havabol.classify.*;
import havabol.parse.*;

import java.io.*;
import java.util.*;

import test.util.Resources;

public class InputSuite
{
    public static void main(String[] args)
    {
        File[] inputs = {
            Resources.P1_IN,
            Resources.P1_IN_X1,
            Resources.P1_IN_X2,
            Resources.P1_IN_X3,
            Resources.P2_IN,
            Resources.P3_IN,
            Resources.P3_IN_SE,
            Resources.P3_IN_PARSER,
        };

        for (File f : inputs) {
            try {
                exec(f);
            } catch (Exception e) {
                System.out.printf("Input test '%s' failed:\n", f.getName());
                e.printStackTrace();
            }
        }
    }

    public static void exec(File inputf)
    {
        // Create the SymbolTable
        SymbolTable symbolTable = new SymbolTable();

        List<Token> tokenList = new ArrayList<>();

        try
        {
            Scanner scan = new Scanner(inputf);
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

        System.out.println("\n -- LEXING FINISHED -- \n");

        try
        {
            Parser p = new Parser(tokenList);
            while ( p.canParse() ) {
                Statement s = p.parse();
                System.out.println(s.debug());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
