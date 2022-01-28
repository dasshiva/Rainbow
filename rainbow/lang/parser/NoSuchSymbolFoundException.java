package rainbow.lang.parser;

import rainbow.lang.parser.exception.ParserException;

public class NoSuchSymbolFoundException extends ParserException {
    public NoSuchSymbolFoundException(String symbol){
        super("The symbol " + symbol + " is undefined in the current context");
    }
}
