package refactor.refactorimpl;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import io.FindNodeUlits;
import model.Issue;
import model.ReCorrect;
import refactor.AbstractRefactor;

import java.util.*;

/**
 * 将case小于等于3的switch语句转换为if
 *
 * @author kangkang
 */
public class ShallowSwitchRefactor extends AbstractRefactor {

    @Override
    public ReCorrect refactor(Issue issue) {
        SwitchStmt switchStmt = (SwitchStmt) issue.getIssueNode();
        transFrom(switchStmt);
        return null;
    }

    /**
     * 转换函数
     */
    private void transFrom(SwitchStmt switchStmt) {
        Node node = switchStmt.getParentNode().get();
        Expression selector = switchStmt.getSelector();
        Statement statement = buildStmt(selector, switchStmt.getEntries());
        //这个地方有可能出错
        BlockStmt blockStmt = (BlockStmt) switchStmt.getParentNode().get();
        int index = blockStmt.getStatements().indexOf(switchStmt);
        if (statement.isBlockStmt()) {
            blockStmt.getStatements().addAll(index,statement.asBlockStmt().getStatements());
        } else {
            blockStmt.getStatements().add(index, statement.asIfStmt());
        }
        blockStmt.remove(switchStmt);
        System.out.println(node.getParentNode().get());
    }


    /**
     * 组合分支语句
     */
    private Statement buildStmt(Expression selector, List<SwitchEntry> switchEntries) {
        IfStmt p = null, q = null;
        boolean head = true;
        for (int i = 0; i < switchEntries.size(); i++) {
            Statement statement = createIfStmt(selector, switchEntries, i);
            if (statement == null) {
                continue;
            }
            if (head) {
                if (statement.isIfStmt()) {
                    p = statement.asIfStmt();
                }
                if (statement.isBlockStmt()) {
                    return statement;
                }
                q = p;
                head = false;
                continue;
            }
            if (statement.isIfStmt()) {
                p.setElseStmt(statement.asIfStmt());
                p = statement.asIfStmt();
            }
            if (statement.isBlockStmt()) {
                p.setElseStmt(statement.asBlockStmt());
            }
        }
        System.out.println(q);
        return q;
    }

    /**
     * 根据case生成分支语句
     */
    private Statement createIfStmt(Expression selector, List<SwitchEntry> switchEntrys, int index) {
        BlockStmt blockStmt = new BlockStmt();
        SwitchEntry switchEntry = switchEntrys.get(index);

        if (switchEntry.getStatements().size() == 0) {
            return null;
        }

        /**
         * 添加自己的statmes
         * */

        if (switchEntry.getStatements().size() == 1 && switchEntry.getStatements().get(0).isBlockStmt()) {
            blockStmt = switchEntry.getStatements().get(0).asBlockStmt();
        } else {
            blockStmt.getStatements().addAll(switchEntry.getStatements());
        }

        /**
         * 从当前位置向下搜索,如果遇到没有break或者return 将他们的statme提取到当前分支
         * */
        for (int i = index; i < switchEntrys.size() - 1; i++) {
            if (ishasBreakOrReturn(switchEntrys.get(i))) {
                break;
            }
            blockStmt.getStatements().addAll(switchEntrys.get(i + 1).getStatements());
        }

        /**
         * 清除当前分支blockstate中的break
         * */
        cleanBreakStmt(blockStmt);

        /**
         * 如果为default分支 则直接返回分支
         * */
        Expression condition = buildCondition(selector, switchEntry);
        if (condition == null) {
            return blockStmt;
        }

        /**
         * 如果不是default 则返回if
         */
        IfStmt ifStmt = new IfStmt();
        ifStmt.setCondition(condition);
        ifStmt.setThenStmt(blockStmt);
        return ifStmt;
    }

    /**
     * 生成表达式
     * condition =>
     */
    private Expression buildCondition(Expression select, SwitchEntry switchEntry) {

        boolean isString = false;
        boolean isEmum = false;
        boolean isOrder = false;
        //判断是否为一个default 如果是就返回空
        if (switchEntry.getLabels() == null || switchEntry.getLabels().size() == 0) {
            return null;
        }
        BinaryExpr condition = new BinaryExpr();
        condition.setOperator(BinaryExpr.Operator.EQUALS);
        condition.setLeft(select);
        condition.setRight(switchEntry.getLabels().get(0));
        return condition;
    }

    /**
     * 去除分支中的break
     */
    public void cleanBreakStmt(BlockStmt blockStmt) {
        ArrayList<BreakStmt> breakStmts = new ArrayList<>();
        for (Statement stmt : blockStmt.getStatements()) {
            if (stmt.isBreakStmt()) {
                breakStmts.add(stmt.asBreakStmt());
            }
        }
        blockStmt.getStatements().removeAll(breakStmts);
    }

    /**
     * 判断这个case是否有break return 和 throw
     */
    private boolean ishasBreakOrReturn(SwitchEntry switchEntry) {
        final List<Statement> statements;
        if (switchEntry.getStatements().size() == 1 && switchEntry.getStatements().get(0).isBlockStmt()) {
            statements = switchEntry.getStatements().get(0).asBlockStmt().getStatements();
        } else {
            statements = switchEntry.getStatements();
        }
        if (statements.size() == 0) {
            return false;
        }
        if (statements.get(statements.size() - 1).isBreakStmt()) {
            return true;
        }
        if (statements.get(statements.size() - 1).isReturnStmt()) {
            return true;
        }
        if (statements.get(statements.size() - 1).isThrowStmt()) {
            return true;
        }
        return false;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        List<SwitchStmt> switchStmts = FindNodeUlits.getSwitch("D:\\gitProject\\W8X\\core\\src\\test\\java\\SwitchSample.java");
        ShallowSwitchRefactor shallowSwitchRefactor = new ShallowSwitchRefactor();
        for (SwitchStmt switchStmt : switchStmts) {
            shallowSwitchRefactor.transFrom(switchStmt);
        }
        System.out.println();
    }
}

