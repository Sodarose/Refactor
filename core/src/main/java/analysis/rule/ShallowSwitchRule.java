package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.SwitchStmt;
import model.Issue;
import model.IssueContext;
import model.JavaModel;
import refactor.refactorimpl.ShallowSwitchRefactor;

import java.util.List;

/**
 * 浅的Switch
 */
public class ShallowSwitchRule extends AbstractRuleVisitor {
    private JavaModel javaModel;
    private final int MAX_DEEP = 3;

    /**
     * 收集switch
     */
    private final BaseVisitor<SwitchStmt> switchStmtVisitor = new BaseVisitor<SwitchStmt>() {
        @Override
        public void visit(SwitchStmt n, Object arg) {
            getList().add(n);
            super.visit(n, arg);
        }
    };

    @Override
    public IssueContext apply(List<JavaModel> javaModels) {
        for (JavaModel javaModel : javaModels) {
            this.javaModel = javaModel;
            collectSwitchStmt(javaModel.getUnit());
        }
        check();
        return getContext();
    }

    /**
     * 收集switch
     */
    private void collectSwitchStmt(CompilationUnit unit) {
        unit.accept(switchStmtVisitor, null);
    }

    /**
     * 检查
     */
    private void check() {
        List<SwitchStmt> switchStmts = switchStmtVisitor.getList();
        for (SwitchStmt switchStmt : switchStmts) {
            /*if(!hasBreakOrReturn(switchStmt)){
                continue;
            }*/
            if (switchStmt.getEntries().size() > MAX_DEEP) {
                continue;
            }
            Issue issue = new Issue();
            issue.setJavaModel(javaModel);
            issue.setIssueNode(switchStmt);
            issue.setRefactorName(getSolutionClassName());
            issue.setDescription(getDescription());
            issue.setRuleName(getRuleName());
            getContext().getIssues().add(issue);
        }
    }


}
