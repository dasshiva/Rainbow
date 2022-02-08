package rainbow.lang.parser.exception;

import rainbow.lang.parser.Types;

public class InvalidCastException extends ParserException {
    public InvalidCastException (Types toType) {
        super("cannot cast from string to " + toType.transform());
    }
}
