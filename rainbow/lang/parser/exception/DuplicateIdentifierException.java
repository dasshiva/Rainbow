package rainbow.lang.parser.exception;

public class DuplicateIdentifierException extends ParserException {
    public DuplicateIdentifierException (String identifier) {
        super("Identifier " + identifier + " has already been declared in this scope");
    }
}
