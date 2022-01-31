package rainbow.lang.parser;

import rainbow.lang.parser.exception.IllegalTypeException;
/* This enum contains all the possible data types supported by the language */
public enum Types {
    TYPE_INT, /* internal representation of 'int' */
    TYPE_DECIMAL, /* internal representation of 'decimal' */
    TYPE_STRING; /* internal representation of 'string' */

    /*
    * This function changes the enum declarations into the user-friendly data types,
    * and it is used only for exception message formatting
    */

    public String transform() {
        switch(this){
            case TYPE_INT:
            return "int";
            case TYPE_DECIMAL:
            return "decimal";
            case TYPE_STRING:
            return "string";
            default: return null;
        }
    }
    public static Types transformToEnum (String target) {
	    switch (target) {
		    case "int":
			    return TYPE_INT;
		    case "decimal":
			    return TYPE_DECIMAL;
		    case "string":
			    return TYPE_STRING;
		    default:
			    throw new IllegalTypeException(target);
	    }
    }
}
