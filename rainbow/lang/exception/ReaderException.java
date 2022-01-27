package rainbow.lang.exception;

public class ReaderException extends BasicException {
    public ReaderException(String filename,String reason){
        super("Problems trying to read "+ filename + reason);
    }
}
