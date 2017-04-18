package havabol.storage;

import havabol.*;
import havabol.sym.*;
import havabol.util.*;

import java.util.*;

/**
 * Each StorageManager will be associated with a SymbolTable.
 * The reasoning for this is such that each scope can be maintained
 * individually and literal scope creep will not be an issue.
 *
 * This way, it is also trivial for a new scope to inherit the parent scope.
 *
 * @author Sean Johnson <isr830@my.utsa.edu>
 */
public class StorageManager
{

    private static Map<SymbolTable, StorageManager> instMap = new HashMap<>();

    public static StorageManager global()
    {
        StorageManager inst = instMap.get(SymbolTable.getGlobal());
        if ( inst == null ) {
            return new StorageManager();
        }

        return inst;
    }

    public static StorageManager scoped(SymbolTable st)
    {
        StorageManager inst = instMap.get(st);
        if ( inst == null ) {
            return new StorageManager(st);
        }

        return inst;
    }

    private final Map<String, SMValue> store = new HashMap<>();
    private SymbolTable scope = null;

    /**
     * This constructor builds the global storage manager instance.
     *
     * This is directly associated with the global symbol table.
     */
    public StorageManager()
    {
        SymbolTable gsym = SymbolTable.getGlobal();
        instMap.put(gsym, this);

        this.scope = gsym;
    }

    /**
     * Builds a scope-specific storage manager instance.
     *
     * When a StorageManager is building with a scope, it should
     * normally inherit values from surrounding scopes.
     *
     * Odd thing is, our scopes do not directly inherit values
     * from the parent scope - they perform lookups, which will
     * take into account shadowing and such. The storage manager
     * also needs to do this.
     */
    public StorageManager(SymbolTable st)
    {
        instMap.put(st, this);

        this.scope = st;
    }

    public SMValue get(STIdentifier ident)
    {
        if ( ident == null ) {
            return null;
        }

        // Since scope and storage manager are directly related, it should
        // remain relatively easy to perform ident lookups.
        if ( this.scope.hasLocalSymbol(ident.getSymbol()) ) {
            return this.store.get(ident.getSymbol());
        }

        if ( this.scope.getParent() != null ) {
            return this.scope.getParent().getStorageManager().get(ident);
        }

        return null;
    }

    public SMValue getOrInit(STIdentifier ident)
    {
        if ( ident == null ) {
            return null;
        }

        SMValue val = this.get(ident);
        if ( val == null ) {
            val = new SMValue(ident);
            this.store.put(ident.getSymbol(), val);
        }

        return val;
    }
}
