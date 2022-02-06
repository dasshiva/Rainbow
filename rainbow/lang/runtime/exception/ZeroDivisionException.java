package rainbow.lang.runtime.exception;

public class ZeroDivisionException extends RtException {
	public ZeroDivisionException (){
		super("Variable or immediate value passed to Div statement " +" has value 0 or 0.0");
	}
}
