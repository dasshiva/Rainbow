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
                if (target.size()==0) continue;
                parseList();
                target.clear();
            }
        } catch (IOException e) {
            throw new ReaderException(Props.getProp("-input"),e.getMessage());
        }
    }
    private void parseList() {
        if (target.get(0).equals("init"))
        parseInitStatement();
    }
    private void parseInitStatement() {
        Types type = null;
        String ID = null;
        Object val = null;
        boolean haveType = false, haveID=false, haveVal = false;
        for (String str:target){ 
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
                        case TYPE_DECIMAL:
                        val = Integer.parseInt(str);
                        break;
                        case TYPE_INT:
                        val = Double.parseDouble(str);
                        break;
                        case TYPE_STRING:
                        val = str;
                    }
                    haveVal = true;
                }
                catch (NumberFormatException e) {
                    throw new IllegalValueException(type, str);
                }
            }
        }
        if (!haveType || !haveID || !haveVal) 
        throw new SyntaxError("init statement body is incomplete : Missing type , identifier or value");
    }
}
