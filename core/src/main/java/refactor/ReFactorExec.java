package refactor;

import analysis.BaseVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Name;
import model.Issue;
import model.IssueContext;

import java.util.Iterator;
import java.util.List;

/**
 * 重构执行类
 * @author kangkang
 * */
public class ReFactorExec {
    public void factor(IssueContext context){
        runFactor(context);
    }
    /**
     * 通过反射生成重构实例进行重构
     * */
    private void runFactor(IssueContext context){
       Iterator<Issue> it = context.getIssues().iterator();
    }
}
