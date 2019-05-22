package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import io.FileUlits;
import model.Issue;
import model.IssueContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 空条件for循环
 * for(;;){
 *
 * }
 * for(;xx;){
 *
 * }
 * trandform
 * while(xx){
 *
 * }
 * while(true){
 *
 * }
 * @author kangkang
 * */
public class VoidPoolRule extends AbstractRuleVisitor {

    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for (CompilationUnit unit : units) {
            collectFor(unit);
        }
        return getContext();
    }

    private void collectFor(CompilationUnit unit) {
        List<ForStmt> forStmts = unit.findAll(ForStmt.class);
        check(forStmts);
    }

    private void check(List<ForStmt> forStmts) {
        for(ForStmt forStmt : forStmts){
            if(forStmt.getInitialization().size()!=0){
                continue;
            }
            if(forStmt.getUpdate().size()!=0){
                return;
            }
            Issue issue = new Issue();
            issue.setUnitNode(forStmt.findRootNode());
            issue.setIssueNode(forStmt);
            issue.setRefactorName(getSolutionClassName());
            getContext().getIssues().add(issue);
        }
    }

    public static void main(String []args){

    }
}