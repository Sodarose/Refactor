package analysis.rule;


import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.IfStmt;
import model.Issue;
import model.IssueContext;


import java.util.List;

/**
 * rule if深层嵌套扫描
 *
 * @author kangkang
 */
public class DeeplyIfStmtsRule extends AbstractRuleVisitor {

    private final int MAX_DEEP = 3;
    /**
     * 表层搜索观察者
     */
    private final BaseVisitor<IfStmt> exteVisitor = new BaseVisitor<IfStmt>() {
        @Override
        public void visit(IfStmt n, Object arg) {
            getList().add(n);
        }
    };

    /**
     * 针对单个If深层搜索
     */
    private final BaseVisitor<IfStmt> internalVisitor = new BaseVisitor<IfStmt>() {
        @Override
        public void visit(IfStmt n, Object arg) {
            int deep = (int) arg;
            ++deep;
            if (deep >= MAX_DEEP) {
                Issue issue = new Issue();
                issue.setUnitNode(n.findRootNode());
                issue.setIssueNode(getCaller());
                getContext().getIssues().add(issue);
                return;
            }
            super.visit(n, deep);
        }
    };

    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for (CompilationUnit unit : units) {
            collectIfstmt(unit);
        }
        check();
        return getContext();
    }

    /**
     * 拿到类表层ifstmts
     */
    private void collectIfstmt(CompilationUnit unit) {
        unit.accept(exteVisitor, null);
    }

    /**
     * 递归每一个表层ifStemts 探测其深度
     */
    private void check() {
        List<IfStmt> exteIfStmts = exteVisitor.getList();
        for (IfStmt ifStmt : exteIfStmts) {
            int deep = 0;
            internalVisitor.setCaller(ifStmt);
            ifStmt.accept(internalVisitor, deep);
        }
    }

}
