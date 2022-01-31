package rainbow.lang.parser;

class Comment {
    public static int commentIndex (String toParse) {
        int index = -1;
        for (int i = 0 ; i < toParse.length() ; i++){
            if (toParse.charAt(i) == '#') {
                index = i;
                break;
            }
        }
        return index;
    }
}
