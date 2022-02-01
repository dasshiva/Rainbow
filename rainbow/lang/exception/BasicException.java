package rainbow.lang.exception;

public class BasicException extends RuntimeException {
    public BasicException(String msg){
        super(msg,null,true,false);
    }
}
