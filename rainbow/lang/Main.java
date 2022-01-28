package rainbow.lang;

import rainbow.lang.parser.ParserMain;

/*
 * The main class which launches the Rainbow interpreter
 * Its basic function is to call the methods which initializes 
 * the appllication-wide properties (Props.initProps) and parse the 
 * command-line arguements and then invoke the parse method 
 * of the ParserMain class to parse the given file
 */

public class Main {
    public static void main(String[] args) {
        Props.initProps();
        CommandParser cp = new CommandParser(args);
        cp.parseAll();
        ParserMain pm = new ParserMain(new IO());
        pm.parse();
    }
}
