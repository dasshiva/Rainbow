package rainbow.lang.runtime.exception;

import rainbow.lang.Props;
import rainbow.lang.exception.BasicException;

public class RtException extends BasicException {
	public RtException(String cause) {
		super(String.format("%s:%d %s\nError: %s",
				Props.getProp("input"),
				Integer.parseInt(Props.getProp("lineno")),
				Props.getProp("line"),
				cause));
	}
}
