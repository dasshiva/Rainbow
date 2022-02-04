package rainbow.lang.runtime;

import rainbow.lang.parser.Types;
import rainbow.lang.parser.exception.InvalidCastException;
import rainbow.lang.Props;

public class RuntimeMethods {
	public static void INIT (Object[] args) {
		if (args.length == 5) 
			SymbolTable.addSymbol((String) args[1],
                                (Types) args[2], args[3], Attrs.ATTR_READONLY);
		else 
			SymbolTable.addSymbol((String) args[1],
				(Types) args[2], args[3]);
	}
	public static void PRINT (Object[] args){
		for (int i = 1; i < args.length; i++) {
			Object sym = SymbolTable.getValue((String) args[i]);
			System.out.println(sym.toString());
		}
	}
	public static Object CAST (Object[] args){
		if (args[0] == Types.TYPE_STRING)
			throw new InvalidCastException((Types) args[1]);
		else if (args[0] == Types.TYPE_INT && args[1] == Types.TYPE_DECIMAL)
			return ((Integer)SymbolTable.getValue((String) args[2])).doubleValue();
		else if (args[0] == Types.TYPE_DECIMAL && args[1] == Types.TYPE_INT) {
			if (!(Props.getProp("no-warn").equals("T")))
				System.out.println("WARNING : Casting from decimal to int will cause loss of precision");
			return ((Double)SymbolTable.getValue((String) args[2])).intValue();
		}
		else
			return args[2].toString();
	}
	public static void ADD (Object[] args) {
		SymbolTable.checkReadonly(args[args.length - 1]);
		Types ty = SymbolTable.getType((String) args[args.length - 1]) , temp;
		int isum = 0;
		double dsum = 0.0;
		String finalString = "";
		boolean lastIter = false, intSum = false, doubleSum = false;
		if (ty == Types.TYPE_INT)
			intSum = true;
		else if (ty == Types.TYPE_DECIMAL)
			doubleSum = true;
		for (int i = 1; i < args.length; i++) {
			if (i == args.length-1)
				lastIter = true;
			Object arg = args[i];
			if (lastIter) {
				if (intSum)
					SymbolTable.modifySymbol((String) arg, isum);
				else if (doubleSum)
					SymbolTable.modifySymbol((String) arg, dsum);
				else
					SymbolTable.modifySymbol((String) arg, finalString);
				return;
			}
			Object[] details = SymbolTable.fetchIfDefined(arg.toString());
			temp = (Types) details[0];
			if (ty != temp) {
				details[1] = CAST(new Object[] {temp,ty,arg} );
			}
			if (intSum)
				isum += (Integer) details[1];
			else if (doubleSum)
				dsum += (Double) details[1];
			else
				finalString = finalString.concat((String) details[1]);
		}
	}
	public static void SUB (Object[] args) {
		SymbolTable.checkReadonly(args[args.length - 1]);
		Types resTy = SymbolTable.getType((String) args[args.length -1]);
		int ires = 0 ;
		double dres = 0.0;
		boolean intres = false, doubleres = false;
		if (resTy == Types.TYPE_INT)
			intres = true;
		else if (resTy == Types.TYPE_DECIMAL)
			doubleres = true;
		else {}
		Object[] op1 = SymbolTable.fetchIfDefined((String) args[1]);
		Object[] op2 = SymbolTable.fetchIfDefined((String) args[2]);
		if (intres) {
			ires = (Integer) castIfNeeded((Types) op1[0], resTy, op1[1]) -
					(Integer) castIfNeeded((Types) op2[0], resTy, op2[1]);
			SymbolTable.modifySymbol((String) args[args.length - 1] , ires);
		}
		else {
			dres = (Double) castIfNeeded((Types) op1[0], resTy , op1[1]) -
					(Double) castIfNeeded((Types) op2[0], resTy , op2[1]);
			SymbolTable.modifySymbol((String) args[args.length - 1] , dres);
		}
	}
	private static Object castIfNeeded (Types from , Types to , Object toCast) {
		if (from != to)
			return CAST(new Object[] { from,to,toCast } );
		return toCast;
	}
}
