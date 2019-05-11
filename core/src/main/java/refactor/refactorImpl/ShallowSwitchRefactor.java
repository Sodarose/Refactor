package refactor.refactorImpl;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import model.Issue;
import model.ReCorrect;
import refactor.AbstractRefactor;

import java.util.*;

/**
 * 将case小于等于3的switch语句转换为if
 */
public class ShallowSwitchRefactor extends AbstractRefactor {

    @Override
    public ReCorrect refactor(Issue issue) {
        SwitchStmt switchStmt = (SwitchStmt) issue.getIssueNode();
        transFrom(switchStmt);
        return null;
    }

    private void transFrom(SwitchStmt switchStmt) {
        IfStmt ifStmt = buildStmt(switchStmt.getSelector(), switchStmt.getEntries());
        try {
            BlockStmt blockStmt = (BlockStmt) switchStmt.getParentNode().get();
            int index = blockStmt.getStatements().indexOf(switchStmt);
            blockStmt.getStatements().add(index, ifStmt);
            blockStmt.remove(switchStmt);
            System.out.println(ifStmt);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到转换后的列表
     */
    private IfStmt buildStmt(Expression selector, List<SwitchEntry> switchEntries) {
        IfStmt p = null, q = null;
        boolean head = true;
        for (SwitchEntry switchEntry : switchEntries) {
            Expression condition = buildCondition(selector, switchEntry.getLabels().get(0));
            Statement statement = buildIfStmt(condition, switchEntry);
            if (statement.isIfStmt()) {
                if (head) {
                    p = statement.asIfStmt();
                    q = p;
                    continue;
                }
                p.setElseStmt(statement.asIfStmt());
                p = statement.asIfStmt();
            }
            if (statement.isBlockStmt()) {
                p.setElseStmt(statement.asBlockStmt());
            }
        }
        return q;
    }

    /**
     * 处理列表
     */
    private List<Statement> sloveStateList(Map<SwitchEntry, Statement> map) {
        List<Statement> statements = new ArrayList<>();
        return statements;
    }

    /**
     * 生成表达式
     */
    private Expression buildCondition(Expression select, Expression expression) {
        BinaryExpr condition = new BinaryExpr();
        condition.setOperator(BinaryExpr.Operator.EQUALS);
        condition.setLeft(select);
        condition.setRight(expression);
        return condition;
    }


    /**
     * @param condition   if条件
     * @param switchEntry case 分支
     */
    private Statement buildIfStmt(Expression condition, SwitchEntry switchEntry) {
        if (isDefault(switchEntry)) {
            BlockStmt blockStmt = new BlockStmt();
            blockStmt.getStatements().addAll(switchEntry.getStatements());
            return blockStmt;
        }
        IfStmt ifStmt = new IfStmt();
        ifStmt.setCondition(condition);
        BlockStmt blockStmt = new BlockStmt();
        blockStmt.getStatements().addAll(switchEntry.getStatements());
        ifStmt.setThenStmt(blockStmt);
        return ifStmt;
    }

    /**
     * 检测是否含有break或者return
     */
    private boolean cheakBreakOrReturn(SwitchEntry switchEntry) {
        List<Statement> statements = switchEntry.getStatements();
        for (Statement statement : statements) {
            if (statement.isBreakStmt()) {
                return true;
            }
            if (statement.isReturnStmt()) {
                return true;
            }
        }
        return false;
    }

    private boolean isDefault(SwitchEntry switchEntry) {
        return switchEntry.getLabels().get(0) == null ? true : false;
    }

}
