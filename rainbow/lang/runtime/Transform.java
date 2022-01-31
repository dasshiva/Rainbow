package rainbow.lang.runtime;

public class Transform {
	public static Ins transform (String ins) {
		switch(ins) {
			case "init": return Ins.INIT;
			case "print": return Ins.PRINT;
			case "add" : return Ins.ADD;
			default: return null;
		}
	}
}
