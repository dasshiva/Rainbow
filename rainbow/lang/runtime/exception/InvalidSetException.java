package rainbow.lang.runtime.exception;

import rainbow.lang.parser.Types;

public class InvalidSetException extends RtException {
	public InvalidSetException (String sym,Types ty,String val){
		super("Cannot set variable "+ sym +" of type " + 
				ty.transform() +" to " +val);
	}
}
