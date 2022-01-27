package rainbow.lang.parser.exception;

public class IllegalIdentifierException extends ParserException {
	public IllegalIdentifierException (String id){
		super(id + " is not a valid Java identifier"+
		"\nTip: " + id +" is a Java keyword which cannot be used in this context");
	}
}
