package rainbow.lang.parser.exception;

public class IllegalTypeException extends ParserException {
    public IllegalTypeException(String type) {
        super(type + " is not a valid type for a variable");
    }
}
