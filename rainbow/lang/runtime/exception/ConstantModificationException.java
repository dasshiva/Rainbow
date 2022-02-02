
package rainbow.lang.runtime.exception;

public class ConstantModificationException extends RtException {
	public ConstantModificationException (String sym){
		super("Readonly variable " + sym + " cannot have its value modified");
	}
}
