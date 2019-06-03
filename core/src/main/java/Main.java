import analysis.process.Analysis;

import com.github.javaparser.ast.CompilationUnit;
import model.*;
import refactor.ReFactorExec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Main {
    private static IssueContext issueContext;
    private static Map<Object, List<Issue>> map = new HashMap<>();






    public static void main(String[] args) {
        /**
         * 分析代码
         * */
        Analysis analysis = new Analysis();
        analysis.analysis("D:\\gitProject\\W8X");
        //得到issueContext
        /**
         * 重构代码
         * */
    }
}
