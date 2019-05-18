package refactor;

import analysis.BaseVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Name;
import lombok.Data;
import model.Issue;
import model.IssueContext;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 重构执行类
 *
 * @author kangkang
 */
@Data
public class ReFactorExec {
    private Set<Node> nodeSet = new HashSet<>();
    public void factor(IssueContext context) {
        runFactor(context);
    }

    /**
     * 通过反射生成重构实例进行重构
     */
    private void runFactor(IssueContext context) {
        Iterator<Issue> it = context.getIssues().iterator();
        try {
            while (it.hasNext()) {
                Issue issue = it.next();
                Refactor refactor = (Refactor) Class.forName(issue.getRefactorName()).newInstance();
                refactor.refactor(issue);
                nodeSet.add(issue.getUnitNode());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
