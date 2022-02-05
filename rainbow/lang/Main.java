package rainbow.lang;

import rainbow.lang.parser.ParserMain;
import rainbow.lang.exception.BasicException;

/*
 * The main class which launches the Rainbow interpreter
 * Its basic function is to call the methods which initializes 
 * the application-wide properties (Props.initProps()) and parse the
 * command-line arguments and then invoke the parse method
 * of the ParserMain class to parse the given file
 */

public class Main {
    public static void main(String[] args) {
        Props.initProps();
	try {
		CommandParser cp = new CommandParser(args);
		cp.parseAll();
		ParserMain pm = new ParserMain(new IO());
		pm.parse();
	}
	catch (BasicException e){
		System.out.println(e.getMessage());
		System.exit(1);
	}
    }
}
