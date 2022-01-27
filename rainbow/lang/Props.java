package rainbow.lang;

import java.util.Properties;

public class Props {
    private static Properties props = new Properties();
    private static final String defString = "NO";
    private static final String helpString = "Available options include :\n"+
            "-help                     Print this help menu\n"+
            "-version                  Print version information\n" +
            "-input (mandatory option) Specify the file to be parsed\n";
    private Props() {}
    public static void initProps(){
        props.setProperty("version","0.0.1");
        props.setProperty("input",defString);
        props.setProperty("help","JParser " + props.getProperty("version") + "\n" + helpString);
    }
    public static boolean isDefault(String str){
        return defString.equals(str);
    }
    public static void addProp(String key,String val){
        props.setProperty(key,val);
    }
    public static String getProp(String key){
        return props.getProperty(key);
    }
}
