package Importtest;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {
    private static Pattern pattern=Pattern.compile("[a-zA-Z]*Map+<[A-Za-z0-9]*,[A-Za-z0-9]*>");
    public static void main(String[] args){
        PatternTest.flag("HashMap<String,Interger>");
    }
    public static void flag(String name){
       Matcher m=pattern.matcher(name);
       if(m.find()){
           System.out.println(m.group(0));
       }
    }
}
