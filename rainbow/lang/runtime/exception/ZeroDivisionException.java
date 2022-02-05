package rainbow.lang.runtime.exception;

public class ZeroDivisionException extends RtException {
	public ZeroDivisionException (String sym){
		super("Varaible " + sym + " passed to Div action " +
				" has value 0 or 0.0");
	}
}
