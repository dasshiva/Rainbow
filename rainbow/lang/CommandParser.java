package rainbow.lang;

import rainbow.lang.exception.InvalidOptionException;
import rainbow.lang.exception.NoArgException;

public class CommandParser {
    private final String[] args;
    private enum OptionType {
        NEED_ARGS,
        // NO_NEED_ARGS,
        STOP_HERE
    }
    private final String[] stopOpts = {"-version","-help"};
    private final String[] argOpts = {"-input"};
    public CommandParser(String[] args){
        if (args.length==0){
            System.out.println("No option specified");
            System.out.println(Props.getProp("help"));
            System.exit(1);
        }
        this.args=args;
    }
    public void parseAll(){
        String currentOption=null;
        boolean needArgs=false;
        for (String each:args){
            if (isOpt(each)){
                OptionType ty;
                if ((ty=isValidOption(each))!=null){
                    currentOption=each.substring(1);
                    if (ty==OptionType.NEED_ARGS){
                        needArgs=true;
                    }
                    else if (ty==OptionType.STOP_HERE){
                        System.out.println(Props.getProp(currentOption));
                        System.exit(0);
                    }
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
    }
    private boolean isOpt(String str){
        return '-' == str.charAt(0);
    }
    private OptionType isValidOption(String str){
        for (String each:stopOpts){
            if (each.equals(str))
                return OptionType.STOP_HERE;
        }
        for (String each:argOpts){
            if (each.equals(str))
                return OptionType.NEED_ARGS;
        }
        return null;
    }
}
