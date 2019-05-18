package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import model.Issue;
import model.IssueContext;
import ulits.FindNodeUlits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final String labeledStmt = "LabeledStmt";
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

    private void collectWhile(CompilationUnit unit) {
        unit.accept(visitor, null);
    }

    private void check() {
        List<WhileStmt> whileStmts = visitor.getList();

        for (WhileStmt whileStmt : whileStmts) {
            //不是(参数<=参数)这样类型的跳过
            if (!whileStmt.getCondition().isBinaryExpr()) {
                continue;
            }
            if (!whileStmt.getCondition().asBinaryExpr().getLeft().isNameExpr()) {
                continue;
            }

            //存放一些数据以便省略重构所需要的重复操作
            Map<String, Object> map = new HashMap<>(5);
            if (!checkWhileStmt(whileStmt, map)) {
                continue;
            }

            Issue issue = new Issue();
            issue.setIssueNode(whileStmt);
            issue.setUnitNode(whileStmt.findRootNode());
            issue.setRefactorName(getSolutionClassName());
            getContext().getIssues().add(issue);
        }
    }

    /**
     * 找到该表达式使用者初始化语句
     * 并且该变量没有被使用
     */
    private boolean checkWhileStmt(WhileStmt whileStmt, Map<String, Object> map) {
        //确定搜索范围
        if (whileStmt.getParentNode().isPresent()) {
            return false;
        }
        Node parent = whileStmt.getParentNode().get();
        String fullName = parent.getClass().getName();
        String name = fullName.substring(fullName.lastIndexOf(".") + 1);
        //防止 T:whlie()
        if (labeledStmt.equals(name)) {
            if (!parent.getParentNode().isPresent()) {
                return false;
            }
            parent = parent.getParentNode().get();
        }
        if (!"BlockStmt".equals(parent)) {
            return false;
        }
        BinaryExpr binaryExpr = whileStmt.getCondition().asBinaryExpr();
        String left = binaryExpr.getLeft().toString();
        String right = binaryExpr.getRight().toString();
        Boolean isLeft = false;
        Boolean isRight = false;
        if (isUpdate(left, map, whileStmt.getBody())) {
            isLeft = true;
        }
        if (isUpdate(right, map, whileStmt.getBody())) {
            isRight = true;
        }
        if (isLeft && isRight) {
            return false;
        }
        //查找声明该变量的语句
        List<VariableDeclarationExpr> variableExprs = null;
        if (isLeft) {
            variableExprs = FindNodeUlits.findVariableDeclarationExprByName(parent, left, false);
        }
        if (isRight) {
            variableExprs = FindNodeUlits.findVariableDeclarationExprByName(parent, right, false);
        }
        if (variableExprs == null || variableExprs.size() > 1) {
            return false;
        }
        VariableDeclarationExpr variableExpr = variableExprs.get(0);
        map.put("InitVariableExpr", variableExpr);
        return true;
    }

    private boolean isUpdate(String variableName, Map<String, Object> map, Statement body) {

        return true;
    }

    public static void main(String[] args) {
        WhileChangeForRule whileChangeForRule = new WhileChangeForRule();
    }

}
