package rainbow.lang.parser.exception;

public class InsufficientArguementException extends ParserException {
    public InsufficientArguementException (String ins, int expect , int given) {
        super(ins + " needs at least " + expect + " argument but found only "+ given);
    }
}
