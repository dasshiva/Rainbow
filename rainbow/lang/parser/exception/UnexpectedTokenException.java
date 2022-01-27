package rainbow.lang.parser.exception;

public class UnexpectedTokenException extends ParserException {
    public UnexpectedTokenException (char token, String hint){
        super ("Token "+ transform(token) + " is not expected here",hint);
    }
    public UnexpectedTokenException (String cause) {
        super ("Expected" + cause + "here");
    }
    private static String transform (char ch){
	    return switch (ch) {
		    case '\n' : yield "newline";
		    case ' ' :  yield "whitespace";
		    default :  yield Character.toString(ch);
	    };
    }
}
