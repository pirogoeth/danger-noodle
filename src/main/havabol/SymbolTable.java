package havabol;

import havabol.classify.*;
import havabol.storage.*;
import havabol.sym.*;

import java.util.*;

/**
 * @author Sean Johnson <sean.johnson@maio.me>
 */
public class SymbolTable
{

    // Global symbol table instance
    private static SymbolTable globalInst = null;

    public static SymbolTable getGlobal()
    {
        if (globalInst == null) {
            return new SymbolTable(true);
        }

        return globalInst;
    }

    // Backing HashMap for the table.
    private final Map<String, STEntry> tab = new HashMap<>();

    // A SymbolTable can have an upper-linked table for static scoping -
    // if a symbol is not defined in the current scope, we can look upward!
    private SymbolTable parent = null;

    public SymbolTable()
    {
        this(true);
    }

    public SymbolTable(boolean isGlobalTable)
    {
        if ( isGlobalTable ) {
            this.initGlobal();
            globalInst = this;
        }

        // Any other initialization?
    }

    public SymbolTable(SymbolTable parent)
    {
        // If the symbol table we are creating has a parent, it is NOT the
        // global table.
        this(false);

        this.parent = parent;
    }

    private void initGlobal()
    {
        // Flow control builtins
        this.putSymbol(new STControl("def", Subclass.FLOW));
        this.putSymbol(new STControl("enddef", Subclass.END));
        this.putSymbol(new STControl("if", Subclass.FLOW));
        this.putSymbol(new STControl("endif", Subclass.END));
        this.putSymbol(new STControl("else", Subclass.END));
        this.putSymbol(new STControl("for", Subclass.FLOW));
        this.putSymbol(new STControl("endfor", Subclass.END));
        this.putSymbol(new STControl("while", Subclass.FLOW));
        this.putSymbol(new STControl("endwhile", Subclass.END));

        // Operator builtins
        this.putSymbol(new STEntry("and", Primary.OPERATOR));
        this.putSymbol(new STEntry("or", Primary.OPERATOR));
        this.putSymbol(new STEntry("not", Primary.OPERATOR));
        this.putSymbol(new STEntry("in", Primary.OPERATOR));
        this.putSymbol(new STEntry("notin", Primary.OPERATOR));

        this.putSymbol(new STEntry("=", Primary.OPERATOR));
        this.putSymbol(new STEntry("%", Primary.OPERATOR));
        this.putSymbol(new STEntry("*", Primary.OPERATOR));
        this.putSymbol(new STEntry("/", Primary.OPERATOR));
        this.putSymbol(new STEntry("^", Primary.OPERATOR));
        this.putSymbol(new STEntry("+", Primary.OPERATOR));
        this.putSymbol(new STEntry("-", Primary.OPERATOR));
        this.putSymbol(new STEntry("#", Primary.OPERATOR));

        // Comparators builtins
        this.putSymbol(new STEntry("==", Primary.OPERATOR));
        this.putSymbol(new STEntry(">=", Primary.OPERATOR));
        this.putSymbol(new STEntry("<=", Primary.OPERATOR));
        this.putSymbol(new STEntry("!=", Primary.OPERATOR));

        this.putSymbol(new STEntry("<", Primary.OPERATOR));
        this.putSymbol(new STEntry(">", Primary.OPERATOR));
        this.putSymbol(new STEntry("!", Primary.OPERATOR));

        // Built-in functions
        // TODO - Add a varargs argtype subclass?
        this.putSymbol(new STFunction("print", Subclass.BUILTIN, Subclass.VOID));
        this.putSymbol(new STFunction("LENGTH", Subclass.BUILTIN, Subclass.INTEGER));
        this.putSymbol(new STFunction("MAXLENGTH", Subclass.BUILTIN, Subclass.INTEGER));
        this.putSymbol(new STFunction("SPACES", Subclass.BUILTIN, Subclass.INTEGER));
        this.putSymbol(new STFunction("ELEM", Subclass.BUILTIN, Subclass.INTEGER));
        this.putSymbol(new STFunction("MAXELEM", Subclass.BUILTIN, Subclass.INTEGER));

        // Built-in types
        this.putSymbol(new STControl("Int", Subclass.DECLARE));
        this.putSymbol(new STControl("Float", Subclass.DECLARE));
        this.putSymbol(new STControl("String", Subclass.DECLARE));
        this.putSymbol(new STControl("Bool", Subclass.DECLARE));
        this.putSymbol(new STControl("Date", Subclass.DECLARE));
    }

    /**
     * Returns the related storage manager.
     *
     * @return StorageManager
     */
    public StorageManager getStorageManager()
    {
        return StorageManager.scoped(this);
    }

    /**
     * Returns the parent SymbolTable, if any.
     *
     * @return SymbolTable
     */
    public SymbolTable getParent()
    {
        return this.parent;
    }

    /**
     * lookupSym() looks up the token for this symbol table and up all parents
     * recursively until it is found.
     *
     * @param t Token
     * @return STEntry
     */
    public STEntry lookupSym(Token t)
    {
        return this.lookupSym(t.tokenStr);
    }

    public STEntry lookupSym(String s)
    {
        STEntry e = this.getSymbol(s);
        if ( e == null ) {
            if (this.parent != null) {
                return this.parent.lookupSym(s);
            }

            return null;
        }

        return e;
    }

    /**
     * getSymbol() *only* looks for the token in the current symbol table.
     *
     * @param t Token
     * @return STEntry
     */
    public STEntry getSymbol(Token t)
    {
        return this.getSymbol(t.tokenStr);
    }

    public STEntry getSymbol(String s)
    {
        return this.tab.get(s);
    }

    public void putSymbol(STEntry sym)
    {
        this.putSymbol(sym.getSymbol(), sym);
    }

    public void putSymbol(Token t, STEntry sym)
    {
        if ( t.subClassif == Subclass.IDENTIFIER.getCid() && sym instanceof STIdentifier ) {
            StorageManager localSM = this.getStorageManager();
            t.setStoredValue(localSM.getOrInit((STIdentifier) sym));
        }

        this.putSymbol(t.tokenStr, sym);
    }

    private void putSymbol(String s, STEntry sym)
    {
        tab.put(s, sym);
    }

    public boolean hasSymbol(Token t)
    {
        return this.hasSymbol(t.tokenStr);
    }

    public boolean hasSymbol(String s)
    {
        boolean local = this.tab.containsKey(s);
        if ( ! local && this.parent != null ) {
            return this.parent.hasSymbol(s);
        }

        return local;
    }

    public boolean hasLocalSymbol(Token t)
    {
        return this.hasLocalSymbol(t.tokenStr);
    }

    public boolean hasLocalSymbol(String s)
    {
        return this.tab.containsKey(s);
    }

    /**
     * Reclassifies a token and returns success status.
     *
     * @param t Token to reclassify.
     * @return boolean success
     */
    public boolean reclassifyToken(Token t)
    {
        STEntry e = this.getSymbol(t);
        if ( e == null ) {
            // Can't reclassify, not in the symbol table.
            return false;
        }

        int newPrim = e.getPrimaryClass().getCid();
        int newSub = e.getSubClass().getCid();

        if ( newPrim != t.primClassif || newSub != t.subClassif ) {
            t.primClassif = newPrim;
            t.subClassif = newSub;

            return true;
        }

        return false;
    }
}
