package rainbow.lang;

import rainbow.lang.exception.InvalidOptionException;
import rainbow.lang.exception.NoArgException;

/* 
* The CommandParser class parses the command line arguements and sets
* the global properties accordingly so that the parser can do its work 
* or prints the verion and a help menu of the program
*/

public class CommandParser {
    /* Stores the array of command-line arguements given to main() */
    private final String[] args;

    /* 
    * An enum which describes the various categories of options.
    * NEED_ARGS describes a command-line option that needs an arguement (eg. -input)
    * NO_NEED_ARGS describes a command-line arguement that does 
    * not need arguements (eg. -no-warn)
    * STOP_HERE describes a command-line arguement where the program
    * needs to display some text and exit immediately (eg. -version)
    */

    private enum OptionType {
        NEED_ARGS,
        NO_NEED_ARGS,
        STOP_HERE
    }

    /* The options which are described by STOP_HERE */
    private final String[] stopOpts = {"-version","-help"};

    /* The options which are described by NEED_ARGS */
    private final String[] argOpts = {"-input","-prefix"};

    /* The options which are described by NO_NEED_ARGS */
    private final String[] noArgOpts = {"-no-warn"};

    public CommandParser(String[] args){

        /* Print a help message if we recieved no options and exit */

        if (args.length==0){
            System.out.println("No option specified");
            System.out.println(Props.getProp("help"));
            System.exit(1);
        }
        this.args=args;
    }
    /* Parse all the commad-line arguements and set the global properties */
    public void parseAll(){
        /* stores the current option without the '-' in the beginning */
        String currentOption=null;

        /* true if currentOption is an option that needs arguements */
        boolean needArgs=false;

        /* 
        Loop through all the arguements and parse them
        * If any of them beigns with a '-', then its an option.
        * If it is an option determine if it needs arguements and loop again to collect its 
        * arguement if it does need arguements,
        * Throw an exception if the option is unrecognized
        */

        for (String each:args){
            if (isOpt(each)){
                OptionType ty;
                if ((ty=isValidOption(each))!=null){
                    currentOption=each.substring(1);
                    if (ty==OptionType.NO_NEED_ARGS)
                        Props.addProp(currentOption,"T");
                    else if (ty==OptionType.STOP_HERE){
                        System.out.println(Props.getProp(currentOption));
                        System.exit(0);
                    }
		    else 
			    needArgs = true;
                }
                else
                    throw new InvalidOptionException(each);
            }
            else if(needArgs) {
                Props.addProp(currentOption,each);
                needArgs=false;
            }
        }
        if (needArgs)
            throw new NoArgException("-"+currentOption);
	else if (Props.getProp("input") == null) 
		Props.addProp("input",args[0]);
    }

    /* 
    * Small method to check if a string is an 
    * option by checking its 1st character
    */

    private boolean isOpt(String str){
        return '-' == str.charAt(0);
    }

    /* 
    * Determine if the option is valid and return an instance of the
    * OptionType enum describing the type of the option i.e whether it needs arguements or not
    * Return null if this option is not a valid option
    */

    private OptionType isValidOption(String str){
        for (String each:stopOpts){
            if (each.equals(str))
                return OptionType.STOP_HERE;
        }

	for (String each:noArgOpts) {
		if(each.equals(str))
			return OptionType.NO_NEED_ARGS;
	}

        for (String each:argOpts){
            if (each.equals(str))
                return OptionType.NEED_ARGS;
        }
        return null;
    }
}
