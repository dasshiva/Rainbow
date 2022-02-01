package rainbow.lang.parser.exception;

import rainbow.lang.Props;
import rainbow.lang.exception.BasicException;

public class ParserException extends BasicException  {
    public ParserException(String cause,String hint){
        super(String.format("\n%s\n%s:%d %s\n" +
                        "Were you trying to : %s ?",
                Props.getProp("line"),
                Props.getProp("input"),
                Integer.parseInt(Props.getProp("lineno")),
                cause,hint));
    }
    public ParserException(String cause) {
        super(String.format("%s:%d %s \nError: %s\n",
                Props.getProp("input"),
                Integer.parseInt(Props.getProp("lineno")),
		Props.getProp("line"),
                cause));
    }
}
