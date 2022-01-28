package rainbow.lang.parser;

import java.io.IOException;
import java.util.Iterator;

import rainbow.lang.IO;
import rainbow.lang.Props;

public class Split implements Iterator<String> {
	private String currentString;
	private String toParse;
	private final IO src;
	private int currentIndex , maxIndex;
	private boolean isLeft,isFinished;
	public Split (IO src) {
		this.src = src;
	}
	private void split(){
		StringBuilder sb = new StringBuilder();
		for (; currentIndex < maxIndex; currentIndex++){
			char ins = toParse.charAt(currentIndex);
			if (!Character.isWhitespace(ins))
				sb.append(ins);
			else {
				currentIndex += 1;
				if (currentIndex >= toParse.length())
					isLeft = false;
				else if (sb.length()==0)
					continue;
				currentString = sb.toString();
				return;
			}
		}
	}
	public void getNext() throws IOException {
		while (true) {
			toParse = src.read() + ' ';
			int temp;
			Props.addProp("lineno", Integer.toString(src.getLineno()));                            
			Props.addProp("line", toParse);
			if ((temp = Comment.commentIndex(toParse)) == 0)
				continue;
			else if (checkEOFString(toParse)) {
				src.close();
				isFinished = true;
				isLeft = false;
				break;
			}
			else if (temp == -1) {
				currentIndex = 0;
				isLeft = true;
				maxIndex = toParse.length();
			}
			else {
				currentIndex = 0;
				isLeft = true;
				maxIndex = temp;
			}
			break;
		}
	}

	@Override
	public String next() {
		if (isFinished)
		return toParse;
		split();
		return currentString;
	}
	@Override
	public boolean hasNext(){
		return isLeft;
	}
	private boolean checkEOFString(String str){
		if (str.charAt(0) == 'E' && str.charAt(1) == 'N' 
				&& str.charAt(2) == 'D') 
			return true;
		return false;
	}

	public boolean EOF() throws IOException {
		if (isFinished)
			src.close();
		return isFinished;
	}
}
