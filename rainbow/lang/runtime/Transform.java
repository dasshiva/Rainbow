package rainbow.lang.runtime;

public class Transform {
	public static Ins transform (String ins) {
		switch(ins) {
			case "init": return Ins.INIT;
			case "print": return Ins.PRINT;
			default: return null;
		}
	}
}
