package rainbow.lang;

public class Main {
    public static void main(String[] args) {
        Props.initProps();
        CommandParser cp = new CommandParser(args);
        cp.parseAll();
        //ParserMain pm = new ParserMain(new IO(Props.getProp("input")));
    }
}
