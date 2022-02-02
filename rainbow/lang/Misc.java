package rainbow.lang;

public class Misc {	
    public static void StackTracePrinter(Throwable e) {
        StackTraceElement[] elems = e.getStackTrace();
        System.out.println("Stack Trace:");
        for (StackTraceElement elem: elems){
            System.out.println(elem.toString());
        }
    }
}
