package rainbow.lang.exception;

public class NoSuchFileException extends BasicException {
    public NoSuchFileException (String filename) {
        super(filename + ": No such file found");
    }
}
