package rainbow.lang.runtime;

public enum Ins {
	INIT, PRINT, ADD, SUB, MUL, DIV, CAST;
	public static Ins transform (String ins) {
		switch(ins) {
			case "Set": return Ins.INIT;
			case "Print": return Ins.PRINT;
			case "Add" : return Ins.ADD;
			case "Cast" : return Ins.CAST;
			case "Sub" : return Ins.SUB;
		        case "Div" : return Ins.DIV;
			case "Mul" : return Ins.MUL;
			default: return null;
		}
	}
}
