package test;

import havabol.Token;
import havabol.Scanner;
import havabol.classify.*;
import havabol.parse.*;
import havabol.sym.*;

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
            Resources.P4_IN_ARRAYS,
            Resources.P4_IN_EVAL,
        };

        for (File f : inputs) {
            try {
                exec(f);
            } catch (Exception e) {
                System.out.printf("Input test '%s' failed:\n", f.getName());
                e.printStackTrace();
                return;
            }
        }
    }

    public static void exec(File inputf) throws Exception
    {
        // Create the SymbolTable
        SymbolTable symbolTable = SymbolTable.getGlobal();
        List<Token> tokenList = new ArrayList<>();
        List<Statement> stmtList = new ArrayList<>();

        Scanner scan = new Scanner(inputf);
        scan.currentToken.printToken();
        tokenList.add(scan.currentToken);

        while (! scan.getNext().isEmpty())
        {
            scan.currentToken.printToken();
            tokenList.add(scan.currentToken);
        }

        System.out.println("\n -- LEXING FINISHED -- \n");

        Parser p = new Parser(tokenList);
        while ( p.canParse() ) {
            Statement s = p.parse();
            stmtList.add(s);
            System.out.println(s.debug());
        }

        System.out.println("\n -- PARSING FINISHED -- \n");
    }
}
