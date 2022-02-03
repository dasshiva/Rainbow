package rainbow.lang.runtime;

public enum Ins {
	INIT, PRINT, ADD, SUB, CAST;
	public static Ins transform (String ins) {
		switch(ins) {
			case "Set": return Ins.INIT;
			case "Print": return Ins.PRINT;
			case "Add" : return Ins.ADD;
			case "Cast" : return Ins.CAST;
			case "Sub" : return Ins.SUB;
			default: return null;
		}
	}
}
