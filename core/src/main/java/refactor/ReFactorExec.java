package refactor;

import analysis.BaseVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Name;
import model.Issue;
import model.IssueContext;

import java.util.Iterator;
import java.util.List;

public class ReFactorExec {
    BaseVisitor<ClassOrInterfaceDeclaration> visitor = new BaseVisitor<ClassOrInterfaceDeclaration>(){
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
            System.out.println(n.getName());
        }
    };
    public void factor(IssueContext context){
        System.out.println(context.getIssues().size());
        runFactor(context);
    }
    private void runFactor(IssueContext context){
       Iterator<Issue> it = context.getIssues().iterator();
        while(it.hasNext()){
            Issue issue = it.next();
            CompilationUnit unit = (CompilationUnit)issue.getUnitNode();
            issue.getUnitNode().accept(visitor,null);
        }

    }
}
