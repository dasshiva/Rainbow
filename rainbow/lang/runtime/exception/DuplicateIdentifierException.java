package rainbow.lang.runtime.exception;

public class DuplicateIdentifierException extends RtException {
    public DuplicateIdentifierException (String identifier) {
        super("Identifier " + identifier + " has already been declared in this scope");
    }
}
