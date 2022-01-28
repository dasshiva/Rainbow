package rainbow.lang.parser;

import java.io.IOException;
import java.util.ArrayList;

import rainbow.lang.Props;
import rainbow.lang.exception.ReaderException;
import rainbow.lang.parser.exception.IllegalTypeException;
import rainbow.lang.parser.exception.IllegalValueException;
import rainbow.lang.parser.exception.SyntaxError;

public class TokenProcessor {
    private final Split sp;
    private final ArrayList<String> target = new ArrayList<>();
    private final SymbolTable symTab = new SymbolTable();
    public TokenProcessor(Split sp) {
        this.sp = sp;
    }
    public void parseAll() {
        try {
            while(!sp.EOF()){
                sp.getNext();
                while(sp.hasNext()) {
                    target.add(sp.next());
                }
                parseList();
                target.clear();
            }
        } catch (IOException e) {
            throw new ReaderException(Props.getProp("-input"),e.getMessage());
        }
    }
    private void parseList() {
	if (target.size() == 0) return;
    else {
        if (target.get(0).equals("init"))
            parseInitStatement();
        else if (target.get(0).equals("print"))
            parsePrintStatement();
       }
    }
    private void parseInitStatement() {
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
                    symTab.addSymbol(ID, type, val);
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
        Object[] val = symTab.retrieveSymbol(target.get(1));
        if ((Types) val[0] == Types.TYPE_INT) 
            System.out.println(Integer.toString((Integer) val[1]));
        else if ((Types) val[0] == Types.TYPE_DECIMAL)
            System.out.println(Double.toString((Double) val[1]));
        else 
            System.out.println((String) val[1]);
    }
}
