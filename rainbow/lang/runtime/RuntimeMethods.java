package rainbow.lang.runtime;

import rainbow.lang.parser.Types;
import rainbow.lang.parser.exception.InvalidCastException;

public class RuntimeMethods {
	public static void INIT (Object[] args) {
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
		Object ret = null;
		if ((Types)args[0] == Types.TYPE_STRING)
			throw new InvalidCastException((Types) args[1]);
		else if ((Types) args[0] == Types.TYPE_INT && (Types) args[1] == Types.TYPE_DECIMAL)
			return ((Integer)SymbolTable.getValue((String) args[2])).doubleValue();
		else if ((Types) args[0] == Types.TYPE_DECIMAL && (Types) args[1] == Types.TYPE_INT) {
			System.out.println("WARNING : Casting from decimal to int will cause loss of precision");
			return ((Double)SymbolTable.getValue((String) args[2])).intValue();
		}
		else
			return args[2].toString();
	}
	public static void ADD (Object[] args) {
		Types ty = (Types) SymbolTable.fetchIfDefined((String) args[args.length -1],"Type"),temp;
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
}
