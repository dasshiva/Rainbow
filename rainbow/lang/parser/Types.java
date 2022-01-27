package rainbow.lang.parser;

public enum Types {
    TYPE_INT,
    TYPE_DECIMAL,
    TYPE_STRING;
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
}
