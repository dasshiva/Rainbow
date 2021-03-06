package rainbow.lang;

import rainbow.lang.exception.NoInputFileException;
import rainbow.lang.exception.NoSuchFileException;
import rainbow.lang.Props;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.io.File.separatorChar;

/*
* This class contains the I/0 routines used by the Split class to read from the file
* line by line. It uses a BufferedReader to perform this operation. It also sets the 
* lineno and line properties which contains the line number of the file which is currently 
* being read and the line which has just been read from the file respectively.
* 
*/

public class IO {

    /* 
    * A set of fields storing the line number, the line which has just been read
    * and the name of the file from which we are reading respectively.
    */

    private int lineno = 0;
    private String currentLine;
    private final String filename;

    /* The source from which we will be reading*/
    private final BufferedReader reader;

    public IO () {
        filename = Props.getProp("input");

        /* 
        * If the input property is set, try to open the file
        * otherwise throw an exception to notify the user
        */
        
        if (filename!=null){
            try {
                reader = new BufferedReader(new FileReader(filename));
            }
            catch (FileNotFoundException fe){
                throw new NoSuchFileException(filename);
            }
        }
        else
            throw new NoInputFileException();

    }

    /* This constructor will be used by the "Include" statement
     * (is still being implemented) to open a file given as
     * the argument as String to Include
     */

    public IO (String filename) {
	if (filename.charAt(0) != separatorChar) 
		this.filename = Props.getProp("prefix") + separatorChar + filename;
	else 
		this.filename = filename;
	try {
	    reader = new BufferedReader(new FileReader(this.filename));
	    Props.addProp("isinclude","T");
	    Props.addProp("include",this.filename);
	}
	catch (FileNotFoundException fe) {
	    throw new NoSuchFileException(this.filename);
	}

    }

    /* 
    * This method reads the file line by line and returns the line which
    * was just read. On EOF it returns a special token called END to the parser
    * which signals to it that the file is complete and closes the stream. 
    * It also sets the line and lineno global properties which are used by all parser exceptions
    */

    public String read() throws IOException  {
        currentLine = reader.readLine();
        if (currentLine == null) {
            reader.close();
            return "END";
        }
        lineno++;
        Props.addProp("line", currentLine);
        Props.addProp("lineno", Integer.toString(lineno));
        return currentLine;
    }

    /*
    * This method is called  when a fatal runtime
    * error happens such as being unable to call up methods from RuntimeMethods
    * by reflection or during a failure of a runtime operation such as trying to
    * redeclare an already defined variable etc.
    */

    public void closeAndExit () throws IOException {
	    reader.close();
	    System.exit(1);
    }
}
