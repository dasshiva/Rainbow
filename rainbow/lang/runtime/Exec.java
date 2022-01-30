package rainbow.lang.runtime;

import java.lang.reflect.Method;

public class Exec {
	private Exec () {}
	public static boolean exec(Object[] args){
		try {
			Enum action = (Enum) args[0];
			Class<?> cl = RuntimeMethods.class;
			Method m = cl.getMethod(action.name(),new Class[] { Object[].class }) ;
			m.invoke(null,new Object[] {args});
			return true;
		}
		catch (Exception e) {
			System.err.println("FATAL RUNTIME ERROR: " +
			e.getClass().toString() + " " + e.getMessage() + "\n"  + "Tip: This is an internal error which means that its a bug(s) in the code. Please report this");
			StackTracePrinter(e);
			return false;
		}
	}
	public static void StackTracePrinter(Throwable e) {
		StackTraceElement[] elems = e.getStackTrace();
		System.out.println("Stack Trace:");
		for (StackTraceElement elem: elems){
			System.out.println(elem.toString());
		}
	}
}
