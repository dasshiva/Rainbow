package rainbow.lang.runtime.exception;

import rainbow.lang.parser.exception.ParserException;

public class RtException extends ParserException {
	public RtException(String cause){
		super(cause);
	}
}
