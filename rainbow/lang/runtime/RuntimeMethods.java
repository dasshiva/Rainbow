package rainbow.lang.runtime;

import rainbow.lang.parser.Types;
import rainbow.lang.parser.exception.InvalidCastException;
import rainbow.lang.Props;
import rainbow.lang.runtime.exception.InvalidArguementException;
import rainbow.lang.runtime.exception.ZeroDivisionException;
import rainbow.lang.runtime.exception.RtException;
import java.util.Arrays;

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
		printer(args, false);
	}
	public static void PRINTLN (Object[] args) {
		printer(args, true);

	}
	private static void printer (Object[] args, boolean withNewLine) {
		for (int i = 1; i < args.length; i++) {        
			try {              
				Object sym = SymbolTable.fetchIfDefined((String) args[i], "");     
				if (withNewLine) 
					System.out.println(sym.toString());
				else 
					System.out.print(sym.toString());
			}                                        
			catch (RtException e) {                  
				Object[] dets = tryParse(args[i]); 
				if (withNewLine)
					System.out.println(dets[1].toString());
				else 
					System.out.print(dets[1].toString());
			}                                            
		}
	}
	public static Object CAST (Object[] args){
		if (args[0] == Types.TYPE_STRING)
			throw new InvalidCastException((Types) args[1]);
		else if (args[0] == Types.TYPE_INT && args[1] == Types.TYPE_DECIMAL)
		{
			return ((Integer) SymbolTable.fetchIfDefined(args[2].toString(), "")).doubleValue();
		}

		else if (args[0] == Types.TYPE_DECIMAL && args[1] == Types.TYPE_INT) {
			if (!(Props.getProp("no-warn").equals("T")))
				System.out.println("WARNING : Casting from decimal to int will cause loss of precision");
			return ((Double) SymbolTable.fetchIfDefined((args[2].toString()), "")).intValue();
		}
		else if (args[0] == args[1])
			return SymbolTable.fetchIfDefined((String) args[2], "");
		else 
			return args[2].toString();
	}
	public static Object SIMPLECAST (Object[] args) {
		if (args[0] == Types.TYPE_STRING)
			throw new InvalidCastException((Types) args[1]);
		else if (args[0] == Types.TYPE_INT && args[1] == Types.TYPE_DECIMAL)
			return ((Integer) args[2]).doubleValue();
		else if (args[0] == Types.TYPE_DECIMAL && args[1] == Types.TYPE_INT) {
			if (!(Props.getProp("no-warn").equals("T")))
				System.out.println("WARNING : Casting from decimal to int will cause loss of precision");
			return ((Double) args[2]).intValue();
		}
		else
			return args[2].toString();
	}
	public static void ADD (Object[] args) {
		SymbolTable.checkReadonly(args[args.length - 1]);
		Types ty = (Types) SymbolTable.fetchIfDefined((String) args[args.length - 1],"Type") , temp;
		int isum = 0;
		double dsum = 0.0;
		String finalString = "";
		boolean lastIter = false, intSum = false, doubleSum = false;
		if (ty == Types.TYPE_INT)
			intSum = true;
		else if (ty == Types.TYPE_DECIMAL)
			doubleSum = true;
		Object[] details = null;
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
			try {
				details = SymbolTable.fetchIfDefined(arg.toString());
			}
			catch (RtException e) {
				details = tryParse(arg);
			}
			temp = (Types) details[0];
			details[1] = castIfNeeded(temp,ty,details[1]);
			if (intSum)
				isum += (int) details[1];//Integer.parseInt(details[1].toString());
			else if (doubleSum)
				dsum += (double) details[1]; // Double.parseDouble( details[1].toString());
			else
				finalString = finalString.concat((String) details[1]);
		}
	}
	public static void MUL (Object[] args) {
		SymbolTable.checkReadonly(args[args.length - 1]);
		Types ty = (Types) SymbolTable.fetchIfDefined((String) args[args.length - 1], "Type") , temp;         
		int isum = 1;
		double dsum = 1.0;   
		boolean lastIter = false, intSum = false, doubleSum = false;   
		if (ty == Types.TYPE_INT)  
			intSum = true;       
		else if (ty == Types.TYPE_DECIMAL)     
			doubleSum = true; 
		else 
			throw new InvalidArguementException("string","Sub");
		Object[] details = null ;
		String imName = null;
		for (int i = 1; i < args.length; i++) {
			if (i == args.length-1)
				lastIter = true;
			Object arg = args[i]; 
			if (lastIter) {
				if (intSum)
					SymbolTable.modifySymbol((String) arg, isum);
				else 
					SymbolTable.modifySymbol((String) arg, dsum);     
				return;
			}    
			try {                                  
				details = SymbolTable.fetchIfDefined(arg.toString());          
			}                        
			catch (RtException e) {                    
				details = tryParse(arg);
			}
			temp = (Types) details[0];
			details[1] = castIfNeeded(temp,ty,details[1]);
			if (intSum)
				isum *= Integer.parseInt(details[1].toString());
			else 
				dsum *= Double.parseDouble( details[1].toString());
                }
        }
	public static void SUB (Object[] args) {
		SymbolTable.checkReadonly(args[args.length - 1]);
		Types resTy = (Types) SymbolTable.fetchIfDefined((String) args[args.length -1], "Type");
		int ires = 0 ;
		double dres = 0.0;
		boolean intres = false;
		if (resTy == Types.TYPE_INT)
			intres = true;
		else if (resTy == Types.TYPE_DECIMAL) {}
		else
			throw new InvalidArguementException("string","Sub");
		Object[] op1 = null , op2 = null;
		try {
			op1 = SymbolTable.fetchIfDefined((String) args[1]);
		}
		catch (RtException e){
			op1 = tryParse(args[1]);
		}
		try {
			op2 = SymbolTable.fetchIfDefined((String) args[2]);
		}
		catch (RtException e){                 
			op2 = tryParse(args[2]);
		}
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

	public static void DIV (Object[] args) { 
		SymbolTable.checkReadonly(args[args.length - 1]);
		Types resTy = (Types) SymbolTable.fetchIfDefined((String) args[args.length -1], "Type"); 
		int ires = 0 ; 
		double dres = 0.0; 
		boolean intres = false;
		Object[] op1 = null, op2 = null;
		if (resTy == Types.TYPE_INT)          
			intres = true; 
		else if (resTy == Types.TYPE_DECIMAL) {}      
		else    
			throw new InvalidArguementException("string","Sub");  
		try {               
			op1 = SymbolTable.fetchIfDefined((String) args[1]);                   
		}                           
		catch (RtException e){                 
			op1 = tryParse(args[1]);
		}                                                    
		try {
			op2 = SymbolTable.fetchIfDefined((String) args[2]);                  
		}                                   
		catch (RtException e){                            
			op2 = tryParse(args[2]); 
		}
		if (op2[1] instanceof Integer && (int)op2[1] == 0 ||
		op2[1] instanceof Double && (double)op2[1] == 0.0)
			throw new ZeroDivisionException();
		if (intres) {    
			ires = (Integer) castIfNeeded((Types) op1[0], resTy, op1[1]) /(Integer) castIfNeeded((Types) op2[0], resTy, op2[1]);
			SymbolTable.modifySymbol((String) args[args.length - 1] , ires);
		} 
		else {
			dres = (Double) castIfNeeded((Types) op1[0], resTy , op1[1])/(Double) castIfNeeded((Types) op2[0], resTy , op2[1]);
			SymbolTable.modifySymbol((String) args[args.length - 1] , dres);
		}  
	}

	private static Object castIfNeeded (Types from , Types to , Object toCast) {
		if (from != to)
			return SIMPLECAST(new Object[] { from,to,toCast } );
		return toCast;
	}
	private static Object[] tryParse (Object val){
		try {
			return new Object[] { Types.TYPE_INT, Integer.parseInt((String) val) };
		}
		catch (NumberFormatException ex1) {
			try {
				return new Object[] { Types.TYPE_DECIMAL , Double.parseDouble((String) val)};
			}
			catch (NumberFormatException ex2) {}
			return new Object[] { Types.TYPE_STRING , (String) val};
		}
	}
}
