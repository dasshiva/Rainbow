package rainbow.lang.parser;

import rainbow.lang.parser.exception.DuplicateIdentifierException;

import java.util.ArrayList;

public class SymbolTable {
    private ArrayList<String> identifiers;
    private ArrayList<Types> types;
    private ArrayList<Object> vals;
    public SymbolTable () {
        identifiers = new ArrayList<>();
        types = new ArrayList<>();
        vals = new ArrayList<>();
    }
    public void addSymbol (String name, Types ty ,Object value){
        if (identifiers.contains(name))
            throw new DuplicateIdentifierException(name);
        identifiers.add(name);
        types.add(ty);
        vals.add(value);
    }
    public Object[] retrieveSymbol(String sym) {
        if (!identifiers.contains(sym)) 
            throw new NoSuchSymbolFoundException(sym);
        final int index = identifiers.lastIndexOf(sym);
        return new Object[] { types.get(index), vals.get(index)};
    }
}
