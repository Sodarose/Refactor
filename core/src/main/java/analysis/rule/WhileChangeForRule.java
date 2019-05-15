package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.WhileStmt;
import model.Issue;
import model.IssueContext;

import java.util.List;

/**
 * 转换为for语句会更好
 * int i
 * while(i<100){
 * stmt;
 * i++;
 * }
 *
 * @author kangkang
 */
public class WhileChangeForRule extends AbstractRuleVisitor {
    final BaseVisitor<WhileStmt> visitor = new BaseVisitor<WhileStmt>() {
        @Override
        public void visit(WhileStmt n, Object arg) {
            getList().add(n);
            super.visit(n, arg);
        }
    };

    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for (CompilationUnit unit : units) {
            collectWhile(unit);
        }
        check();
        return getContext();
    }
    private void collectWhile(CompilationUnit unit){
        unit.accept(visitor,null);
    }

    private void check() {
        List<WhileStmt> whileStmts = visitor.getList();
        for(WhileStmt whileStmt:whileStmts){
            if(!checkInit(whileStmt)){
                continue;
            }
            if(!checkUpdate(whileStmt)){
                continue;
            }
            Issue issue = new Issue();
            issue.setIssueNode(whileStmt);
            issue.setUnitNode(whileStmt.findRootNode());
            getContext().getIssues().add(issue);
        }
    }

    /**
     * 找到该表达式使用者初始化语句
     * 并且该变量没有被使用
     * */
    private boolean checkInit(WhileStmt whileStmt){
        return false;
    }

    /**
     * 检查while主体中有没有例如i++之类的
     * */
    private boolean checkUpdate(WhileStmt whileStmt){
        return false;
    }

}
