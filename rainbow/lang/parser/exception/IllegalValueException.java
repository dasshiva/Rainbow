package rainbow.lang.parser.exception;

import rainbow.lang.parser.Types;

public class IllegalValueException extends ParserException {
    public IllegalValueException (Types ty,String val){
        super(val + " is not a valid value for type "+ty.transform());
    }
}
