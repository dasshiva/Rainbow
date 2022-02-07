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
    private boolean error, isRset;
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
	catch (ParserException e) {
		System.out.println(e.getMessage().trim());
		error = true;
	}
    }
    private void parseList() {
	if (target.size() == 0 || target.get(0).equals("")) return;
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
    private void parseRSetStatement() {
	    isRset = true;
	    parseSetStatement();
	    isRset = false;
    }
    private void parseMulStatement() {
	    parseAddStatement();
    }
    private void parseDivStatement() {
	    parseSubStatement();
    }
    private void parsePrintlnStatement() {
	    parsePrintStatement();
    }
    private void parseSetStatement() {
        Types type = null;
        String ID = null;
        Object val = null;
        boolean haveType = false, haveID=false, haveVal = false;
	if (target.size() == 3) {
		ID = target.get(1);
		val = target.get(2);
		try {
			val = Integer.parseInt((String) val);
		}
		catch (NumberFormatException e) {
			try {
				val = Double.parseDouble((String) val);
			}
			catch (NumberFormatException e2) {} 
		}
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
		    if(isRset) {
			    if (!Exec.exec(new Object[] { Ins.transform("Set"),ID, type, val, true}))
				    error = true;
		    }
		    else {
			    if (!Exec.exec(new Object[] { Ins.transform(target.get(0)),ID, type, val}))
				    error = true;
		    }
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
           throw new InsufficientArguementException(target.get(0),1,0);
        Object[] args = new Object[target.size()];
        args[0] = Ins.transform(target.get(0));
        for (int i = 1; i < target.size(); i++) {
            args[i] = target.get(i);
        }
        if (!Exec.exec(args))
		error = true;
    }
    private void parseAddStatement() {
        if(target.size()<4)
            throw new InsufficientArguementException(target.get(0), 3,target.size()-1);
        Object[] args = new Object[target.size()];
        args[0] = Ins.transform(target.get(0));
        for (int i = 1; i < args.length ; i++) {
            args[i] = target.get(i);
        }
        if (!Exec.exec(args))                                 
		error = true;
    }
    private void parseSubStatement () {
        if(target.size()<4)
            throw new InsufficientArguementException(target.get(0), 3,target.size()-1);
        Object[] ar = new Object[4];
        ar[0] = Ins.transform(target.get(0));
        for (int i = 1; i<4 ; i++) {
            ar[i] = target.get(i);
        }
        if (!Exec.exec(ar))                                     
		error = true;
    }
    private void parseCastStatement() {
	    Types toType = null;
        Object newVal = null;
	    boolean needID = false, needCastID = false;
	    for (int i = 1; i < target.size(); i++){
		    String read = target.get(i);
		    if (needID) {
			    Types t = (Types) SymbolTable.fetchIfDefined(read, "Type");
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
				    Exec.exec(new Object[]{Ins.transform("Set"), read,toType,newVal});
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
            case "Cast" : case "RSet" : case "Sub" :
	    case "Mul": case "Div": case "Println" : return true;
            default: return false;
        }
    }
}
