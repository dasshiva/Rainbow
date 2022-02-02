package rainbow.lang.parser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import rainbow.lang.Props;
import rainbow.lang.exception.ReaderException;
import rainbow.lang.parser.exception.*;

import rainbow.lang.runtime.*;
import rainbow.lang.runtime.exception.NoSuchSymbolFoundException;

import static rainbow.lang.Misc.StackTracePrinter;

class TokenProcessor {
    private final Split sp;
    private final ArrayList<String> target = new ArrayList<>();
    private boolean error;
    public TokenProcessor(Split sp) {
        this.sp = sp;
    }
    public void parseAll() {
        try {
            while(!sp.EOF()){
                sp.getNext(error);
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
            error = true;
        }
        catch (InvocationTargetException ex1){
            System.out.println(ex1.getCause().getMessage());
            error = true;
        }
       }
    }

    private void parseSetStatement() {
        Types type = null;
        String ID = null;
        Object val = null;
        boolean haveType = false, haveID=false, haveVal = false;
	if (target.size() == 3) {
		ID = target.get(1);
		val = target.get(2);
		SymbolTable.modifySymbol(ID,val);
		return;
	}
        for (int i=1;i<target.size();i++){ 
	    String str = target.get(i);
            if (!haveType) {
                type = Types.transformtoEnum(str);
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
                    if (!Exec.exec(new Object[] { Ins.transform("init"),ID, type, val}))
				    error = true;
                }
                catch (NumberFormatException e) {
                    throw new IllegalValueException(type, str);
                }
            }
        }
        if (!haveType || !haveID || !haveVal) 
        throw new SyntaxError("Set statement body is incomplete : Missing type , identifier or value");
    }

    private void parsePrintStatement() {
        if(target.size()<2)
           throw new SyntaxError("print statement body is incomplete : Missing the variable to print");
        Object[] args = new Object[target.size()];
        args[0] = Ins.transform("print");
        for (int i = 1; i < target.size(); i++) {
            args[i] = target.get(i);
        }
        Exec.exec(args);
    }
    private void parseAddStatement() {
        if(target.size()<2)
            throw new SyntaxError("Add statement body is incomplete : Missing operands");
        Object[] args = new Object[target.size()];
        args[0] = Ins.transform("add");
        for (int i = 1; i < args.length ; i++) {
            args[i] = target.get(i);
        }
        Exec.exec(args);
    }

    private void parseCastStatement() {
	    Types toType = null;
        Object newVal = null;
	    boolean needID = false, needCastID = false;
	    for (int i = 1; i < target.size(); i++){
		    String read = target.get(i);
		    if (needID) {
			    Types t = SymbolTable.getType(read);
			    if (t == toType)
				    return;
                if (t == Types.TYPE_STRING)
                    throw new InvalidCastException(toType);
			    newVal = RuntimeMethods.CAST(new Object[]{t,toType,read});
                needCastID = true;
                needID = false;
		    }
		    else if (needCastID) {
                try {
                    SymbolTable.isDefined(read);
                }
                catch (NoSuchSymbolFoundException e) {
                    Exec.exec(new Object[]{Ins.transform("init"), read,toType,newVal});
                }
                SymbolTable.modifySymbol(read,newVal);
            }
		    else {
			    toType = Types.transformtoEnum(read);
			    needID = true;
		    }
	    }
    }
    private boolean validateKeyword (String key) {
        switch(key){
            case "Set": case "Print": case "Add" :
                case "Cast" : return true;
                default: return false;
        }
    }
}
