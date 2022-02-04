package rainbow.lang.runtime.exception;

public class InvalidArguementException extends RtException {
    public InvalidArguementException(String type , String action) {
        super(" variable of type " + type + " cannot be an argument for " + action);
    }
}
