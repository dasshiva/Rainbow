package rainbow.lang.runtime;

import rainbow.lang.parser.Types;

class RuntimeMethods {
	public static void INIT (Object[] args) {
		SymbolTable.addSymbol((String) args[1],
				(Types) args[2], args[3]);
	}
	public static void PRINT (Object[] args){
		Object[] sym = SymbolTable.getSymbol((String)args[1]);
		System.out.println(sym[1].toString());
	}
}
