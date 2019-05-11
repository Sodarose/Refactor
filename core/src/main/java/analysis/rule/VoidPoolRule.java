package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.ForStmt;
import model.Issue;
import model.IssueContext;

import java.util.List;

/**
 * 空条件for循环
 * */
public class VoidPoolRule extends AbstractRuleVisitor {

    private final BaseVisitor<ForStmt> forVisitor = new BaseVisitor<ForStmt>(){
        @Override
        public void visit(ForStmt n, Object arg) {
            getList().add(n);
            super.visit(n, arg);
        }
    };

    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for (CompilationUnit unit : units) {
            collectFor(unit);
        }
        check();
        return getContext();
    }

    private void collectFor(CompilationUnit unit) {
        unit.accept(forVisitor,null);
    }

    private void check() {
        List<ForStmt> forStmts = forVisitor.getList();
        for(ForStmt forStmt : forStmts){
            if(forStmt.getInitialization()==null||forStmt.getInitialization().size()==0){
                continue;
            }
            if(forStmt.getCompare().get()==null){
                continue;
            }
            if(forStmt.getUpdate()==null||forStmt.getUpdate().size()==0){
                continue;
            }
            Issue issue = new Issue();
            issue.setUnitNode(forStmt.findRootNode());
            issue.setIssueNode(forStmt);
            getContext().getIssues().add(issue);
        }
    }
}
