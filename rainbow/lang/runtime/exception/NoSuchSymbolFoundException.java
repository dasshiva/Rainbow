package rainbow.lang.runtime.exception;

public class NoSuchSymbolFoundException extends RtException {
    public NoSuchSymbolFoundException(String symbol){
        super("The symbol " + symbol + " is undefined in the current context");
    }
}
