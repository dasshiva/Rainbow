package rainbow.lang.runtime;

public enum Ins {
	INIT, PRINT, ADD, CAST;
	public static Ins transform (String ins) {
		switch(ins) {
			case "init": return Ins.INIT;
			case "print": return Ins.PRINT;
			case "add" : return Ins.ADD;
			case "cast" : return Ins.CAST;
			default: return null;
		}
	}
}
