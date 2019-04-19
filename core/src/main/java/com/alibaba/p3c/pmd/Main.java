package com.alibaba.p3c.pmd;
import net.sourceforge.pmd.cli.PMDCommandLineInterface;

public class Main {
    public static void main(String[] args) {
        args=new String[6];
        args[0]="-d";
        args[1]="C:\\Users\\Administrator\\eclipse-workspace\\po\\src\\test\\Main.java";
        args[2]="-R";
        args[3]="rulesets/java/ali-comment.xml,rulesets/java/ali-concurrent.xml,rulesets/java/ali-constant.xml,rulesets/java/ali-exception.xml,rulesets/java/ali-flowcontrol.xml,rulesets/java/ali-naming.xml,rulesets/java/ali-oop.xml,rulesets/java/ali-orm.xml,rulesets/java/ali-other.xml,rulesets/java/ali-set.xml" ;
        args[4]="-f";
        args[5]="text";
        PMDCommandLineInterface.run(args);
    }
}
