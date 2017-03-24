/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package havabol.util;

/**
 *
 * @author fish
 */
public class debugObj {
    public boolean bShowToken;
    public boolean bShowExpr;
    public boolean bShowAssign;
    
    private static debugObj instance;

    public static debugObj get() {
        if ( instance == null ) {
            return new debugObj();
        }

        return instance;
    }

    private debugObj(){
        bShowToken = false;
        bShowExpr = false;
        bShowAssign = false;

        instance = this;
    }
}
