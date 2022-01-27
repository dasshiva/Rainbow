package rainbow.lang.parser;

import rainbow.lang.IO;

public class ParserMain {
    private final IO reader;
    public ParserMain(IO readFrom){
        reader=readFrom;
    }
    public void parse() {
        TokenProcessor tp = new TokenProcessor(new Split(reader));
        tp.parseAll();
    }
}
