package rainbow.lang.parser;

import rainbow.lang.parser.exception.DuplicateIdentifierException;

import java.util.ArrayList;
import java.util.BitSet;

public class SymbolTable {
    ArrayList<String> identifiers;
    ArrayList<Object> vals;
    ArrayList<BitSet> attribs;
    public SymbolTable () {
        identifiers = new ArrayList<>();
        vals = new ArrayList<>();
        attribs = new ArrayList<>();
    }
    public void addSymbol (String name, Object value, BitSet attrib){
        if (identifiers.contains(name))
            throw new DuplicateIdentifierException(name);
        identifiers.add(name);
        vals.add(value);
        attribs.add(attrib);
    }
}
