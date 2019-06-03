package refactor.refactorimpl;

import analysis.rule.VoidPoolRule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import io.FileUlits;
import model.Issue;
import refactor.AbstractRefactor;

import java.util.ArrayList;
import java.util.List;


/**
 * for(;;)=>while(true)
 * and
 * for(;expr;)=>while(expr)
 * @author kangkang
 */
public class VoidPoolRefactor extends AbstractRefactor {

    @Override
    public void refactor(Issue issue) {
        ForStmt forStmt = (ForStmt) issue.getIssueNode();
        System.out.println("xxxx"+forStmt);
        System.out.println(forStmt);
        tranFromWhile(forStmt);
    }

    /**
     * 转换方法
     * */
    private void tranFromWhile(ForStmt forStmt) {
        final Statement stmt = forStmt.getBody();

        //构建body
        BlockStmt blockStmt;
        if (stmt.isBlockStmt()) {
            blockStmt = stmt.asBlockStmt();
        } else {
            blockStmt = new BlockStmt();
            blockStmt.getStatements().add(stmt);
        }

        //构建表达式
        Expression condition;
        if (forStmt.getCompare().isPresent()) {
            condition = forStmt.getCompare().get();
        } else {
            BooleanLiteralExpr temp = new BooleanLiteralExpr();
            temp.setValue(true);
            condition = temp;

        }

        WhileStmt whileStmt = new WhileStmt();
        whileStmt.setCondition(condition);
        whileStmt.setBody(blockStmt);
        Node parent = forStmt.getParentNode().get();
        parent.replace(whileStmt,forStmt);
    }

}
