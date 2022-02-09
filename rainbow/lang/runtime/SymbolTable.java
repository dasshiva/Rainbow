package rainbow.lang.runtime;

import rainbow.lang.runtime.exception.DuplicateIdentifierException;
import rainbow.lang.runtime.exception.NoSuchSymbolFoundException;
import rainbow.lang.runtime.exception.InvalidSetException;
import rainbow.lang.runtime.exception.ConstantModificationException;
import rainbow.lang.parser.Types;

import java.util.ArrayList;

public class SymbolTable {
    private static final ArrayList<String> identifiers = new ArrayList<>();
    private static final ArrayList<Types> types = new ArrayList<>();
    private static final ArrayList<Object> vals = new ArrayList<>();
    private static final ArrayList<Attrs> attrs = new ArrayList<>();
    private SymbolTable () {}
    public static void addSymbol (String name, Types ty ,Object value){
        addSymbol(name,ty,value,Attrs.NO_ATTR);
    }
    public static void addSymbol(String name, Types ty, Object value,  Attrs attr){
	    if (identifiers.contains(name))
		    throw new DuplicateIdentifierException(name);    
	    identifiers.add(name);
	    types.add(ty);
	    vals.add(value);
	    attrs.add(attr);
    }
    private static Object[] getSymbol(String sym) {
        isDefined(sym);
        final int index = identifiers.lastIndexOf(sym);
        return new Object[] { types.get(index),vals.get(index) , attrs.get(index)};
    }
    public static void modifySymbol (String sym,Object newVals) {
	if ((Attrs) fetchIfDefined(sym,"Attr") == Attrs.ATTR_READONLY)
		throw new ConstantModificationException(sym);

	Types ty = (Types) fetchIfDefined(sym,"Type");
	if ((ty == Types.TYPE_STRING && newVals instanceof String)
	|| (ty == Types.TYPE_DECIMAL && newVals instanceof Double)
	|| (ty == Types.TYPE_INT && newVals instanceof Integer) )
		vals.set(identifiers.lastIndexOf(sym),newVals);
	else
		throw new InvalidSetException(sym,ty,newVals.toString());
    }
    public static void isDefined(String sym){
	    if (!identifiers.contains(sym))
		    throw new NoSuchSymbolFoundException(sym);
    }
    public static Object fetchIfDefined (String sym,String what) {
        isDefined(sym);
	Object[] dets = getSymbol(sym);
        if (what.equals("Type"))
            return dets[0];
	else if (what.equals("Attr"))
	    return dets[2];
	return dets[1];
    }
    public static Object[] fetchIfDefined (String sym){
	    isDefined(sym);
	    Object[] dets = getSymbol(sym);
	    return new Object[] { dets[0], dets[1] };
    }
    public static void checkReadonly(Object sym) {
        if (SymbolTable.fetchIfDefined((String)sym,"Attr") == Attrs.ATTR_READONLY)
            throw new ConstantModificationException((String) sym);
    }
    public static void delSymbol (String sym) {
	    isDefined(sym);
	    final int index = identifiers.lastIndexOf(sym);
	    identifiers.remove(index);
	    vals.remove(index);
	    attrs.remove(index);
	    types.remove(index);
    }

}
