package rainbow.lang.runtime;

import rainbow.lang.runtime.exception.DuplicateIdentifierException;
import rainbow.lang.runtime.exception.NoSuchSymbolFoundException;
import rainbow.lang.parser.Types;

import java.util.ArrayList;

public class SymbolTable {
    private static ArrayList<String> identifiers = new ArrayList<>();
    private static ArrayList<Types> types = new ArrayList<>();
    private static ArrayList<Object> vals = new ArrayList<>();
    private SymbolTable () {}
    public static void addSymbol (String name, Types ty ,Object value){
        if (identifiers.contains(name))
            throw new DuplicateIdentifierException(name);
        identifiers.add(name);
        types.add(ty);
        vals.add(value);
    }
    public static Object[] getSymbol(String sym) {
        if (!identifiers.contains(sym)) 
            throw new NoSuchSymbolFoundException(sym);
        final int index = identifiers.lastIndexOf(sym);
        return new Object[] { types.get(index), vals.get(index)};
    }
    public static Types getType (String sym){
	    Object[] ret = getSymbol(sym);
	    return (Types) ret[0];
    }
}
