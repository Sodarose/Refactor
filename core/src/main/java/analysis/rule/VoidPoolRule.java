package analysis.rule;

import analysis.AbstractRuleVisitor;
import analysis.BaseVisitor;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.ForStmt;
import io.FileUlits;
import model.Issue;
import model.IssueContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 空条件for循环
 * */
public class VoidPoolRule extends AbstractRuleVisitor {

    private final BaseVisitor<ForStmt> forVisitor = new BaseVisitor<ForStmt>(){
        @Override
        public void visit(ForStmt n, Object arg) {
            getList().add(n);
            super.visit(n, arg);
        }
    };

    @Override
    public IssueContext apply(List<CompilationUnit> units) {
        for (CompilationUnit unit : units) {
            collectFor(unit);
        }
        check();
        return getContext();
    }

    private void collectFor(CompilationUnit unit) {
        unit.accept(forVisitor,null);
    }

    private void check() {
        List<ForStmt> forStmts = forVisitor.getList();
        for(ForStmt forStmt : forStmts){
            if(forStmt.getInitialization().size()!=0){
                continue;
            }
            if(forStmt.getUpdate().size()!=0){
                return;
            }
            Issue issue = new Issue();
            issue.setUnitNode(forStmt.findRootNode());
            issue.setIssueNode(forStmt);
            issue.setRefactorName(getSolutionClassName());
            getContext().getIssues().add(issue);
        }
    }

    public static void main(String []args){
        String source = FileUlits.readFile("D:\\gitProject\\W8X\\core\\src\\test\\java\\ForWhileSampe.java");
        CompilationUnit unit = StaticJavaParser.parse(source);
        List<CompilationUnit> list = new ArrayList<>();
        list.add(unit);
        VoidPoolRule voidPoolRule = new VoidPoolRule();
        voidPoolRule.apply(list);
        System.out.println(voidPoolRule.getContext().getIssues().size());
    }
}
