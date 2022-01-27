package rainbow.lang;

import rainbow.lang.exception.NoInputFileException;
import rainbow.lang.exception.NoSuchFileException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IO {
    private int lineno = 0;
    private String currentLine;
    private final String filename;
    private final BufferedReader reader;
    public IO (String filename) {
        if (filename!=null){
            try {
                reader = new BufferedReader(new FileReader(filename));
                this.filename = filename;
            }
            catch (FileNotFoundException fe){
                throw new NoSuchFileException(filename);
            }
        }
        else
            throw new NoInputFileException();

    }
    public String read() throws IOException  {
        currentLine = reader.readLine();
        if (currentLine == null)
            return "EOF";
        lineno++;
        return currentLine;
    }
    public int getLineno(){
        return lineno;
    }
    public String getFilename(){
        return filename;
    }
}
