package rainbow.lang.runtime;

import rainbow.lang.runtime.exception.RtException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static rainbow.lang.Misc.StackTracePrinter;

public class Exec {
	private Exec () {}
	public static boolean exec(Object[] args){
		try {
			Enum action = (Enum) args[0];
			Class<?> cl = RuntimeMethods.class;
			Method m = cl.getMethod(action.name(), Object[].class) ;
			m.invoke(null,new Object[] {args});
			return true;
		}
		catch (InvocationTargetException ex1) {
			System.out.println(ex1.getCause().getMessage().trim());
			return false;
		}
		catch (Exception ex2) {
			System.err.println("FATAL RUNTIME ERROR: " +
			ex2.getClass().toString() + " " + ex2.getMessage() + "\n"  + "Tip: This is an internal error which means that its a bug(s) in the code. Please report this");
			StackTracePrinter(ex2);
			return false;
		}
	}

}
