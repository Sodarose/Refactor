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
import model.ReCorrect;
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
    public ReCorrect refactor(Issue issue) {
        ForStmt forStmt = (ForStmt) issue.getIssueNode();
        tranFromWhile(forStmt);
        return null;
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
        System.out.println(this.getClass().getName());
    }

    public static void main(String args[]){
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\ForWhileSampe.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<CompilationUnit> list = new ArrayList<>();
        list.add(unit);
        VoidPoolRule voidPoolRule = new VoidPoolRule();
        voidPoolRule.apply(list);
        VoidPoolRefactor voidPoolRefactor = new VoidPoolRefactor();
        for(Issue issue:voidPoolRule.getContext().getIssues()){
            voidPoolRefactor.refactor(issue);
        }
    }
}
