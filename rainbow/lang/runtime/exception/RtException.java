package rainbow.lang.runtime.exception;

import rainbow.lang.Props;
import rainbow.lang.exception.BasicException;

public class RtException extends BasicException {
	public RtException(String cause) {
		super(String.format("\n%s\n%s:%d %s",
				Props.getProp("line"),
				Props.getProp("input"),
				Integer.parseInt(Props.getProp("lineno")),
				cause));
	}
}
