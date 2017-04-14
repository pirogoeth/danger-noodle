package havabol;

import havabol.Token;
import havabol.Scanner;
import havabol.classify.*;
import havabol.eval.*;
import havabol.parse.*;
import havabol.sym.*;
import havabol.util.*;
import static havabol.util.Text.*;

import java.util.*;

public class HavaBol
{
    private static Debug dbg = Debug.get();

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
                if ( dbg.bShowToken ) {
                    System.out.println(lpads(
                        8,
                        "... " + scan.currentToken.getDebugInfo()
                    ));
                }

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

        List<EvalResult> res = new ArrayList<>();

        try {
            Evaluator e = new Evaluator(stmts);
            while ( e.canEval() ) {
                EvalResult er = e.evaluate();
                res.add(er);
                if ( dbg.bShowExpr ) {
                    System.out.println(lpads(
                        8,
                        "... " + er.debug(12)
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
