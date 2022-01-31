package rainbow.lang.parser.exception;

public class IllegalStatementException extends ParserException {
    public IllegalStatementException (String stat) {
        super(stat + " is not a valid statement type");
    }
}
