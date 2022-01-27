package rainbow.lang.exception;

public class InvalidOptionException extends BasicException {
    public InvalidOptionException(String opt){
        super(opt+" is not a valid command line option");
    }
}
