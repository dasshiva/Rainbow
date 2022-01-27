package rainbow.lang.exception;

public class NoInputFileException extends BasicException {
    public NoInputFileException() {
        super("No input file specified (specify one with the -input flag)");
    }
}
