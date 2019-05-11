package refactor.refactorImpl;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import model.Issue;
import model.ReCorrect;
import refactor.AbstractRefactor;

import java.util.*;

/**
 *
 * */
public class ShallowSwitchRefactor extends AbstractRefactor {

    @Override
    public ReCorrect refactor(Issue issue) {
        SwitchStmt switchStmt = (SwitchStmt) issue.getIssueNode();
        return null;
    }

    private void  transFrom(SwitchStmt switchStmt){

    }

    /**
     * 得到转换后的列表
     * */
    private IfStmt buildStmt(Expression selector, List<SwitchEntry> switchEntries){
        IfStmt p = null;
        boolean head = true;
        for(SwitchEntry switchEntry:switchEntries){
            Expression condition = buildCondition(selector,switchEntry.getLabels().get(0));
            Statement statement = buildIfStmt(condition,switchEntry);
            if(statement.isIfStmt()){
                if(head){

                }
            }
        }
        return null;
    }

    /**
     * 处理列表
     * */
    private List<Statement> sloveStateList(Map<SwitchEntry,Statement> map){
        List<Statement> statements = new ArrayList<>();

        return statements;
    }

    private Expression buildCondition(Expression select,Expression expression){
        BinaryExpr condition = new BinaryExpr();
        condition.setOperator(BinaryExpr.Operator.EQUALS);
        condition.setLeft(select);
        condition.setRight(expression);
        return condition;
    }


    /**
     * @param condition if条件
     * @param switchEntry case 分支
     * */
    private Statement buildIfStmt(Expression condition, SwitchEntry switchEntry){
        if(isDefault(switchEntry)){
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

    private boolean cheakBreakOrReturn(SwitchEntry switchEntry){
        List<Statement> statements = switchEntry.getStatements();
        for(Statement statement:statements){
            if(statement.isBreakStmt()){
                return true;
            }
            if(statement.isReturnStmt()){
                return true;
            }
        }
        return false;
    }

    private boolean isDefault(SwitchEntry switchEntry){
        return switchEntry.getLabels().get(0)==null?true:false;
    }

}
