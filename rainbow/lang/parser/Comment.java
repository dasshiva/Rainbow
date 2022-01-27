package rainbow.lang.parser;

import rainbow.lang.parser.exception.UnexpectedTokenException;

public class Comment {
    private static boolean inMulti = false;
    public static int commentIndex (String line){
        boolean com = false;
        for (int i=0;i<line.length();i++){
            char c = line.charAt(i);
            if (!inMulti) {
                if (c == '/') {
                    if (com)
                        return i-2;
                    com = true;
                }
                else if (com) {
                    if (c == '*')
                        inMulti = true;
                    else
                        throw new UnexpectedTokenException(c,
                                "Create a single-line comment with '/' or multiline comment with /*");
                }
            }
            else {
                boolean mayStopHere = false;
                if (c == '*')
                    mayStopHere = true;
                else if (mayStopHere) {
                    if (c == '/') {
                        inMulti = false;
                        return i+1;
                    }
                }
            }
        }
        if (!inMulti)
            return -1;
        return 0;
    }
}
