package rainbow.lang.parser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import rainbow.lang.Props;
import rainbow.lang.exception.ReaderException;
import rainbow.lang.parser.exception.*;

import rainbow.lang.runtime.Exec;
import rainbow.lang.runtime.Transform;

import static rainbow.lang.Misc.StackTracePrinter;

class TokenProcessor {
    private final Split sp;
    private final ArrayList<String> target = new ArrayList<>();
    private boolean fatalError;
    public TokenProcessor(Split sp) {
        this.sp = sp;
    }
    public void parseAll() {
        try {
            while(!sp.EOF()){
                sp.getNext(fatalError);
                while(sp.hasNext()) {
                    target.add(sp.next());
                }
                parseList();
                target.clear();
            }
        } catch (IOException e) {
            throw new ReaderException(Props.getProp("input"),e.getMessage());
        }
    }
    private void parseList() {
	if (target.size() == 0) return;
    else {
        try {
            String statType = target.get(0);
            if (!validateKeyword(statType))
                throw new IllegalStatementException(statType);
            Class<?> tp = TokenProcessor.class;
            String methodName = "parse" + target.get(0) + "Statement";
            Method toCall = tp.getDeclaredMethod(methodName);
            toCall.setAccessible(true);
            toCall.invoke(this);
        }
        catch  (NoSuchMethodException | IllegalAccessException ex2) {
            System.err.println("FATAL RUNTIME ERROR: " + ex2.getClass().toString() + " " + ex2.getMessage() + "\n"
                    + "Tip: This is an internal error which means that its a bug(s) in the code. Please report this");
            StackTracePrinter(ex2);
            fatalError = true;
        }
        catch (InvocationTargetException ex1){
            throw new ParserException(ex1.getCause().getMessage());
        }
       }
    }

    private void parseSetStatement() {
        Types type = null;
        String ID = null;
        Object val = null;
        boolean haveType = false, haveID=false, haveVal = false;
        for (int i=1;i<target.size();i++){ 
	    String str = target.get(i);
            if (!haveType) {
                switch(str) {
                    case "int":
                    type = Types.TYPE_INT;
                    break;
                    case "decimal":
                    type = Types.TYPE_DECIMAL;
                    break;
                    case "string":
                    type = Types.TYPE_STRING;
                    break;
                    default: throw new IllegalTypeException(str);
                }
                haveType = true;
            }
            else if (!haveID) {
                ID = str;
                haveID = true;
            }
            else  {
                try {
                    switch(type) {
                        case TYPE_INT:
                        val = Integer.parseInt(str);
                        break;
                        case TYPE_DECIMAL:
                        val = Double.parseDouble(str);
                        break;
                        case TYPE_STRING:
                        val = str;
                    }
                    haveVal = true;
                    if (!Exec.exec(new Object[] { Transform.transform("init"),ID, type, val})) 
				    fatalError = true;
                }
                catch (NumberFormatException e) {
                    throw new IllegalValueException(type, str);
                }
            }
        }
        if (!haveType || !haveID || !haveVal) 
        throw new SyntaxError("init statement body is incomplete : Missing type , identifier or value");
    }

    private void parsePrintStatement() {
        if(!(target.size()==2)) 
           throw new SyntaxError("print statement body is incomplete : Missing the variable to print");
        Exec.exec(new Object[] 
		{ Transform.transform("print"), target.get(1)}) ;
    }

    private boolean validateKeyword (String key) {
        switch(key){
            case "Set": case "Print": return true;
            default: return false;
        }
    }
}
