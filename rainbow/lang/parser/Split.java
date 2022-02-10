package rainbow.lang.parser;

import java.io.IOException;
import java.util.Iterator;

import rainbow.lang.IO;

/*
* This class splits the tokens from the line it receives from src on the basis
* of whitespace and returns them with the next() method.
 */

class Split implements Iterator<String> {

	/*
	* The 2 fields which store the current state of the parsed string
	* and the string to be parsed
	*/

	private String currentString;
	private String toParse;
	private final IO src;

	/*
	* currentIndex stores the index at which we are inside toParse
	* and maxIndex represents the index at which we know that it is
	* the end of the string
	*/

	private int currentIndex , maxIndex;
	private boolean isLeft,isFinished;

	public Split (IO src) {
		this.src = src;
	}

	/*
	* The most important function of this class which does the actual work
	* of splitting the string into tokens.
	*/

	private void split(){
		/* a flag which is set if we are inside string */
		boolean inString = false;

		/* a StringBuilder to store the split string */
		StringBuilder sb = new StringBuilder();
		for (; currentIndex < maxIndex; currentIndex++){
			char ins = toParse.charAt(currentIndex);

			/*
			* if inString is set then we are inside a string, and we should
			* add all characters to the string irrespective of whether their
			* whitespace or not
			*/

			if (inString) {
				if (ins == '"') {
					inString = false;
				}
				sb.append(ins);
			}

			/*
			* If we are not inside a string then check if the current character is whitespace
			* If it is whitespace then go to the else clause otherwise check if it is double quotes. (")
			* If they are double quotes then set the inString flag and continue to the next iteration.
			* Otherwise, add the character to the split string
			*/

			else if (!Character.isWhitespace(ins)) {
				if (ins == '"') { 
					inString = true;
				}
				sb.append(ins);
			}

			else {

				/*
				* Increment currentIndex by 1 or the loop will begin from the same whitespace
				* resulting us entering infinite loop
				*/

				currentIndex += 1;
				if (currentIndex >= toParse.length())
					/* The string's end has been reached and the caller should get us more input by calling getNext() */
					isLeft = false;

				else if (sb.length()==0) /* don't return if the parsed string is empty */
					continue;
				currentString = sb.toString();
				return;
			}
		}
	}

	/*
	* This file reads one line from the file and sets it to currentString
	* so that split() can use it to split the token into strings.
	* The exitNow parameter is only true if the application has run into a fatal internal error
	* or we have an Runtime exception like DuplicateIdentifierException etc.
	* and has to exit immediately
	 */

	public void getNext(boolean exitNow) throws IOException {
		if (exitNow)
			src.closeAndExit();
		toParse = "";
		int temp, index = -1;
		/* loop until the string is not a comment */
		toParse = src.read() + ' ';
		index = Comment.commentIndex(toParse);
		if (index == 0) {
			while(index == 0) {
				toParse = src.read() + ' ';
				index = Comment.commentIndex(toParse);
			}
		}
		/* if toParse is the special END token then we have reached EOF */
		if (checkEOFString(toParse)) {
			isFinished = true;
			isLeft = false;
		}
		/* If index is -1, this string is not a comment */
		else if (index == -1) {
			currentIndex = 0;
			isLeft = true;
			maxIndex = toParse.length();
		}
		/* Otherwise, loop only till we reach the comment's beginning */
		else {
			currentIndex = 0;
			isLeft = true;
			maxIndex = index;
		}
	}

	/*
	* return the 'END' string if we reached EOF or call split()
	* on toParse and return the split string
	*/
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

	/* check if the given string is the special EOF token 'EOF' */
	private boolean checkEOFString(String str){
		return str.charAt(0) == 'E' && str.charAt(1) == 'N'
				&& str.charAt(2) == 'D';
	}

	/* inform the caller if we have reached EOF */
	public boolean EOF() throws IOException {
		return isFinished;
	}
}
