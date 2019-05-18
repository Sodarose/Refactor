package refactor.refactorimpl;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import model.Issue;
import model.ReCorrect;
import refactor.AbstractRefactor;

import java.util.List;
import java.util.Map;

/**
 * @author kangkang
 */
public class IfTransformSwitchRefactor extends AbstractRefactor {
    @Override
    public ReCorrect refactor(Issue issue) {
        IfStmt ifStmt = (IfStmt) issue.getIssueNode();
        Map<String, Object> data = issue.getData();
        transformSwitch(ifStmt, data);
        return null;
    }

    private boolean transformSwitch(IfStmt ifStmt, Map<String, Object> data) {
        if (data == null) {
            return false;
        }
        Expression selector = null;
        try {
            selector = (Expression) data.get("selector");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (selector == null) {
            return false;
        }
        SwitchStmt switchStmt = createSwitch(ifStmt, selector);
        if (!ifStmt.getParentNode().isPresent()) {
            return false;
        }
        Node parent = ifStmt.getParentNode().get();
        //替换
        parent.replace(switchStmt, ifStmt);
        return true;
    }

    /**
     * 生成switch
     */
    private SwitchStmt createSwitch(IfStmt ifStmt, Expression selector) {
        SwitchStmt switchStmt = new SwitchStmt();
        switchStmt.setSelector(selector);
        NodeList<SwitchEntry> switchEntries = new NodeList<>();
        while (ifStmt.hasElseBranch()) {
            //生成case 分支
            if (ifStmt.getElseStmt().get().isIfStmt()) {
                switchEntries.add(createCaseBranch(ifStmt.getCondition(), selector, ifStmt.getThenStmt()));
                ifStmt = ifStmt.getElseStmt().get().asIfStmt();
            }
            //生成default分支
            else {
                switchEntries.add(createCaseBranch(null, selector, ifStmt.getElseStmt().get()));
            }
        }
        switchStmt.setEntries(switchEntries);
        return switchStmt;
    }

    /**
     * 生成case分支
     */
    private SwitchEntry createCaseBranch(Expression condition, Expression selector, Statement statement) {
        SwitchEntry switchEntry = new SwitchEntry();
        NodeList<Expression> labels = new NodeList<>();
        if (condition != null && condition.isBinaryExpr()) {
            BinaryExpr t = condition.asBinaryExpr();
            Expression left = t.getLeft();
            Expression right = t.getRight();
            if (selector.equals(left)) {
                labels.add(right);
            }
            if (selector.equals(right)) {
                labels.add(left);
            }
        }
        if (condition != null && condition.isFieldAccessExpr()) {
            String fullName = condition.asFieldAccessExpr().getName().getIdentifier();
            String eq = fullName.substring(fullName.lastIndexOf("(") + 1, fullName.lastIndexOf(")"));
            Expression left = condition.asFieldAccessExpr().getScope();
            Expression right = StaticJavaParser.parseExpression(eq);
            if (selector.equals(left)) {
                labels.add(right);
            }
            if (selector.equals(right)) {
                labels.add(left);
            }
        }
        switchEntry.setLabels(labels);
        switchEntry.setStatements(addBreakIntoStmt(statement));
        return switchEntry;
    }

    /**
     * 添加break
     */
    private NodeList<Statement> addBreakIntoStmt(Statement statement) {
        NodeList<Statement> stmts = new NodeList<>();
        // if的then 有三种情况 1 blockstmt 2.但return 语句 或者单抛出异常一句 3.单别的类型语句
        if (statement.isBlockStmt()) {
            if (hasReturnStmt(statement)) {
                stmts.addAll(statement.asBlockStmt().getStatements());
                return stmts;
            }
            statement.asBlockStmt().getStatements().add(new BreakStmt());
            return stmts;
        }
        if (statement.isReturnStmt() || statement.isThrowStmt()) {
            stmts.add(statement);
            return stmts;
        }
        stmts.add(statement);
        stmts.add(new BreakStmt());
        return stmts;
    }

    /**

     */
    private boolean hasReturnStmt(Statement statement) {
        List<ReturnStmt> returnStmts = statement.findAll(ReturnStmt.class);
        for (ReturnStmt re : returnStmts) {
            if (re.getParentNode().get().equals(statement)) {
                return false;
            }

        }
        return true;
    }
}
