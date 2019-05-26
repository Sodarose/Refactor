import analysis.process.Analysis;

import com.github.javaparser.ast.CompilationUnit;
import model.Issue;
import model.IssueContext;
import org.checkerframework.checker.units.qual.A;
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

    public static void analysis(String path) {
        Analysis analysis = new Analysis();
        analysis.analysis(path);
        issueContext = analysis.results();
        for (Issue issue : issueContext.getIssues()) {
            if (map.containsKey(issue.getUnitNode())) {
                map.get(issue.getUnitNode()).add(issue);
            } else {
                List<Issue> issues = new ArrayList<>();
                issues.add(issue);
                map.put(issue.getUnitNode(), issues);
            }
        }
    }

    public static void refactor() {
        ReFactorExec reFactorExec = new ReFactorExec();
        reFactorExec.factor(issueContext);
    }


    public static void main(String[] args) {
        //分析
        System.out.println("开始扫描");
<<<<<<< HEAD
        analysis("D:\\gitProject\\W8X");
=======
        Analysis analysis = new Analysis();
        analysis.analysis("E:\\w8x-dev");
>>>>>>> 5e58a87b19021c5cfa3deb11a1ab00c316d3dee2
        System.out.println("扫描结束");
        //重构
    }
}
